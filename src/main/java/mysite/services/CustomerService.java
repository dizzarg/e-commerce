package mysite.services;

import mysite.exception.RepositoryException;
import mysite.exception.ShopServiceException;
import mysite.models.Customer;
import mysite.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final static Logger logger = LoggerFactory.getLogger(ShopService.class);

    @Inject
    public CustomerService(@Named("jdbcCustomerRepository") CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void addCustomer(Customer customer) {
        try {
            if(customerRepository.existsCustomer(customer.getUsername())){
                throw new RepositoryException("Customer exists");
            }
            customerRepository.addCustomer(customer);
            logger.info("Customer was added");
        } catch (RepositoryException e) {
            logger.error("Could not add customer", e);
            throw new ShopServiceException("Could not add customer: " + e.getMessage(), e);
        }
    }

    public Customer getCustomer(String customerUsername) {
        try {
            Customer customer = customerRepository.getCustomer(customerUsername);
            logger.info("Customer was loaded");
            return customer;
        } catch (RepositoryException e) {
            logger.error("Could not load customer", e);
            throw new ShopServiceException("Could not load customer: " + e.getMessage(), e);
        }
    }

    public void updateCustomer(Customer customer) {
        try {
            customerRepository.updateCustomer(customer);
            logger.info("Customer was updated");
        } catch (RepositoryException e) {
            logger.error("Could not update customer", e);
            throw new ShopServiceException("Could not update customer: " + e.getMessage(), e);
        }
    }

    public  void removeCustomer(String customerUsername) {
        try {
            customerRepository.removeCustomer(customerUsername);
            logger.info("Customer was removed");
        } catch (RepositoryException e) {
            logger.error("Could not remove customer", e);
            throw new ShopServiceException("Could not remove customer: " + e.getMessage(), e);
        }
    }

}
