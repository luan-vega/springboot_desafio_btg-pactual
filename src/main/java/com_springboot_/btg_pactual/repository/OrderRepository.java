package com_springboot_.btg_pactual.repository;

import com_springboot_.btg_pactual.entity.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderEntity, Long>  {
}
