package com.example.demo.data;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Objects;

@Entity
@Immutable
@Table(name = "Meeting")
public final class Meeting {

	@Id
	@GeneratedValue
	@Column(name = "meetingid")
	private final Long id;

	@Column(name = "start_time")
	private final LocalTime startTime;

	@Column(name = "end_time")
	private final LocalTime endTime;

	@Column(name = "days")
	private final String days;

	@Transient
	private final EnumSet<Day> daySet;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "locid")
	private final Location location;

	public Meeting(
			Long id,
			LocalTime startTime,
			LocalTime endTime,
			String days,
			Location location
	) {
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
		this.days = days;
		this.daySet = Day.setFromString(days);
		this.location = location;
	}

	public Meeting() {
		this(null, null,null,"",null);
	}

	public LocalTime startTime() {
		return startTime;
	}

	public LocalTime endTime() {
		return endTime;
	}

	public String days() {
		return days;
	}

	public EnumSet<Day> daySet() {
		return daySet.clone();
	}

	public Location location() {
		return location;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Meeting) obj;
		return Objects.equals(this.startTime, that.startTime) &&
				Objects.equals(this.endTime, that.endTime) &&
				Objects.equals(this.days, that.days) &&
				Objects.equals(this.location, that.location);
	}

	@Override
	public int hashCode() {
		return Objects.hash(startTime, endTime, days, location);
	}

	@Override
	public String toString() {
		return "Meeting[" +
				"startTime=" + startTime + ", " +
				"endTime=" + endTime + ", " +
				"days=" + days + ", " +
				"location=" + location + ']';
	}
}
