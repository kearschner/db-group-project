package com.example.demo.data;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "Course_Attribute")
public class CourseAttribute {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "attributeid")
	private final Long id;

	@Column(name = "name")
	private final String name;

	public CourseAttribute(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public CourseAttribute() {
		this(null, "");
	}

	Long id() {return id;}
	String name() { return name; }
}
