package com.example.demo.data;


import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Immutable
@Table(name = "Instructor")
public final class Instructor {
	@Id
	@Column(name = "name")
	private final String name;

	public Instructor(

			String name
	) {
		this.name = name;
	}

	public Instructor() {
		this.name = "";
	}

	public String name() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Instructor) obj;
		return Objects.equals(this.name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public String toString() {
		return "Instructor[" +
				"name=" + name + ']';
	}
}
