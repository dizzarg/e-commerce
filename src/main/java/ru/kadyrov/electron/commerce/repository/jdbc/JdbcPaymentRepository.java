package ru.kadyrov.electron.commerce.repository.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.kadyrov.electron.commerce.exception.RepositoryException;
import ru.kadyrov.electron.commerce.models.Payment;
import ru.kadyrov.electron.commerce.repository.PaymentRepository;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcPaymentRepository implements PaymentRepository {

    private static final String FIND_ALL = "SELECT id, order_id, amount, create_dt FROM payments";
    private static final String FIND_BY_ID = "SELECT order_id, amount, create_dt FROM payments WHERE id=?";
    private static final String INSERT = "INSERT INTO payments (order_id, amount) VALUES (?,?);";

    private final JdbcTemplate jdbcTemplate;

    @Inject
    public JdbcPaymentRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Payment addPayment(Payment payment) throws RepositoryException {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, payment.getOrder().getId());
            ps.setInt(2, payment.getAmount());
            return ps;
        }, holder);
        int newPaymentId = holder.getKey().intValue();
        payment.setId(newPaymentId);
        return payment;
    }


    @Override
    public List<Payment> getPayments() throws RepositoryException {
        return jdbcTemplate.query(FIND_ALL, new PaymentRowMapper());
    }

    @Override
    public Optional<Payment> getById(Integer id) throws RepositoryException {
        return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID, new Object[]{id}, new PaymentRowMapper()));
    }

    private class PaymentRowMapper implements RowMapper<Payment> {
        @Override
        public Payment mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            Payment payment = new Payment();
            payment.setId(rs.getInt(1));
            payment.setAmount(rs.getInt(3));
            return payment;
        }
    }
}
