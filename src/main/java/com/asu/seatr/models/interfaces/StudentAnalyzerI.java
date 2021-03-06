package com.asu.seatr.models.interfaces;

import com.asu.seatr.exceptions.CourseException;
import com.asu.seatr.exceptions.StudentException;
import com.asu.seatr.models.Student;

public interface StudentAnalyzerI {
	public int getId();
	public void setId(int id);
	public void createStudent(String student_ext_id,String external_course_id,int analyzer_id) throws CourseException, StudentException;
	public void deleteStudent(String student_ext_id,String external_course_id, int analyzer_id);
	public void updateStudent(String student_ext_id,String external_course_id, int analyzer_id);
	public Student getStudent(String student_ext_id,String external_course_id, int analyzer_id);
}
