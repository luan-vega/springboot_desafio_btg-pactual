package com_springboot_btg_pactual.listener.dto;

import java.math.BigDecimal;

public record OrderItemEvent(String produto,
                            Integer quantidade,
                            BigDecimal preco) {
}
