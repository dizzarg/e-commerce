package mysite.repository;

import mysite.exception.RepositoryException;
import mysite.models.Product;

import java.util.List;

public interface ProductRepository {

    void addProduct(Product product) throws RepositoryException;

    Product getProduct(int id) throws RepositoryException;

    List<Product> getProducts() throws RepositoryException;

    void removeProduct(int id) throws RepositoryException;

    void updateProduct(Product product) throws RepositoryException;

    void decreaseQuantityOfProductsByOne(List<Integer> ids) throws RepositoryException;

    void increaseQuantityOfProductsByOne(List<Integer> ids) throws RepositoryException;

//    int getHighestId() throws RepositoryException;
}
