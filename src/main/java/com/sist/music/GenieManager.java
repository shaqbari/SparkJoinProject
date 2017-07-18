package com.sist.music;

import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class GenieManager {
	public static void main(String[] args) {
		GenieManager genieManager=new GenieManager();
		genieManager.genieTop100();
		
	}
	
	public List<MusicVO> genieTop100(){
		List<MusicVO> list=new ArrayList<MusicVO>();
		
		try {
			Document doc=Jsoup.connect("http://www.genie.co.kr/chart/top100").get();
			
			//div class list
			Elements rank=doc.select("div.list span.number");
			Elements title=doc.select("div.list span.music a");
			Elements singer=doc.select("div.list span.meta a.artist");
			
			for (int a = 0; a < rank.size(); a++) {
				System.out.println(rank.get(a).text()+" "+title.get(a).text()+" "+singer.get(a).text() );
				
				MusicVO vo=new MusicVO();
				vo.setRank(Integer.parseInt(rank.get(a).text().trim()));
				vo.setTitle(title.get(a).text());
				vo.setSinger(singer.get(a).text());
				
				list.add(vo);
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return list;		
	}
	
}
