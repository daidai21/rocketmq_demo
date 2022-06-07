package com.example.demo;

import java.util.concurrent.ConcurrentHashMap;

/**
 * mock 订单存储
 */
public class OrderRepository {

    /**
     * orderId为索引
     */
    private static final ConcurrentHashMap<String, String> db = new ConcurrentHashMap<>();

    public boolean saveOrder(String orderId, String orderInfo) {
        if (db.containsKey(orderId)) {
            return false;
        } else {
            db.put(orderId, orderInfo);
            return true;
        }
    }

}
