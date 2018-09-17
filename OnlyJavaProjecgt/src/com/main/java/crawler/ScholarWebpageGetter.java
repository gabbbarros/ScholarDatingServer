package com.main.java.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScholarWebpageGetter {
	public static final String URL_START = "https://scholar.google.com/citations?user=";
	public static final String URL_END = "&hl=en";

	public ScholarWebpageGetter() {
	}

	public static String getPageHTML(final String URL) throws IOException {
		String line = "", all = "";
		URL myUrl = null;
		BufferedReader in = null;
		try {
			myUrl = new URL(URL);
			in = new BufferedReader(new InputStreamReader(myUrl.openStream()));
			
			while ((line = in.readLine()) != null) {
				all += line;
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println(URL);
			return null;
			//System.exit(1);
		} finally {
			if (in != null) {
				in.close();
			}
		}

		return all;
	}
	public HashMap<InfoType,Object> getInformation(String userId) {
		String url = URL_START + userId + URL_END;
		String page;
		try {
			page = getPageHTML(url);
			
			HashMap<InfoType,Object> result = parseText(page);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	private HashMap<InfoType, Object> parseText(String html) {
		HashMap<InfoType,Object> result = new HashMap<InfoType,Object>();
		String aux = "";
		String[] auxStr = null;
		
		Document doc = Jsoup.parse(html);
		result.put(InfoType.Name, getName(doc));
		result.put(InfoType.Affiliation, getAfilliation(doc));
		auxStr = getNumCitations(doc).split(",");
		result.put(InfoType.Citations,auxStr[0]);
		result.put(InfoType.HIndex,auxStr[1]);
		result.put(InfoType.I10,auxStr[2]);
		result.put(InfoType.CoAuthors,getCoAuthors(doc));
		result.put(InfoType.Papers,getPapers(doc));
		
		Iterator<InfoType> it = result.keySet().iterator();
		while(it.hasNext()) {
			InfoType k = (InfoType) it.next();
			System.out.println(k.name()+"\t"+result.get(k));
		}
		return result;
	}


	private String getName(Document doc) {
		return doc.title().split(" -")[0];
	}
	
	private String getAfilliation(Document doc) {
		Elements content = doc.getElementsByClass("gsc_prf_ila");
		if(content.size() > 0) 
			return content.first().text();
		else
			return "None";
	}
	
	private ArrayList<String> getPapers(Document doc) {
		ArrayList<String> papers = new ArrayList<String>();
		
		Element table = doc.getElementById("gsc_a_b");
		Elements lines = table.getElementsByClass("gsc_a_tr");
		for(Element line : lines) {
			papers.add(line.getElementsByClass("gsc_a_t").first().getElementsByTag("a").first().text());
		}
		
		return papers;
	}
	
	private ArrayList<CoAuthor> getCoAuthors(Document doc) {
		Element nav = doc.getElementById("gsc_rsb_co");
		Elements lines = nav.getElementsByClass("gsc_rsb_a");
		ArrayList<CoAuthor> authors = new ArrayList<CoAuthor>();
		for(Element e1 : lines) {
			Elements li = e1.getElementsByTag("li");
			for(Element e : li) {
				Elements descs = e.getElementsByClass("gsc_rsb_a_desc");
				Element el = descs.first().getElementsByTag("a").first();
				String link = el.attr("href");
				String name = el.text();
				String userID = link.substring(link.indexOf("=")+1, link.indexOf("&"));
				
				authors.add(new CoAuthor(userID, name));
				//image
				//el = e.getElementById("gsc_rsb-"+userId+"-img");
				//https://scholar.google.com/citations?view_op=medium_photo&user=lr4I9BwAAAAJ&citpid=2
				//https://scholar.google.com/citations?view_op=medium_photo&user=lr4I9BwAAAAJ&citpid=2
				
				
				//"gsc_rsb-"+userId+"-img"
		
			}
		}
		
		return authors;
	}
	
	private String getNumCitations(Document doc) {
		Element nav = doc.getElementById("gsc_rsb_cit");
		Element table = nav.getElementById("gsc_rsb_st");
		Elements fromTable = table.getAllElements();
		String[] firstLine = fromTable.first().text().split(" ");
		String result = "";
		System.out.println(fromTable.first().text());

		int c1=0;
		for(int i = 1; i < firstLine.length; i++) {
			if(i== 4 || i == 7 || i == 10) { //I hate this line
				result+=firstLine[i]+ ",";
			}
		}
		
		return result;
	}
	
	
	public static void main(String[] args) {
//		new WebpageGetter().getRandomListOfLinks();
//		System.out.println();
//		System.out.println("------------------");

		//works!
		//System.out.println(new WebpageGetter().getCleanTextFromWebpage("https://scholar.google.com/citations?user=n8wVYHkAAAAJ&hl=en"));

		ScholarWebpageGetter tester = new ScholarWebpageGetter();
		//mine
		tester.getInformation("n8wVYHkAAAAJ");
		//julian
		//tester.getInformation("lr4I9BwAAAAJ");
		//ahmed
	}
}
