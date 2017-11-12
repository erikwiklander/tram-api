package io.wiklandia.tramapi;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class CacheConfig {

	private final TramProperties props;

	@Bean
	public CacheManager cacheManager(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
		RedisCacheManager cm = new RedisCacheManager(redisTemplate);
		cm.setDefaultExpiration(props.getCacheTtl());
		return cm;
	}

}
