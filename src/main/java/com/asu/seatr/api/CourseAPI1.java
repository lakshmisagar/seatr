package com.asu.seatr.api;



import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.asu.seatr.exceptions.CourseException;
import com.asu.seatr.handlers.CourseHandler;
import com.asu.seatr.models.Course;
import com.asu.seatr.utils.MyMessage;
import com.asu.seatr.utils.MyResponse;
import com.asu.seatr.utils.MyStatus;

@Path("/course2")
public class CourseAPI1 {
	
	@Path("/1")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Course getCourse(@QueryParam("external_id") String external_idStr){
		Course course=null;
		if(external_idStr!=null)
			try {
				course=CourseHandler.getByExternalId(external_idStr);
			} catch (CourseException e) {
				Response rb = Response.status(Status.NOT_FOUND)
						.entity(MyResponse.build(e.getMyStatus(), e.getMyMessage())).build();			
				throw new WebApplicationException(rb);
			}
		return course;		
	}
	
	@Path("/delete")
	@GET
	public void delCourse(@QueryParam("id") String idStr,@QueryParam("external_id") String external_idStr){

		try {
			if(idStr!=null)
				CourseHandler.delete(CourseHandler.readById(Integer.valueOf(idStr)));
			else if(external_idStr!=null)
				CourseHandler.delete(CourseHandler.getByExternalId(external_idStr));
		} catch(CourseException e) {
			Response rb = Response.status(Status.NOT_FOUND)
					.entity(MyResponse.build(e.getMyStatus(), e.getMyMessage())).build();			
			throw new WebApplicationException(rb);
		}
			
		
	}
	
	@Path("/add")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void addCourse(Course c){
		CourseHandler.save(c);
	}
	
	@Path("/update")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateCourse(@QueryParam("external_id") String external_id,Course c ){
		CourseHandler.updateCourseByExternalID(external_id, c);
	}
	
	@Path("/setanalyzer")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void setAnalyzer()
	{
		
	}
	
}
