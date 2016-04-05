package ru.kadyrov.electron.commerce.services;

import ru.kadyrov.electron.commerce.models.Cart;

public interface CartService {

    Cart createCart(String session);

    Cart getCart(String session);

    void cleanCart(String session);

}
