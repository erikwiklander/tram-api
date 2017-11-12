package io.wiklandia.tramapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.wiklandia.tramapi.model.Departure;
import io.wiklandia.tramapi.repo.StopRepository;
import io.wiklandia.tramapi.service.Direction;
import io.wiklandia.tramapi.service.RobotService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class SiteController {

	private final StopRepository pointRepo;
	private final RobotService robotService;

	@GetMapping("closestId")
	public ResponseEntity<Long> getClosestId(@RequestParam("lo") double lon, @RequestParam("la") double lat) {
		log.info("Getting closest: {} {}", lon, lat);
		return ResponseEntity.ok().body(pointRepo.getClosest(lon, lat).get(0).getId());
	}

	@GetMapping("solna")
	public ResponseEntity<List<Departure>> getSolna(@RequestParam("id") long id) {
		log.info("Getting towards solna from: {}", id);
		return ResponseEntity.ok().body(robotService.getDepartures(id, Direction.SOLNA));
	}

	@GetMapping("sickla")
	public ResponseEntity<List<Departure>> getSickla(@RequestParam("id") long id) {
		log.info("Getting towards sickla from: {}", id);
		return ResponseEntity.ok().body(robotService.getDepartures(id, Direction.SICKLA));
	}

}
