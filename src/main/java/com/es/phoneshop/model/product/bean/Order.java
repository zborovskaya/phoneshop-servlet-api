package com.es.phoneshop.model.product.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Order implements Serializable {
    private List<CartItem> items;
    private BigDecimal subTotalCost;
    private BigDecimal deliveryCost;
    private BigDecimal totalCost;
    private int totalQuantity;

    public List<CartItem> getItems() {
        return items;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public BigDecimal getSubTotalCost() {
        return subTotalCost;
    }

    public BigDecimal getDeliveryCost() {
        return deliveryCost;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public void setSubTotalCost(BigDecimal subTotalCost) {
        this.subTotalCost = subTotalCost;
    }

    public void setDeliveryCost(BigDecimal deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        if (!super.equals(o)) return false;
        Order order = (Order) o;
        return Objects.equals(items, order.items) && Objects.equals(subTotalCost, order.subTotalCost)
                && Objects.equals(deliveryCost, order.deliveryCost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), items, subTotalCost, deliveryCost);
    }
}
