package io.wiklandia.tramapi.sl;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Departure {

	private String destinationString;
	private int direction;
	private LocalDateTime timetable;
	private LocalDateTime expected;

}
