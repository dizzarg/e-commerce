package ru.kadyrov.electron.commerce.repository.memory;

import org.springframework.stereotype.Repository;
import ru.kadyrov.electron.commerce.exception.RepositoryException;
import ru.kadyrov.electron.commerce.models.Customer;
import ru.kadyrov.electron.commerce.models.Order;
import ru.kadyrov.electron.commerce.repository.OrderRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryOrderRepository implements OrderRepository {
    private HashMap<Integer, Order> orders = new HashMap<Integer, Order>();
    private final AtomicInteger orderIDGenerator = new AtomicInteger(0);

    @Override
    public Order addOrder(Customer customer) throws RepositoryException {
        Order order = new Order(orderIDGenerator.incrementAndGet(), customer.getUsername(), customer.getShoppingCart().getProductIds());
        orders.put(order.getId(), order);
        return order;
    }

    @Override
    public void removeOrder(int id) throws RepositoryException {
        if (orders.containsKey(id)) {
            orders.remove(id);
        } else {
            throw new RepositoryException("Could not remove order: order does not exist in repository:");
        }
    }

    @Override
    public Order getOrder(int orderId) throws RepositoryException {
        if (orders.containsKey(orderId)) {
            return orders.get(orderId);
        }
        throw new RepositoryException("Cannot get order: order does not exist in repository.");
    }

    // Get orders for a specific user
    @Override
    public List<Order> getOrders(String customerUsername) throws RepositoryException {
        ArrayList<Order> orderList = new ArrayList<Order>();
        for (Order order : orders.values()) {
            if (order.getUsername().equals(customerUsername)) {
                orderList.add(order);
            }
        }
        if (orderList.isEmpty()) {
            throw new RepositoryException("No orders for this user");
        }
        return orderList;
    }

    @Override
    public void updateOrder(Order order) throws RepositoryException {
        if (orders.containsKey(order.getId())) {
            orders.replace(order.getId(), order);
        } else {
            throw new RepositoryException("No order with this ID exists in repository");
        }
    }
}