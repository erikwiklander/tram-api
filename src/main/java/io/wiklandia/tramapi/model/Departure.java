package io.wiklandia.tramapi.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Departure {
	private LocalDateTime depTime;
	private LocalDateTime rtDepTime;
	private String end;
}
