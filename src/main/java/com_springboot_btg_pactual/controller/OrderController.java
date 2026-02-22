package com_springboot_btg_pactual.controller;

import com_springboot_btg_pactual.controller.dto.ApiResponse;
import com_springboot_btg_pactual.controller.dto.OrderResponse;
import com_springboot_btg_pactual.controller.dto.PaginationResponse;
import com_springboot_btg_pactual.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// C:/Users/luand/OneDrive/Documentos/Estudos/TI/Desafios/springboot_desafio_btg-pactual/src/main/java/com_springboot_/btg_pactual/controller/OrderController.java
@Slf4j
@RestController
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> listOrders(@PathVariable("customerId") Long customerId,
                                                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        // 1. Busca os pedidos de forma paginada
        var pageResponse = orderService.findAllByCustumerId(customerId, PageRequest.of(page, pageSize));

        // 2. Busca o valor total de todos os pedidos do cliente
        var totalOnOrders = orderService.findTotalOnOrdersByCustomerId(customerId);

        // 3. Monta e retorna a resposta final
        return ResponseEntity.ok(new ApiResponse<>(
                Map.of("totalOnOrders", totalOnOrders),
                pageResponse.getContent(),
                PaginationResponse.fromPage(pageResponse)
        ));
        
       // log.info("Page response: {}", pageResponse);
    }
}

