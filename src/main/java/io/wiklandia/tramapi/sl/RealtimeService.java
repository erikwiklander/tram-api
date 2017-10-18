package io.wiklandia.tramapi.sl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.wiklandia.tramapi.TramProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class RealtimeService {

	private final TramProperties props;
	private final RestTemplate restTemplate;

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

	@Cacheable("realtime")
	public List<Departure> getDepartures(String siteId) throws JsonProcessingException, IOException {

		String url = UriComponentsBuilder.fromHttpUrl(props.getRealtimeApi()).queryParam("key", props.getRealtimeKey())
				.queryParam("siteid", siteId).queryParam("timewindow", "60").build().toString();

		log.debug("Calling {}", url);
		ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);

		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode node = objectMapper.readTree(resp.getBody());

		List<Departure> result = new ArrayList<>();
		for (JsonNode tram : node.findPath("ResponseData").findPath("Trams")) {
			int lineNumber = tram.findPath("LineNumber").asInt();
			if (lineNumber == 22) {
				result.add(
						new Departure(tram.findPath("Destination").asText(), tram.findPath("JourneyDirection").asInt(),
								parseDate(tram.findPath("TimeTabledDateTime").asText()),
								parseDate(tram.findPath("ExpectedDateTime").asText())));
			}
		}

		return result;

	}

	@CacheEvict(allEntries = true, cacheNames = { "realtime" })
	@Scheduled(fixedDelay = 60_000)
	public void evict() {
	}

	private LocalDateTime parseDate(String s) {
		return LocalDateTime.parse(s, FORMATTER);
	}

}
