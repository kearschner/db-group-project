package data;

import java.time.LocalTime;
import java.util.EnumSet;

public record Meeting(
	LocalTime startTime,
	LocalTime endTime,
	EnumSet<Day> days,
	Location location
) { }
