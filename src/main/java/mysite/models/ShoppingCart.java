package mysite.models;

import mysite.exception.ModelException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other instanceof ShoppingCart) {
            ShoppingCart sc = (ShoppingCart) other;
            if (this.getProductIds().equals(sc.getProductIds())) {
                return true;
            }
        }
        return false;
    }
}