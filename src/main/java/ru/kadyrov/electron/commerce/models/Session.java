package ru.kadyrov.electron.commerce.models;

import ru.kadyrov.electron.commerce.exception.ModelException;

public class Session {

    private final String sessionId;

    private final Cart shoppingCart;

    public Session(String sessionId) {
        this.sessionId = sessionId;
        this.shoppingCart = new Cart(sessionId);
    }

    public void addProductToShoppingCart(final Product product) {
        shoppingCart.addItem(product);
    }

    public void removeProductFromShoppingCart(final Product product) throws ModelException {
        shoppingCart.removeItem(product);
    }

    public Cart getShoppingCart() {
        return shoppingCart;
    }

}
