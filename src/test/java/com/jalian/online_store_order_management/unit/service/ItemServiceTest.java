package com.jalian.online_store_order_management.unit.service;

import com.jalian.online_store_order_management.dao.ItemDao;
import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.Order;
import com.jalian.online_store_order_management.domain.Product;
import com.jalian.online_store_order_management.domain.Store;
import com.jalian.online_store_order_management.domain.key.ItemKey;
import com.jalian.online_store_order_management.dto.ItemDto;
import com.jalian.online_store_order_management.dto.ProductOperationDto;
import com.jalian.online_store_order_management.service.ProductService;
import com.jalian.online_store_order_management.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemDao itemDao;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Order order;
    private Store store;
    private Product product;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(100L);
        store = new Store("Test Store");
        store.setId(1L);
        product = new Product(store, 100.0, "Description A", "Product A");
        product.setId(10L);
        product.setInventory(50);
        itemDto = new ItemDto(10L, 5);
    }

    @Test
    void saveItems_success() {
        when(productService.findProductById(10L)).thenReturn(product);
        when(productService.dischargeProduct(any(ProductOperationDto.class))).thenReturn(null);
        when(itemDao.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0, Item.class));

        List<Item> savedItems = itemService.saveItems(List.of(itemDto), order);

        verify(productService).findProductById(10L);
        verify(productService).dischargeProduct(argThat(dto ->
                dto.productId().equals(10L) && dto.amount() == 5));
        verify(itemDao).save(any(Item.class));

        assertThat(savedItems).hasSize(1);
        Item savedItem = savedItems.get(0);
        assertThat(savedItem.getPrimaryKey()).isNotNull();
        assertThat(savedItem.getPrimaryKey().getOrderId()).isEqualTo(100L);
        assertThat(savedItem.getPrimaryKey().getProductId()).isEqualTo(10L);
        assertThat(savedItem.getProduct()).isEqualTo(product);
        assertThat(savedItem.getOrder()).isEqualTo(order);
        assertThat(savedItem.getCount()).isEqualTo(5);
        assertThat(savedItem.getPresentInventory()).isEqualTo(50);
    }

    @Test
    void saveItems_emptyList_returnsEmptyList() {
        List<Item> savedItems = itemService.saveItems(new ArrayList<>(), order);
        assertThat(savedItems).isEmpty();
        verify(productService, never()).findProductById(any());
        verify(productService, never()).dischargeProduct(any());
        verify(itemDao, never()).save(any());
    }

    @Test
    void saveItems_multipleItems_success() {
        var product1 = product;
        var product2 = new Product(store, 200.0, "Description B", "Product B");
        product2.setId(20L);
        product2.setInventory(100);
        var itemDto1 = new ItemDto(10L, 3);
        var itemDto2 = new ItemDto(20L, 7);

        when(productService.findProductById(10L)).thenReturn(product1);
        when(productService.findProductById(20L)).thenReturn(product2);
        when(productService.dischargeProduct(any(ProductOperationDto.class))).thenReturn(null);
        when(itemDao.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0, Item.class));

        var savedItems = itemService.saveItems(List.of(itemDto1, itemDto2), order);

        verify(productService).findProductById(10L);
        verify(productService).findProductById(20L);
        verify(productService, times(2)).dischargeProduct(any(ProductOperationDto.class));
        verify(itemDao, times(2)).save(any(Item.class));

        assertThat(savedItems).hasSize(2);
        var savedItem1 = savedItems.get(0);
        var savedItem2 = savedItems.get(1);

        assertThat(savedItem1.getPrimaryKey().getOrderId()).isEqualTo(100L);
        assertThat(savedItem1.getPrimaryKey().getProductId()).isEqualTo(10L);
        assertThat(savedItem1.getProduct()).isEqualTo(product1);
        assertThat(savedItem1.getCount()).isEqualTo(3);
        assertThat(savedItem1.getPresentInventory()).isEqualTo(50);

        assertThat(savedItem2.getPrimaryKey().getOrderId()).isEqualTo(100L);
        assertThat(savedItem2.getPrimaryKey().getProductId()).isEqualTo(20L);
        assertThat(savedItem2.getProduct()).isEqualTo(product2);
        assertThat(savedItem2.getCount()).isEqualTo(7);
        assertThat(savedItem2.getPresentInventory()).isEqualTo(100);
    }
}