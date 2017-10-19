package io.wiklandia.tramapi.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.wiklandia.tramapi.repo.StopRepository;
import lombok.AllArgsConstructor;
import model.Stop;

@RestController
@RequestMapping("station")
@AllArgsConstructor
public class SiteController {

	private final StopRepository pointRepo;

	@GetMapping("stops")
	public List<Stop> getAll() {
		return pointRepo.getAllStops();
	}

	@GetMapping("closest")
	public List<Stop> getClosest(@RequestParam("lo") double lon, @RequestParam("la") double lat) {
		return pointRepo.getClosest(lon, lat);
	}

	@GetMapping("next")
	public List<String> next(@RequestParam("id") long id) {

		String d1 = pointRepo.getSickla(id);
		String d2 = pointRepo.getSolna(id);

		return Arrays.asList(d1, d2);
	}

}
