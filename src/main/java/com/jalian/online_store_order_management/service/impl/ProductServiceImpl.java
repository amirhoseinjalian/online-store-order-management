package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.annotation.NotNull;
import com.jalian.online_store_order_management.annotation.Valid;
import com.jalian.online_store_order_management.dao.ProductDao;
import com.jalian.online_store_order_management.domain.Product;
import com.jalian.online_store_order_management.dto.ProductDto;
import com.jalian.online_store_order_management.dto.ProductFetchDto;
import com.jalian.online_store_order_management.dto.ProductOperationDto;
import com.jalian.online_store_order_management.exception.EntityNotFoundException;
import com.jalian.online_store_order_management.exception.ValidationException;
import com.jalian.online_store_order_management.factory.ProductInventoryOperatorFactory;
import com.jalian.online_store_order_management.service.ProductService;
import com.jalian.online_store_order_management.service.StoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;

    private final StoreService storeService;

    public ProductServiceImpl(StoreService storeService, ProductDao productDao) {
        this.storeService = storeService;
        this.productDao = productDao;
    }

    @Override
    @Transactional
    @Valid
    public Long addProduct(@NotNull ProductDto productDto) {
        if (!storeService.existStore(productDto.storeId())) {
            throw new ValidationException("Store does not exist");
        }
        if (productDto.name() == null || productDto.name().isEmpty()) {
            throw new ValidationException("Product name cannot be empty");
        }
        if (productDto.price() < 0) {
            throw new ValidationException("Product price cannot be a negative number");
        }
        var store = storeService.findStore(productDto.storeId());
        var product = new Product(store, productDto.price(), productDto.description(), productDto.name());
        return productDao.save(product).getId();
    }

    @Override
    @Transactional(readOnly = true)
    @Valid
    public ProductFetchDto getProductById(@NotNull Long productId) {
        var product = findEntityById(productId);
        return ProductFetchDto.of(product);
    }

    private Product findEntityById(Long productId) {
        var product = productDao.findByIdSafe(productId);
        if (product.isEmpty()) {
            throw new EntityNotFoundException("Product with id " + productId + " does not exist");
        }
        return product.get();
    }

    @Transactional(readOnly = true)
    public Product findProductById(@NotNull Long productId) {
        return findEntityById(productId);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    @Valid
    public ProductFetchDto doOperation(@NotNull ProductOperationDto dto) {
        var product = findEntityById(dto.productId());
        var operator = ProductInventoryOperatorFactory.getInstance(dto.strategy());
        product = operator.doOperation(product, dto.amount());
        product = productDao.save(product);
        return ProductFetchDto.of(product);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean belongsToStore(Long productId, Long storeId) {
        var product = findEntityById(productId);
        var store = storeService.findStore(storeId);
        return product.getStore().equals(store);
    }
}
