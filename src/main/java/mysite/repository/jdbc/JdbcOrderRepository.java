package mysite.repository.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import mysite.exception.RepositoryException;
import mysite.models.Order;
import mysite.models.Product;
import mysite.repository.OrderRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private static final String INSERT = "INSERT INTO orders (customerUsername, dateCreated, dateShipped, products) VALUES (?,?,?,?);";
    private static final String FIND_BY_ID = "SELECT customerUsername, dateCreated, dateShipped, products FROM orders where id=?;";
    private static final String FIND_BY_USER_NAME = "SELECT id, dateCreated, dateShipped, products FROM orders where customerUsername=?;";
    private static final String DELETE_BY_ID = "DELETE FROM orders where id=?;";
    private static final String UPDATE_BY_ID = "UPDATE orders SET customerUsername = ?, dateCreated = ?, dateShipped = ?, products = ? where id=?;";

    @Inject
    private DataSource dataSource;

    @Override
    public void addOrder(Order order) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(INSERT)) {
                stmt.setString(1, order.getUsername());
                stmt.setTimestamp(2, new Timestamp(order.getDateCreated().getTime()));
                if(order.isShipped()){
                    stmt.setTimestamp(3, new Timestamp(order.getDateShipped().getTime()));
                } else {
                    stmt.setNull(3, Types.TIMESTAMP);
                }
                ObjectMapper mapper = new ObjectMapper();
                String s = null;
                try {
                    s = mapper.writeValueAsString(order.getProductIds());
                } catch (JsonProcessingException e) {
                    s="[]";
                }
                stmt.setString(4, s);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Cannot create order",e);
        }
    }

    @Override
    public Order getOrder(int id) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID)) {
                stmt.setInt(1, id);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()){
                        String customerName = resultSet.getString(1);
                        Date createDate = resultSet.getTimestamp(2);
                        Date shipDate = resultSet.getTimestamp(3);
                        String ids = resultSet.getString(4);
                        ObjectMapper mapper = new ObjectMapper();
                        JavaType type = mapper.getTypeFactory().
                                constructCollectionType(List.class, Integer.class);
                        // TODO: создать OrderItem и там хранить id
                        ArrayList<Integer> shoppingCartProductIds = null;
                        try {
                            shoppingCartProductIds = mapper.readValue(ids, type);
                        } catch (IOException e) {
                            shoppingCartProductIds = new ArrayList<>();
                        }
                        return new Order(id, customerName, shoppingCartProductIds, createDate, shipDate);
                    }
                    // TODO: моджет стоит бросить исключение
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Cannot load customer",e);
        }
    }

    @Override
    public List<Order> getOrders(String customerUsername) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            List<Order> orders = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(FIND_BY_USER_NAME)) {
                stmt.setString(1, customerUsername);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()){
                        Integer id = resultSet.getInt(1);
                        Date createDate = resultSet.getTimestamp(2);
                        Date shipDate = resultSet.getTimestamp(3);
                        // TODO: создать OrderItem и там хранить id
                        String ids = resultSet.getString(4);
                        ObjectMapper mapper = new ObjectMapper();
                        JavaType type = mapper.getTypeFactory().
                                constructCollectionType(List.class, Integer.class);
                        // TODO: создать OrderItem и там хранить id
                        ArrayList<Integer> shoppingCartProductIds = null;
                        try {
                            shoppingCartProductIds = mapper.readValue(ids, type);
                        } catch (IOException e) {
                            shoppingCartProductIds = new ArrayList<>();
                        }
//                        ArrayList<Integer> shoppingCartProductIds = new ArrayList<>();
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
    public void updateOrder(Order newOrder) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(UPDATE_BY_ID)) {
                stmt.setString(1, newOrder.getUsername());
                stmt.setTimestamp(2, new Timestamp(newOrder.getDateCreated().getTime()));
                stmt.setString(3, newOrder.getProductIds().toString());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Cannot update order",e);
        }
    }
}
