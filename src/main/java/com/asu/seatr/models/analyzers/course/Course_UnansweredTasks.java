package com.asu.seatr.models.analyzers.course;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.PropertyValueException;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.exception.ConstraintViolationException;

import com.asu.seatr.exceptions.CourseException;
import com.asu.seatr.handlers.CourseHandler;
import com.asu.seatr.models.Course;
import com.asu.seatr.models.interfaces.CourseAnalyzerI;
import com.asu.seatr.utils.MyMessage;
import com.asu.seatr.utils.MyStatus;


@Entity
@Table(name="c_a1")
public class Course_UnansweredTasks implements CourseAnalyzerI{
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private int id;

	@OneToOne
	@JoinColumn(name="course_id", referencedColumnName="id", unique = true, nullable=false) //id of a course
	Course course;

	//Properties
	@Column(name="mastery_threshold")
	private Double threshold;
	@Column(name="teaching_unit")
	private String teaching_unit;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;		
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public void setCourseByExt_id(String course_ext_id) throws CourseException {
		Course course=CourseHandler.getByExternalId(course_ext_id);
		this.course=course;

	}

	public String getCourseExtId(){
		return this.course.getExternal_id();
	}

	public String getCourseDesc(){
		return this.course.getDescription();
	}

	public Double getThreshold() {
		return threshold;
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}

	public String getTeaching_unit() {
		return teaching_unit;
	}

	public void setTeaching_unit(String teaching_unit) {
		this.teaching_unit = teaching_unit;
	}

	@Override
	public void createCourse(String external_course_id, String description) throws CourseException {
		Course course = new Course();
		course.setExternal_id(external_course_id);
		course.setDescription(description);
		try {
			CourseHandler.save(course);
		} 

		catch (ConstraintViolationException cve) {
			course = CourseHandler.getByExternalId(external_course_id);
			this.course = course;
		} 

		catch (PropertyValueException pve) {
			throw new CourseException(MyStatus.ERROR, MyMessage.COURSE_PROPERTY_NULL);
		} 
		this.course = course;

	}



	@Override
	public void updateCourse(String external_course_id, String description) throws CourseException {

	}

	@Override
	public void deleteCourse(String external_course_id, int analyzer_id) {

	}

	@Override
	public Course getCourse(String external_course_id, int analyzer_id) {
		return null;
	}



}
