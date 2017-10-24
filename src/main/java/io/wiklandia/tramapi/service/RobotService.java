package io.wiklandia.tramapi.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;

import io.wiklandia.tramapi.TramProperties;
import io.wiklandia.tramapi.repo.StopRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Departure;
import model.Stop;

@Slf4j
@Service
@AllArgsConstructor
public class RobotService {

	private final TramProperties props;
	private final RestTemplate restTemplate;
	private final StopRepository stopRepo;

	@Cacheable("realtime")
	public List<Departure> getNext(long id, Stop directionStop) {

		if (directionStop == null) {
			return Collections.emptyList();
		}

		// @formatter:off
		String url = UriComponentsBuilder.fromHttpUrl(props.getRobotApi())
				.queryParam("key", props.getRobotKey())
				.queryParam("id", id)
				.queryParam("direction", directionStop.getId())
				.queryParam("passlist", "0")
				.queryParam("products", "64")
				.queryParam("format", "json")
				.build()
				.toString();
		// @formatter:on

		log.debug("Calling api: {}", url);

		long t0 = System.currentTimeMillis();

		JsonNode res = restTemplate.getForEntity(url, JsonNode.class).getBody();

		log.debug("Call done: ({}ms)", System.currentTimeMillis() - t0);

		List<Departure> deps = new ArrayList<>();
		for (JsonNode departure : res.findPath("Departure")) {
			String time = departure.findPath("time").asText();
			String date = departure.findPath("date").asText();
			String rtTime = departure.findPath("rtTime").asText();
			String rtDate = departure.findPath("rtDate").asText();
			String direction = departure.findPath("direction").asText();

			String end = stopRepo.getName(direction);

			LocalDateTime rt = null;
			if (rtTime != null && rtTime.length() > 0) {
				rt = LocalDateTime.parse(String.format("%sT%s", rtDate, rtTime));
			}

			Departure d = new Departure(LocalDateTime.parse(String.format("%sT%s", date, time)), rt, end);

			deps.add(d);

		}

		return deps;

	}

	@CacheEvict(allEntries = true, cacheNames = { "realtime" })
	@Scheduled(fixedDelay = 60_000)
	public void evict() {
		// empty
	}

}
