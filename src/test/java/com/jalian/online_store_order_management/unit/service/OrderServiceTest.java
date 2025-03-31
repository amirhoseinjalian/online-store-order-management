package com.jalian.online_store_order_management.unit.service;

import com.jalian.online_store_order_management.constant.OrderStatus;
import com.jalian.online_store_order_management.dao.OrderDao;
import com.jalian.online_store_order_management.domain.Order;
import com.jalian.online_store_order_management.domain.Store;
import com.jalian.online_store_order_management.domain.User;
import com.jalian.online_store_order_management.dto.AddOrderDto;
import com.jalian.online_store_order_management.dto.ItemDto;
import com.jalian.online_store_order_management.exception.ValidationException;
import com.jalian.online_store_order_management.service.ItemService;
import com.jalian.online_store_order_management.service.PayService;
import com.jalian.online_store_order_management.service.ProductService;
import com.jalian.online_store_order_management.service.StoreService;
import com.jalian.online_store_order_management.service.UserService;
import com.jalian.online_store_order_management.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OrderServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private ProductService productService;
    @Mock
    private UserService userService;
    @Mock
    private StoreService storeService;
    @Mock
    private ItemService itemService;
    @Mock
    private PayService payService;
    @InjectMocks
    private OrderServiceImpl orderService;

    private AddOrderDto addOrderDto;
    private User user;
    private Store store;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        store = new Store("Test Store");
        store.setId(1L);
        itemDto = new ItemDto(10L, 3);
        addOrderDto = new AddOrderDto(1L, 1L, List.of(itemDto));
        when(orderDao.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            if (o.getId() == null) {
                o.setId(100L);
            }
            return o;
        });
    }

    @Test
    void addOrder_success() {
        when(storeService.belongToStore(eq(1L), eq(1L))).thenReturn(true);
        when(productService.belongsToStore(eq(10L), eq(1L))).thenReturn(true);
        when(userService.findUserEntityById(eq(1L))).thenReturn(user);
        when(storeService.findStore(eq(1L))).thenReturn(store);
        when(itemService.saveItems(anyList(), any(Order.class))).thenReturn(List.of());
        doNothing().when(payService).pay(any(User.class), any());

        var orderId = orderService.addOrder(addOrderDto, payService);

        assertThat(orderId).isEqualTo(100L);
        var orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderDao, atLeast(2)).save(orderCaptor.capture());
        var savedOrders = orderCaptor.getAllValues();
        var finalOrder = savedOrders.get(savedOrders.size() - 1);
        assertThat(finalOrder.getOrderStatus()).isEqualTo(OrderStatus.FINISHED);
        verify(payService).pay(eq(user), any());
    }

    @Test
    void addOrder_userNotBelong_throwsException() {
        when(storeService.belongToStore(eq(1L), eq(1L))).thenReturn(false);
        var ex = assertThrows(ValidationException.class,
                () -> orderService.addOrder(addOrderDto, payService));
        assertThat(ex.getMessage()).contains("User does not belong to store: 1");
        verify(storeService).belongToStore(eq(1L), eq(1L));
        verifyNoInteractions(productService, userService, itemService, payService);
    }

    @Test
    void addOrder_productNotBelong_throwsException() {
        when(storeService.belongToStore(eq(1L), eq(1L))).thenReturn(true);
        when(productService.belongsToStore(eq(10L), eq(1L))).thenReturn(false);
        var ex = assertThrows(ValidationException.class,
                () -> orderService.addOrder(addOrderDto, payService));
        assertThat(ex.getMessage()).contains("Product does not belong to store: 10");
        verify(productService).belongsToStore(eq(10L), eq(1L));
    }
}