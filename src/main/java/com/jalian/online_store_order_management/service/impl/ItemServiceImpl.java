package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.dao.ItemDao;
import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.Order;
import com.jalian.online_store_order_management.domain.key.ItemKey;
import com.jalian.online_store_order_management.dto.ItemDto;
import com.jalian.online_store_order_management.dto.ProductOperationDto;
import com.jalian.online_store_order_management.service.ItemService;
import com.jalian.online_store_order_management.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemDao;
    private final ProductService productService;

    public ItemServiceImpl(ItemDao itemDao, ProductService productService) {
        this.itemDao = itemDao;
        this.productService = productService;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public List<Item> saveItems(List<ItemDto> items, Order savedOrder) {
        var savedItems = new ArrayList<Item>();
        items.forEach(itemDto -> {
            var product = productService.findProductById(itemDto.productId());
            var item = new Item(
                    new ItemKey(savedOrder.getId(), product.getId()),
                    product,
                    savedOrder,
                    itemDto.count(),
                    product.getInventory()
            );
            productService.dischargeProduct(
                    new ProductOperationDto(
                            itemDto.productId(),
                            itemDto.count()
                    )
            );
            savedItems.add(itemDao.save(item));
        });
        return savedItems;
    }
}
