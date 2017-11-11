package io.wiklandia.tramapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;

@EnableScheduling
@EnableCaching
@EnableAsync
@SpringBootApplication
@AllArgsConstructor
public class TramApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TramApiApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CacheManager cacheManager(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
		RedisCacheManager cm = new RedisCacheManager(redisTemplate);
		cm.setDefaultExpiration(60);
		return cm;
	}

}
