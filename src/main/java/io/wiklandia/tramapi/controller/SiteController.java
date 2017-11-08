package io.wiklandia.tramapi.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.wiklandia.tramapi.model.Departure;
import io.wiklandia.tramapi.model.Stop;
import io.wiklandia.tramapi.repo.StopRepository;
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
		log.debug("Getting closest: {} {}", lon, lat);
		return ResponseEntity.ok().cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
				.body(pointRepo.getClosest(lon, lat).get(0).getId());
	}

	@GetMapping("solna")
	public ResponseEntity<List<Departure>> getSolna(@RequestParam("id") long id) {
		log.debug("Getting towards solna from: {}", id);
		Stop s = pointRepo.getPrev(id);
		return ResponseEntity.ok().cacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES)).body(getNext(id, s));
	}

	@GetMapping("sickla")
	public ResponseEntity<List<Departure>> getSickla(@RequestParam("id") long id) {
		log.debug("Getting towards sickla from: {}", id);
		Stop s = pointRepo.getNext(id);
		return ResponseEntity.ok().cacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES)).body(getNext(id, s));
	}

	private List<Departure> getNext(long id, Stop s) {
		return robotService.getNext(id, s);
	}

}
