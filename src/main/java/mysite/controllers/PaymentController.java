package mysite.controllers;

import mysite.models.Order;
import mysite.models.Payment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/payments")
public class PaymentController extends ApiController {

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    Payment createOrder(@RequestParam("orderId") Integer orderId) {
        return shopService.createPayment(orderId);
    }

}
