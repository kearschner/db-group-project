package com.example.demo.data;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Immutable
@IdClass(CourseId.class)
@Table(name = "Course")
public final class Course {

	@Id
	@Column(name = "subject")
	private final String subject;

	@Id
	@Column(name = "course_number")
	private final String courseNumber;

	@Column(name = "title")
	private final String title;

	@Column(name = "credits")
	private final int credits;

	@Column(name = "department")
	private final String department;

	@Column(name = "permit_required")
	private final boolean permitRequired;

	@Column(name = "attributes")
	@ElementCollection
	private final Set<String> attributes;

	public Course(
					String subject,
					String courseNumber,
					String title,
					int credits,
					String department,
					boolean permitRequired,
					Set<String> attributes
	) {
		this.title = title;
		this.courseNumber = courseNumber;
		this.subject = subject;
		this.credits = credits;
		this.department = department;
		this.permitRequired = permitRequired;
		this.attributes = attributes;
	}

	public Course() {
		this("","", "", 0, "", false, null);
	}

	public String title() {
		return title;
	}

	public String courseNumber() {
		return courseNumber;
	}

	public String subject() {
		return subject;
	}

	public int credits() {
		return credits;
	}

	public String department() {
		return department;
	}

	public boolean permitRequired() {
		return permitRequired;
	}

	public Set<String> attributes() {
		return attributes;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Course) obj;
		return	Objects.equals(this.title, that.title) &&
				Objects.equals(this.courseNumber, that.courseNumber) &&
				Objects.equals(this.subject, that.subject) &&
				this.credits == that.credits &&
				Objects.equals(this.department, that.department) &&
				this.permitRequired == that.permitRequired &&
				Objects.equals(this.attributes, that.attributes);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, courseNumber, subject, credits, department, permitRequired, attributes);
	}

	@Override
	public String toString() {
		return "Course[" +
				"title=" + title + ", " +
				"courseNumber=" + courseNumber + ", " +
				"subject=" + subject + ", " +
				"credits=" + credits + ", " +
				"department=" + department + ", " +
				"permitRequired=" + permitRequired + ", " +
				"allAttributes=" + attributes + ']';
	}
}
