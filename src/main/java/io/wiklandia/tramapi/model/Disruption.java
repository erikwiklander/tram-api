package io.wiklandia.tramapi.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Disruption implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String header;
	private final String description;

}
