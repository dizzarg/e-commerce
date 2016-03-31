package ru.kadyrov.electron.commerce.repository;

import ru.kadyrov.electron.commerce.exception.RepositoryException;
import ru.kadyrov.electron.commerce.models.Payment;
import ru.kadyrov.electron.commerce.models.PaymentContext;

import java.util.List;

public interface PaymentRepository {

    Payment addPayment(Payment context) throws RepositoryException;

    List<Payment> getPayments() throws RepositoryException;
}
