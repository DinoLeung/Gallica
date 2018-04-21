package com.commonsense.hkgalden.model;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "buser")
public class BlockedUser {
	
	@DatabaseField(columnName = "u_id", id = true)
	private int userid;

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}
	


}
