package ru.kadyrov.electron.commerce.models;

import ru.kadyrov.electron.commerce.exception.ModelException;

public class Session {

    private final String sessionId;
    private SessionShoppingCart shoppingCart = new SessionShoppingCart();

    public Session(String sessionId) {
        this.sessionId = sessionId;
    }

    public void addProductToShoppingCart(int productId) {
        shoppingCart.addProduct(productId, 1);
    }

    public void removeProductFromShoppingCart(int productId) throws ModelException {
        shoppingCart.removeProduct(productId, 1);
    }

    public SessionShoppingCart getShoppingCart() {
        return shoppingCart;
    }

}
