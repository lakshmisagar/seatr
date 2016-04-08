package com.asu.seatr.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.asu.seatr.models.analyzers.task.*;
import com.asu.seatr.models.analyzers.task_kc.*;

@Entity
@Table(name = "task", uniqueConstraints = @UniqueConstraint(columnNames = {"external_id", "course_id"}))
public class Task {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	
	@Column(name = "external_id", nullable=false)
	private String external_id;
	
	@ManyToOne
	@JoinColumn(name = "course_id", referencedColumnName = "id", nullable=false)
	private Course course;
	
	@OneToMany(mappedBy = "task", cascade=CascadeType.ALL)
	private List<StudentTask> StudentTask;
	
	@OneToMany(mappedBy = "task", cascade=CascadeType.ALL)
	private List<T_A1> T_A1;
	
	@OneToMany(mappedBy = "task", cascade=CascadeType.ALL)
	private List<TK_A1> TK_A1;
	
	public final static String p_id = "id";
	public final static String p_external_id = "external_id";
	public final static String p_course = "course";
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getExternal_id() {
		return external_id;
	}
	public void setExternal_id(String external_id) {
		this.external_id = external_id;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	
	public List<StudentTask> getStudentTask() {
		return StudentTask;
	}
	public void setStudentTask(List<StudentTask> studentTask) {
		StudentTask = studentTask;
	}
	public List<T_A1> getT_A1() {
		return T_A1;
	}
	public void setT_A1(List<T_A1> t_A1) {
		T_A1 = t_A1;
	}
	public List<TK_A1> getTK_A1() {
		return TK_A1;
	}
	public void setTK_A1(List<TK_A1> tK_A1) {
		TK_A1 = tK_A1;
	}	
	
}