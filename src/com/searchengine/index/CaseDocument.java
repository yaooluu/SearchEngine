package com.searchengine.index;

import org.apache.lucene.document.*;

import com.searchengine.dao.Case;

public class CaseDocument {

	private static final String ID = "id";

	//private static final String INDEX_TIME = "indextime";
	
	private static final String TITLE = "title";
	
	private static final String DATE = "date";

	private static final String TYPE = "type";
	
	private static final String INTRODUCTION = "introduction";
	
	private static final String DETAIL = "detail";
	
	private static final String LOCATION = "location";
	
	
	public static Document buildProductDocument(Case c, int id) {

		Document doc = new Document();

		Field identifier = new Field(ID, id + "", Field.Store.YES,
				Field.Index.UN_TOKENIZED);

		/*
		long mills = System.currentTimeMillis();
		Field indextime = new Field(INDEX_TIME, mills + "", Field.Store.YES,
				Field.Index.UN_TOKENIZED);
		*/
		
		Field title = new Field(TITLE, c.getTitle(),
				Field.Store.YES, Field.Index.TOKENIZED);
		
		Field date = new Field(DATE, c.getDate(),
				Field.Store.YES, Field.Index.UN_TOKENIZED);

		Field type = new Field(TYPE, c.getType(),
				Field.Store.YES, Field.Index.UN_TOKENIZED);

		Field introduction = new Field(INTRODUCTION, c.getIntroduction(),
				Field.Store.YES, Field.Index.TOKENIZED);
	
		Field detail = new Field(DETAIL, c.getDetail(),
				Field.Store.YES, Field.Index.TOKENIZED);
		
		Field location = new Field(LOCATION, c.getLocation(),
				Field.Store.YES, Field.Index.TOKENIZED);
		
		String text = "";
		text += c.getTitle();
		text += c.getIntroduction();
		text += c.getDetail();
		
		Field all = new Field("all", text, Field.Store.YES, Field.Index.TOKENIZED);

		// add all
		doc.add(identifier);
		//doc.add(indextime);
		doc.add(title);
		doc.add(date);
		doc.add(type);
		doc.add(introduction);
		doc.add(detail);
		doc.add(location);
		doc.add(all);

		return doc;

	}

}
