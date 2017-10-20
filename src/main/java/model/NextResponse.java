package model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NextResponse {

	private List<Departure> solna;
	private List<Departure> sickla;

}
