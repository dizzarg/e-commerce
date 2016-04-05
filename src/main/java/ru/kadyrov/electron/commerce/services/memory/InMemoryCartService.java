package ru.kadyrov.electron.commerce.services.memory;

import org.springframework.stereotype.Service;
import ru.kadyrov.electron.commerce.models.Cart;
import ru.kadyrov.electron.commerce.models.Product;
import ru.kadyrov.electron.commerce.repository.ProductRepository;
import ru.kadyrov.electron.commerce.services.CartService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class InMemoryCartService implements CartService {

    @Named("jdbcProductRepository")
    @Inject
    private ProductRepository productRepository;

    private ConcurrentMap<String, Cart> store = new ConcurrentHashMap<>();

    @Override
    public Cart createCart(final String session) {
        Cart value = new Cart(session);
        Cart cart = store.putIfAbsent(session, value);
        return cart == null ? value : cart;
    }

    @Override
    public Cart getCart(String session) {
        return store.get(session);
    }

    public void addProduct(final String sessionId, final Product product) {
        Cart cart = store.get(sessionId);
        cart.addItem(product);
    }

    @Override
    public void cleanCart(final String session) {
        createCart(session).clean();
    }

}
