package io.wiklandia.tramapi.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Stop implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private long id;
	private double longitude;
	private double latitude;
	private String fullname;

}
