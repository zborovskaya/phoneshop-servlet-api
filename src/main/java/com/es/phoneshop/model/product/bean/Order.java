package com.es.phoneshop.model.product.bean;

import com.es.phoneshop.model.product.service.PaymentMethod;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Order extends GenericBean implements Serializable {
    private String secureId;
    private List<CartItem> items;
    private BigDecimal subTotalCost;
    private BigDecimal deliveryCost;
    private BigDecimal totalCost;
    private int totalQuantity;

    private String firstName;
    private String lastName;
    private String phone;

    private String deliveryAddress;
    private LocalDate deliveryDate;

    private PaymentMethod paymentMethod;

    public String getSecureId() {
        return secureId;
    }

    public void setSecureId(String secureId) {
        this.secureId = secureId;
    }


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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return totalQuantity == order.totalQuantity && Objects.equals(secureId, order.secureId)
                && Objects.equals(items, order.items) && Objects.equals(subTotalCost, order.subTotalCost)
                && Objects.equals(deliveryCost, order.deliveryCost) && Objects.equals(totalCost, order.totalCost)
                && Objects.equals(firstName, order.firstName) && Objects.equals(lastName, order.lastName)
                && Objects.equals(phone, order.phone) && Objects.equals(deliveryAddress, order.deliveryAddress)
                && Objects.equals(deliveryDate, order.deliveryDate) && paymentMethod == order.paymentMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(secureId, items, subTotalCost, deliveryCost, totalCost, totalQuantity, firstName, lastName, phone, deliveryAddress, deliveryDate, paymentMethod);
    }
}
