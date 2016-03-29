package mysite.repository.memory;

import mysite.exception.RepositoryException;
import mysite.models.Product;
import mysite.models.ProductParameters;
import mysite.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryProductRepository implements ProductRepository {
    private HashMap<Integer, Product> products = new HashMap<Integer, Product>();
    private final AtomicInteger productIDGenerator = new AtomicInteger(0);

    @Override
    public Product addProduct(ProductParameters productParameters) {
        Product product = new Product(productIDGenerator.incrementAndGet(), productParameters);
        products.put(product.getId(), product);
        return product;
    }

    @Override
    public Product getProduct(int id) throws RepositoryException {
        if (products.containsKey(id)) {
            return products.get(id);
        }
        throw new RepositoryException("Cannot get product: product with this id does not exist in repository");
    }

    @Override
    public List<Product> getProducts() {
        return new ArrayList<Product>(products.values());
    }

    @Override
    public void removeProduct(int productId) {
        products.remove(productId);
    }

    @Override
    public void updateProduct(Product product) {
        products.replace(product.getId(), product);
    }

    @Override
    public void decreaseQuantityOfProductsByOne(List<Integer> ids)
            throws RepositoryException {
        for (int productId : ids) {
            products.get(productId).decreaseQuantity(1);
        }
    }

    @Override
    public void increaseQuantityOfProductsByOne(List<Integer> ids)
            throws RepositoryException {
        for (int productId : ids) {
            products.get(productId).increaseQuantity(1);
        }
    }
}
