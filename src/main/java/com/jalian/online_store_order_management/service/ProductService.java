package com.jalian.online_store_order_management.service;

import com.jalian.online_store_order_management.domain.Product;
import com.jalian.online_store_order_management.dto.ProductDto;
import com.jalian.online_store_order_management.dto.ProductFetchDto;
import com.jalian.online_store_order_management.dto.ProductOperationDto;

public interface ProductService {

    Long addProduct(ProductDto productDto);

    ProductFetchDto getProductById(Long productId);

    ProductFetchDto chargeProduct(ProductOperationDto dto);

    ProductFetchDto dischargeProduct(ProductOperationDto dto);

    boolean belongsToStore(Long productId, Long storeId);

    Product findProductById(Long productId);
}
