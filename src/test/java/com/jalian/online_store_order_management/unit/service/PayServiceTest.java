package com.jalian.online_store_order_management.unit.service;

import com.jalian.online_store_order_management.constant.BalanceOperation;
import com.jalian.online_store_order_management.domain.Item;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PayServiceTest {

    @Mock
    private UserService userService;

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
        when(userService.updateBalance(argThat(new UpdateBalanceMatcher(user.getId(), totalPrice, BalanceOperation.MINUS))))
                .thenReturn(null);
        syncPayService.pay(user, List.of(item1, item2));
        verify(userService).updateBalance(argThat(new UpdateBalanceMatcher(user.getId(), totalPrice, BalanceOperation.MINUS)));
    }

    @Test
    void testASyncPayService_pay() {
        double totalPrice = (2 * 10.0) + (3 * 5.0);
        when(userService.updateBalance(argThat(new UpdateBalanceMatcher(user.getId(), totalPrice, BalanceOperation.MINUS))))
                .thenReturn(null);
        asyncPayService.pay(user, List.of(item1, item2));
        verify(userService).updateBalance(argThat(new UpdateBalanceMatcher(user.getId(), totalPrice, BalanceOperation.MINUS)));
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