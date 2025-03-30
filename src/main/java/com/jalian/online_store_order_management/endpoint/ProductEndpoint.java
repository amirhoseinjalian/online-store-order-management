package com.jalian.online_store_order_management.endpoint;

import com.jalian.online_store_order_management.dto.ProductDto;
import com.jalian.online_store_order_management.dto.ProductFetchDto;
import com.jalian.online_store_order_management.dto.ProductOperationDto;
import com.jalian.online_store_order_management.service.ProductService;
import com.jalian.online_store_order_management.web.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductEndpoint {

    private final ProductService productService;

    public ProductEndpoint(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse<Long>> addProduct(@RequestBody ProductDto productDto) {
        var savedProduct = productService.addProduct(productDto);
        return new ResponseEntity<>(
                new BaseResponse<>(savedProduct, "product added successfully"),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<BaseResponse<ProductFetchDto>> fetchProduct(@PathVariable Long id) {
        return new ResponseEntity<>(
                new BaseResponse<>(productService.getProductById(id), "Product found successfully"),
                HttpStatus.OK
        );
    }

    @PutMapping("/discharge")
    public ResponseEntity<BaseResponse<ProductFetchDto>> doOperation(@RequestBody ProductOperationDto ProductOperationDto) {
        return new ResponseEntity<>(
                new BaseResponse<>(
                        productService.dischargeProduct(ProductOperationDto),
                        "Operation done successfully"
                ),
                HttpStatus.OK
        );
    }
}
