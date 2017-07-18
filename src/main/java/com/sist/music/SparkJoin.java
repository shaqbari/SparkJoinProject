package com.sist.music;

import java.io.StringReader;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;

import com.google.common.base.Optional;

import au.com.bytecode.opencsv.CSVReader;
import scala.Tuple2;

import java.io.*;

public class SparkJoin {
	public static void main(String[] args) {
		try {
			File dir=new File("./music-data/genie-melon");
			if (dir.exists()) {
				File[] files=dir.listFiles();//안에있는폴더를 다 지워야 폴더가 지워진다.
				for (File f : files) {
					f.delete();
				}
				dir.delete(); //rm -rf
			}
			
			SparkConf conf=new SparkConf().setAppName("Music").setMaster("local");
			JavaSparkContext sc=new JavaSparkContext(conf);
			JavaRDD<String> genie=sc.textFile("./music-data/genie.csv");
			
			//csv파일은 한줄씩 읽으면서String으로 가져온다.
			JavaPairRDD<String, String> geniePair=genie.mapToPair(new PairFunction<String, String, String>() {

				@Override
				public Tuple2<String, String> call(String s) throws Exception {
					CSVReader csv=new CSVReader(new StringReader(s));
					try {
						String[] d=csv.readNext();
														//title rank
						return new Tuple2<String, String>(d[1], d[0]);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
					
					//맨마지막에 값이 없을 경우
					return new Tuple2<String, String> ("-1", "1");
				}
			});
			
			
			JavaRDD<String> melon=sc.textFile("./music-data/melon.csv");
			JavaPairRDD<String, String> melonPair=melon.mapToPair(new PairFunction<String, String, String>() {

				@Override
				public Tuple2<String, String> call(String s) throws Exception {
					CSVReader csv=new CSVReader(new StringReader(s));
					try {
						String[] d=csv.readNext();
														//title rank
						return new Tuple2<String, String>(d[1], d[0]);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
					
					//맨마지막에 값이 없을 경우
					return new Tuple2<String, String> ("-1", "1");
				}
			});
			
			//(key, value)를 tuple이라 한다. ==> (key, (1,1))
			JavaPairRDD<String, Tuple2<String, String>> joinData=geniePair.join(melonPair);
			//JavaPairRDD<String, Tuple2<String, Optional<String>>> joinData=geniePair.leftOuterJoin(melonPair);
			JavaPairRDD<String, Tuple2<String, String>> so=joinData.sortByKey(false);//false면 desc, true면 asc정렬
			//joinData.saveAsTextFile("./music-data/genie-melon");
			so.saveAsTextFile("./music-data/genie-melon");
			MusicDAO.myCreateCSV();
			MusicDAO dao=new MusicDAO("myrank");
			dao.myRankInsert();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
