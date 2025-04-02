package com.jalian.online_store_order_management.unit.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jalian.online_store_order_management.constant.OrderStatus;
import com.jalian.online_store_order_management.dto.AddOrderDto;
import com.jalian.online_store_order_management.dto.OrderFetchDto;
import com.jalian.online_store_order_management.exception.EntityNotFoundException;
import com.jalian.online_store_order_management.exception.ValidationException;
import com.jalian.online_store_order_management.endpoint.OrderEndpoint;
import com.jalian.online_store_order_management.service.OrderService;
import com.jalian.online_store_order_management.service.PayService;
import com.jalian.online_store_order_management.service.impl.ASyncPayServiceImpl;
import com.jalian.online_store_order_management.service.impl.SyncPayServiceImpl;
import com.jalian.online_store_order_management.web.BaseResponse;
import com.jalian.online_store_order_management.exception.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the {@link OrderEndpoint} class.
 * <p>
 * This class tests various endpoint scenarios such as adding orders synchronously and asynchronously,
 * fetching orders by ID, and handling validation and exception cases.
 * </p>
 *
 * @author amirhosein jalian
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OrderEndpointTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private OrderEndpoint orderEndpoint;

    @Mock
    private OrderService orderService;
    @Mock
    private SyncPayServiceImpl syncPayService;
    @Mock
    private ASyncPayServiceImpl asyncPayService;

    /**
     * Initializes the mock MVC and sets up the OrderEndpoint before each test.
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderEndpoint)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    /**
     * Tests adding an order synchronously and checking the response.
     */
    @Test
    void addOrderSync_success() throws Exception {
        var dto = new AddOrderDto(1L, 1L, List.of());
        when(orderService.addOrder(any(AddOrderDto.class), any(PayService.class))).thenReturn(100L);

        mockMvc.perform(post("/orders/add/sync")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", equalTo(100)))
                .andExpect(jsonPath("$.message", is("Order created successfully")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    /**
     * Tests adding an order asynchronously and checking the response.
     */
    @Test
    void addOrderAsync_success() throws Exception {
        var dto = new AddOrderDto(1L, 1L, List.of());
        when(orderService.addOrder(any(AddOrderDto.class), any(PayService.class))).thenReturn(200L);

        mockMvc.perform(post("/orders/add/async")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(200)))
                .andExpect(jsonPath("$.message", is("Order created successfully")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    /**
     * Tests fetching an order by ID and checking the response.
     */
    @Test
    void findOrderById_success() throws Exception {
        var orderFetchDto = new OrderFetchDto(100L, null, null, OrderStatus.FINISHED, List.of());
        when(orderService.findOrderById(100L)).thenReturn(orderFetchDto);

        mockMvc.perform(get("/orders/find/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.orderId", is(100)))
                .andExpect(jsonPath("$.result.status", is("FINISHED")))
                .andExpect(jsonPath("$.message", is("Order fetched successfully")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    /**
     * Tests the scenario where an order is not found by ID and the appropriate exception is thrown.
     */
    @Test
    void findOrderById_notFound_exception() throws Exception {
        when(orderService.findOrderById(100L))
                .thenThrow(new EntityNotFoundException("Order", "id", "100"));

        mockMvc.perform(get("/orders/find/100"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result", is(-1)))
                .andExpect(jsonPath("$.message", containsString("Order")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    /**
     * Tests adding an order synchronously and handling validation exception.
     */
    @Test
    void addOrderSync_validationException() throws Exception {
        var dto = new AddOrderDto(1L, 1L, List.of());
        when(orderService.addOrder(any(AddOrderDto.class), any(PayService.class)))
                .thenThrow(new ValidationException("Validation failed"));

        mockMvc.perform(post("/orders/add/sync")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result", is(-1)))
                .andExpect(jsonPath("$.message", containsString("Validation failed")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }
}
