package service;

public class SqlStatementCreater {
	
	private String sql;
	
	public String createAllCourseStatement() {	
	    sql = "SELECT * FROM Course;";
		return sql;
	}
	
	public String createCourseStatement(int id) {
		sql = "SELECT * FROM Course WHERE courseID="+id;
		return sql;

	}

	public String createPerformersOfCourseStatement(String dozRef) {
		
		String sql= "SELECT * FROM Person WHERE uid='"+dozRef+"'";
		return sql;
	}
	
	public String createPlacesOfCourseStatement(String placeRef) {
		
		sql = "SELECT * FROM Place WHERE uid='"+placeRef+"'";
		return sql;
	}	

	public String createPlaceByIDStatement(int id) {
		
		sql = "SELECT * FROM Place WHERE placeID="+id ;
		return sql;
	}		
	
	public String createSuperEventStatement(int id) {
		
		sql = "SELECT * FROM Course WHERE uid='"+id+"'";
		return sql;
	}		
	

}


