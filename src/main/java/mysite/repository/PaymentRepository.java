package mysite.repository;

import mysite.exception.RepositoryException;
import mysite.models.Payment;
import mysite.models.PaymentContext;

public interface PaymentRepository {

    Payment addPayment(PaymentContext context) throws RepositoryException;

}
