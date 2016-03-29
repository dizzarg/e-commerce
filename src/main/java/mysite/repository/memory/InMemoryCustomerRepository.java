package mysite.repository.memory;

import mysite.exception.RepositoryException;
import mysite.models.Customer;
import mysite.repository.CustomerRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class InMemoryCustomerRepository implements CustomerRepository {
    private HashMap<String, Customer> customers = new HashMap<String, Customer>();

    @Override
    public Customer addCustomer(Customer customer) throws RepositoryException {
        if (existsCustomer(customer.getUsername())) {
            throw new RepositoryException("Could not add customer: customer already exists");
        }
        customers.put(customer.getUsername(), customer);
        return customer;
    }

    @Override
    public Customer getCustomer(String username) throws RepositoryException {
        if (existsCustomer(username)) {
            return customers.get(username);
        }
        throw new RepositoryException("Could not get customer: customer does not exist");
    }

    @Override
    public List<Customer> getCustomers() {
        return new ArrayList<Customer>(customers.values());
    }

    @Override
    public void updateCustomer(Customer customer) throws RepositoryException {
        if (existsCustomer(customer.getUsername())) {
            customers.replace(customer.getUsername(), customer);
        } else {
            throw new RepositoryException("Could not update customer: customer does not exist");
        }
    }

    @Override
    public void removeCustomer(String username) throws RepositoryException {
        if (existsCustomer(username)) {
            customers.remove(username);
            return;
        }
        throw new RepositoryException("Could not remove customer: customer does not exist.");
    }

    @Override
    public boolean existsCustomer(String username) throws RepositoryException {
        return customers.containsKey(username);
    }
}
