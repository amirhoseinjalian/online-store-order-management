package com.jalian.online_store_order_management.unit.service;

import com.jalian.online_store_order_management.dao.ProductDao;
import com.jalian.online_store_order_management.domain.Product;
import com.jalian.online_store_order_management.domain.Store;
import com.jalian.online_store_order_management.dto.ProductDto;
import com.jalian.online_store_order_management.dto.ProductOperationDto;
import com.jalian.online_store_order_management.exception.EntityNotFoundException;
import com.jalian.online_store_order_management.exception.ValidationException;
import com.jalian.online_store_order_management.service.StoreService;
import com.jalian.online_store_order_management.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private StoreService storeService;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductDto productDto;
    private Store store;
    private Product product;

    @BeforeEach
    void setUp() {
        store = new Store("Test Store");
        store.setId(1L);
        productDto = new ProductDto("Product A", "Description A", 100.0, 1L);
        product = new Product(store, 100.0, "Description A", "Product A");
        product.setId(1L);
    }

    @Test
    void addProduct_success() {
        when(storeService.existStore(1L)).thenReturn(true);
        when(storeService.findStore(1L)).thenReturn(store);
        when(productDao.save(any(Product.class))).thenAnswer(invocation -> {
            var p = invocation.getArgument(0, Product.class);
            p.setId(1L);
            return p;
        });
        var id = productService.addProduct(productDto);
        assertThat(id).isEqualTo(1L);
        verify(storeService).existStore(1L);
        verify(storeService).findStore(1L);
        verify(productDao).save(any(Product.class));
    }

    @Test
    void addProduct_storeNotExist_throwsException() {
        when(storeService.existStore(1L)).thenReturn(false);
        var ex = assertThrows(ValidationException.class, () -> productService.addProduct(productDto));
        assertThat(ex.getMessage()).contains("Store does not exist");
        verify(storeService).existStore(1L);
    }

    @Test
    void addProduct_emptyName_throwsException() {
        var dto = new ProductDto("", "Desc", 50.0, 1L);
        when(storeService.existStore(1L)).thenReturn(true);
        var ex = assertThrows(ValidationException.class, () -> productService.addProduct(dto));
        assertThat(ex.getMessage()).contains("Product name cannot be empty");
    }

    @Test
    void addProduct_negativePrice_throwsException() {
        var dto = new ProductDto("Product B", "Desc", -10.0, 1L);
        when(storeService.existStore(1L)).thenReturn(true);
        var ex = assertThrows(ValidationException.class, () -> productService.addProduct(dto));
        assertThat(ex.getMessage()).contains("Product price cannot be a negative number");
    }

    @Test
    void getProductById_success() {
        when(productDao.findByIdSafe(1L)).thenReturn(Optional.of(product));
        var result = productService.getProductById(1L);
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Product A");
        assertThat(result.storeName()).isEqualTo("Test Store");
        verify(productDao).findByIdSafe(1L);
    }

    @Test
    void getProductById_notFound_throwsException() {
        when(productDao.findByIdSafe(2L)).thenReturn(Optional.empty());
        var ex = assertThrows(EntityNotFoundException.class, () -> productService.getProductById(2L));
        assertThat(ex.getMessage()).contains("Product with id 2 does not exist");
        verify(productDao).findByIdSafe(2L);
    }

    @Test
    void dischargeProduct_success() {
        product.setInventory(10);
        var dto = new ProductOperationDto(1L, 3);
        when(productDao.findByIdSafe(1L)).thenReturn(Optional.of(product));
        when(productDao.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0, Product.class));
        var result = productService.dischargeProduct(dto);
        assertThat(result.inventory()).isEqualTo(7);
        verify(productDao).findByIdSafe(1L);
        verify(productDao).save(any(Product.class));
    }

    @Test
    void chargeProduct_success() {
        product.setInventory(10);
        var dto = new ProductOperationDto(1L, 5);
        when(productDao.findByIdSafe(1L)).thenReturn(Optional.of(product));
        when(productDao.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0, Product.class));
        var result = productService.chargeProduct(dto);
        assertThat(result.inventory()).isEqualTo(15);
        verify(productDao).findByIdSafe(1L);
        verify(productDao).save(any(Product.class));
    }

    @Test
    void belongsToStore_true() {
        when(productDao.findByIdSafe(1L)).thenReturn(Optional.of(product));
        when(storeService.findStore(1L)).thenReturn(store);
        var result = productService.belongsToStore(1L, 1L);
        assertThat(result).isTrue();
        verify(productDao).findByIdSafe(1L);
        verify(storeService).findStore(1L);
    }

    @Test
    void belongsToStore_false() {
        var otherStore = new Store("Other Store");
        otherStore.setId(2L);
        when(productDao.findByIdSafe(1L)).thenReturn(Optional.of(product));
        when(storeService.findStore(2L)).thenReturn(otherStore);
        var result = productService.belongsToStore(1L, 2L);
        assertThat(result).isFalse();
        verify(productDao).findByIdSafe(1L);
        verify(storeService).findStore(2L);
    }
}