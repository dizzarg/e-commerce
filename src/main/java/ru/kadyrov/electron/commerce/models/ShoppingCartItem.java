package ru.kadyrov.electron.commerce.models;

public class ShoppingCartItem {
    private final int product;
    private final int count;

    public ShoppingCartItem(int product, int count) {
        this.product = product;
        this.count = count;
    }

    public int getProduct() {
        return product;
    }

    public int getCount() {
        return count;
    }

}
