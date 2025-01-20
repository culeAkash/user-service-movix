package com.movix.user.service.services;

import org.redisson.api.RBloomFilter;

public interface BloomFilterService {

    void loadAllDataInBloomFilter();

    RBloomFilter<String> getUserNameBloomFilter();
}
