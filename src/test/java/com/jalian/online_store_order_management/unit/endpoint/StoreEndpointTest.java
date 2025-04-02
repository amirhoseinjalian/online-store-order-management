package com.jalian.online_store_order_management.unit.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jalian.online_store_order_management.dto.AddStoreDto;
import com.jalian.online_store_order_management.endpoint.StoreEndpoint;
import com.jalian.online_store_order_management.exception.ConstraintViolationException;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the {@link StoreEndpoint} class.
 * <p>
 * This class tests the store-related endpoints such as adding a store and handling exception cases.
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
}
