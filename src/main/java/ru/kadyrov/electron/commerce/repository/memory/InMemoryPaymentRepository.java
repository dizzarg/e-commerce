package ru.kadyrov.electron.commerce.repository.memory;

import org.springframework.stereotype.Repository;
import ru.kadyrov.electron.commerce.exception.RepositoryException;
import ru.kadyrov.electron.commerce.models.Payment;
import ru.kadyrov.electron.commerce.repository.PaymentRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryPaymentRepository implements PaymentRepository {

    private Map<Integer, Payment> store = new HashMap<>();
    private AtomicInteger index = new AtomicInteger(0);

    @Override
    public Payment addPayment(Payment payment) throws RepositoryException {
        payment.setId(index.incrementAndGet());
        store.put(payment.getId(), payment);
        return payment;
    }

    @Override
    public List<Payment> getPayments() throws RepositoryException {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Payment> getById(Integer id) throws RepositoryException {
        return Optional.ofNullable(store.get(id));
    }

}
