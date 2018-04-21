package com.commonsense.hkgalden.model;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "posts")

public class Posts
{
	@DatabaseField(columnName = "id", generatedId = true)
	private int id;

	@DatabaseField(columnName = "topic_id")
	private String topicId;

	@DatabaseField(columnName = "ltime")
	private String ltime;

	@DatabaseField(columnName = "author")
	private String author;

	@DatabaseField(columnName = "topicTitle")
	private String topicTitle;
	
	@DatabaseField(columnName = "count")
	private String count;
	
	@DatabaseField(columnName = "rate")
	private String rate;
	
	
	public String getAuthor() {
		return author;
	}


	public String getCount(){
		return count;
	}


	public int getId() {
		return id;
	}


	public String getLtime() {
		return ltime;
	}


	public String getRate(){
		return rate;
	}


	public String getTopicId() {
		return topicId;
	}


	public String getTopicTitle() {
		return topicTitle;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public void setCount(String count){
		this.count = count;
	}


	public void setId(int id) {
		this.id = id;
	}
	
	public void setLtime(String ltime) {
		this.ltime = ltime;
	}
	
	public void setRate(String rate){
		this.rate = rate;
	}
	
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	
	public void setTopicTitle(String topicTitle) {
		this.topicTitle = topicTitle;
	}
	
}
