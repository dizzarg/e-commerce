package mysite.repository;

import mysite.exception.RepositoryException;
import mysite.models.Customer;

import java.util.List;

public interface CustomerRepository {

    void addCustomer(Customer customer) throws RepositoryException;

    Customer getCustomer(String customerUsername) throws RepositoryException;

    List<Customer> getCustomers() throws RepositoryException;

    void updateCustomer(Customer customer) throws RepositoryException;

    void removeCustomer(String username) throws RepositoryException;
}
