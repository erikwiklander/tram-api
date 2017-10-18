package io.wiklandia.tramapi.sl;

import lombok.Data;

@Data
public class StopPoint {

	private final int pointNumber;
	private final int areaNumber;
	private final String name;
	private final double ncoord;
	private final double ecoord;
	private final int direction;
	private final int siteId;
}
