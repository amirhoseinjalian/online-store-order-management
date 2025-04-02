package com.jalian.online_store_order_management.unit.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jalian.online_store_order_management.dto.ProductDto;
import com.jalian.online_store_order_management.dto.ProductFetchDto;
import com.jalian.online_store_order_management.dto.ProductOperationDto;
import com.jalian.online_store_order_management.endpoint.ProductEndpoint;
import com.jalian.online_store_order_management.exception.handler.GlobalExceptionHandler;
import com.jalian.online_store_order_management.exception.EntityNotFoundException;
import com.jalian.online_store_order_management.exception.ValidationException;
import com.jalian.online_store_order_management.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the {@link ProductEndpoint} class.
 * <p>
 * This class tests various endpoint scenarios for product-related operations such as adding a product,
 * fetching product details, charging a product, and handling validation and exception cases.
 * </p>
 *
 * @author amirhosein jalian
 */
@ExtendWith(MockitoExtension.class)
public class ProductEndpointTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private ProductEndpoint productEndpoint;

    @Mock
    private ProductService productService;

    /**
     * Initializes the mock MVC and sets up the ProductEndpoint before each test.
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productEndpoint)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    /**
     * Tests adding a product and checking the response.
     */
    @Test
    void addProduct_success() throws Exception {
        var dto = new ProductDto("Product A", "Description A", 100.0, 10L);
        when(productService.addProduct(any(ProductDto.class))).thenReturn(1L);

        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result", is(1)))
                .andExpect(jsonPath("$.message", is("Product added successfully")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    /**
     * Tests adding a product and handling validation exception.
     */
    @Test
    void addProduct_exception() throws Exception {
        var dto = new ProductDto("Product A", "Description A", 100.0, 10L);
        when(productService.addProduct(any(ProductDto.class)))
                .thenThrow(new ValidationException("Invalid product data"));

        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result", is(-1)))
                .andExpect(jsonPath("$.message", containsString("Invalid product data")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    /**
     * Tests fetching a product by ID and checking the response.
     */
    @Test
    void fetchProduct_success() throws Exception {
        var fetchDto = new ProductFetchDto(1L, "Product A", "Description A", 100.0, 50, "Test Store");
        when(productService.getProductById(1L)).thenReturn(fetchDto);

        mockMvc.perform(get("/products/find/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id", is(1)))
                .andExpect(jsonPath("$.result.name", is("Product A")))
                .andExpect(jsonPath("$.result.description", is("Description A")))
                .andExpect(jsonPath("$.result.price", is(100.0)))
                .andExpect(jsonPath("$.result.inventory", is(50)))
                .andExpect(jsonPath("$.result.storeName", is("Test Store")))
                .andExpect(jsonPath("$.message", is("Product found successfully")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    /**
     * Tests the scenario where a product is not found by ID and the appropriate exception is thrown.
     */
    @Test
    void fetchProduct_notFound_exception() throws Exception {
        when(productService.getProductById(1L))
                .thenThrow(new EntityNotFoundException("Product", "id", "1"));

        mockMvc.perform(get("/products/find/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result", is(-1)))
                .andExpect(jsonPath("$.message", containsString("Product")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    /**
     * Tests charging a product and checking the response.
     */
    @Test
    void chargeProduct_success() throws Exception {
        var opDto = new ProductOperationDto(1L, 5);
        var fetchDto = new ProductFetchDto(1L, "Product A", "Description A", 100.0, 45, "Test Store");
        when(productService.chargeProduct(any(ProductOperationDto.class))).thenReturn(fetchDto);

        mockMvc.perform(put("/products/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(opDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id", is(1)))
                .andExpect(jsonPath("$.result.name", is("Product A")))
                .andExpect(jsonPath("$.result.inventory", is(45)))
                .andExpect(jsonPath("$.message", is("Operation done successfully")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    /**
     * Tests charging a product and handling validation exception.
     */
    @Test
    void chargeProduct_exception() throws Exception {
        var opDto = new ProductOperationDto(1L, 5);
        when(productService.chargeProduct(any(ProductOperationDto.class)))
                .thenThrow(new ValidationException("Operation failed"));

        mockMvc.perform(put("/products/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(opDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result", is(-1)))
                .andExpect(jsonPath("$.message", containsString("Operation failed")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }
}
