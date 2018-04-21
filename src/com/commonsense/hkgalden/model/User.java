package com.commonsense.hkgalden.model;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user")
public class User
{
	@DatabaseField(columnName = "id", generatedId = true)
	private int id;
	
	@DatabaseField(columnName = "userid")
	private String userid;
	
	@DatabaseField(columnName = "user_token")
	private String userToken;
	


	@DatabaseField(columnName = "username")
	private String username;
		
	@DatabaseField(columnName = "gender")
	private String gender;
		
	@DatabaseField(columnName = "avatar")
	private String avatar;
	
	@DatabaseField(columnName = "avatar_url")
	private String avatarUrl;
	
	@DatabaseField(columnName = "badge")
	private String badge;

	@DatabaseField(columnName = "blocked")
	private String blocked;
	
	@DatabaseField(columnName = "subscribed")
	private String subscribed;

	public String getAvatar() {
		return avatar;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public String getBadge() {
		return badge;
	}

	public String getBlocked() {
		return blocked;
	}

	public String getGender() {
		return gender;
	}

	public int getId() {
		return id;
	}

	public String getSubscribed() {
		return subscribed;
	}

	public String getUserid() {
		return userid;
	}

	public String getUsername() {
		return username;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

	public void setBlocked(String blocked) {
		this.blocked = blocked;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSubscribed(String subscribed) {
		this.subscribed = subscribed;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	
	
}
