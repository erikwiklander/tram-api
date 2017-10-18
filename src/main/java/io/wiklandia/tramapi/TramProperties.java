package io.wiklandia.tramapi;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("tram")
public class TramProperties {

	private String linedataApi;
	private String linedataKey;

	private String realtimeApi;
	private String realtimeKey;

}
