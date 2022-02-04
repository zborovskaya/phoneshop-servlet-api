package com.es.phoneshop.model.product.bean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

public class Product {
    private Long id;
    private String code;
    private String description;
    /**
     * null means there is no price because the product is outdated or new
     */
    private BigDecimal price;
    /**
     * can be null if the price is null
     */
    private Currency currency;
    private int stock;
    private String imageUrl;
    private List<PriceHistory> priceHistoryLog;

    public Product() {
        priceHistoryLog = new ArrayList<>();
    }

    public Product(String code, String description, PriceHistory priceHistory, Currency currency,
                   int stock, String imageUrl) {
        this.code = code;
        this.description = description;
        this.price = priceHistory.getPrice();
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
        priceHistoryLog = new ArrayList<>();
        priceHistoryLog.add(priceHistory);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setNewPrice(BigDecimal price) {
        this.price = price;
        priceHistoryLog.add(new PriceHistory(LocalDate.now(), price));
    }

    public void setNewPriceHistory(PriceHistory priceHistory) {
        this.price = price;
        priceHistoryLog.add(priceHistory);
    }

    public List<PriceHistory> getPriceHistoryLog() {
        return priceHistoryLog;
    }

    public void setPriceHistoryLog(List<PriceHistory> priceHistoryLog) {

        this.priceHistoryLog = priceHistoryLog;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return stock == product.stock && Objects.equals(id, product.id) && Objects.equals(code, product.code) &&
                Objects.equals(description, product.description) && Objects.equals(price, product.price) &&
                Objects.equals(currency, product.currency) && Objects.equals(imageUrl, product.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, description, price, currency, stock, imageUrl);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", currency=" + currency +
                ", stock=" + stock +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}