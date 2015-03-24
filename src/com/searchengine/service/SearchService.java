package com.searchengine.service;


import com.searchengine.service.dao.SearchRequest;
import com.searchengine.service.dao.SearchResult;
import com.searchengine.service.dao.SearchResults;

public interface SearchService {
	public abstract SearchResults getSearchResults(SearchRequest request);

	public abstract SearchResult getSearchResultById(int id);
}
