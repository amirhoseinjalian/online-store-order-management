package com.jalian.online_store_order_management.integration.service;

import com.jalian.online_store_order_management.dao.ProductDao;
import com.jalian.online_store_order_management.domain.Product;
import com.jalian.online_store_order_management.dto.AddStoreDto;
import com.jalian.online_store_order_management.dto.ProductDto;
import com.jalian.online_store_order_management.dto.ProductOperationDto;
import com.jalian.online_store_order_management.exception.ValidationException;
import com.jalian.online_store_order_management.service.ProductService;
import com.jalian.online_store_order_management.service.StoreService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductServiceConcurrencyTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private StoreService storeService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private Long storeId;
    private Long productId;

    private final long initialInventory = 0L;
    private final long highInventory = 1000L;

    @BeforeEach
    public void setup() {
        storeId = storeService.addStore(new AddStoreDto("Test Store " + UUID.randomUUID()));
        var productDto = new ProductDto("Test Product", "Test Description", 100.0, storeId);
        productId = productService.addProduct(productDto);

        var opt = productDao.findById(productId);
        opt.ifPresent(product -> {
            product.setInventory(initialInventory);
            productDao.save(product);
        });
    }

    @AfterEach
    public void cleanup() {
        productDao.deleteById(productId);
    }

    @Test
    public void testConcurrentProductCharge() throws InterruptedException {
        int threadCount = 100;
        long chargeAmount = 10L;
        long expectedInventory = initialInventory + threadCount * chargeAmount;
        var latch = new CountDownLatch(threadCount);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        var dto = new ProductOperationDto(productId, chargeAmount);
                        productService.chargeProduct(dto);
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
        }

        var product = fetchProductInTransaction();
        assertEquals(expectedInventory, product.getInventory(), "Concurrent charge failed.");
    }

    @Test
    public void testConcurrentProductDischarge() throws InterruptedException {
        setProductInventory(highInventory);

        int threadCount = 50;
        long dischargeAmount = 5L;
        long expectedInventory = highInventory - threadCount * dischargeAmount;

        var template = new TransactionTemplate(transactionManager);
        var latch = new CountDownLatch(threadCount);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        var dto = new ProductOperationDto(productId, dischargeAmount);
                        template.execute(status -> {
                            productService.dischargeProduct(dto);
                            return null;
                        });
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
        }

        var product = fetchProductInTransaction();
        assertEquals(expectedInventory, product.getInventory(), "Concurrent discharge failed.");
    }

    @Test
    public void testConcurrentMixedProductOperations() throws InterruptedException {
        setProductInventory(highInventory);

        var chargeThreads = 50;
        var dischargeThreads = 50;
        var chargeAmount = 10L;
        var dischargeAmount = 5L;
        var expectedInventory = highInventory + (chargeThreads * chargeAmount) - (dischargeThreads * dischargeAmount);

        var template = new TransactionTemplate(transactionManager);
        var totalThreads = chargeThreads + dischargeThreads;
        var latch = new CountDownLatch(totalThreads);
        var tasks = new CopyOnWriteArrayList<Runnable>();

        for (int i = 0; i < chargeThreads; i++) {
            tasks.add(() -> {
                try {
                    var dto = new ProductOperationDto(productId, chargeAmount);
                    productService.chargeProduct(dto);
                } finally {
                    latch.countDown();
                }
            });
        }

        for (int i = 0; i < dischargeThreads; i++) {
            tasks.add(() -> {
                try {
                    var dto = new ProductOperationDto(productId, dischargeAmount);
                    template.execute(status -> {
                        productService.dischargeProduct(dto);
                        return null;
                    });
                } finally {
                    latch.countDown();
                }
            });
        }

        Collections.shuffle(tasks);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            tasks.forEach(executor::submit);
            latch.await();
        }

        var product = fetchProductInTransaction();
        assertEquals(expectedInventory, product.getInventory(), "Mixed concurrent operations with shuffle failed.");
    }

    @Test
    public void testConcurrentIllegalDischarge() throws InterruptedException {
        setProductInventory(50L);

        int threadCount = 20;
        long dischargeAmount = 10L;
        var exceptionCount = new AtomicInteger(0);
        var template = new TransactionTemplate(transactionManager);
        var latch = new CountDownLatch(threadCount);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        var dto = new ProductOperationDto(productId, dischargeAmount);
                        template.execute(status -> {
                            try {
                                productService.dischargeProduct(dto);
                            } catch (Exception e) {
                                exceptionCount.incrementAndGet();
                                status.setRollbackOnly();
                            }
                            return null;
                        });
                    } catch (Exception e) {
                        exceptionCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
        }

        var product = fetchProductInTransaction();
        assertTrue(product.getInventory() >= 0, "Inventory became negative!");
        assertTrue(exceptionCount.get() > 0, "No exception was thrown for illegal discharge.");
    }

    private void setProductInventory(long inventory) {
        var product = fetchProductInTransaction();
        product.setInventory(inventory);
        productDao.save(product);
    }

    private Product fetchProductInTransaction() {
        return new TransactionTemplate(transactionManager)
                .execute(status -> productDao.findByIdSafe(productId).orElseThrow());
    }
}
