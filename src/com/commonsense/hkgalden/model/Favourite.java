package com.commonsense.hkgalden.model;

import java.util.Date;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "favourite")
public class Favourite
{

	@DatabaseField(columnName = "t_id", id = true)
	private int t_id;

	@DatabaseField(columnName = "date")
	private Date date;

	public String getId() {
		return String.valueOf(t_id);
	}

	public void setId(String id) {
		this.t_id = Integer.valueOf(id);
	}

	public Date getDate() {
		return date;
	}

	public void setDate() {
		Date date = new Date();
		this.date = date;
	}
}
