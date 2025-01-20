package com.movix.user.service.services.impl;

import com.movix.user.service.entities.User;
import com.movix.user.service.repositories.UserRepository;
import com.movix.user.service.services.BloomFilterService;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class BloomFilterServiceImpl implements BloomFilterService {

    private RedissonClient redissonClient;
    private UserRepository userRepository;

    private RBloomFilter<String> userNameBloomFilter;


    @EventListener(ApplicationReadyEvent.class)
    @Override
    public void loadAllDataInBloomFilter() {
        RBloomFilter<String> stringBloomFilter = getUserNameBloomFilter();

        List<User> users = this.userRepository.findAll();

        users.forEach(user->{
            stringBloomFilter.add(user.getUsername());
        });

    }

    @Override
    @Synchronized
    public RBloomFilter<String> getUserNameBloomFilter() {
        if (null == userNameBloomFilter) {
            RBloomFilter<String> stringRBloomFilter = redissonClient.getBloomFilter("usernames");
            stringRBloomFilter.tryInit(99999, 0.001);
            userNameBloomFilter = stringRBloomFilter;
        }

        return userNameBloomFilter;
    }
}
