package model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class Departure {

	LocalTime time;
	LocalDate date;
	String end;
	String dest;

}
