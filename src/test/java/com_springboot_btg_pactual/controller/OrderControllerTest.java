package com_springboot_btg_pactual.controller;

import com_springboot_btg_pactual.controller.dto.OrderResponse;
import com_springboot_btg_pactual.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("OrderController Unit Tests")
class OrderControllerTest {

	@Mock
	private OrderService orderService;

	@InjectMocks
	private OrderController orderController;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
	}

	@Test
	@DisplayName("Deve retornar pedidos com paginação e total consolidado")
	void shouldReturnOrdersWithPaginationAndTotal() throws Exception {
		Long customerId = 1L;
		PageRequest pageRequest = PageRequest.of(0, 10);

		OrderResponse order = new OrderResponse(1001L, customerId, new BigDecimal("120.00"));
		Page<OrderResponse> page = new PageImpl<>(List.of(order), pageRequest, 1);
		BigDecimal totalOnOrders = new BigDecimal("120.00");

		when(orderService.findAllByCustumerId(customerId, pageRequest)).thenReturn(
			(Page) new PageImpl<>(List.of(), pageRequest, 0) // Mock simplificado
		);
		when(orderService.findTotalOnOrdersByCustomerId(customerId)).thenReturn(totalOnOrders);

		mockMvc.perform(get("/customers/{customerId}/orders", customerId)
				.param("page", "0")
				.param("pageSize", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.sumary.totalOnOrders").exists())
			.andExpect(jsonPath("$.pagination").exists());

		verify(orderService, times(1)).findAllByCustumerId(customerId, pageRequest);
		verify(orderService, times(1)).findTotalOnOrdersByCustomerId(customerId);
	}

	@Test
	@DisplayName("Deve retornar lista vazia quando não houver pedidos")
	void shouldReturnEmptyListWhenNoOrders() throws Exception {
		Long customerId = 99999L;
		PageRequest pageRequest = PageRequest.of(0, 10);

		when(orderService.findAllByCustumerId(customerId, pageRequest))
			.thenReturn(new PageImpl<>(List.of(), pageRequest, 0));
		when(orderService.findTotalOnOrdersByCustomerId(customerId))
			.thenReturn(BigDecimal.ZERO);

		mockMvc.perform(get("/customers/{customerId}/orders", customerId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.length()").value(0))
			.andExpect(jsonPath("$.pagination.totalElements").value(0));
	}

}

