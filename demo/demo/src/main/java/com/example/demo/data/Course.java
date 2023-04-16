package com.example.demo.data;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Immutable
@Table(name = "Course")
public final class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "courseid")
	private final Long courseId;

	@Column(name = "title")
	private final String title;

	@Column(name = "course_number")
	private final String courseNumber;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "code")
	private final Subject subject;

	@Column(name = "credits")
	private final int credits;

	@Column(name = "department")
	private final String department;

	@Column(name = "permit_required")
	private final boolean permitRequired;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
		name = "Course_All_Attributes",
		joinColumns = { @JoinColumn(name = "courseid") },
		inverseJoinColumns = { @JoinColumn(name = "attributeid") }
	)
	private final Set<CourseAttribute> allAttributes;

	public Course(
					Long courseId,
					String title,
					String courseNumber,
					Subject subject,
					int credits,
					String department,
					boolean permitRequired,
					Set<CourseAttribute> allAttributes
	) {
		this.courseId = courseId;
		this.title = title;
		this.courseNumber = courseNumber;
		this.subject = subject;
		this.credits = credits;
		this.department = department;
		this.permitRequired = permitRequired;
		this.allAttributes = allAttributes;
	}

	public Course() {
		this(null, "","", null, 0, "", false, null);
	}

	public Long courseId() {
		return courseId;
	}

	public String title() {
		return title;
	}

	public String courseNumber() {
		return courseNumber;
	}

	public Subject subject() {
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

	public Set<CourseAttribute> allAttributes() {
		return allAttributes;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Course) obj;
		return Objects.equals(this.courseId, that.courseId) &&
				Objects.equals(this.title, that.title) &&
				Objects.equals(this.courseNumber, that.courseNumber) &&
				Objects.equals(this.subject, that.subject) &&
				this.credits == that.credits &&
				Objects.equals(this.department, that.department) &&
				this.permitRequired == that.permitRequired &&
				Objects.equals(this.allAttributes, that.allAttributes);
	}

	@Override
	public int hashCode() {
		return Objects.hash(courseId, title, courseNumber, subject, credits, department, permitRequired, allAttributes);
	}

	@Override
	public String toString() {
		return "Course[" +
				"courseId=" + courseId + ", " +
				"title=" + title + ", " +
				"courseNumber=" + courseNumber + ", " +
				"subject=" + subject + ", " +
				"credits=" + credits + ", " +
				"department=" + department + ", " +
				"permitRequired=" + permitRequired + ", " +
				"allAttributes=" + allAttributes + ']';
	}
}
