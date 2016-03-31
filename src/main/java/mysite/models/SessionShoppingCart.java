package mysite.models;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionShoppingCart {

    private Map<Integer, Integer> items = new ConcurrentHashMap<>();

    public void addProduct(Integer productId, Integer count){
        items.putIfAbsent(productId, count);
    }

    public void removeProduct(Integer productId, Integer count){
        items.putIfAbsent(productId, count);
    }

}
