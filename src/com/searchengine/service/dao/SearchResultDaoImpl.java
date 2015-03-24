package com.searchengine.service.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.searchengine.service.SearchServiceImpl;

public class SearchResultDaoImpl implements SearchResultDao {
	
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public SearchResult getSearchResultById(int id) {
		final int id_db = id;
		final int seqId = Integer.parseInt(SearchServiceImpl.map.get(id+""));
		
		//spring集成的功能
		final SearchResult sr = new SearchResult();
		JdbcTemplate template = new JdbcTemplate(dataSource);
		
		template.query("select * from emergency where id=?",
				new PreparedStatementSetter() {

					public void setValues(PreparedStatement ps)
							throws SQLException {
						ps.setInt(1, id_db);

					}
				}, new RowCallbackHandler() {

					public void processRow(ResultSet rs) throws SQLException {
						try{
							sr.setId(seqId);
							sr.setTitle(rs.getString("title"));
							sr.setDate(rs.getString("date"));
							sr.setType(rs.getString("type"));
							sr.setIntroduction(rs.getString("introduction"));
							sr.setDetail(rs.getString("detail"));
							sr.setLocation(rs.getString("location"));
							sr.setLatitude(rs.getString("latitude"));
							sr.setLongitude(rs.getString("longitude"));
							sr.setLink(rs.getString("link"));
							
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				});
		return sr;
	}
}
