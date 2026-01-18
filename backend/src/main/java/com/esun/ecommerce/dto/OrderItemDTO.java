package com.esun.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 單一訂單項目 DTO
 */
public class OrderItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "productId 不可為空")
    private String productId;

    @Min(value = 1, message = "quantity 必須 >= 1")
    private Integer quantity;

    public OrderItemDTO() {
    }

    public OrderItemDTO(String productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderItemDTO{" +
                "productId='" + productId + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
