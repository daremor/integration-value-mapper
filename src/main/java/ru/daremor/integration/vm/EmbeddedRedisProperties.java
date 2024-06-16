package ru.daremor.integration.vm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
@ConditionalOnProperty(name="application.redis.embedded", havingValue="true")
public class EmbeddedRedisProperties {
	
    private int redisPort;
    private String redisHost;

    public EmbeddedRedisProperties(@Value("${spring.redis.port}") int redisPort, 
    							   @Value("${spring.redis.host}") String redisHost) {
        this.redisPort = redisPort;
        this.redisHost = redisHost;
    }

	public int getRedisPort() {
		return redisPort;
	}

	public String getRedisHost() {
		return redisHost;
	}
    
}