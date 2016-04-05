package ru.kadyrov.electron.commerce.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kadyrov.electron.commerce.models.Payment;

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
