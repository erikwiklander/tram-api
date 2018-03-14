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
	private String disruptionApi;
	private String disruptionKey;
	private boolean requireSsl = false;

	private List<String> allowedOrigins;
}
