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

/**
 * The ProductServiceConcurrencyTest class performs integration tests to verify concurrent operations
 * on product inventory.
 * <p>
 * It tests the following scenarios:
 * <ul>
 *   <li>Concurrent product charging.</li>
 *   <li>Concurrent product discharging.</li>
 *   <li>Concurrent mixed product operations (charging and discharging) with task shuffling.</li>
 *   <li>Concurrent illegal discharge attempts that should throw exceptions.</li>
 * </ul>
 * The tests use virtual threads (via Executors.newVirtualThreadPerTaskExecutor())
 * and Spring's transaction management to simulate concurrent access.
 * </p>
 *
 * @author amirhosein jalian
 */
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

    /**
     * Sets up a new store and product before each test.
     * <p>
     * The product is initialized with a specified inventory value.
     * </p>
     */
    @BeforeEach
    public void setup() {
        storeId = storeService.addStore(new AddStoreDto("Test Store " + UUID.randomUUID()));
        var productDto = new ProductDto("Test Product", "Test Description", 100.0, storeId);
        productId = productService.addProduct(productDto);

        // Set the product's inventory to the initial value.
        var opt = productDao.findById(productId);
        opt.ifPresent(product -> {
            product.setInventory(initialInventory);
            productDao.save(product);
        });
    }

    /**
     * Cleans up the created product after each test.
     */
    @AfterEach
    public void cleanup() {
        productDao.deleteById(productId);
    }

    /**
     * Tests concurrent charging of a product's inventory.
     * <p>
     * A specified number of virtual threads concurrently perform a charge operation.
     * The test asserts that the final inventory is as expected.
     * </p>
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
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

    /**
     * Tests concurrent discharging of a product's inventory.
     * <p>
     * The product is first set to a high inventory value. Then, a specified number of virtual threads concurrently
     * perform a discharge operation. The final inventory is then verified.
     * </p>
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
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

    /**
     * Tests concurrent mixed operations (both charging and discharging) on a product's inventory.
     * <p>
     * A total number of virtual threads, divided between charging and discharging tasks, are executed concurrently.
     * The tasks are shuffled to simulate unpredictable execution order.
     * The final inventory is then verified against the expected value.
     * </p>
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
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

        // Create charging tasks.
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

        // Create discharging tasks.
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

        // Shuffle tasks to randomize execution order.
        Collections.shuffle(tasks);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            tasks.forEach(executor::submit);
            latch.await();
        }

        var product = fetchProductInTransaction();
        assertEquals(expectedInventory, product.getInventory(), "Mixed concurrent operations with shuffle failed.");
    }

    /**
     * Tests concurrent illegal discharge operations.
     * <p>
     * This test sets the product inventory to a low value and attempts to discharge more than available concurrently.
     * It verifies that exceptions are thrown and that the inventory does not go negative.
     * </p>
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
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

    /**
     * Sets the inventory of the product to the specified value within a transaction.
     *
     * @param inventory the inventory value to set.
     */
    private void setProductInventory(long inventory) {
        var product = fetchProductInTransaction();
        product.setInventory(inventory);
        productDao.save(product);
    }

    /**
     * Fetches the product entity within a new transaction.
     *
     * @return the {@link Product} entity.
     */
    private Product fetchProductInTransaction() {
        return new TransactionTemplate(transactionManager)
                .execute(status -> productDao.findByIdSafe(productId).orElseThrow());
    }
}
