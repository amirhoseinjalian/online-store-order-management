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

@RestController
@RequestMapping("/orders")
public class OrderEndpoint {

    private final OrderService orderService;
    private final SyncPayServiceImpl syncPayService;
    private final ASyncPayServiceImpl asyncPayService;

    public OrderEndpoint(OrderService orderService, SyncPayServiceImpl syncPayService, ASyncPayServiceImpl asyncPayService) {
        this.orderService = orderService;
        this.syncPayService = syncPayService;
        this.asyncPayService = asyncPayService;
    }

    private ResponseEntity<BaseResponse<Long>> addOrder(AddOrderDto dto, PayService payService) {
        return ResponseEntity.ok(
                new BaseResponse<>(orderService.addOrder(dto, payService),
                        "Order created successfully"
                )
        );
    }

    @PostMapping("/add/sync")
    public ResponseEntity<BaseResponse<Long>> addOrderSync(@RequestBody AddOrderDto dto) {
        return addOrder(dto, syncPayService);
    }

    @PostMapping("/add/async")
    public ResponseEntity<BaseResponse<Long>> addOrderAsync(@RequestBody AddOrderDto dto) {
        return addOrder(dto, asyncPayService);
    }

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
