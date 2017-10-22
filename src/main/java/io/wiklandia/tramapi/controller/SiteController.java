package io.wiklandia.tramapi.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.wiklandia.tramapi.repo.StopRepository;
import io.wiklandia.tramapi.service.RobotService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Departure;
import model.NextResponse;
import model.Stop;

@Slf4j
@RestController
@RequestMapping("station")
@AllArgsConstructor
public class SiteController {

	private final StopRepository pointRepo;
	private final RobotService robotService;

	@GetMapping("stops")
	public List<Stop> getAll() {
		log.debug("Gettig all");
		return pointRepo.getAllStops();
	}

	@GetMapping("closest")
	public List<Stop> getClosest(@RequestParam("lo") double lon, @RequestParam("la") double lat) {
		log.debug("Getting closest: {} {}", lon, lat);
		return pointRepo.getClosest(lon, lat);
	}

	@GetMapping("closestId")
	public long getClosestId(@RequestParam("lo") double lon, @RequestParam("la") double lat) {
		log.debug("Getting closest: {} {}", lon, lat);
		return pointRepo.getClosest(lon, lat).get(0).getId();
	}

	/**
	 * @param id
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@GetMapping("next")
	public NextResponse test(@RequestParam("id") long id) throws InterruptedException, ExecutionException {

		log.debug("Getting departures from: {}", id);

		if (!pointRepo.exists(id)) {
			throw new UnknownIdException(String.format("Id does not exist: '%s'", id));
		}
		Stop d1 = pointRepo.getPrev(id);
		Stop d2 = pointRepo.getNext(id);
		Future<List<Departure>> node1 = robotService.getNext(id, d1);
		Future<List<Departure>> node2 = robotService.getNext(id, d2);

		return new NextResponse(node1.get(), node2.get());

	}

}
