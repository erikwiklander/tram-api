package io.wiklandia.tramapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.wiklandia.tramapi.model.Disruption;
import io.wiklandia.tramapi.service.DisruptionService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class DisruptionController {

	private final DisruptionService disruptionService;

	@GetMapping("disruptions")
	public List<Disruption> getDisruptions() {
		return disruptionService.getDisruptions();
	}

}
