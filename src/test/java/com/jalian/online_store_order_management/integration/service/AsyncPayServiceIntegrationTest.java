package com.jalian.online_store_order_management.integration.service;

import com.jalian.online_store_order_management.constant.OrderStatus;
import com.jalian.online_store_order_management.dao.OrderDao;
import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.Order;
import com.jalian.online_store_order_management.domain.Product;
import com.jalian.online_store_order_management.domain.User;
import com.jalian.online_store_order_management.dto.UpdateBalanceDto;
import com.jalian.online_store_order_management.service.UserService;
import com.jalian.online_store_order_management.service.impl.ASyncPayServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * The AsyncPayServiceIntegrationTest class verifies the asynchronous behavior of the ASyncPayServiceImpl.
 * <p>
 * It uses a combination of Spring Boot's testing support and Awaitility to ensure that the payment operation
 * executes in a separate thread from the test's main thread.
 * </p>
 *
 * This integration test mocks the dependencies of ASyncPayServiceImpl to isolate and validate its asynchronous execution.
 *
 * @author amirhosein jalian
 */
@SpringBootTest
public class AsyncPayServiceIntegrationTest {

    @MockBean
    private UserService userService;

    @MockBean
    private OrderDao orderDao;

    @Autowired
    private ASyncPayServiceImpl asyncPayService;

    private User user;
    private Product product1;
    private Product product2;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        product1 = new Product();
        product1.setId(101L);
        product1.setPrice(10.0);

        product2 = new Product();
        product2.setId(102L);
        product2.setPrice(5.0);

        item1 = new Item();
        item1.setCount(2);
        item1.setProduct(product1);

        item2 = new Item();
        item2.setCount(3);
        item2.setProduct(product2);
    }

    /**
     * Tests that the asynchronous payment execution occurs in a separate thread.
     * <p>
     * The test verifies that the call to pay() is executed asynchronously by comparing the thread
     * name of the payment execution against the main test thread. Awaitility is used to wait until the
     * asynchronous execution completes.
     * </p>
     */
    @Test
    void testAsyncExecution() {
        AtomicReference<String> asyncThreadName = new AtomicReference<>();

        when(userService.updateBalance(any(UpdateBalanceDto.class))).thenAnswer(invocation -> {
            asyncThreadName.set(Thread.currentThread().getName());
            return null;
        });

        when(orderDao.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            if (o.getId() == null) {
                o.setId(1L);
            }
            return o;
        });

        Order order = new Order();
        order.setOrderStatus(OrderStatus.INITIALIZED);

        String testThreadName = Thread.currentThread().getName();
        asyncPayService.pay(user, order, List.of(item1, item2));

        await().atMost(Duration.ofSeconds(5)).until(() -> asyncThreadName.get() != null);
        assertThat(asyncThreadName.get()).isNotEqualTo(testThreadName);
    }
}
