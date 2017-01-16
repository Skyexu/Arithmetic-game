package com.sac.wmath.model;

/**
 *分数类 
 *
 */

public class Score implements Comparable<Score> {

	//排行榜日期，时间
	private String scoreDate;
	public int scoreNum;

	public Score(String date, int num){
		scoreDate=date;
		scoreNum=num;
	}

	
	public int compareTo(Score sc){
		//return 0 if equal
		//1 if passed greater than this
		//-1 if this greater than passed
		return sc.scoreNum>scoreNum? 1 : sc.scoreNum<scoreNum? -1 : 0;
	}

	//return score display text
	public String getScoreText(){
		return scoreDate+" - "+scoreNum;
	}

}
