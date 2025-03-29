package com.jalian.online_store_order_management.service.impl;

import com.jalian.online_store_order_management.annotation.NotNull;
import com.jalian.online_store_order_management.annotation.Valid;
import com.jalian.online_store_order_management.dao.ProductDao;
import com.jalian.online_store_order_management.domain.Product;
import com.jalian.online_store_order_management.dto.ProductDto;
import com.jalian.online_store_order_management.dto.ProductFetchDto;
import com.jalian.online_store_order_management.dto.ProductOperationDto;
import com.jalian.online_store_order_management.exception.RecoveryException;
import com.jalian.online_store_order_management.exception.ValidationException;
import com.jalian.online_store_order_management.factory.ProductInventoryOperatorFactory;
import com.jalian.online_store_order_management.service.ProductService;
import com.jalian.online_store_order_management.service.StoreService;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.StaleObjectStateException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Valid
    public ProductFetchDto getProductById(@NotNull Long productId) {
        return ProductFetchDto.of(productDao.findByIdSafe(productId));
    }

    @Override
    @Transactional
    @Retryable(
            retryFor = {
                    OptimisticLockException.class,
                    StaleObjectStateException.class,
                    ObjectOptimisticLockingFailureException.class,
                    PessimisticLockingFailureException.class,
            },
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000),
            recover = "recoverDoOperation"
    )
    @Valid
    public ProductFetchDto doOperation(@NotNull ProductOperationDto dto) {
        var product = productDao.findByIdSafe(dto.productId());
        var operator = ProductInventoryOperatorFactory.getInstance(dto.strategy());
        product = operator.doOperation(product, dto.amount());
        return ProductFetchDto.of(productDao.save(product));
    }

    @Recover
    public ProductFetchDto recoverDoOperation(Exception e, ProductOperationDto dto) {
        throw new RecoveryException("Operation failed. Another operation might be performing");
    }
}
