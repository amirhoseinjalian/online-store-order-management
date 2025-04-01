package com.jalian.online_store_order_management.unit.service;

import com.jalian.online_store_order_management.constant.BalanceOperation;
import com.jalian.online_store_order_management.constant.OrderStatus;
import com.jalian.online_store_order_management.dao.OrderDao;
import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.Order;
import com.jalian.online_store_order_management.domain.Product;
import com.jalian.online_store_order_management.domain.User;
import com.jalian.online_store_order_management.dto.UpdateBalanceDto;
import com.jalian.online_store_order_management.service.UserService;
import com.jalian.online_store_order_management.service.impl.ASyncPayServiceImpl;
import com.jalian.online_store_order_management.service.impl.SyncPayServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PayServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private SyncPayServiceImpl syncPayService;

    @InjectMocks
    private ASyncPayServiceImpl asyncPayService;

    private User user;
    private Product product1;
    private Product product2;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        asyncPayService = new ASyncPayServiceImpl(userService, orderDao);
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

    @Test
    void testSyncPayService_pay() {
        double totalPrice = (2 * 10.0) + (3 * 5.0);
        when(userService.updateBalance(argThat(
                new UpdateBalanceMatcher(user.getId(), totalPrice, BalanceOperation.MINUS)))
        ).thenReturn(null);
        Order order = new Order();
        syncPayService.pay(user, order, List.of(item1, item2));
        verify(userService).updateBalance(argThat(
                new UpdateBalanceMatcher(user.getId(), totalPrice, BalanceOperation.MINUS))
        );
    }

    @Test
    void testASyncPayService_pay() {
        double totalPrice = (2 * 10.0) + (3 * 5.0);
        when(userService.updateBalance(argThat(
                new UpdateBalanceMatcher(user.getId(), totalPrice, BalanceOperation.MINUS)))
        ).thenReturn(null);
        Order order = new Order();
        order.setOrderStatus(OrderStatus.FINISHED);
        when(orderDao.save(any(Order.class))).thenReturn(order);
        asyncPayService.pay(user, new Order(), List.of(item1, item2));
        verify(userService).updateBalance(argThat(
                new UpdateBalanceMatcher(user.getId(), totalPrice, BalanceOperation.MINUS))
        );
    }

    @Test
    void testASyncPayService_retrySuccess() {
        double totalPrice = (2 * 10.0) + (3 * 5.0);
        when(userService.updateBalance(argThat(
                new UpdateBalanceMatcher(user.getId(), totalPrice, BalanceOperation.MINUS)))
        )
                .thenThrow(new RuntimeException("Temporary failure"))
                .thenThrow(new RuntimeException("Temporary failure"))
                .thenReturn(null);
        Order order = new Order();
        order.setOrderStatus(OrderStatus.INITIALIZED);
        when(orderDao.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            if (o.getId() == null) {
                o.setId(100L);
            }
            return o;
        });
        asyncPayService.pay(user, order, List.of(item1, item2));
        await().atMost(Duration.ofSeconds(5)).untilAsserted(() ->
                verify(userService, times(3)).updateBalance(argThat(
                        new UpdateBalanceMatcher(user.getId(), totalPrice, BalanceOperation.MINUS)))
        );
        await().atMost(Duration.ofSeconds(5)).untilAsserted(() ->
                assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.FINISHED)
        );
    }

    @Test
    void testASyncPayService_retryFailure() {
        double totalPrice = (2 * 10.0) + (3 * 5.0);
        when(userService.updateBalance(argThat(
                new UpdateBalanceMatcher(user.getId(), totalPrice, BalanceOperation.MINUS)))
        )
                .thenThrow(new RuntimeException("Permanent failure"));
        Order order = new Order();
        order.setOrderStatus(OrderStatus.INITIALIZED);
        when(orderDao.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            if (o.getId() == null) {
                o.setId(100L);
            }
            return o;
        });
        asyncPayService.pay(user, order, List.of(item1, item2));
        await().atMost(Duration.ofSeconds(5)).untilAsserted(() ->
                assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.FAILED)
        );
    }

    private static class UpdateBalanceMatcher implements ArgumentMatcher<UpdateBalanceDto> {
        private final Long expectedUserId;
        private final double expectedAmount;
        private final BalanceOperation expectedOperation;

        UpdateBalanceMatcher(Long expectedUserId, double expectedAmount, BalanceOperation expectedOperation) {
            this.expectedUserId = expectedUserId;
            this.expectedAmount = expectedAmount;
            this.expectedOperation = expectedOperation;
        }

        @Override
        public boolean matches(UpdateBalanceDto dto) {
            return dto != null &&
                    dto.userId().equals(expectedUserId) &&
                    Double.compare(dto.amount(), expectedAmount) == 0 &&
                    dto.operation() == expectedOperation;
        }
    }
}