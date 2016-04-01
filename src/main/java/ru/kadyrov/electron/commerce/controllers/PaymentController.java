package ru.kadyrov.electron.commerce.controllers;

import ru.kadyrov.electron.commerce.models.Payment;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/payments")
public class PaymentController extends ApiController {

    @RequestMapping(method = RequestMethod.POST)
    public Payment createOrder(@RequestParam("orderId") Integer orderId) {
        return shopService.createPayment(orderId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Payment> getPayment() {
        return shopService.getPayments();
    }

}
