package io.wiklandia.tramapi.controller;

import java.beans.PropertyEditorSupport;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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

	@GetMapping("dep")
	public ResponseEntity<List<Departure>> getDepartures(@RequestParam("direction") Direction direction,
			@RequestParam("id") long id) {
		long t0 = System.currentTimeMillis();
		try {
			log.info("Getting towards {} from: {}", direction, id);
			return ResponseEntity.ok().body(robotService.getDepartures(id, direction));
		} finally {
			log.info("Total request time: {}ms", System.currentTimeMillis() - t0);
		}
	}

	/**
	 * This will make the enum matching case insensitve, makes the client a bit
	 * prettier :)
	 * 
	 * @param dataBinder
	 */
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(Direction.class, new PropertyEditorSupport() {

			@Override
			public void setAsText(String text) {
				setValue(Direction.valueOf(text.toUpperCase()));
			}

		});
	}

}
