package ru.kadyrov.electron.commerce.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cart {

    private List<CartItem> items = new ArrayList<>();
    private final String sessionId;

    public Cart(final String sessionId) {
        this.sessionId = sessionId;
    }

    public void addItem(final Product product) {
        CartItem item = new CartItem(product);
        int index = items.indexOf(item);
        if (index > -1) {
            items.get(index).incrementQuantity();
        } else {
            items.add(item);
        }
    }

    public void setItemQuantity(final Product product, final Integer quantity) {
        if (quantity == 0) {
            removeItem(product);
        }
        CartItem item = new CartItem(product);
        int index = items.indexOf(item);
        items.get(index).setQuantity(quantity);
    }

    public void removeItem(final Product product) {
        items.remove(new CartItem(product));
    }

    public void clean() {
        items.clear();
    }

    public Integer getTotalPrice() {
        Integer sum = 0;
        for (CartItem item : items) {
            sum += item.getTotalPrice();
        }
        return sum;
    }

    public List<CartItem> getItems() {
        return items;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(sessionId, cart.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "sessionId='" + sessionId + '\'' +
                '}';
    }
}
