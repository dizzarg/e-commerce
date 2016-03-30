package mysite.repository.jdbc;

import mysite.exception.RepositoryException;
import mysite.models.Payment;
import mysite.models.PaymentContext;
import mysite.repository.PaymentRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcPaymentRepository implements PaymentRepository {
    @Override
    public Payment addPayment(PaymentContext context) throws RepositoryException {
        return new Payment();
    }
}
