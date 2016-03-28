package mysite.services;

import mysite.exception.ModelException;
import mysite.exception.RepositoryException;
import mysite.exception.ShopServiceException;
import mysite.models.Customer;
import mysite.models.Order;
import mysite.models.Product;
import mysite.models.ProductParameters;
import mysite.repository.CustomerRepository;
import mysite.repository.OrderRepository;
import mysite.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ShopService {
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private final static Logger logger = LoggerFactory.getLogger(ShopService.class);

    private final AtomicInteger productIDGenerator = new AtomicInteger(0);
    private final AtomicInteger orderIDGenerator = new AtomicInteger(0);

    @Inject
    public ShopService(@Named("jdbcCustomerRepository") CustomerRepository customerRepository,
                       @Named("jdbcProductRepository") ProductRepository productRepository,
                       @Named("jdbcOrderRepository") OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public  Product addProduct(ProductParameters productParams) {
        Product newProduct;
        try {
            newProduct = new Product(getNextProductId(), productParams);
            productRepository.addProduct(newProduct);
        } catch (RepositoryException e) {
            throw new ShopServiceException("Could not add product: "
                    + e.getMessage(), e);
        }
        return newProduct;
    }

    // Defaults to quantity: 1 if no amount is provided.
    public void addProductToCustomer(int productId, String customerUsername) {
        addProductToCustomer(productId, customerUsername, 1);
    }

    public void removeProductToCustomer(int productId, String customerUsername, int amount){
        try {
            Customer customer = customerRepository.getCustomer(customerUsername);
            try {
                customer.removeProductFromShoppingCart(productId);
                customerRepository.updateCustomer(customer);
            } catch (ModelException e) {
                e.printStackTrace();
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
            throw new ShopServiceException("Could not add product to customer: " + e.getMessage(), e);
        }
    }

    public  void addProductToCustomer(int productId, String customerUsername, int amount) {
        try {
            if (productRepository.getProduct(productId).getQuantity() >= amount) {
                Customer customer = customerRepository.getCustomer(customerUsername);

                for (int i = 0; i < amount; i++) {
                    customer.addProductToShoppingCart(productId);
                }
                // Make the repository record the changes to customer
                customerRepository.updateCustomer(customer);
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
            throw new ShopServiceException("Could not add product to customer: " + e.getMessage(), e);
        }
    }

    public  Product getProductWithId(int productId) {
        try {
            return productRepository.getProduct(productId);
        } catch (RepositoryException e) {
            throw new ShopServiceException("Could not getProduct: " + e.getMessage(), e);
        }
    }

    public  List<Product> getProducts() {
        try {
            return productRepository.getProducts();
        } catch (RepositoryException e) {
            throw new ShopServiceException("Could not get products.: " + e.getMessage(), e);
        }
    }

    public  void removeProduct(int productId) {
        try {
            try {
                // If there are any customers first remove the item from their
                // carts
                for (Customer c : customerRepository.getCustomers()) {
                    boolean aProductWasRemoved = c.removeProductsWithIdFromShoppingCart(productId);
                    if (aProductWasRemoved) {
                        updateCustomer(c);
                    }
                }
            } catch (Exception e) {
                // TODO Be more specific and handle exception
                // No users in DB - no action needed
            }

            productRepository.removeProduct(productId);
        } catch (RepositoryException e) {
            throw new ShopServiceException("Could not remove product: " + e.getMessage(), e);
        }
    }

    public  void updateProduct(int productId, ProductParameters productParams) {
        try {
            productRepository.updateProduct(new Product(productId, productParams));
        } catch (RepositoryException e) {
            throw new ShopServiceException("Could not updateProduct: " + e.getMessage(), e);
        }
    }

    public  void addCustomer(Customer customer) {
        try {
            customerRepository.addCustomer(customer);
        } catch (RepositoryException e) {
            throw new ShopServiceException("Could not add customer: " + e.getMessage(), e);
        }
    }

    public  Customer getCustomer(String customerUsername) {
        try {
            return customerRepository.getCustomer(customerUsername);
        } catch (RepositoryException e) {
            throw new ShopServiceException("Could not get customer: " + e.getMessage(), e);
        }
    }

    public  void updateCustomer(Customer customer) {
        try {
            customerRepository.updateCustomer(customer);
        } catch (RepositoryException e) {
            throw new ShopServiceException("Could not update customer: " + e.getMessage(), e);
        }
    }

    public  void removeCustomer(String customerUsername) {
        try {
            customerRepository.removeCustomer(customerUsername);
        } catch (RepositoryException e) {
            throw new ShopServiceException("Could not remove customer: " + e.getMessage(), e);
        }
    }

    public  Order createOrder(String customerUsername) {
        Order newOrder;
        try {
            Customer customer = customerRepository.getCustomer(customerUsername);
            ArrayList<Integer> orderedProductIds = customer.getShoppingCart().getProductIds();
            if (orderedProductIds.isEmpty()) {
                throw new ShopServiceException("This user has no items in their cart");
            }
            // decrease stock quantity of products in product repository
            productRepository.decreaseQuantityOfProductsByOne(orderedProductIds);
            try {
                // place a order containing the products removed from stock
                int orderId = getNextOrderId();
                newOrder = new Order(orderId, customerUsername, orderedProductIds);
                orderRepository.addOrder(newOrder);
                try {
                    // clear the customers shopping cart
                    customer.getShoppingCart().clear();
                    customerRepository.updateCustomer(customer);
                } catch (RepositoryException e) {
                    orderRepository.removeOrder(orderId);
                    throw e;
                }
            } catch (RepositoryException e) {
                productRepository.increaseQuantityOfProductsByOne(orderedProductIds);
                throw e;
            }
        } catch (RepositoryException e) {
            throw new ShopServiceException("Could not create order: " + e.getMessage(), e);
        }
        return newOrder;
    }

    public  Order getOrder(int orderId) {
        try {
            Order order = orderRepository.getOrder(orderId);
            return order;
        } catch (RepositoryException e) {
            throw new ShopServiceException("Could not get order: " + e.getMessage(), e);
        }
    }

    public  List<Order> getOrders(String customerUsername) {
        try {
            customerRepository.getCustomer(customerUsername); // Should throw
            // exception if
            // user
            // does not exist
            return orderRepository.getOrders(customerUsername);
        } catch (RepositoryException e) {
            throw new ShopServiceException("Could not get orders: " + e.getMessage(), e);
        }
    }

    public  void updateOrder(Order order) {
        try {
            orderRepository.updateOrder(order);
        } catch (RepositoryException e) {
            throw new ShopServiceException("Could not update order: " + e.getMessage(), e);
        }
    }

    public  void removeOrder(int orderId) {
        try {
            orderRepository.removeOrder(orderId);
        } catch (RepositoryException e) {
            throw new ShopServiceException("Could not remove order: " + e.getMessage(), e);
        }
    }

    private int getNextProductId() {
        return productIDGenerator.incrementAndGet();
    }

    private int getNextOrderId() {
        return orderIDGenerator.incrementAndGet();
    }
}