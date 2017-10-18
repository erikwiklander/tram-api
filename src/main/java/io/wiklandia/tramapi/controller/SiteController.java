package io.wiklandia.tramapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.wiklandia.tramapi.repo.PointRepository;
import io.wiklandia.tramapi.sl.StopPoint;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("station")
@AllArgsConstructor
public class SiteController {

	private final PointRepository pointRepo;

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
}
