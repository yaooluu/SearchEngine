package com.searchengine.service.dao;

public class SearchRequest {

	private int page;
	private int numOfPage;
	private int startIndex;
	private String query;
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getNumOfPage() {
		return numOfPage;
	}
	
	public void setNumOfPage(int numOfPage) {
		this.numOfPage = numOfPage;
	}
	
	public int getStartIndex() {
		return startIndex;
	}
	
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	public String getQuery() {
		return query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
}
