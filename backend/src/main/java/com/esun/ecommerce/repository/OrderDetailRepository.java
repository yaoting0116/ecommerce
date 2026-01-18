package com.esun.ecommerce.repository;

import com.esun.ecommerce.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository for OrderDetail entity.
 * Primary key type is Long (orderItemSn).
 */
@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    /**
     * Get all order detail rows for a given orderId.
     */
    List<OrderDetail> findByOrderId(String orderId);

    /**
     * Delete all order details for a given orderId.
     * Note: method is modifying, so a transaction is required when calling.
     */
    @Modifying
    @Transactional
    void deleteByOrderId(String orderId);

    /**
     * Sum the itemPrice for a given orderId.
     * Returns null if there are no rows.
     * Adjust return type if your entity uses BigDecimal for prices.
     */
    @Query("SELECT SUM(od.itemPrice) FROM OrderDetail od WHERE od.orderId = :orderId")
    Long sumItemPriceByOrderId(@Param("orderId") String orderId);
}
