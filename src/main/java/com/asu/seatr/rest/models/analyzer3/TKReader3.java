package com.asu.seatr.rest.models.analyzer3;

import com.asu.seatr.rest.models.interfaces.TKAReaderI;
import com.asu.seatr.rest.models.interfaces.TKReaderI;

public class TKReader3 implements TKReaderI{

	private boolean replace;
	private String external_course_id;
	private TKAReader3[] tkaReader;
	@Override
	public boolean getReplace() {
		// TODO Auto-generated method stub
		return replace;
	}

	@Override
	public void setReplace(boolean replace) {
		// TODO Auto-generated method stub
		this.replace = replace;
	}


	public TKAReader3[] getTkaReader() {
		// TODO Auto-generated method stub
		return tkaReader;
	}



	public void setTkaReader(TKAReader3[] tkaReader) {


		// TODO Auto-generated method stub
		this.tkaReader = tkaReader;
	}

	public String getExternal_course_id() {
		return external_course_id;
	}

	public void setExternal_course_id(String external_course_id) {
		this.external_course_id = external_course_id;
	}

}
