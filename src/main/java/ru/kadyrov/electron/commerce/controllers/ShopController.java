package ru.kadyrov.electron.commerce.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.kadyrov.electron.commerce.models.Cart;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/shop")
public class ShopController extends ApiController {

    private final Logger logger = LoggerFactory.getLogger(ShopController.class);

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void addProductToCustomer(HttpServletRequest request,  @RequestParam("productId") int productId,
                                     @RequestParam("userName") String customerUsername,
                                     @RequestParam(value = "amount", required = false) Integer amount) {
        HttpSession session = request.getSession(true);
        logger.info("addProductToCustomer: " + productId + "|" + session.getId());
//        shopService.addProductToCustomer(productId, session.getId(), amount);
        shopService.addProductToCart(productId, session.getId(), amount);
    }

    @RequestMapping(value = "/carts", method = RequestMethod.GET)
    public List<Cart> getCarts(HttpServletRequest request){
        HttpSession session = request.getSession();
        List<Cart> carts = session==null? new ArrayList<>() : shopService.getCarts(session.getId());
        logger.info("getCarts: count:" + carts.size());
        return carts;
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public void removeProductToCustomer(@RequestParam("productId") int productId,
                                        @RequestParam("userName") String customerUsername,
                                        @RequestParam(value = "amount", required = false) Integer amount) {
        amount = amount == null ? 1 : amount;
        logger.info("removeProductFromCustomer: " + productId + "|" + customerUsername);
//        shopService.removeProductFromCustomer(productId, customerUsername);
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
