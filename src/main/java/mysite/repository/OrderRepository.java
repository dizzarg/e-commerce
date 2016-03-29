package mysite.repository;

import mysite.exception.RepositoryException;
import mysite.models.Customer;
import mysite.models.Order;

import java.util.List;

public interface OrderRepository {

    Order addOrder(Customer customer) throws RepositoryException;

    //Order addOrder(Order order) throws RepositoryException;

    Order getOrder(int id) throws RepositoryException;

    void removeOrder(int id) throws RepositoryException;

    List<Order> getOrders(String customerUsername) throws RepositoryException;

    void updateOrder(Order newOrder) throws RepositoryException;
}