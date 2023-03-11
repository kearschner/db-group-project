package data;

import java.time.LocalDate;
import java.util.List;

public record Section(
	Course forCourse,
	String crn,
	String sectionNumber,
	LocalDate startDate,
	LocalDate endDate,
	int waitlistSlotsAvailable,
	int waitlistSlotsCap,
	int seatsAvailable,
	int seatsCap,
	InstructionalMethod instructMethod,
	Instructor primaryInstructor,
	Instructor[] allInstructors,
	List<Meeting> meets
) { }
