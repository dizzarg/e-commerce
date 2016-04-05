package ru.kadyrov.electron.commerce.models;

import ru.kadyrov.electron.commerce.exception.ModelException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ShoppingCart {

    private ArrayList<Integer> productIds = new ArrayList<>();

    public void addProduct(int product) {
        productIds.add(product);
    }

    public void removeProduct(Integer productId) throws ModelException {
        if (productIds.contains(productId)) {
            productIds.remove(productId);
        } else {
            throw new ModelException("Cannot remove product from cart: product does not exist.");
        }
    }

    /**
     * Removes all products with the specified id from this shopping cart.
     *
     * @param productId
     * @return true if a product was removed
     */
    public boolean removeAllProductsWithId(Integer productId) throws ModelException {
        return productIds.removeAll(Collections.singleton(productId));
    }

    public ArrayList<Integer> getProductIds() {
        return productIds;
    }

    public void clear() {
        productIds.clear();
    }

    @Override
    public String toString() {
        return "ShoppingCart [products=" + productIds + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCart cart = (ShoppingCart) o;
        return Objects.equals(productIds, cart.productIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productIds);
    }
}