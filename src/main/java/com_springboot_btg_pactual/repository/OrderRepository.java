package com_springboot_btg_pactual.repository;

import com_springboot_btg_pactual.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderEntity, Long>  {

    Page<OrderEntity> findAllByCustomerId(Long custumerId, PageRequest pageRequest);
}
