package ru.kadyrov.electron.commerce.repository;

import org.springframework.stereotype.Repository;
import ru.kadyrov.electron.commerce.models.Cart;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class CartRepository {

    private List<Cart> store = new ArrayList<>();

    public List<Cart> getCarts(final String sessionId) {
        Objects.requireNonNull(sessionId, "Session id should not be null");
        return store.stream().filter(cart -> sessionId.equals(cart.getSession())).collect(Collectors.toList());
    }

    public void add(final Cart cart) {

        store.add(cart);
    }
}
