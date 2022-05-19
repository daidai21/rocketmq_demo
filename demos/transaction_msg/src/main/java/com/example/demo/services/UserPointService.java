package com.example.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserPointService {

    /**
     * 增加用户积分
     * @param userId
     */
    public void addPointByUser(Long userId) {
        log.info("给用户增加积分 userId={}", userId);
    }
}
