package com.asu.seatr.api.analyzer3;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.asu.seatr.exceptions.CourseException;
import com.asu.seatr.exceptions.StudentException;
import com.asu.seatr.exceptions.StudentTaskException;
import com.asu.seatr.exceptions.TaskException;
import com.asu.seatr.handlers.StudentTaskAnalyzerHandler;
import com.asu.seatr.models.analyzers.studenttask.ST_A3;
import com.asu.seatr.rest.models.analyzer3.STAReader3;
import com.asu.seatr.utils.MyMessage;
import com.asu.seatr.utils.MyResponse;
import com.asu.seatr.utils.MyStatus;
import com.asu.seatr.utils.Utilities;

@Path("analyzer/3/studenttasks")
public class StudentTaskAPI_3 {
	static Logger logger = Logger.getLogger(StudentTaskAPI_3.class);

	//create student task
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createStudentTask(STAReader3 sta){

		try {
			if(!Utilities.checkExists(sta.getExternal_course_id())) {
				throw new CourseException(MyStatus.ERROR, MyMessage.COURSE_ID_MISSING);
			}
			if(!Utilities.checkExists(sta.getExternal_student_id())) {
				throw new StudentException(MyStatus.ERROR, MyMessage.STUDENT_ID_MISSING);
			}
			if(!Utilities.checkExists(sta.getExternal_task_id())) {
				throw new TaskException(MyStatus.ERROR, MyMessage.TASK_ID_MISSING);
			}
			ST_A3 sta3 = new ST_A3();

			try {
				ST_A3 sta3Recent = (ST_A3) StudentTaskAnalyzerHandler.readOrderByTimestamp(ST_A3.class, sta.getExternal_student_id(), sta.getExternal_course_id(), sta.getExternal_task_id()).get(0);
				System.out.println(sta3Recent.getId());
				sta3.setD_current_n(sta3Recent.getD_current_n()+1);				

			} catch (StudentTaskException ste) {
				if (ste.getMyMessage() == MyMessage.STUDENT_TASK_NOT_FOUND || ste.getMyMessage() == MyMessage.STUDENT_TASK_ANALYZER_NOT_FOUND) {
					sta3.setD_current_n(1);
				}
			}
			sta3.createStudentTask(sta.getExternal_student_id(),sta.getExternal_course_id(),sta.getExternal_task_id(), 3);
			//sta3.setD_current_n(sta.getD_current_n());
			//List<StudentTask> stList = StudentTaskHandler.readByExtId(sta.getExternal_student_id(), sta.getExternal_course_id(), sta.getExternal_task_id());

			//sta3.setD_timestsamp(System.currentTimeMillis());
			sta3.setD_is_answered(sta.getD_is_answered());

			StudentTaskAnalyzerHandler.save(sta3);


			//Student student = StudentHandler.getByExternalId(sta.getExternal_student_id(), sta.getExternal_course_id());
			//Task task = TaskHandler.readByExtId(sta.getExternal_task_id(), sta.getExternal_course_id());
			//Course course=CourseHandler.getByExternalId(sta.getExternal_course_id());
			//if(sta.getD_status().equals("done"))
			//	RecommTaskHandler.completeATask(student, course,task);

			return Response.status(Status.CREATED)
					.entity(MyResponse.build(MyStatus.SUCCESS, MyMessage.STUDENT_TASK_CREATED)).build();
		}

		catch(CourseException e) {
			Response rb = Response.status(Status.OK)
					.entity(MyResponse.build(e.getMyStatus(), e.getMyMessage())).build();
			throw new WebApplicationException(rb);
		}
		catch(TaskException e) {
			Response rb = Response.status(Status.OK)
					.entity(MyResponse.build(e.getMyStatus(), e.getMyMessage())).build();
			throw new WebApplicationException(rb);
		}
		catch(StudentException e) {
			Response rb = Response.status(Status.OK)
					.entity(MyResponse.build(e.getMyStatus(), e.getMyMessage())).build();
			throw new WebApplicationException(rb);
		}
		catch(Exception e){
			logger.error("Exception while creating student", e);
			Response rb = Response.status(Status.BAD_REQUEST)
					.entity(MyResponse.build(MyStatus.ERROR, MyMessage.BAD_REQUEST)).build();
			throw new WebApplicationException(rb);
		}

	}


}