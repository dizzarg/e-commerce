package ru.kadyrov.electron.commerce.services;

import ru.kadyrov.electron.commerce.exception.ModelException;
import ru.kadyrov.electron.commerce.exception.RepositoryException;
import ru.kadyrov.electron.commerce.exception.ShopServiceException;
import ru.kadyrov.electron.commerce.models.*;
import ru.kadyrov.electron.commerce.repository.CustomerRepository;
import ru.kadyrov.electron.commerce.repository.OrderRepository;
import ru.kadyrov.electron.commerce.repository.PaymentRepository;
import ru.kadyrov.electron.commerce.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ShopService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    private final static Logger logger = LoggerFactory.getLogger(ShopService.class);

    @Inject
    public ShopService(@Named("jdbcCustomerRepository") CustomerRepository customerRepository,
                       @Named("jdbcProductRepository") ProductRepository productRepository,
                       @Named("jdbcOrderRepository") OrderRepository orderRepository,
                       @Named("jdbcPaymentRepository") PaymentRepository paymentRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    public Product addProduct(ProductContext productParams) {
        try {
            Product product = productRepository.addProduct(productParams);
            logger.info("Product was loaded");
            return product;
        } catch (RepositoryException e) {
            logger.error("Could not add product: ", e);
            throw new ShopServiceException("Could not add product: " + e.getMessage(), e);
        }
    }

    public void removeProductFromCustomer(int productId, String customerUsername){
        try {
            Customer customer = customerRepository.getCustomer(customerUsername);
            customer.removeProductFromShoppingCart(productId);
            customerRepository.updateCustomer(customer);
            logger.info("Product was removed from Customer Shopping cart");
        } catch (ModelException | RepositoryException e) {
            logger.error("Could not remove product from Customer Shopping cart: ", e);
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
                logger.info("Product was added to Customer Shopping cart");
            } else{
                logger.warn("Cannot add product to Customer Shopping cart");
            }
        } catch (RepositoryException e) {
            logger.error("Cannot add product to Customer Shopping cart", e);
            throw new ShopServiceException("Could not add product to Customer Shopping cart: " + e.getMessage(), e);
        }
    }

    public  Product getProductWithId(int productId) {
        try {
            Product product = productRepository.getProduct(productId);
            logger.info("Product by id was loaded");
            return product;
        } catch (RepositoryException e) {
            logger.error("Could not load product", e);
            throw new ShopServiceException("Could not load product: " + e.getMessage(), e);
        }
    }

    public  List<Product> getProducts() {
        try {
            List<Product> products = productRepository.getProducts();
            logger.info("Products was loaded");
            return products;
        } catch (RepositoryException e) {
            logger.error("Could not load products", e);
            throw new ShopServiceException("Could not load products.: " + e.getMessage(), e);
        }
    }

    public  void removeProduct(int productId) {
        try {
            // If there are any customers first remove the item from their
            // carts
            for (Customer c : customerRepository.getCustomers()) {
                boolean aProductWasRemoved = c.removeProductsWithIdFromShoppingCart(productId);
                if (aProductWasRemoved) {
                    customerRepository.updateCustomer(c);
                }
            }
            productRepository.removeProduct(productId);
            logger.info("Product was removed");
        } catch (ModelException | RepositoryException e) {
            logger.error("Could not remove product", e);
            throw new ShopServiceException("Could not remove product: " + e.getMessage(), e);
        }
    }

    public  void updateProduct(int productId, ProductContext productParams) {
        try {
            productRepository.updateProduct(new Product(productId, productParams));
            logger.info("Product was updated");
        } catch (RepositoryException e) {
            logger.error("Could not update product", e);
            throw new ShopServiceException("Could not update Product: " + e.getMessage(), e);
        }
    }

    public Payment createPayment(Integer orderId){
        try{
            Order order = orderRepository.getOrder(orderId);
            Integer sum = 0;
            for (Integer productId : order.getProductIds()){
                Product product = productRepository.getProduct(productId);
                sum+= product.getPrice();
            }
            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setAmount(sum);
            Payment payment1 = paymentRepository.addPayment(payment);
            logger.info("Payments was created");
            return payment1;
        } catch (RepositoryException ex){
            logger.error("Cannot create payment", ex);
            throw new ShopServiceException("Cannot create payment", ex);
        }
    }

    public List<Payment> getPayments(){
        try {
            List<Payment> payments = paymentRepository.getPayments();
            logger.info("Payments was loaded");
            return payments;
        } catch (RepositoryException ex) {
            logger.error("Cannot load payments", ex);
            throw new ShopServiceException("Cannot load payments", ex);
        }
    }

    public Order createOrder(String customerUsername) {
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
                // TODO: uses transactions
                Order order = orderRepository.addOrder(customer);
                try {
                    // clear the customers shopping cart
                    customer.getShoppingCart().clear();
                    customerRepository.updateCustomer(customer);
                } catch (RepositoryException e) {
                    orderRepository.removeOrder(order.getId());
                    throw e;
                }
                logger.info("Order was created");
                return order;
            } catch (RepositoryException e) {
                productRepository.increaseQuantityOfProductsByOne(orderedProductIds);
                throw e;
            }
        } catch (RepositoryException e) {
            logger.error("Could not create order", e);
            throw new ShopServiceException("Could not create order: " + e.getMessage(), e);
        }
    }

    public  Order getOrder(int orderId) {
        try {
            Order order = orderRepository.getOrder(orderId);
            logger.info("Order was loaded");
            return order;
        } catch (RepositoryException e) {
            logger.error("Could not load order", e);
            throw new ShopServiceException("Could not load order: " + e.getMessage(), e);
        }
    }

    public  List<Order> getOrders(String customerUsername) {
        try {
            if(!customerRepository.existsCustomer(customerUsername)){
                throw new RepositoryException("Customer does not exist");
            }
            List<Order> orders = orderRepository.getOrders(customerUsername);
            logger.info("Orders was loaded");
            return orders;
        } catch (RepositoryException e) {
            logger.error("Could not load orders", e);
            throw new ShopServiceException("Could not load orders: " + e.getMessage(), e);
        }
    }

    public  void updateOrder(Order order) {
        try {
            orderRepository.updateOrder(order);
            logger.info("Orders was updated");
        } catch (RepositoryException e) {
            logger.error("Could not update order", e);
            throw new ShopServiceException("Could not update order: " + e.getMessage(), e);
        }
    }

    public  void removeOrder(int orderId) {
        try {
            orderRepository.removeOrder(orderId);
            logger.info("Orders was removed");
        } catch (RepositoryException e) {
            logger.error("Could not remove order", e);
            throw new ShopServiceException("Could not remove order: " + e.getMessage(), e);
        }
    }

}
