package ru.kadyrov.electron.commerce.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.kadyrov.electron.commerce.models.Cart;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/shop")
public class ShopController extends ApiController {

    private final Logger logger = LoggerFactory.getLogger(ShopController.class);

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void addProductToCustomer(@RequestParam("productId") int productId,
                                     @RequestParam("userName") String customerUsername,
                                     @RequestParam(value = "amount", required = false) Integer amount) {
        amount = amount == null ? 1 : amount;
        logger.info("addProductToCustomer: " + productId + "|" + customerUsername);
        shopService.addProductToCustomer(productId, customerUsername, amount);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public void removeProductToCustomer(@RequestParam("productId") int productId,
                                        @RequestParam("userName") String customerUsername,
                                        @RequestParam(value = "amount", required = false) Integer amount) {
        amount = amount == null ? 1 : amount;
        logger.info("removeProductFromCustomer: " + productId + "|" + customerUsername);
        shopService.removeProductFromCustomer(productId, customerUsername);
    }

    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    public @ResponseBody Cart getCart(HttpServletRequest request){
        String id = request.getSession().getId();
        Cart cart = shopService.getCart(id);
        logger.info("createCart: "+cart+ " id "+ id);
        return cart;
    }

    @RequestMapping(value = "/cart/{productId}", method = RequestMethod.PUT)
    public void addToCart(@PathVariable("productId") int productId, HttpServletRequest request){
        shopService.addToCart(productId, request.getSession().getId());
    }

}
