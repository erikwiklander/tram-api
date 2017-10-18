package io.wiklandia.tramapi.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.wiklandia.tramapi.repo.PointRepository;
import io.wiklandia.tramapi.sl.Departure;
import io.wiklandia.tramapi.sl.RealtimeService;
import io.wiklandia.tramapi.sl.StopPoint;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("station")
@AllArgsConstructor
public class SiteController {

	private final PointRepository pointRepo;
	private final RealtimeService realtimeService;

	@GetMapping("points")
	public List<StopPoint> getAll() {
		return pointRepo.getAllPoints();
	}

	@GetMapping("areas")
	public List<StopPoint> getAreas(@RequestParam(value = "q", required = false, defaultValue = "") String q) {
		return pointRepo.getAreas(q);
	}

	@GetMapping("closest")
	public List<StopPoint> getClosest(@RequestParam("n") double n, @RequestParam("e") double e) {
		return pointRepo.getClosest(n, e);
	}

	@GetMapping("realtime")
	public List<Departure> realtime(@RequestParam("siteId") String siteId) throws JsonProcessingException, IOException {
		log.debug("Accessing realtime with id: {}", siteId);
		return realtimeService.getDepartures(siteId);
	}
}
