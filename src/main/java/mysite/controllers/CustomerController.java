package mysite.controllers;

import mysite.models.Customer;
import mysite.models.Order;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/customers")
public class CustomerController extends ApiController{

    @RequestMapping(method = RequestMethod.POST)
    public void addCustomer(@RequestBody Customer customer){
        shopService.addCustomer(customer);
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.GET)
    public Customer getCustomer(@PathVariable("userName") String customerUsername) {
        return shopService.getCustomer(customerUsername);
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.DELETE)
    public void removeCustomer(@PathVariable("userName") String customerUsername) {
        shopService.removeCustomer(customerUsername);
    }

    @RequestMapping(value = "/{userName}/orders", method = RequestMethod.GET)
    public List<Order> getOrders(@PathVariable("userName") String customerUsername) {
        return shopService.getOrders(customerUsername);
    }
}
