package io.wiklandia.tramapi.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import model.Stop;

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
	private SortedMap<Long, Stop> byId = new TreeMap<>();

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

		return new ArrayList<Stop>(sort.values());
	}

	public String getSickla(long id) {
		int index = ids.indexOf(id);
		log.debug("sickla {}", index);
		if (index >= ids.size()) {
			return stops.get(index).getName();
		} else {
			return null;
		}
	}

	public String getSolna(long id) {
		int index = ids.indexOf(id);
		log.debug("solna {}", index);
		if (index < ids.size()) {
			return stops.get(index).getName();
		} else {
			return null;
		}
	}

	@PostConstruct
	public void loadData() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		this.stops = objectMapper.readValue(new ClassPathResource("22.json").getInputStream(),
				new TypeReference<List<Stop>>() {
				});

		for (Stop stop : this.stops) {
			this.byId.put(stop.getId(), stop);
			this.ids.add(stop.getId());
		}

	}

}
