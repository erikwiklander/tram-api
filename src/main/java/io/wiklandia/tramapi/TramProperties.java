package io.wiklandia.tramapi;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("tram")
public class TramProperties {

	private String robotApi;
	private String robotKey;
	private int cacheTtl = 60;

	private List<String> allowedOrigins;
}
