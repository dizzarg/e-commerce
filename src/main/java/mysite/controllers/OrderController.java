package mysite.controllers;

import mysite.models.Order;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/orders")
public class OrderController extends ApiController {

    @RequestMapping(method = RequestMethod.POST)
    public Order createOrder(@RequestParam("userName") String customerUsername) {
        return shopService.createOrder(customerUsername);
    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
    public Order getOrder(@PathVariable("orderId") int orderId) {
        return shopService.getOrder(orderId);
    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.DELETE)
    public void removeOrder(@PathVariable("orderId") int productId) {
        shopService.removeOrder(productId);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateOrder(@RequestBody Order order) {
        shopService.updateOrder(order);
    }

}