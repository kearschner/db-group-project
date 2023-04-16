package com.example.demo.data;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Immutable
@Table(name = "Building")
public final class Building {
	@Id
	@Column(name = "build_code")
	private final String buildingCode;
	@Column(name = "name")
	private final String name;

	public Building(
			String buildingCode,

			String name
	) {
		this.buildingCode = buildingCode;
		this.name = name;
	}

	public Building() {
		this("","");
	}

	public String buildingCode() {
		return buildingCode;
	}

	public String name() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Building) obj;
		return Objects.equals(this.buildingCode, that.buildingCode) &&
				Objects.equals(this.name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(buildingCode, name);
	}

	@Override
	public String toString() {
		return "Building[" +
				"buildingCode=" + buildingCode + ", " +
				"name=" + name + ']';
	}
}
