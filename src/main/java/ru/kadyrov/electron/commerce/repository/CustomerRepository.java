package ru.kadyrov.electron.commerce.repository;

import ru.kadyrov.electron.commerce.exception.RepositoryException;
import ru.kadyrov.electron.commerce.models.Customer;

import java.util.List;

public interface CustomerRepository {

    Customer addCustomer(Customer customer) throws RepositoryException;

    Customer getCustomer(String customerUsername) throws RepositoryException;

    List<Customer> getCustomers() throws RepositoryException;

    void updateCustomer(Customer customer) throws RepositoryException;

    void removeCustomer(String username) throws RepositoryException;

    boolean existsCustomer(String username) throws RepositoryException;
}
