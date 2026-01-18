package com.esun.ecommerce.repository;

import com.esun.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Order entity.
 * Primary key type is String (orderId).
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    /**
     * Find an order by its orderId.
     * (Spring Data will implement this automatically.)
     */
    Optional<Order> findByOrderId(String orderId);

    /**
     * Find all orders with a specific payStatus.
     * Example: 0 = unpaid, 1 = paid
     */
    List<Order> findByPayStatus(Integer payStatus);

    /**
     * Find orders by a list of orderIds.
     */
    List<Order> findByOrderIdIn(List<String> orderIds);
}
