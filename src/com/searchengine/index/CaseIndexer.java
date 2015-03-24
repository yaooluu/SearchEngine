package com.searchengine.index;

import java.io.*;

import jeasy.analysis.MMAnalyzer;

import org.apache.lucene.analysis.*;
import org.apache.lucene.index.*;

import com.searchengine.config.PropertyConfiguration;
import com.searchengine.dao.Case;

public class CaseIndexer {

	private String indexPath = "";	
	private IndexWriter writer = null;	
	private Analyzer analyzer = null;	
	private String dictionary_file = PropertyConfiguration.getWordDictionary();
	
	public CaseIndexer(String indexPath) throws Exception {
		this.indexPath = indexPath;
		initialize();
	}
	
	private void initialize() throws Exception {
		
		analyzer = new MMAnalyzer();	//建立索引时，使用JE分词器
		
//		FileReader reader = new FileReader(dictionary_file);
//		((MMAnalyzer)analyzer).addDictionary(reader);
		writer = new IndexWriter(indexPath, analyzer, true);
	}
		
	public void close() {
		try{
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
			writer = null;
		}
	}
	
	public void addCase(Case c, int id) throws Exception {
		writer.addDocument(CaseDocument.buildProductDocument(c, id));
	}
	
	public void optimizeIndex() throws Exception {
		writer.optimize();
	}
	

}
