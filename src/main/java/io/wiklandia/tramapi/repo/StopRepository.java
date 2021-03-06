package io.wiklandia.tramapi.repo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.wiklandia.tramapi.controller.UnknownIdException;
import io.wiklandia.tramapi.model.Stop;
import lombok.extern.slf4j.Slf4j;

/**
 * Repository for all points along tvärbanan
 * 
 *
 */
@Slf4j
@Component
public class StopRepository {

	private List<Stop> stops = new ArrayList<>();
	private List<Long> ids = new ArrayList<>();
	private Map<String, String> nameByFullName = new HashMap<>();
	private Map<Long, String> nameById = new HashMap<>();

	public List<Stop> getAllStops() {
		return this.stops;
	}

	public List<Stop> getClosest(double lon, double lat) {

		SortedMap<Double, Stop> sort = new TreeMap<>();

		for (Stop p : this.stops) {
			double v = ((p.getLongitude() - lon) * (p.getLongitude() - lon))
					+ ((p.getLatitude() - lat) * (p.getLatitude() - lat));
			sort.put(v, p);
		}

		return new ArrayList<>(sort.values());
	}

	@PostConstruct
	public void loadData() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		this.stops = objectMapper.readValue(new ClassPathResource("22.json").getInputStream(),
				new TypeReference<List<Stop>>() {
				});

		for (Stop stop : this.stops) {
			this.ids.add(stop.getId());
			nameById.put(stop.getId(), stop.getName());
			nameByFullName.put(stop.getFullname(), stop.getName());
		}

	}

	public String getName(String fullName) {
		return nameByFullName.get(fullName);
	}

	public String nameById(Long id) {
		return nameById.get(id);
	}

	public Stop getPrev(long id) {

		if (!ids.contains(id)) {
			throw new UnknownIdException(String.format("Id does not exist: '%s'", id));
		}

		int index = ids.indexOf(id);
		if (index == 0) {
			return null;
		} else {
			return stops.get(index - 1);
		}
	}

	public Stop getNext(long id) {

		if (!ids.contains(id)) {
			throw new UnknownIdException(String.format("Id does not exist: '%s'", id));
		}

		log.debug("ids: {}", ids);

		int index = ids.indexOf(id);
		if (index == ids.size() - 1) {
			return null;
		} else {
			return stops.get(index + 1);
		}
	}

}
