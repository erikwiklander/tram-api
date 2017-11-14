package io.wiklandia.tramapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;

import io.wiklandia.tramapi.TramProperties;
import io.wiklandia.tramapi.model.Disruption;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class DisruptionService {

	private final TramProperties props;

	@Cacheable("disruptions")
	public List<Disruption> getDisruptions() {

		// @formatter:off
		String url = UriComponentsBuilder.fromHttpUrl(props.getDisruptionApi())
				.queryParam("key", props.getDisruptionKey())
//				.queryParam("LineNumber", "22")
				.build()
				.toString();
		// @formatter:on

		RestTemplate restTemplate = new RestTemplate();

		log.info("Calling {}", url);

		JsonNode res = restTemplate.getForEntity(url, JsonNode.class).getBody();

		List<Disruption> disruptions = new ArrayList<>();
		for (JsonNode disruption : res.findPath("ResponseData")) {
			disruptions.add(
					new Disruption(disruption.findPath("Header").asText(), disruption.findPath("Details").asText()));
		}

		return disruptions;
	}

}
