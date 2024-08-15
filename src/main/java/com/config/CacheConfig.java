package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@EnableCaching
public class CacheConfig {
    @Autowired
    private CacheManager cacheManager;

    @Bean
    public CommandLineRunner cacheInitializer() {
        return args -> {
            // Redis 캐시 비우기
            cacheManager.getCacheNames().forEach(cacheName -> {
                Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
            });
        };
    }
}
