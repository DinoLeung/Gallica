package com.commonsense.hkgalden.adapter;

import com.google.gson.annotations.SerializedName;

public class Topics {

	
	@SerializedName("id")
	private String id;
	
	@SerializedName("tle")
	private String tle;
	
	@SerializedName("uname")
	private String uname;
	
	@SerializedName("uid")
	private String uid;
	
	@SerializedName("count")
	private String count;
	
	@SerializedName("ltime")
	private String ltime;
	
	@SerializedName("tlock")
	private String tlock;
	
	@SerializedName("rate")
	private String rate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTle() {
		return tle;
	}
	public void setTle(String tle) {
		this.tle = tle;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getLtime() {
		return ltime;
	}
	public void setLtime(String ltime) {
		this.ltime = ltime;
	}
	public String getTlock() {
		return tlock;
	}
	public void setTlock(String tlock) {
		this.tlock = tlock;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}

	
	
}
