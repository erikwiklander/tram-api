package model;

import lombok.Data;

@Data
public class Stop {

	private final String name;
	private final long id;
	private final double longitude;
	private final double latitude;
	private final String fullname;

}
