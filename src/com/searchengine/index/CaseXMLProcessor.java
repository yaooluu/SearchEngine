package com.searchengine.index;

import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.select.Elements;

import com.searchengine.config.PropertyConfiguration;
import com.searchengine.dao.Case;
import com.searchengine.dao.CaseJDBC;

public class CaseXMLProcessor {
 
	private String[] directories;
	private static final String indexPath = PropertyConfiguration.getIndexStorePath();
	
	private static final String dbUrl = PropertyConfiguration.getDBUrl();
	private static final String dbUsr = PropertyConfiguration.getDBUsr();
	private static final String dbPwd = PropertyConfiguration.getDBPwd();

	private CaseJDBC caseJDBC = null;
	private CaseIndexer indexer = null;
	
	private static int count = 0;

	public CaseXMLProcessor() {
		initialize();
	}

	public void initialize() {
		try {
			caseJDBC = new CaseJDBC(dbUrl, dbUsr, dbPwd);
			indexer = new CaseIndexer(indexPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setDirectories(String[] directories) {
		this.directories = directories;
	}

	protected void process() throws Exception {

		if (caseJDBC == null) {
			throw new Exception("Database connection failed, pls retry!!");
		}

		if (directories == null || directories.length == 0) {
			return;
		}

		for (int i = 0; i < directories.length; i++) {
			File f = new File(directories[i]);
			traverse(f);
		}

		indexer.optimizeIndex();
		caseJDBC.close();
		indexer.close();
	}

	// 遍历提取一个文件夹下，所有案例信息的XML文件
	private void traverse(File file) throws Exception {

		String[] files = file.list();
		for (int i = 0; i < files.length; i++) {

			File caseFile = new File(file, files[i]);
			InputStream is = new FileInputStream(caseFile);
			String str = InputStream2String(is,"utf-8");
			
			System.out.println("加载："+caseFile.getName());
			
			Document doc = null;
			doc = Jsoup.parse(str,"", new Parser(new XmlTreeBuilder()));	//加载并解析刚才保存的html文件

			Elements results = doc.select("accident");
			for(Element result : results) {
				
				String s[] = new String[10];
				for(int j=0;j<9;j++) {
					s[j] = result.child(j).text();
					//System.out.println(s[j]);
				}
				//System.out.println("-------------------------");
				
				count++;
				if(count%100==0)
					System.out.println(count);
				
				if(count == 1000) break;
				
				Case c = new Case();
				c.setTitle(s[0]);
				c.setDate(s[1]);
				c.setType(s[2]);
				c.setIntroduction(s[3]);
				c.setDetail(s[4]);
				c.setLocation(s[5]);
				c.setLatitude(s[6]); 
				c.setLongitude(s[7]);
				c.setLink(s[8]);
			
				//添加case到数据库，并返回id
				int nextid = caseJDBC.addCase(c);
	
				//添加case到索引
				indexer.addCase(c, nextid);			
			
			}
		}		
	} 


	public static void main(String[] args) throws Exception {
		CaseXMLProcessor pro = new CaseXMLProcessor();
		pro.initialize();

		String path1 = "d:/cases";
		pro.setDirectories(new String[] { path1 });
		pro.process();
		System.out.println("total:"+count);
	}
	
	//将InputStream转为String类型
	private String InputStream2String(InputStream str, String charset)
			throws IOException {
		
		BufferedReader buff = new BufferedReader(new InputStreamReader(str,
				charset));
		StringBuffer res = new StringBuffer();
		String line = "";
		while ((line = buff.readLine()) != null) {
			res.append(line + "\n");
		}
		return res.toString();
	}

}
