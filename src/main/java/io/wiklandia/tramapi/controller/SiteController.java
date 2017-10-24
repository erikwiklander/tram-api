package io.wiklandia.tramapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.wiklandia.tramapi.repo.StopRepository;
import io.wiklandia.tramapi.service.RobotService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Departure;
import model.Stop;

@Slf4j
@RestController
@AllArgsConstructor
public class SiteController {

	private final StopRepository pointRepo;
	private final RobotService robotService;

	@GetMapping("closestId")
	public long getClosestId(@RequestParam("lo") double lon, @RequestParam("la") double lat) {
		log.debug("Getting closest: {} {}", lon, lat);
		return pointRepo.getClosest(lon, lat).get(0).getId();
	}

	@GetMapping("solna")
	public List<Departure> getSolna(@RequestParam("id") long id) {
		log.debug("Getting towards solna from: {}", id);
		Stop s = pointRepo.getPrev(id);
		return getNext(id, s);
	}

	@GetMapping("sickla")
	public List<Departure> getSickla(@RequestParam("id") long id) {
		log.debug("Getting towards sickla from: {}", id);
		Stop s = pointRepo.getNext(id);
		return getNext(id, s);
	}

	private List<Departure> getNext(long id, Stop s) {
		return robotService.getNext(id, s);
	}

}
