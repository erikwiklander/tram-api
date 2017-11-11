package io.wiklandia.tramapi.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Stop implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String name;
	private final long id;
	private final double longitude;
	private final double latitude;
	private final String fullname;

}
