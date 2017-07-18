package com.sist.music;

import java.util.*;

import com.mongodb.DBCursor;
import com.sist.music.*;

public class MusicManager {
	public static void main(String[] args) {
		try {
			/*MusicDAO dao=new MusicDAO("genie");
			GenieManager g=new GenieManager();
			List<MusicVO> gList=g.genieTop100();
			for (MusicVO vo : gList) {
				dao.musicInsert(vo);
			}
			
			dao=new MusicDAO("melon");
			MelonManager m=new MelonManager();
			List<MusicVO> mList=m.melonTop100();
			for (MusicVO vo : mList) {
				dao.musicInsert(vo);
			}
			*/
			
			
			
			
			MusicDAO dao=new MusicDAO("genie");
			dao.getMusicData();//FileWriter는 폴더 만들어줘야 한다.
			
			dao=new MusicDAO("melon");
			dao.getMusicData();

			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	
}
