package com.jalian.online_store_order_management.endpoint;

import com.jalian.online_store_order_management.dto.ProductDto;
import com.jalian.online_store_order_management.dto.ProductFetchDto;
import com.jalian.online_store_order_management.dto.ProductOperationDto;
import com.jalian.online_store_order_management.service.ProductService;
import com.jalian.online_store_order_management.web.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The ProductEndpoint class provides REST endpoints for managing products in the system.
 * <p>
 * It supports adding a new product, fetching product details by ID, and performing operations
 * on a product's inventory.
 * </p>
 *
 * @author amirhosein jalian
 */
@RestController
@RequestMapping("/products")
public class ProductEndpoint {

    private final ProductService productService;

    /**
     * Constructs a new ProductEndpoint with the specified ProductService.
     *
     * @param productService the service used for product operations.
     */
    public ProductEndpoint(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Adds a new product to the system.
     *
     * @param productDto the product data transfer object containing product details.
     * @return a ResponseEntity containing a BaseResponse with the created product ID.
     */
    @PostMapping("/add")
    public ResponseEntity<BaseResponse<Long>> addProduct(@RequestBody ProductDto productDto) {
        var savedProduct = productService.addProduct(productDto);
        return new ResponseEntity<>(
                new BaseResponse<>(savedProduct, "Product added successfully"),
                HttpStatus.CREATED
        );
    }

    /**
     * Fetches a product by its unique identifier.
     *
     * @param id the unique identifier of the product.
     * @return a ResponseEntity containing a BaseResponse with the product details.
     */
    @GetMapping("/find/{id}")
    public ResponseEntity<BaseResponse<ProductFetchDto>> fetchProduct(@PathVariable Long id) {
        return new ResponseEntity<>(
                new BaseResponse<>(productService.getProductById(id), "Product found successfully"),
                HttpStatus.OK
        );
    }

    /**
     * Performs an operation on a product's inventory.
     * <p>
     * This endpoint supports operations such as increasing or decreasing the product inventory.
     * </p>
     *
     * @param productOperationDto the product operation data transfer object containing the product ID and the operation amount.
     * @return a ResponseEntity containing a BaseResponse with the updated product details.
     */
    @PutMapping("/charge")
    public ResponseEntity<BaseResponse<ProductFetchDto>> doOperation(@RequestBody ProductOperationDto productOperationDto) {
        return new ResponseEntity<>(
                new BaseResponse<>(
                        productService.chargeProduct(productOperationDto),
                        "Operation done successfully"
                ),
                HttpStatus.OK
        );
    }
}
