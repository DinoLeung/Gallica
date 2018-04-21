package com.commonsense.hkgalden.model;

import java.util.Date;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "history")
public class History {
	
	@DatabaseField(columnName = "t_id", id = true)
	private int t_id;
	
	@DatabaseField(columnName = "title")
	private String title;
	
	@DatabaseField(columnName = "uname")
	private String uname;
	
	@DatabaseField(columnName = "page")
	private String page;
	
	@DatabaseField(columnName = "ident")
	private String ident;
	
	@DatabaseField(columnName = "date")
	private Date date;

	public String getId() {
		return String.valueOf(t_id);
	}

	public void setId(String id) {
		this.t_id = Integer.valueOf(id);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}
	
	public String getPage(){
		return page;
	}
	
	public void setPage(String page){
		this.page = page;
	}

	public String getIdent() {
		return ident;
	}
	
	public void setIdent(String ident){
		this.ident = ident;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate() {
		Date date = new Date();
		this.date = date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

}
