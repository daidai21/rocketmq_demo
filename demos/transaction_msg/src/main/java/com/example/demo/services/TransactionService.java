package com.example.demo.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 模拟事务中心
 */
@Slf4j
@Service
public class TransactionService {

    private static final ConcurrentHashMap<String, Boolean> localTrans = new ConcurrentHashMap<>();

    public Boolean getTransactionStatusById(String transactionId) {
        log.info("transactionId={}, status={}", transactionId, localTrans.getOrDefault(transactionId, false));
        return localTrans.getOrDefault(transactionId, false);
    }

    public void createTransaction(String transactionId) {
        log.info("transactionId={}", transactionId);
        localTrans.put(transactionId, false);
    }

}
