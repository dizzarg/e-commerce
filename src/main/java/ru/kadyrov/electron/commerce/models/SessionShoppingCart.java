package ru.kadyrov.electron.commerce.models;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionShoppingCart {

    private Map<Integer, Integer> items = new ConcurrentHashMap<>();

    public void addProduct(Integer productId, Integer quantity){
        items.putIfAbsent(productId, quantity);
    }

    public void removeProduct(Integer productId, Integer quantity){
        items.putIfAbsent(productId, quantity);
    }

}
