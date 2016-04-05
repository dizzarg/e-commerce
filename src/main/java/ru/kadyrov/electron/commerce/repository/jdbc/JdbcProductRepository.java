package ru.kadyrov.electron.commerce.repository.jdbc;

import ru.kadyrov.electron.commerce.exception.RepositoryException;
import ru.kadyrov.electron.commerce.models.Product;
import ru.kadyrov.electron.commerce.models.ProductContext;
import ru.kadyrov.electron.commerce.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcProductRepository implements ProductRepository {

    private static final String FIND_ALL = "SELECT id, title,category,manufacturer,description,img,quantity,price FROM products";
    private static final String FIND_BY_ID = "SELECT id, title,category,manufacturer,description,img,quantity,price FROM products where id=?";
    private static final String DELETE_BY_ID = "DELETE FROM products where id=?";
    private static final String UPDATE_BY_ID = "UPDATE products SET title = ?, category = ?,manufacturer = ?,description = ?,img = ?,quantity = ?,price = ? where id=?";
    private static final String DECREASE_QUANTITY="UPDATE products SET quantity=quantity-1 WHERE id = ?";
    private static final String INCREASE_QUANTITY="UPDATE products SET quantity=quantity+1 WHERE id = ?";
    private static final String INSERT = "INSERT INTO products (title,category,manufacturer,description,img,quantity,price) VALUES (?,?,?,?,?,?,?);";

    @Inject
    private DataSource dataSource;

    @Override
    public Product addProduct(ProductContext productContext) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, productContext.getTitle());
                stmt.setString(2, productContext.getCategory());
                stmt.setString(3, productContext.getManufacturer());
                stmt.setString(4, productContext.getDescription());
                stmt.setString(5, productContext.getImg());
                stmt.setInt(6, productContext.getQuantity());
                stmt.setInt(7, productContext.getPrice());
                stmt.executeUpdate();
                ResultSet resultSet = stmt.getGeneratedKeys();
                if(resultSet.next()){
                    int id = resultSet.getInt(1);
                    return new Product(id, productContext);
                }
                throw new RepositoryException("Database cannot generate primary key value");
            }
        } catch (SQLException e) {
            throw new RepositoryException("Cannot create product",e);
        }
    }

    @Override
    public Product getProduct(int id) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID)) {
                stmt.setInt(1, id);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()){
                        String title = resultSet.getString(2);
                        String category = resultSet.getString(3);
                        String manufacturer = resultSet.getString(4);
                        String description = resultSet.getString(5);
                        String img = resultSet.getString(6);
                        int quantity = resultSet.getInt(7);
                        int price = resultSet.getInt(8);
                        ProductContext param = new ProductContext(
                                title, category, manufacturer, description, img, price, quantity
                        );
                        return new Product(id, param);
                    }
                    throw new RepositoryException("Product not found");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Cannot load product",e);
        }
    }

    @Override
    public List<Product> getProducts() throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            List<Product> products = new ArrayList<>();
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet resultSet = stmt.executeQuery(FIND_ALL)) {
                    while (resultSet.next()){
                        int id = resultSet.getInt(1);
                        String title = resultSet.getString(2);
                        String category = resultSet.getString(3);
                        String manufacturer = resultSet.getString(4);
                        String description = resultSet.getString(5);
                        String img = resultSet.getString(6);
                        int quantity = resultSet.getInt(7);
                        int price = resultSet.getInt(8);
                        ProductContext param = new ProductContext(
                                title, category, manufacturer, description, img, price, quantity
                        );
                        products.add(new Product(id, param));
                    }
                }
            }
            return products;
        } catch (SQLException e) {
            throw new RepositoryException("Cannot load products",e);
        }
    }

    @Override
    public void removeProduct(int id) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(DELETE_BY_ID)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Cannot remove product",e);
        }
    }

    @Override
    public void updateProduct(Product product) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(UPDATE_BY_ID)) {
                stmt.setString(1, product.getTitle());
                stmt.setString(2, product.getCategory());
                stmt.setString(3, product.getManufacturer());
                stmt.setString(4, product.getDescription());
                stmt.setString(5, product.getImg());
                stmt.setInt(6, product.getQuantity());
                stmt.setInt(7, product.getPrice());
                stmt.setInt(8, product.getId());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Cannot update product",e);
        }
    }

    @Override
    public void decreaseQuantityOfProductsByOne(List<Integer> ids) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            executeBatch(ids, conn, DECREASE_QUANTITY);
        } catch (SQLException e) {
            throw new RepositoryException("Cannot decrease quantity for product",e);
        }
    }

    @Override
    public void increaseQuantityOfProductsByOne(List<Integer> ids) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            executeBatch(ids, conn, INCREASE_QUANTITY);
        } catch (SQLException e) {
            throw new RepositoryException("Cannot increase quantity for product",e);
        }
    }

    private void executeBatch(List<Integer> ids, Connection conn, String query) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            for (Integer id : ids) {
                stmt.setInt(1, id);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
}
