package ru.kadyrov.electron.commerce.repository.jdbc;

import ru.kadyrov.electron.commerce.exception.RepositoryException;
import ru.kadyrov.electron.commerce.models.Customer;
import ru.kadyrov.electron.commerce.models.ShoppingCart;
import ru.kadyrov.electron.commerce.repository.CustomerRepository;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class JdbcCustomerRepository implements CustomerRepository {

    private static final String FIND_ALL = "SELECT id, username,passwd,email,firstName,lastName,address,phoneNumber FROM customers";
    private static final String FIND_BY_NAME = "SELECT id, passwd,email,firstName,lastName,address,phoneNumber FROM customers where username=?";
    private static final String EXISTS_BY_NAME = "SELECT id FROM customers where username=?";
    private static final String DELETE_BY_NAME = "DELETE FROM customers where username=?";
    private static final String UPDATE_BY_NAME = "UPDATE customers SET passwd = ?,email = ?,firstName = ?,lastName = ?,address = ?,phoneNumber = ? where username=?";
    private static final String INSERT = "INSERT INTO customers (username,passwd,email,firstName,lastName,address,phoneNumber) VALUES (?,?,?,?,?,?,?);";

    private ConcurrentMap<String, ShoppingCart> customerCart = new ConcurrentHashMap<>();

    @Inject
    private DataSource dataSource;

    @Override
    public Customer addCustomer(Customer customer) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(INSERT)) {
                stmt.setString(1, customer.getUsername());
                stmt.setString(2, customer.getPassword());
                stmt.setString(3, customer.getEmail());
                stmt.setString(4, customer.getFirstName());
                stmt.setString(5, customer.getLastName());
                stmt.setString(6, customer.getAddress());
                stmt.setString(7, customer.getPhoneNumber());
                stmt.executeUpdate();
                return customer;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Cannot create customer",e);
        }
    }

    @Override
    public Customer getCustomer(String customerUsername) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(FIND_BY_NAME)) {
                stmt.setString(1, customerUsername);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()){
                        Integer id = resultSet.getInt(1);
                        String password = resultSet.getString(2);
                        String email = resultSet.getString(3);
                        String firstName = resultSet.getString(4);
                        String lastName = resultSet.getString(5);
                        String address = resultSet.getString(6);
                        String phoneNumber = resultSet.getString(7);
                        ShoppingCart cart = customerCart.get(customerUsername);
                        return new Customer(customerUsername, password, email,
                                firstName,lastName, address, phoneNumber, cart);
                    }
                    throw new RepositoryException("Customer not found");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Cannot load customer",e);
        }
    }

    @Override
    public List<Customer> getCustomers() throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            List<Customer> customers = new ArrayList<>();
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet resultSet = stmt.executeQuery(FIND_ALL)) {
                    while (resultSet.next()){
                        Integer id = resultSet.getInt(1);
                        String customerUsername = resultSet.getString(2);
                        String password = resultSet.getString(3);
                        String email = resultSet.getString(4);
                        String firstName = resultSet.getString(5);
                        String lastName = resultSet.getString(6);
                        String address = resultSet.getString(7);
                        String phoneNumber = resultSet.getString(8);
                        ShoppingCart cart = customerCart.get(customerUsername);
                        Customer customer = new Customer(customerUsername, password,
                                email,firstName,lastName, address, phoneNumber, cart);
                        customers.add(customer);
                    }
                }
            }
            return customers;
        } catch (SQLException e) {
            throw new RepositoryException("Cannot load customers",e);
        }
    }

    @Override
    public void updateCustomer(Customer customer) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(UPDATE_BY_NAME)) {
                stmt.setString(1, customer.getPassword());
                stmt.setString(2, customer.getEmail());
                stmt.setString(3, customer.getFirstName());
                stmt.setString(4, customer.getLastName());
                stmt.setString(5, customer.getAddress());
                stmt.setString(6, customer.getPhoneNumber());
                stmt.setString(7, customer.getUsername());
                stmt.executeUpdate();
                customerCart.put(customer.getUsername(), customer.getShoppingCart());
            }
        } catch (SQLException e) {
            throw new RepositoryException("Cannot update customer",e);
        }
    }

    @Override
    public void removeCustomer(String username) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(DELETE_BY_NAME)) {
                stmt.setString(1, username);
                stmt.executeUpdate();
                customerCart.remove(username);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Cannot remove customer",e);
        }
    }

    @Override
    public boolean existsCustomer(String customerUsername) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(EXISTS_BY_NAME)) {
                stmt.setString(1, customerUsername);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Cannot load customer",e);
        }
    }
}
