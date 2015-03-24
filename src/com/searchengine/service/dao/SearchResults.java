package com.searchengine.service.dao;

import java.util.ArrayList;

public class SearchResults {
	
	//搜索结果id集合
	private ArrayList<String> results = new ArrayList<String>();
	
	private int startIndex;
	private int minPage;
	private int maxPage;	
	private int hasNext;

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getMinPage() {
		return minPage;
	}

	public void setMinPage(int minPage) {
		this.minPage = minPage;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public int getHasNext() {
		return hasNext;
	}

	public void setHasNext(int hasNext) {
		this.hasNext = hasNext;
	}

	public ArrayList<String> getResults() {
		return results;
	}

	public void setResults(ArrayList<String> results) {
		this.results = results;
	}


}
