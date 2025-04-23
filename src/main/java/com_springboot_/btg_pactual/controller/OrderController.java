package com_springboot_.btg_pactual.controller;

import com_springboot_.btg_pactual.controller.dto.ApiResponse;
import com_springboot_.btg_pactual.controller.dto.OrderResponse;
import com_springboot_.btg_pactual.controller.dto.PaginationResponse;
import com_springboot_.btg_pactual.service.OrderService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class OrderController {

    //private final Logger logger = LoggerFactory.getLogger();

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> listOrders(@PathVariable("customerId") Long customerId,
                                                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        var pageResponse = orderService.findAllByCustumerId(customerId, PageRequest.of(page, pageSize));
        var totalOnOrders = orderService.findTotalOnOrdersByCustomerId(customerId);

        return ResponseEntity.ok(new ApiResponse<>(
                Map.of("totalOnOrders", totalOnOrders),
                pageResponse.getContent(),
                PaginationResponse.fromPage(pageResponse)
        ));
        //logger.info("Page response: {}", pageResponse);
    }

}
