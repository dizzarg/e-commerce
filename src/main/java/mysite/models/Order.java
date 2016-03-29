package mysite.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Order {
    private final int id;
    private final String customerUsername;
    private Date dateCreated, dateShipped = null;
    private ArrayList<Integer> productIds = new ArrayList<>();
    private List<OrderItem> items = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public Order(int id,
                 String customerUsername,
                 ArrayList<Integer> shoppingCartProductIds) {
        this.id = id;
        this.productIds = (ArrayList<Integer>) shoppingCartProductIds.clone();
        this.customerUsername = customerUsername;
        this.dateCreated = new Date(System.currentTimeMillis());
    }

    @SuppressWarnings("unchecked")
    public Order(int id,
                 String customerUsername,
                 ArrayList<Integer> shoppingCartProductIds,
                 Date dateCreated,
                 Date dateShipped) {
        this.id = id;
        this.productIds = (ArrayList<Integer>) shoppingCartProductIds.clone();
        this.customerUsername = customerUsername;
        this.dateCreated = dateCreated;
    }

    public int getId() {
        return id;
    }

    public void shipIt() {
        dateShipped = new Date(System.currentTimeMillis());
    }

    public boolean isShipped() {
        if (dateShipped != null) {
            return true;
        }
        return false;
    }

    public Date getDateShipped() {
        return dateShipped;
    }

    public String getUsername() {
        return customerUsername;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public ArrayList<Integer> getProductIds() {
        return productIds;
    }

    @Override
    public String toString() {
        return "Order [userName=" + customerUsername
                + ", dateCreated=" + dateCreated + ", dateShipped="
                + dateShipped + ", products=" + productIds + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                Objects.equals(customerUsername, order.customerUsername) &&
                Objects.equals(dateCreated, order.dateCreated) &&
                Objects.equals(dateShipped, order.dateShipped) &&
                Objects.equals(productIds, order.productIds) &&
                Objects.equals(items, order.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerUsername, dateCreated, dateShipped, productIds, items);
    }
}