package com.jalian.online_store_order_management.unit.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jalian.online_store_order_management.dto.AddStoreDto;
import com.jalian.online_store_order_management.dto.AddUserToStoreDto;
import com.jalian.online_store_order_management.dto.UserFetchDto;
import com.jalian.online_store_order_management.endpoint.StoreEndpoint;
import com.jalian.online_store_order_management.exception.ConstraintViolationException;
import com.jalian.online_store_order_management.exception.EntityNotFoundException;
import com.jalian.online_store_order_management.exception.handler.GlobalExceptionHandler;
import com.jalian.online_store_order_management.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the {@link StoreEndpoint} class.
 * <p>
 * This class tests the store-related endpoints such as adding a store and adding a user to a store.
 * It covers both successful operations and error scenarios.
 * </p>
 *
 * @author amirhosein jalian
 */
@ExtendWith(MockitoExtension.class)
public class StoreEndpointTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private StoreEndpoint storeEndpoint;

    @Mock
    private StoreService storeService;

    /**
     * Sets up the MockMvc instance before each test.
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(storeEndpoint)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    /**
     * Tests the successful creation of a store.
     */
    @Test
    void addStore_success() throws Exception {
        AddStoreDto dto = new AddStoreDto("Test Store");
        when(storeService.addStore(any(AddStoreDto.class))).thenReturn(1L);
        mockMvc.perform(post("/stores/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result", is(1)))
                .andExpect(jsonPath("$.message", is("Store added successfully")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    /**
     * Tests the scenario where store creation fails due to invalid data.
     */
    @Test
    void addStore_exception() throws Exception {
        AddStoreDto dto = new AddStoreDto("Test Store");
        when(storeService.addStore(any(AddStoreDto.class)))
                .thenThrow(new ConstraintViolationException("Invalid store data"));
        mockMvc.perform(post("/stores/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result", is(-1)))
                .andExpect(jsonPath("$.message", containsString("Invalid store data")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    /**
     * Tests the successful addition of a user to a store.
     * <p>
     * This test verifies that when a valid {@link AddUserToStoreDto} is provided,
     * the service method returns an updated list of users as {@link UserFetchDto} objects,
     * and the endpoint returns an HTTP OK status with the expected response body.
     * </p>
     */
    @Test
    void addUserToStore_success() throws Exception {
        var userList = List.of(new UserFetchDto("John", "Doe", "john@example.com", "1", "john", 100.0));
        var dto = new AddUserToStoreDto(1L, 1L);
        when(storeService.addUserToStore(any(AddUserToStoreDto.class))).thenReturn(userList);

        mockMvc.perform(put("/stores/add-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", hasSize(1)))
                .andExpect(jsonPath("$.result[0].username", is("john")))
                .andExpect(jsonPath("$.message", is("User added to store successfully")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    /**
     * Tests the scenario where adding a user to a store fails.
     * <p>
     * This test verifies that when the service method throws an {@link EntityNotFoundException},
     * the endpoint returns an HTTP 404 status along with an error response containing a negative result code.
     * </p>
     */
    @Test
    void addUserToStore_exception() throws Exception {
        var dto = new AddUserToStoreDto(1L, 1L);
        when(storeService.addUserToStore(any(AddUserToStoreDto.class)))
                .thenThrow(new EntityNotFoundException("Store", "id", "1"));

        mockMvc.perform(put("/stores/add-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result", is(-1)))
                .andExpect(jsonPath("$.message", containsString("Store")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }
}