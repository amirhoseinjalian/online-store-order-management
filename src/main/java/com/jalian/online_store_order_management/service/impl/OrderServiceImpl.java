package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.constant.OrderStatus;
import com.jalian.online_store_order_management.dao.OrderDao;
import com.jalian.online_store_order_management.domain.Order;
import com.jalian.online_store_order_management.dto.AddOrderDto;
import com.jalian.online_store_order_management.dto.ItemDto;
import com.jalian.online_store_order_management.exception.ValidationException;
import com.jalian.online_store_order_management.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final ProductService productService;
    private final UserService userService;
    private final StoreService storeService;
    private final ItemService itemService;

    public OrderServiceImpl(OrderDao orderDao, ProductService productService, UserService userService, StoreService storeService, ItemService itemService) {
        this.orderDao = orderDao;
        this.productService = productService;
        this.userService = userService;
        this.storeService = storeService;
        this.itemService = itemService;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Long addOrder(AddOrderDto dto, PayService payService) {
        final var storeId = dto.storeId();
        checkUserBelongsToStore(dto.userId(), storeId);
        checkAllProductsBelongToStore(dto.items(), storeId);
        var order = createNewOrder(dto.userId(), storeId);
        var savedOrder = orderDao.save(order);
        var itemsToPay = itemService.saveItems(dto.items(), savedOrder);
        var user = userService.findUserEntityById(dto.userId());
        payService.pay(user, itemsToPay);
        savedOrder.setOrderStatus(OrderStatus.FINISHED);
        orderDao.save(savedOrder);
        return savedOrder.getId();
    }

    private Order createNewOrder(Long userId, Long storeId) {
        return new Order(
                OrderStatus.INITIALIZED,
                userService.findUserEntityById(userId),
                storeService.findStore(storeId)
        );
    }

    private void checkAllProductsBelongToStore(List<ItemDto> items, Long storeId) {
        items.forEach(itemDto -> {
            if (!productService.belongsToStore(itemDto.productId(), storeId)) {
                throw new ValidationException("Product does not belong to store: " + itemDto.productId());
            }
        });
    }

    private void checkUserBelongsToStore(Long userId, Long storeId) {
        if (!storeService.belongToStore(storeId, userId)) {
            throw new ValidationException("User does not belong to store: " + storeId);
        }
    }
}
