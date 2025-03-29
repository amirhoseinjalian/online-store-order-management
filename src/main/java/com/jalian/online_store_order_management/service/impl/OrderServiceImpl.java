package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.constant.OrderStatus;
import com.jalian.online_store_order_management.constant.ProductOperationStrategy;
import com.jalian.online_store_order_management.dao.OrderDao;
import com.jalian.online_store_order_management.dao.ItemDao;
import com.jalian.online_store_order_management.domain.Order;
import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.key.ItemKey;
import com.jalian.online_store_order_management.dto.AddOrderDto;
import com.jalian.online_store_order_management.dto.ItemDto;
import com.jalian.online_store_order_management.dto.ProductOperationDto;
import com.jalian.online_store_order_management.exception.ValidationException;
import com.jalian.online_store_order_management.service.OrderService;
import com.jalian.online_store_order_management.service.ProductService;
import com.jalian.online_store_order_management.service.StoreService;
import com.jalian.online_store_order_management.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final ProductService productService;
    private final UserService userService;
    private final StoreService storeService;
    private final ItemDao itemDao;

    public OrderServiceImpl(OrderDao orderDao, ProductService productService, UserService userService, StoreService storeService, ItemDao orderProductDao) {
        this.orderDao = orderDao;
        this.productService = productService;
        this.userService = userService;
        this.storeService = storeService;
        this.itemDao = orderProductDao;
    }

    @Override
    @Transactional
    public Long addOrder(AddOrderDto dto) {
        final var storeId = dto.storeId();
        checkUserBelongsToStore(dto.userId(), storeId);
        checkAllProductsBelongToStore(dto.items(), storeId);
        var order = createNewOrder(dto.userId(), storeId);
        var savedOrder = orderDao.save(order);
        addItemsToOrder(dto.items(), savedOrder);
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

    private void addItemsToOrder(List<ItemDto> items, Order savedOrder) {
        items.forEach(itemDto -> {
            var product = productService.findProductById(itemDto.productId());
            var presentInventory = product.getInventory();
            var item = new Item(
                    new ItemKey(savedOrder.getId(), product.getId()),
                    product,
                    savedOrder,
                    itemDto.count(),
                    presentInventory
            );
            productService.doOperation(
                    new ProductOperationDto(
                            itemDto.productId(),
                            ProductOperationStrategy.MINUS,
                            itemDto.count()
                    )
            );
            itemDao.save(item);
        });
    }
}
