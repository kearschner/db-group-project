package com.example.demo.data;

import jakarta.persistence.Embeddable;
import org.hibernate.annotations.Immutable;

import java.io.Serializable;
import java.util.Objects;

@Immutable
public class CourseId implements Serializable {
    private String subject;
    private String courseNumber;

    public CourseId(String subject, String courseNumber) {
        this.subject = subject;
        this.courseNumber = courseNumber;
    }
    public CourseId() {
        this("","");
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getSubject() {
        return subject;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, courseNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CourseId) obj;
        return Objects.equals(this.subject, that.subject) &&
                Objects.equals(this.courseNumber, that.courseNumber);
    }

    @Override
    public String toString() {
        return subject + " " + courseNumber;
    }
}
