package com.jalian.online_store_order_management.endpoint;

import com.jalian.online_store_order_management.dto.AddOrderDto;
import com.jalian.online_store_order_management.dto.OrderFetchDto;
import com.jalian.online_store_order_management.service.OrderService;
import com.jalian.online_store_order_management.service.PayService;
import com.jalian.online_store_order_management.service.impl.ASyncPayServiceImpl;
import com.jalian.online_store_order_management.service.impl.SyncPayServiceImpl;
import com.jalian.online_store_order_management.web.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The OrderEndpoint class provides REST endpoints for managing orders in the online store order management system.
 * <p>
 * It supports order creation using both synchronous and asynchronous payment services and retrieving order details by ID.
 * </p>
 *
 * @author amirhosein jalian
 */
@RestController
@RequestMapping("/orders")
public class OrderEndpoint {

    private final OrderService orderService;
    private final SyncPayServiceImpl syncPayService;
    private final ASyncPayServiceImpl asyncPayService;

    /**
     * Constructs an OrderEndpoint with the specified services.
     *
     * @param orderService   the service used for order management.
     * @param syncPayService the synchronous payment service implementation.
     * @param asyncPayService the asynchronous payment service implementation.
     */
    public OrderEndpoint(OrderService orderService, SyncPayServiceImpl syncPayService, ASyncPayServiceImpl asyncPayService) {
        this.orderService = orderService;
        this.syncPayService = syncPayService;
        this.asyncPayService = asyncPayService;
    }

    /**
     * Adds a new order using the specified payment service.
     *
     * @param dto         the data transfer object containing order details.
     * @param payService  the payment service to process the payment for the order.
     * @return a ResponseEntity containing a BaseResponse with the created order ID.
     */
    private ResponseEntity<BaseResponse<Long>> addOrder(AddOrderDto dto, PayService payService) {
        return ResponseEntity.ok(
                new BaseResponse<>(orderService.addOrder(dto, payService),
                        "Order created successfully"
                )
        );
    }

    /**
     * Creates a new order using the synchronous payment service.
     *
     * @param dto the data transfer object containing order details.
     * @return a ResponseEntity containing a BaseResponse with the created order ID.
     */
    @PostMapping("/add/sync")
    public ResponseEntity<BaseResponse<Long>> addOrderSync(@RequestBody AddOrderDto dto) {
        return addOrder(dto, syncPayService);
    }

    /**
     * Creates a new order using the asynchronous payment service.
     *
     * @param dto the data transfer object containing order details.
     * @return a ResponseEntity containing a BaseResponse with the created order ID.
     */
    @PostMapping("/add/async")
    public ResponseEntity<BaseResponse<Long>> addOrderAsync(@RequestBody AddOrderDto dto) {
        return addOrder(dto, asyncPayService);
    }

    /**
     * Retrieves an order by its unique identifier.
     *
     * @param id the unique identifier of the order.
     * @return a ResponseEntity containing a BaseResponse with the fetched order details.
     */
    @GetMapping("/find/{id}")
    public ResponseEntity<BaseResponse<OrderFetchDto>> findOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(
                new BaseResponse<>(
                        orderService.findOrderById(id),
                        "Order fetched successfully"
                )
        );
    }
}
