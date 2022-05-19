package com.example.demo;


import com.example.demo.services.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@SpringBootApplication
@RestController
@Slf4j
public class OnlineSellerApplication {

    @Resource
    private OrderService orderService;

    public static void main(String[] args) {
        SpringApplication.run(OnlineSellerApplication.class, args);
    }

    @GetMapping("/order/create")
    public String createOrder(@RequestParam(required = false, name = "itemId") Long itemId,
                              @RequestParam(required = false, name = "userId") Long userId) throws MQBrokerException, RemotingException, JsonProcessingException, InterruptedException, MQClientException {
        log.info("================================================================================");
        log.info("start process...");
        log.info("================================================================================");

        log.info("itemId={}, userId={}", itemId, userId);
        long orderId = orderService.create(itemId, userId);
        // log.info("itemId={}, userId={}, orderId={}", itemId, userId, orderId);
        return "orderId=" + orderId;
    }
}
