package io.wiklandia.tramapi.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Departure implements Serializable {
	private static final long serialVersionUID = 1L;
	private LocalDateTime depTime;
	private LocalDateTime rtDepTime;
	private String end;
	private String current;
}
