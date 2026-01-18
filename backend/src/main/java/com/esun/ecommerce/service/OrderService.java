package com.esun.ecommerce.service;

import com.esun.ecommerce.dto.OrderRequestDTO;
import com.esun.ecommerce.entity.OrderDetail;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public List<OrderDetail> createOrder(OrderRequestDTO request) {
        List<OrderDetail> result = new ArrayList<>();

        for (OrderRequestDTO.OrderItemDTO item : request.getItems()) {
            OrderDetail od = new OrderDetail();
            od.setOrderId("ORD-" + System.currentTimeMillis()); // 範例自動生成訂單ID
            od.setProductId(item.getProductId());
            od.setQuantity(item.getQuantity());
            od.setStandPrice(100); // 範例固定價格
            od.setItemPrice(item.getQuantity() * 100);

            em.persist(od);
            result.add(od);
        }

        return result;
    }
}
