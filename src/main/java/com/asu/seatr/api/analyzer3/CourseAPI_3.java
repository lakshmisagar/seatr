package com.asu.seatr.api.analyzer3;

import java.util.StringTokenizer;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.asu.seatr.auth.AuthenticationService;
import com.asu.seatr.exceptions.CourseException;
import com.asu.seatr.exceptions.UserException;
import com.asu.seatr.handlers.CourseAnalyzerHandler;
import com.asu.seatr.handlers.UserCourseHandler;
import com.asu.seatr.models.analyzers.course.C_A3;
import com.asu.seatr.rest.models.analyzer3.CAReader3;
import com.asu.seatr.utils.MyMessage;
import com.asu.seatr.utils.MyResponse;
import com.asu.seatr.utils.MyStatus;
import com.asu.seatr.utils.Utilities;

@Path("/analyzer/3/courses")
public class CourseAPI_3 {
	public static final String AUTHENTICATION_HEADER = "Authorization";

	static Logger logger = Logger.getLogger(CourseAPI_3.class);
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public CAReader3 getCourse(@QueryParam("external_course_id") String external_course_id){		
		try {
			if(!Utilities.checkExists(external_course_id)) {
				throw new CourseException(MyStatus.ERROR, MyMessage.COURSE_ID_MISSING);
			}
			C_A3 ca3  = (C_A3)CourseAnalyzerHandler.readByExtId(C_A3.class, external_course_id).get(0);
			CAReader3 reader=new CAReader3();
			reader.setDescription(ca3.getCourseDesc());
			reader.setExternal_course_id(ca3.getCourseExtId());
			reader.setD_current_unit_no(ca3.getD_current_unit_no());
			reader.setD_max_n(ca3.getD_max_n());
			reader.setS_units(ca3.getS_units());

			return reader;
		} catch (CourseException e) {
			Response rb = Response.status(Status.NOT_FOUND)
					.entity(MyResponse.build(e.getMyStatus(), e.getMyMessage())).build();			
			throw new WebApplicationException(rb);
		} catch(Exception e){
			logger.error("Exception while getting course - analyzer 3", e);			
			Response rb = Response.status(Status.BAD_REQUEST)
					.entity(MyResponse.build(MyStatus.ERROR, MyMessage.BAD_REQUEST)).build();
			throw new WebApplicationException(rb);
		}



	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCourse(CAReader3 reader, @HeaderParam(AUTHENTICATION_HEADER) String authHeader){

		try{
			if(!Utilities.checkExists(reader.getExternal_course_id())) {
				throw new CourseException(MyStatus.ERROR, MyMessage.COURSE_ID_MISSING);
			}

			C_A3 ca3 = new C_A3();
			StringTokenizer tokenizer = AuthenticationService.getUsernameAndPassword(authHeader);
			String username = tokenizer.nextToken();

			ca3.createCourse(reader.getExternal_course_id(), reader.getDescription());
			ca3.setD_current_unit_no(reader.getD_current_unit_no());
			ca3.setD_max_n(reader.getD_max_n());
			ca3.setS_units(reader.getS_units());
			CourseAnalyzerHandler.save(ca3);

			UserCourseHandler.save(username, reader.getExternal_course_id());

			return Response.status(Status.CREATED)
					.entity(MyResponse.build(MyStatus.SUCCESS, MyMessage.COURSE_CREATED)).build();

		} catch (CourseException  e) {
			Response rb = Response.status(Status.OK)
					.entity(MyResponse.build(e.getMyStatus(), e.getMyMessage())).build();			
			throw new WebApplicationException(rb);
		}  catch (UserException e) {
			Response rb = Response.status(Status.OK)
					.entity(MyResponse.build(e.getMyStatus(), e.getMyMessage())).build();			
			throw new WebApplicationException(rb);
		}
		catch(Exception e){
			logger.error("Exception while creating course", e);
			Response rb = Response.status(Status.BAD_REQUEST)
					.entity(MyResponse.build(MyStatus.ERROR, MyMessage.BAD_REQUEST)).build();
			throw new WebApplicationException(rb);
		}


	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCourse(CAReader3 reader){
		try{
			if(!Utilities.checkExists(reader.getExternal_course_id())) {
				throw new CourseException(MyStatus.ERROR, MyMessage.COURSE_ID_MISSING);
			}


			C_A3 ca3 = (C_A3) CourseAnalyzerHandler.readByExtId(C_A3.class, reader.getExternal_course_id()).get(0);

			if(Utilities.checkExists(reader.getD_current_unit_no())) {
				ca3.setD_current_unit_no(reader.getD_current_unit_no());
			}
			if(Utilities.checkExists(reader.getD_max_n())) {
				ca3.setD_max_n(reader.getD_max_n());
			}
			if(Utilities.checkExists(reader.getS_units())) {
				ca3.setS_units(reader.getS_units());
			}

			CourseAnalyzerHandler.update(ca3);
			return Response.status(Status.OK)
					.entity(MyResponse.build(MyStatus.SUCCESS, MyMessage.COURSE_UPDATED))
					.build();
		} catch (CourseException e) {
			Response rb = Response.status(Status.NOT_FOUND)
					.entity(MyResponse.build(e.getMyStatus(), e.getMyMessage())).build();			
			throw new WebApplicationException(rb);
		} 
		catch(Exception e){
			logger.error("Exception while updating course", e);
			Response rb = Response.status(Status.BAD_REQUEST)
					.entity(MyResponse.build(MyStatus.ERROR, MyMessage.BAD_REQUEST)).build();
			throw new WebApplicationException(rb);
		}
	}


	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCourseAnalyzer1(@QueryParam("external_course_id") String external_course_id) {

		try {
			if(!Utilities.checkExists(external_course_id)) {
				throw new CourseException(MyStatus.ERROR, MyMessage.COURSE_ID_MISSING);
			}
			C_A3 c_a3 = (C_A3)CourseAnalyzerHandler.readByExtId(C_A3.class, external_course_id).get(0);
			CourseAnalyzerHandler.delete(c_a3);
			return Response.status(Status.OK)
					.entity(MyResponse.build(MyStatus.SUCCESS, MyMessage.COURSE_ANALYZER_DELETED)).build();
		} catch (CourseException e) {
			Response rb = Response.status(Status.NOT_FOUND)
					.entity(MyResponse.build(e.getMyStatus(), e.getMyMessage())).build();			
			throw new WebApplicationException(rb);
		} catch(Exception e){
			logger.error("Exception while deleting course analyzer", e);
			Response rb = Response.status(Status.BAD_REQUEST)
					.entity(MyResponse.build(MyStatus.ERROR, MyMessage.BAD_REQUEST)).build();
			throw new WebApplicationException(rb);
		}
	}



}