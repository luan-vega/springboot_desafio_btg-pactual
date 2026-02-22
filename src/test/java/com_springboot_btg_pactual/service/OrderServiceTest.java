package com_springboot_btg_pactual.service;

import com_springboot_btg_pactual.entity.OrderEntity;
import com_springboot_btg_pactual.entity.OrderItem;
import com_springboot_btg_pactual.listener.dto.OrderCreatedEvent;
import com_springboot_btg_pactual.listener.dto.OrderItemEvent;
import com_springboot_btg_pactual.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("OrderService Unit Tests")
class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private OrderService orderService;

	private OrderCreatedEvent testEvent;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		List<OrderItemEvent> items = List.of(
			new OrderItemEvent("Lápis", 100, new BigDecimal("1.10")),
			new OrderItemEvent("Caderno", 10, new BigDecimal("1.00"))
		);
		testEvent = new OrderCreatedEvent(1001L, 1L, items);
	}

	@Test
	@DisplayName("Deve salvar pedido com total calculado corretamente")
	void shouldSaveOrderWithCalculatedTotal() {
		ArgumentCaptor<OrderEntity> captor = ArgumentCaptor.forClass(OrderEntity.class);

		orderService.save(testEvent);

		verify(orderRepository, times(1)).save(captor.capture());
		OrderEntity savedOrder = captor.getValue();

		assertEquals(1001L, savedOrder.getOrderId());
		assertEquals(1L, savedOrder.getCustomerId());
		assertEquals(new BigDecimal("120.00"), savedOrder.getTotal());
		assertEquals(2, savedOrder.getItems().size());
	}

	@Test
	@DisplayName("Deve mapear itens do evento para entidade corretamente")
	void shouldMapOrderItemsFromEventCorrectly() {
		ArgumentCaptor<OrderEntity> captor = ArgumentCaptor.forClass(OrderEntity.class);

		orderService.save(testEvent);

    	verify(orderRepository).save(captor.capture());
		List<OrderItem> items = captor.getValue().getItems();

		assertEquals(2, items.size());
		assertEquals("Lápis", items.get(0).getProduct());
		assertEquals(100, items.get(0).getQuantity());
		assertEquals(new BigDecimal("1.10"), items.get(0).getPrice());
	}

	@Test
	@DisplayName("Deve buscar pedidos paginados por cliente")
	void shouldFindOrdersByCustomerIdWithPagination() {
		// Arrange
		Long customerId = 1L;
		PageRequest pageRequest = PageRequest.of(0, 10);

		OrderEntity order = new OrderEntity();
		order.setOrderId(1001L);
		order.setCustomerId(customerId);
		order.setTotal(new BigDecimal("120.00"));

		Page<OrderEntity> expectedPage = new PageImpl<>(List.of(order), pageRequest, 1);
		when(orderRepository.findAllByCustomerId(customerId, pageRequest))
			.thenReturn(expectedPage);

		// Act
		Page<OrderEntity> result = orderRepository.findAllByCustomerId(customerId, pageRequest);

		// Assert
		assertEquals(1, result.getTotalElements());
		assertEquals(1, result.getContent().size());
		assertEquals(1001L, result.getContent().get(0).getOrderId());
		verify(orderRepository, times(1)).findAllByCustomerId(customerId, pageRequest);
	}

	@Test
	@DisplayName("Deve retornar zero para cliente sem pedidos")
	void shouldReturnZeroWhenNoOrdersExist() {
		Long customerId = 99999L;
		PageRequest pageRequest = PageRequest.of(0, 10);

		Page<OrderEntity> emptyPage = new PageImpl<>(List.of(), pageRequest, 0);
		when(orderRepository.findAllByCustomerId(customerId, pageRequest))
			.thenReturn(emptyPage);

		Page<OrderEntity> result = orderRepository.findAllByCustomerId(customerId, pageRequest);

		assertEquals(0, result.getTotalElements());
		assertTrue(result.getContent().isEmpty());
	}

	@Test
	@DisplayName("Deve calcular total consolidado com múltiplos pedidos")
	void shouldCalculateTotalConsolidatedWithMultipleOrders() {
		List<OrderItemEvent> items1 = List.of(
			new OrderItemEvent("Produto A", 2, new BigDecimal("50.00"))
		);
		OrderCreatedEvent event1 = new OrderCreatedEvent(1001L, 1L, items1);

		List<OrderItemEvent> items2 = List.of(
			new OrderItemEvent("Produto B", 1, new BigDecimal("75.00"))
		);
		OrderCreatedEvent event2 = new OrderCreatedEvent(1002L, 1L, items2);

		orderService.save(event1);
		orderService.save(event2);

		verify(orderRepository, times(2)).save(any(OrderEntity.class));
	}

	@Test
	@DisplayName("Deve lidar com evento com um único item")
	void shouldHandleOrderWithSingleItem() {
		List<OrderItemEvent> items = List.of(
			new OrderItemEvent("Lápis", 10, new BigDecimal("1.00"))
		);
		OrderCreatedEvent event = new OrderCreatedEvent(2001L, 2L, items);

		orderService.save(event);

		verify(orderRepository, times(1)).save(argThat(order ->
			order.getOrderId() == 2001L &&
			order.getCustomerId() == 2L &&
			order.getTotal().equals(new BigDecimal("10.00")) &&
			order.getItems().size() == 1
		));
	}

	@Test
	@DisplayName("Deve lidar com valores decimais precisos")
	void shouldHandlePreciseDecimalValues() {
		List<OrderItemEvent> items = List.of(
			new OrderItemEvent("Produto", 3, new BigDecimal("10.99"))
		);
		OrderCreatedEvent event = new OrderCreatedEvent(3001L, 3L, items);

		orderService.save(event);

		verify(orderRepository).save(argThat(order ->
			order.getTotal().equals(new BigDecimal("32.97")) // 3 × 10.99
		));
	}
}

