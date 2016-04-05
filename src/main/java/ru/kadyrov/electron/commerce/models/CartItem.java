package ru.kadyrov.electron.commerce.models;

import java.util.Objects;

public class CartItem {

    private Integer productId;
    private Integer quantity;
    private Integer price;

    public CartItem(final Product product) {
        this(product, 1);
    }

    public CartItem(final Product product, final Integer quantity) {
        this.productId = product.getId();
        this.quantity = quantity;
        this.price = product.getPrice();
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(final Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    public void incrementQuantity() {
        this.quantity++;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(final Integer price) {
        this.price = price;
    }

    public Integer getTotalPrice() {
        return price * quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(productId, cartItem.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
