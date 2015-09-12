package com.zhaiyz.annotationdemo.bean;

import com.zhaiyz.annotationdemo.annotation.Column;
import com.zhaiyz.annotationdemo.annotation.Table;

@Table
public class User {

	@Column
	private long id;

	@Column
	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
