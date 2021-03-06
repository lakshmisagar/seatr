package com.asu.seatr.api.analyzer.required_optional.test;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.Test;

import com.asu.seatr.api.analyzer.required_optional.CourseAPI_Required_Optional;
import com.asu.seatr.handlers.Handler;
import com.asu.seatr.rest.models.analyzer.required_optional.CAReader_Required_Optional;
import com.asu.seatr.utils.Utilities;

public class CourseAPI_Required_Optional_Test extends JerseyTest{
	
	private static String COURSE_3_URL = "analyzer/required_optional/courses/";
	
	@Override
	protected Application configure() {
		enable(TestProperties.DUMP_ENTITY);
		return new ResourceConfig(CourseAPI_Required_Optional.class);
	}
	
	@Before
	public void init()
	{
		Utilities.setJUnitTest(true);
		try
		{
			Utilities.clearDatabase();
		}
		finally
		{
			Utilities.setJUnitTest(false);
		}
	}
	@Test(expected=WebApplicationException.class)
	public void create_Update_Get_Delete_Course_Sucess()
	{
		Utilities.setJUnitTest(true);
		try
		{
			CAReader_Required_Optional ca = new CAReader_Required_Optional();
			ca.setExternal_course_id("1");
			ca.setD_current_unit_no(1);
			ca.setD_max_n(3);
			ca.setDescription("physics");
			ca.setS_units(6);
			Response resp = target(COURSE_3_URL).request().header("Authorization", "Basic Y3NlMzEwOmhlbGxvMTIz")
					.post(Entity.json(ca), Response.class);
			assertEquals("course created", Status.CREATED.getStatusCode(), resp.getStatus());
			
			CAReader_Required_Optional course =  target(COURSE_3_URL)
					.queryParam("external_course_id", "1").request()
					.get(CAReader_Required_Optional.class);
			assertEquals("verify  inserted record","1",course.getD_current_unit_no().toString());
			
			CAReader_Required_Optional ca1 = new CAReader_Required_Optional();
			ca1.setExternal_course_id("1");
			ca1.setD_current_unit_no(2);
			ca1.setD_max_n(3);
			ca1.setDescription("physics");
			ca1.setS_units(6);	
			
			resp = target(COURSE_3_URL).request().header("Authorization", "Basic Y3NlMzEwOmhlbGxvMTIz")
					.put(Entity.json(ca1), Response.class);
			assertEquals("course updated", Status.OK.getStatusCode(), resp.getStatus());
			
			course =  target(COURSE_3_URL)
					.queryParam("external_course_id", "1").request()
					.get(CAReader_Required_Optional.class);
			assertEquals("verify updated teaching unit","2",course.getD_current_unit_no().toString());
			
			resp = target(COURSE_3_URL)
					.queryParam("external_course_id", "1").request()
					.delete(Response.class);
			assertEquals("deleted sucessfully",Status.OK.getStatusCode(),resp.getStatus());
			
			//verify delete
			course =  target(COURSE_3_URL)
					.queryParam("external_course_id", "1").request()
					.get(CAReader_Required_Optional.class);
			
			
			
		}
		catch(WebApplicationException e)
		{
			assertEquals("course not found once deleted",Status.NOT_FOUND.getStatusCode(),e.getResponse().getStatus());
			throw e;
		}
		finally
		{
			Handler.hqlTruncate("Course_Required_Optional");
			Handler.hqlTruncate("UserCourse");
			Handler.hqlTruncate("Course");
			Utilities.setJUnitTest(false);
		}
	}

	@Test
	public void updateCourse_failure()
	{
		Utilities.setJUnitTest(true);
		try
		{
			CAReader_Required_Optional ca1 = new CAReader_Required_Optional();
			ca1.setExternal_course_id("1");
			ca1.setD_current_unit_no(1);
			ca1.setD_max_n(3);
			ca1.setDescription("chemistry");
			ca1.setS_units(6);	
			Response resp = target(COURSE_3_URL).request().header("Authorization", "Basic Y3NlMzEwOmhlbGxvMTIz")
				.put(Entity.json(ca1), Response.class);
			assertEquals("course not found",Status.NOT_FOUND.getStatusCode(),resp.getStatus());
		}
		finally
		{
			Utilities.setJUnitTest(false);
		}
	}
	
	@Test
	public void deleteCourse_failure()
	{
		Utilities.setJUnitTest(true);
		try
		{
		Response resp = target(COURSE_3_URL)
				.queryParam("external_course_id", "1").request()
				.delete(Response.class);
		assertEquals("course not found",Status.NOT_FOUND.getStatusCode(),resp.getStatus());
		}
		finally
		{
			Utilities.setJUnitTest(false);
		}
	}
	

}
