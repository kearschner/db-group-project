package com.example.demo.data;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Immutable
@Table(name = "Subject")
public final class Subject {
	@Id
	@Column(name = "code")
	private final String subjectCode;
	@Column(name = "name")
	private final String subjectName;

	public Subject(String subjectCode, String subjectName) {
		this.subjectCode = subjectCode;
		this.subjectName = subjectName;
	}

	public Subject() {
		this.subjectCode = "";
		this.subjectName = "";
	}

	public String subjectCode() {
		return subjectCode;
	}

	public String subjectName() {
		return subjectName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Subject) obj;
		return Objects.equals(this.subjectCode, that.subjectCode) &&
				Objects.equals(this.subjectName, that.subjectName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(subjectCode, subjectName);
	}

	@Override
	public String toString() {
		return "Subject[" +
				"subjectCode=" + subjectCode + ", " +
				"subjectName=" + subjectName + ']';
	}
}
