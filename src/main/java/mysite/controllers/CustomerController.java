package mysite.controllers;

import mysite.models.Customer;
import mysite.models.Order;
import mysite.services.CustomerService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping(value = "/api/customers")
public class CustomerController extends ApiController{

    private final CustomerService customerService;

    @Inject
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addCustomer(@RequestBody Customer customer){
        customerService.addCustomer(customer);
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.GET)
    public Customer getCustomer(@PathVariable("userName") String customerUsername) {
        return customerService.getCustomer(customerUsername);
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.DELETE)
    public void removeCustomer(@PathVariable("userName") String customerUsername) {
        customerService.removeCustomer(customerUsername);
    }

    @RequestMapping(value = "/{userName}/orders", method = RequestMethod.GET)
    public List<Order> getOrders(@PathVariable("userName") String customerUsername) {
        return shopService.getOrders(customerUsername);
    }
}
