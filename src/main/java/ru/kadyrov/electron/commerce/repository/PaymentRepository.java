package ru.kadyrov.electron.commerce.repository;

import ru.kadyrov.electron.commerce.exception.RepositoryException;
import ru.kadyrov.electron.commerce.models.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {

    Payment addPayment(Payment payment) throws RepositoryException;

    List<Payment> getPayments() throws RepositoryException;

    Optional<Payment> getById(Integer id) throws RepositoryException;
}
