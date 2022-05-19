package com.example.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 商品服务
 */
@Slf4j
@Service
public class ItemService {

    private static final ConcurrentHashMap<Long, Integer> stocks = new ConcurrentHashMap<>();

    static {
        stocks.put(1L, 10);
        stocks.put(2L, 0);
    }

    /**
     * 库存扣减
     *
     * @param itemId
     */
    public boolean decStockByItemId(Long itemId) {
        log.info("库存扣减 itemId={}", itemId);
        if (stocks.get(itemId) > 0) {
            stocks.put(itemId, stocks.get(itemId) - 1);
            return true;
        }
        return false;
    }
}
