package io.wiklandia.tramapi.repo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.wiklandia.tramapi.sl.StopPoint;

/**
 * Repository for all points along tvärbanan
 * 
 *
 */
@Component
public class PointRepository {

	private List<StopPoint> points = new ArrayList<>();
	private List<StopPoint> areas = new ArrayList<>();

	public List<StopPoint> getAllPoints() {
		return points;
	}

	public List<StopPoint> getAreas(String q) {

		if (q.length() == 0) {
			return areas;
		} else {
			return areas.stream().filter(p -> p.getName().toLowerCase().contains(q.toLowerCase()))
					.collect(Collectors.toList());
		}

	}

	public List<StopPoint> getClosest(double n, double e) {

		SortedMap<Double, StopPoint> sort = new TreeMap<>();

		for (StopPoint p : this.areas) {
			double v = ((p.getNcoord() - n) * (p.getNcoord() - n)) + ((p.getEcoord() - e) * (p.getEcoord() - e));
			sort.put(v, p);
		}

		return new ArrayList<StopPoint>(sort.values());
	}

	@PostConstruct
	public void loadData() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		this.points = objectMapper.readValue(new ClassPathResource("points.json").getInputStream(),
				new TypeReference<List<StopPoint>>() {
				});

		this.points.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));

		Map<Integer, StopPoint> un = new LinkedHashMap<>();
		for (StopPoint p : this.points) {
			un.put(p.getAreaNumber(), p);
		}
		this.areas = new ArrayList<StopPoint>(un.values());

	}

}
