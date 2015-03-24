package com.searchengine.dao;

import java.sql.*;

import com.searchengine.config.PropertyConfiguration;


public class CaseJDBC {

	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private PreparedStatement pstmt = null;
	private boolean autoCommit = true;

	public CaseJDBC(String url, String usr, String pwd) throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		System.out.println(url);
		con = DriverManager.getConnection(url, usr, pwd);
		con.setAutoCommit(autoCommit);
	}

	public int addCase(Case c) throws Exception {

		int nextid = getNextId();

		if (nextid < 0) {
			throw new Exception("Can't get next id.");
		}

		// since we get the next id, add the info to db
		String title = c.getTitle();
		String date = c.getDate();
		String type = c.getType();
		String introduction = c.getIntroduction();
		String detail = c.getDetail();
		String location = c.getLocation();
		String latitude = c.getLatitude();
		String longitude = c.getLongitude();
		String link = c.getLink();

		String expr = "insert into emergency (title, date, type, introduction, detail, location, latitude, longitude, link) values(?,?,?,?,?,?,?,?,?)";
		pstmt = con.prepareStatement(expr);
		pstmt.setString(1, title);
		pstmt.setString(2, date);
		pstmt.setString(3, type);
		pstmt.setString(4, introduction);
		pstmt.setString(5, detail);
		pstmt.setString(6, location);
		pstmt.setString(7, latitude);
		pstmt.setString(8, longitude);
		pstmt.setString(9, link);
		pstmt.execute();

		return nextid;
	}

	private int getNextId() throws Exception {

		int result = -1;

		String sql = "select max(id)+1 from emergency";

		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			result = rs.getInt(1);
		}

		return result;
	}

	public void close() {
		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				con = null;
			}
		}
	}

	public static void main(String[] args) {
		String url = PropertyConfiguration.getDBUrl();
		String usr = PropertyConfiguration.getDBUsr();
		String pwd = PropertyConfiguration.getDBPwd();
		/*
		String url = "jdbc:mysql://localhost/searchdb";
		String usr = "root";
		String pwd = "root";
		*/
		try {
			new CaseJDBC(url, usr, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

}
