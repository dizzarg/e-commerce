package ru.kadyrov.electron.commerce.repository.jdbc;

import org.springframework.transaction.annotation.Transactional;
import ru.kadyrov.electron.commerce.exception.RepositoryException;
import ru.kadyrov.electron.commerce.models.Customer;
import ru.kadyrov.electron.commerce.models.Order;
import ru.kadyrov.electron.commerce.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.Date;

@Repository
@Transactional
public class JdbcOrderRepository implements OrderRepository {

    private static final String INSERT = "INSERT INTO orders (customerUsername, dateCreated) VALUES (?,?);";
    private static final String INSERT_ITEM = "INSERT INTO order_items (order_id, product_id) VALUES (?,?);";
    private static final String FIND_BY_ID = "SELECT customerUsername, dateCreated, dateShipped FROM orders where id=?;";
    private static final String FIND_ITEM_BY_ORDER_ID = "SELECT product_id FROM order_items where order_id=?;";
    private static final String FIND_BY_USER_NAME = "SELECT id, dateCreated, dateShipped FROM orders where customerUsername=?;";
    private static final String FIND_ITEM_BY_USER_NAME = "SELECT order_id, product_id FROM order_items where order_id in(SELECT id FROM orders where customerUsername=?);";
    private static final String DELETE_BY_ID = "DELETE FROM orders where id=?;";
    private static final String UPDATE_BY_ID = "UPDATE orders SET customerUsername = ?, dateCreated = ?, dateShipped = ? where id=?;";

    @Inject
    private DataSource dataSource;

    @Override
    // TODO: uses Session Shopping cart value
    public Order addOrder(Customer customer) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, customer.getUsername());
                stmt.setTimestamp(2,  new Timestamp(System.currentTimeMillis()));
                stmt.executeUpdate();
                ResultSet resultSet = stmt.getGeneratedKeys();
                if(resultSet.next()){
                    int id = resultSet.getInt(1);
                    for (Integer productId: customer.getShoppingCart().getProductIds()) {
                        try (PreparedStatement itemStmt = conn.prepareCall(INSERT_ITEM)) {
                            itemStmt.setInt(1, id);
                            itemStmt.setInt(2, productId);
                            itemStmt.executeUpdate();
                        }
                    }
                    return new Order(id, customer.getUsername(), customer.getShoppingCart().getProductIds());
                } else {
                    throw new RepositoryException("Database cannot generate primary key value");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Cannot create order",e);
        }
    }

    @Override
    public Order getOrder(int id) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            ArrayList<Integer> shoppingCartProductIds = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(FIND_ITEM_BY_ORDER_ID)) {
                stmt.setInt(1, id);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()){
                        int productId = resultSet.getInt(1);
                        shoppingCartProductIds.add(productId);
                    }
                }
            }
            try (PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID)) {
                stmt.setInt(1, id);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()){
                        String customerName = resultSet.getString(1);
                        Date createDate = resultSet.getTimestamp(2);
                        Date shipDate = resultSet.getTimestamp(3);
                        return new Order(id, customerName, shoppingCartProductIds, createDate, shipDate);
                    }
                    throw new RepositoryException("Order not found");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Cannot load customer",e);
        }
    }

    @Override
    public List<Order> getOrders(String customerUsername) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            Map<Integer, ArrayList<Integer>> orderItems = new HashMap<>();
            try (PreparedStatement stmt = conn.prepareStatement(FIND_ITEM_BY_USER_NAME)) {
                stmt.setString(1, customerUsername);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        Integer orderId = resultSet.getInt(1);
                        Integer productId = resultSet.getInt(1);
                        ArrayList<Integer> products = orderItems.putIfAbsent(orderId, new ArrayList<>(Collections.singletonList(productId)));
                        if(products != null){
                            products.add(productId);
                        }
                    }
                }
            }
            List<Order> orders = new ArrayList<>(orderItems.keySet().size());
            try (PreparedStatement stmt = conn.prepareStatement(FIND_BY_USER_NAME)) {
                stmt.setString(1, customerUsername);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()){
                        Integer id = resultSet.getInt(1);
                        Date createDate = resultSet.getTimestamp(2);
                        Date shipDate = resultSet.getTimestamp(3);
                        ArrayList<Integer> shoppingCartProductIds = orderItems.get(id);
                        orders.add(new Order(id, customerUsername, shoppingCartProductIds, createDate, shipDate));
                    }
                }
            }
            return orders;
        } catch (SQLException e) {
            throw new RepositoryException("Cannot load orders",e);
        }
    }

    @Override
    public void removeOrder(int id) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(DELETE_BY_ID)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Cannot remove order",e);
        }
    }

    @Override
    public void updateOrder(Order order) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(UPDATE_BY_ID)) {
                stmt.setString(1, order.getUsername());
                stmt.setTimestamp(2, new Timestamp(order.getDateCreated().getTime()));
                stmt.setTimestamp(3, new Timestamp(order.getDateShipped().getTime()));
                stmt.setInt(4, order.getId());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Cannot update order",e);
        }
    }
}
