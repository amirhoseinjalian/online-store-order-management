package com.jalian.online_store_order_management.service;

import com.jalian.online_store_order_management.domain.Product;
import com.jalian.online_store_order_management.dto.ProductDto;
import com.jalian.online_store_order_management.dto.ProductFetchDto;
import com.jalian.online_store_order_management.dto.ProductOperationDto;

/**
 * The ProductService interface defines operations for managing products in the system.
 * <p>
 * It includes methods for adding new products, retrieving product details, performing inventory operations
 * (both charging and discharging), verifying store ownership, and finding products by their unique identifier.
 * </p>
 *
 * @author amirhosein jalian
 */
public interface ProductService {

    /**
     * Adds a new product to the system.
     * <p>
     * This method creates a new product using the details provided in the {@link ProductDto} and returns
     * the unique identifier of the newly created product.
     * </p>
     *
     * @param productDto the data transfer object containing product details.
     * @return the unique identifier of the newly added product.
     */
    Long addProduct(ProductDto productDto);

    /**
     * Retrieves a product by its unique identifier.
     * <p>
     * This method returns a {@link ProductFetchDto} that contains detailed information about the product.
     * </p>
     *
     * @param productId the unique identifier of the product.
     * @return a {@link ProductFetchDto} with the product details.
     */
    ProductFetchDto getProductById(Long productId);

    /**
     * Charges (increases) a product's inventory.
     * <p>
     * This method applies an inventory charging operation based on the provided {@link ProductOperationDto}
     * and returns an updated {@link ProductFetchDto} representing the product's current state.
     * </p>
     *
     * @param dto the data transfer object containing the product ID and the amount to charge.
     * @return a {@link ProductFetchDto} with the updated product details.
     */
    ProductFetchDto chargeProduct(ProductOperationDto dto);

    /**
     * Discharges (decreases) a product's inventory.
     * <p>
     * This method applies an inventory discharging operation based on the provided {@link ProductOperationDto}
     * and returns an updated {@link ProductFetchDto} representing the product's current state.
     * </p>
     *
     * @param dto the data transfer object containing the product ID and the amount to discharge.
     * @return a {@link ProductFetchDto} with the updated product details.
     */
    ProductFetchDto dischargeProduct(ProductOperationDto dto);

    /**
     * Verifies whether a product belongs to a specific store.
     * <p>
     * This method checks if the product identified by the given productId is associated with the store
     * identified by the given storeId.
     * </p>
     *
     * @param productId the unique identifier of the product.
     * @param storeId   the unique identifier of the store.
     * @return {@code true} if the product belongs to the specified store; {@code false} otherwise.
     */
    boolean belongsToStore(Long productId, Long storeId);

    /**
     * Finds and returns a product by its unique identifier.
     * <p>
     * This method is used internally to retrieve a {@link Product} entity from the database.
     * </p>
     *
     * @param productId the unique identifier of the product.
     * @return the {@link Product} entity corresponding to the given productId.
     */
    Product findProductById(Long productId);
}
