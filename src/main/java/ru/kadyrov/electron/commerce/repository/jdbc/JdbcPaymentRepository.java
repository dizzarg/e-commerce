package ru.kadyrov.electron.commerce.repository.jdbc;

import org.springframework.stereotype.Repository;
import ru.kadyrov.electron.commerce.exception.RepositoryException;
import ru.kadyrov.electron.commerce.models.Payment;
import ru.kadyrov.electron.commerce.repository.PaymentRepository;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class JdbcPaymentRepository implements PaymentRepository {

    private static final String FIND_ALL = "SELECT id, order_id, amount, create_dt FROM payments";
    private static final String INSERT = "INSERT INTO payments (order_id, amount) VALUES (?,?);";

    @Inject
    private DataSource dataSource;

    @Override
    public Payment addPayment(Payment payment) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, payment.getOrder().getId());
                stmt.setInt(2, payment.getAmount());
                stmt.executeUpdate();
                ResultSet resultSet = stmt.getGeneratedKeys();
                if(resultSet.next()){
                    int id = resultSet.getInt(1);
                    payment.setId(id);
                    return payment;
                }
                throw new RepositoryException("Database cannot generate primary key value");
            }
        } catch (SQLException e) {
            throw new RepositoryException("Cannot create payment",e);
        }
    }


    @Override
    public List<Payment> getPayments() throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            List<Payment> payments = new ArrayList<>();
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet resultSet = stmt.executeQuery(FIND_ALL)) {
                    while (resultSet.next()){
                        int id = resultSet.getInt(1);
                        int orderId = resultSet.getInt(2);
                        int amount = resultSet.getInt(3);
                        Payment payment = new Payment();
                        payment.setId(id);
                        payment.setAmount(amount);
                        payments.add(payment);
                    }
                }
            }
            return payments;
        } catch (SQLException e) {
            throw new RepositoryException("Cannot load payments",e);
        }
    }
}
