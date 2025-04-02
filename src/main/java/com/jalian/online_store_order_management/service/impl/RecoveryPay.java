package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.constant.OrderStatus;
import com.jalian.online_store_order_management.dao.OrderDao;
import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.Order;
import com.jalian.online_store_order_management.dto.ProductOperationDto;
import com.jalian.online_store_order_management.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecoveryPay {

    private static final Logger log = LoggerFactory.getLogger(RecoveryPay.class);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recoverPayment(Order order, List<Item> items, OrderDao orderDao, ProductService productService) {
        order.setOrderStatus(OrderStatus.FAILED);
        orderDao.save(order);
        items.forEach(item -> productService.chargeProduct(
                new ProductOperationDto(item.getProduct().getId(), item.getCount())
        ));
        log.warn("Recovery logic executed for order {}", order.getId());
    }
}
