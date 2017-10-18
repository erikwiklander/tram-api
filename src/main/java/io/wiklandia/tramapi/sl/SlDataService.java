package io.wiklandia.tramapi.sl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.wiklandia.tramapi.TramProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This contains logic to parse out necessary data for stops on tvärbanan
 * 
 *
 */
@Slf4j
@Service
@AllArgsConstructor
public class SlDataService {

	private final TramProperties props;
	private final RestTemplate restTemplate;

	public List<StopPoint> getStopsOn22() throws JsonProcessingException, IOException {

		JsonNode jourNode = readFile("jour");

		Map<Integer, Integer> directionByPoint = new HashMap<>();
		for (JsonNode node : jourNode.findPath("ResponseData").findPath("Result")) {
			JsonNode lineNumberNode = node.findPath("LineNumber");
			if (lineNumberNode.asInt() == 22) {
				directionByPoint.put(node.findPath("JourneyPatternPointNumber").asInt(),
						node.findPath("DirectionCode").asInt());
			}
		}

		JsonNode stopNode = readFile("stop");

		List<StopPoint> points = new ArrayList<>();
		for (JsonNode node : stopNode.findPath("ResponseData").findPath("Result")) {

			int stopPoint = node.findPath("StopPointNumber").asInt();
			if (directionByPoint.keySet().contains(stopPoint)) {
				StopPoint p = new StopPoint(stopPoint, node.findPath("StopAreaNumber").asInt(),
						node.findPath("StopPointName").asText(), node.findPath("LocationNorthingCoordinate").asDouble(),
						node.findPath("LocationEastingCoordinate").asDouble(), directionByPoint.get(stopPoint));
				points.add(p);
			}

		}

		return points;
	}

	private JsonNode readFile(String model) throws JsonProcessingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readTree(new ClassPathResource(model + ".json").getInputStream());
	}

	private JsonNode callApi(String model) throws JsonProcessingException, IOException {

		String url = UriComponentsBuilder.fromHttpUrl(props.getLinedataApi()).queryParam("model", model)
				.queryParam("key", props.getKey()).build().toString();

		log.debug("Calling: {}", url);

		ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);

		log.debug("Call complete - now parse");

		ObjectMapper objectMapper = new ObjectMapper();

		return objectMapper.readTree(resp.getBody());
	}

}
