package ru.kadyrov.electron.commerce.repository.jdbc;

import ru.kadyrov.electron.commerce.exception.RepositoryException;
import ru.kadyrov.electron.commerce.models.Payment;
import ru.kadyrov.electron.commerce.models.PaymentContext;
import ru.kadyrov.electron.commerce.repository.PaymentRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcPaymentRepository implements PaymentRepository {
    @Override
    public Payment addPayment(PaymentContext context) throws RepositoryException {
        return new Payment();
    }
}
