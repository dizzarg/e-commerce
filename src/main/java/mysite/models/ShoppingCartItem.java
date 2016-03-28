package mysite.models;

/**
 * Created by user on 25.03.2016.
 */
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
