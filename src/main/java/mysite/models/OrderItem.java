package mysite.models;

public final class OrderItem {

    private int id;
    private int productId;
    private int count;

    public OrderItem(int id, int productId, int count) {
        this.id = id;
        this.productId = productId;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public int getCount() {
        return count;
    }
}
