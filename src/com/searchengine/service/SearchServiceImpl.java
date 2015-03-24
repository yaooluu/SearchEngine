package com.searchengine.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;





import jeasy.analysis.MMAnalyzer;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import com.searchengine.config.PropertyConfiguration;
import com.searchengine.service.dao.SearchRequest;
import com.searchengine.service.dao.SearchResult;
import com.searchengine.service.dao.SearchResultDao;
import com.searchengine.service.dao.SearchResults;

public class SearchServiceImpl implements SearchService {
/*
	private static final String ID = "id";	
	private static final String TITLE = "title";
	private static final String DATE = "date";
	private static final String TYPE = "type";
	private static final String INTRODUCTION = "introduction";
	private static final String DETAIL = "detail";
	private static final String LOCATION = "location";
*/
	private String path = this.getClass().getClassLoader().getResource("").getPath();
	
	private String INDEX_STORE_PATH = path.split("/WEB-INF/classes/")[0] + "/index";

	private SearchResultDao searchResultDao;
	
	public static HashMap<String,String> map = null;

	
	//执行检索的主要入口
	public SearchResults getSearchResults(SearchRequest request) {
		
		
		map = new HashMap<String,String>();
		
		SearchResults results = new SearchResults();
		Query query = makeQuery(request.getQuery());
		
		ArrayList<String> list = new ArrayList<String>();	

		try {
			System.out.println(INDEX_STORE_PATH);
			IndexSearcher searcher = new IndexSearcher(INDEX_STORE_PATH);
			Hits hits = searcher.search(query);

			int length = hits.length();
			int startIndex = request.getStartIndex();
			int endIndex;

			if (startIndex > length)
			{
				//impossible
			}
			else
			{
				endIndex = startIndex + 9;
				if (endIndex >= length) {
					endIndex = length;
				}

				for (int i = startIndex; i <= endIndex; i++) {
					Document doc = hits.doc(i-1);
					String id = doc.get("id");
					list.add(id);
					
					//debug
					System.out.println("hits["+(i-1)+"] score:" + hits.score(i-1) + "\t" + doc.get("title"));
					
					//保存ID对应的最终展示顺序
					map.put(id+"", (i-1)%10+"");	//key为数据库和索引中的id，value为结果展示页面上的0-9号div的id
				}
			}

			results.setResults(list);
			
			//分页算法。。。。
			int startPage;
			int endPage;
			
			if (startIndex % 100 == 0) {
				startPage = (startIndex / 100 - 1) * 10 + 1;
			}
			else {
				startPage = (startIndex/100) * 10 + 1;
			}
			
			int span;
			int hasNext;
			
			float temp = ((float)(length - (startPage-1) * 10 ))/10;
			if (temp > 10 )
			{
				span = 9;
				hasNext = 1;
			}
			else if (temp == 10) {
				span = 9;
				hasNext = 0;
			}
			else {
				hasNext = 0;
				if ((int)temp < temp) {
					span = (int)temp;
				}
				else {
					span = (int)temp -1;
				}
				
			}
			endPage = startPage + span;
			
			results.setMinPage(startPage);
			results.setMaxPage(endPage);
			results.setHasNext(hasNext);
			results.setStartIndex(startIndex);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

	public SearchResult getSearchResultById(int id) {
		return searchResultDao.getSearchResultById(id);
	}

	public SearchResultDao getSearchResultDao() {
		return searchResultDao;
	}


	public void setSearchResultDao(SearchResultDao searchResultDao) {
		this.searchResultDao = searchResultDao;
	}

	//处理用户输入检索语句，算法很重要
	private Query makeQuery(String query) {
		
		//暂时构造TermQuery简单处理
		//TermQuery qu = new TermQuery(new Term("all", query.toLowerCase()));
		
		//使用QueryParser处理用户检索输入
		QueryParser parser = new QueryParser("all",new MMAnalyzer());
		parser.setDefaultOperator(QueryParser.AND_OPERATOR);
		Query qu = null;
		try {
			qu = parser.parse(query);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return qu;
	}

}
