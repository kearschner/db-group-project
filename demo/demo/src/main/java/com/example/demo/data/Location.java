package com.example.demo.data;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Immutable
@Table(name = "Location")
public final class Location {
	@Id
	@GeneratedValue
	@Column(name = "locid")
	private final Long id;
	@Column(name = "campus")
	private final String campus;

	@Column(name = "building")
	private final String building;

	@Column(name = "room_number")
	private final String roomNumber;

	public Location(
					Long id,

					String campus,

					String building,

					String roomNumber
	) {
		this.id = id;
		this.campus = campus;
		this.building = building;
		this.roomNumber = roomNumber;
	}

	public Location() {
		this(null,"", null, "");
	}

	public Long id() {
		return id;
	}

	public String campus() {
		return campus;
	}

	public String building() {
		return building;
	}

	public String roomNumber() {
		return roomNumber;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Location) obj;
		return Objects.equals(this.id, that.id) &&
				Objects.equals(this.campus, that.campus) &&
				Objects.equals(this.building, that.building) &&
				Objects.equals(this.roomNumber, that.roomNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, campus, building, roomNumber);
	}

	@Override
	public String toString() {
		return "Location[" +
				"id=" + id + ", " +
				"campus=" + campus + ", " +
				"building=" + building + ", " +
				"roomNumber=" + roomNumber + ']';
	}
}
