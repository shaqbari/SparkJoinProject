package com.sist.music;

import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MelonManager {
	public static void main(String[] args) {
		MelonManager melonManager=new MelonManager();
		melonManager.melonTop100();
		
	}
	
	public List<MusicVO> melonTop100(){
		List<MusicVO> list=new ArrayList<MusicVO>();
		
		try {
			Document doc=Jsoup.connect("http://www.melon.com/chart/index.htm").get();
			
			//div class list
			Elements rank=doc.select("tr.lst50 td.t_left div.wrap span.rank");
			Elements title=doc.select("tr.lst50 td.t_left div.ellipsis strong a");
			Elements singer=doc.select("tr.lst50 td.t_left div.ellipsis a.play_artist span");
			
			for (int a = 0; a < 50; a++) {
				System.out.println(rank.get(a).text()+" ; "+title.get(a).text()+" ; "+singer.get(a).text() );
			
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
