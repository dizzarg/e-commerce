package ru.kadyrov.electron.commerce.repository;

import ru.kadyrov.electron.commerce.exception.RepositoryException;
import ru.kadyrov.electron.commerce.models.Product;
import ru.kadyrov.electron.commerce.models.ProductContext;

import java.util.List;

public interface ProductRepository {

    Product addProduct(ProductContext productContext) throws RepositoryException;

    Product getProduct(int id) throws RepositoryException;

    List<Product> getProducts() throws RepositoryException;

    void removeProduct(int id) throws RepositoryException;

    void updateProduct(Product product) throws RepositoryException;

    void decreaseQuantityOfProductsByOne(List<Integer> ids) throws RepositoryException;

    void increaseQuantityOfProductsByOne(List<Integer> ids) throws RepositoryException;

}
