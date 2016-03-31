package ru.kadyrov.electron.commerce.repository;

import ru.kadyrov.electron.commerce.exception.RepositoryException;
import ru.kadyrov.electron.commerce.models.Payment;
import ru.kadyrov.electron.commerce.models.PaymentContext;

public interface PaymentRepository {

    Payment addPayment(PaymentContext context) throws RepositoryException;

}
