package com.commonsense.hkgalden.model;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "channel")
public class Channel
{

	@DatabaseField(columnName = "ident", id = true)
	private String ident;
	
	@DatabaseField(columnName = "name")
	private String name;

	@DatabaseField(columnName = "color")
	private String color;

	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	

}
