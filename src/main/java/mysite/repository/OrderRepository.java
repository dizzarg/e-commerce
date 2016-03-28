package mysite.repository;

import mysite.exception.RepositoryException;
import mysite.models.Order;

import java.util.List;

public interface OrderRepository {
    public void addOrder(Order order) throws RepositoryException;

    public Order getOrder(int id) throws RepositoryException;

    public void removeOrder(int id) throws RepositoryException;

    public List<Order> getOrders(String customerUsername) throws RepositoryException;

    public void updateOrder(Order newOrder) throws RepositoryException;
}