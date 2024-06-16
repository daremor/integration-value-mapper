package ru.daremor.integration.vm;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;

@Component
@Slf4j
public class EmbededRedis {
	
    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() throws IOException {
        redisServer = RedisServer.builder().port(6379).setting("maxmemory 256M").build();
        log.info("EmbededRedis starting!");
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
    	log.info("EmbededRedis stopping!");
        redisServer.stop();
    }
}