package com.sist.music;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.util.*;
import com.mongodb.*;

import au.com.bytecode.opencsv.CSVReader;


public class MusicDAO {
	private MongoClient mc;
	private DB db;
	private DBCollection dbc;
	private String type;
	
	public MusicDAO(String type){
		try {
			this.type=type;
			
			MongoClient mc=new MongoClient(new ServerAddress(new InetSocketAddress("211.238.142.104", 27017)));
			db=mc.getDB("mydb");
			dbc=db.getCollection(type);
			
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public void musicInsert(MusicVO vo){
		try {
			int no=0;
			DBCursor cursor=dbc.find();
			while (cursor.hasNext()) {
				BasicDBObject obj=(BasicDBObject)cursor.next();
				int i=obj.getInt("no");
				if (no<i) {
					no=i;
				}
			}
			cursor.close();
			
			BasicDBObject obj=new BasicDBObject();
			obj.put("no", no+1);
			obj.put("rank", vo.getRank());
			obj.put("title", vo.getTitle());
			obj.put("singer", vo.getSinger());
			
			dbc.insert(obj);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			
		}
		
	}
	
	
	//데이터 다 가져오기
	public List<MusicVO> getMusicData(){
		List<MusicVO> list=new ArrayList<MusicVO>();

		try {
			
			//DBCursor는 resultset에 해당
			String csv="";
			DBCursor cursor=dbc.find();
			while (cursor.hasNext()) {
				BasicDBObject obj=(BasicDBObject)cursor.next();
				MusicVO vo=new MusicVO();
				vo.setRank(obj.getInt("rank"));
				vo.setTitle(obj.getString("title"));
				vo.setSinger(obj.getString("singer"));
				list.add(vo);
				
				csv+=vo.getRank()+","+vo.getTitle()+"\n";
				
			}
			cursor.close();
			csv=csv.substring(0, csv.lastIndexOf("\n"));

			FileWriter fw=new FileWriter("./music-data/"+type+".csv");
			fw.write(csv);
			fw.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			
		}
		
		return list;
	}
	
	public static void myCreateCSV(){
		try {
			FileReader fr=new FileReader("./music-data/genie-melon/part-00000");
			String data="";
			int i=0;
			while((i=fr.read())!=-1){
				data+=String.valueOf((char)i);
				
			}
			fr.close();
			data=data.replace("(", "");
			data=data.replace(")", "");
			FileWriter fw=new FileWriter("./music-data/genie-melon/myrank.csv");
			fw.write(data);
			fw.close();
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public void myRankInsert(){
		try {
			dbc.drop();//먼저 지운다.
			
			FileReader fr=new FileReader("./music-data/genie-melon/myrank.csv");
			String data="";
			int i=0;
			while((i=fr.read())!=-1){
				data+=String.valueOf((char)i);
				
			}
			fr.close();
			
			//아래는 SparkJoin의 call method에 해당
			String[] temp=data.split("\n");
			for (String s : temp) {
				CSVReader csv=new CSVReader(new StringReader(s));
				String[] ss=csv.readNext();
				BasicDBObject obj=new BasicDBObject();
				obj.put("title", ss[0]);
				obj.put("rating", 100-(Integer.parseInt(ss[1].trim())+Integer.parseInt(ss[2].trim())));
				dbc.insert(obj);
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
}
