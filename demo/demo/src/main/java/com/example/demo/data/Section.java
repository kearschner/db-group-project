package com.example.demo.data;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;
import java.util.Set;
import java.util.Objects;

@Entity
@Immutable
@Table(name = "Section")
public final class Section {
	@Id
	@Column(name = "crn")
	private final String crn;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumns({
			@JoinColumn(name = "target_subject", referencedColumnName = "subject"),
			@JoinColumn(name = "target_course_number", referencedColumnName = "course_number")
	})
	private final Course forCourse;

	@Column(name = "section_number")
	private final String sectionNumber;

	@Column(name = "start_date")
	private final LocalDate startDate;

	@Column(name = "end_date")
	private final LocalDate endDate;

	@Column(name = "wait_avail")
	private final int waitlistSlotsAvailable;

	@Column(name = "wait_cap")
	private final int waitlistSlotsCap;

	@Column(name = "seats_avail")
	private final int seatsAvailable;

	@Column(name = "seats_cap")
	private final int seatsCap;

	@Column(name = "instructional_method")
	@Enumerated(value = EnumType.STRING)
	private final InstructionalMethod instructMethod;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "name")
	private final Instructor primaryInstructor;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "Section_All_Instructors",
			joinColumns = { @JoinColumn(name = "crn") },
			inverseJoinColumns = { @JoinColumn(name = "name") }
	)
	private final Set<Instructor> allInstructors;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "Section_Meetings",
			joinColumns = { @JoinColumn(name = "crn") },
			inverseJoinColumns = { @JoinColumn(name = "meetingid") }
	)
	private final Set<Meeting> meets;

	public Section(String crn,
				   Course forCourse,
				   String sectionNumber,
				   LocalDate startDate,
				   LocalDate endDate,
				   int waitlistSlotsAvailable,
				   int waitlistSlotsCap,
				   int seatsAvailable,
				   int seatsCap,
				   InstructionalMethod instructMethod,
				   Instructor primaryInstructor,
				   Set<Instructor> allInstructors,
				   Set<Meeting> meets
	) {
		this.crn = crn;
		this.forCourse = forCourse;
		this.sectionNumber = sectionNumber;
		this.startDate = startDate;
		this.endDate = endDate;
		this.waitlistSlotsAvailable = waitlistSlotsAvailable;
		this.waitlistSlotsCap = waitlistSlotsCap;
		this.seatsAvailable = seatsAvailable;
		this.seatsCap = seatsCap;
		this.instructMethod = instructMethod;
		this.primaryInstructor = primaryInstructor;
		this.allInstructors = allInstructors;
		this.meets = meets;
	}

	public Section() {
		this("",null, "", null, null, 0,0,0,0,null, null, null, null);
	}

	public String crn() {
		return crn;
	}

	public Course forCourse() {
		return forCourse;
	}

	public String sectionNumber() {
		return sectionNumber;
	}

	public LocalDate startDate() {
		return startDate;
	}

	public LocalDate endDate() {
		return endDate;
	}

	public int waitlistSlotsAvailable() {
		return waitlistSlotsAvailable;
	}

	public int waitlistSlotsCap() {
		return waitlistSlotsCap;
	}

	public int seatsAvailable() {
		return seatsAvailable;
	}

	public int seatsCap() {
		return seatsCap;
	}

	public InstructionalMethod instructMethod() {
		return instructMethod;
	}

	public Instructor primaryInstructor() {
		return primaryInstructor;
	}

	public Set<Instructor> allInstructors() {
		return allInstructors;
	}

	public Set<Meeting> meets() {
		return meets;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Section) obj;
		return Objects.equals(this.crn, that.crn) &&
				Objects.equals(this.forCourse, that.forCourse) &&
				Objects.equals(this.sectionNumber, that.sectionNumber) &&
				Objects.equals(this.startDate, that.startDate) &&
				Objects.equals(this.endDate, that.endDate) &&
				this.waitlistSlotsAvailable == that.waitlistSlotsAvailable &&
				this.waitlistSlotsCap == that.waitlistSlotsCap &&
				this.seatsAvailable == that.seatsAvailable &&
				this.seatsCap == that.seatsCap &&
				Objects.equals(this.instructMethod, that.instructMethod) &&
				Objects.equals(this.primaryInstructor, that.primaryInstructor) &&
				Objects.equals(this.allInstructors, that.allInstructors) &&
				Objects.equals(this.meets, that.meets);
	}

	@Override
	public int hashCode() {
		return Objects.hash(crn, forCourse, sectionNumber, startDate, endDate, waitlistSlotsAvailable, waitlistSlotsCap, seatsAvailable, seatsCap, instructMethod, primaryInstructor, allInstructors, meets);
	}

	@Override
	public String toString() {
		return "Section[" +
				"crn=" + crn + ", " +
				"forCourse=" + forCourse + ", " +
				"sectionNumber=" + sectionNumber + ", " +
				"startDate=" + startDate + ", " +
				"endDate=" + endDate + ", " +
				"waitlistSlotsAvailable=" + waitlistSlotsAvailable + ", " +
				"waitlistSlotsCap=" + waitlistSlotsCap + ", " +
				"seatsAvailable=" + seatsAvailable + ", " +
				"seatsCap=" + seatsCap + ", " +
				"instructMethod=" + instructMethod + ", " +
				"primaryInstructor=" + primaryInstructor + ", " +
				"allInstructors=" + allInstructors + ", " +
				"meets=" + meets + ']';
	}
}
