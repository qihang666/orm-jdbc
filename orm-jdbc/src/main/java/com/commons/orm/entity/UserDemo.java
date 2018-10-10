package com.commons.orm.entity;

import java.util.Date;

import com.commons.orm.sql.SqlAnnotation.Column;
import com.commons.orm.sql.SqlAnnotation.Id;
import com.commons.orm.sql.SqlAnnotation.Table;
@Table(name = "user_1")
public class UserDemo{
	@Id(name="id")
	private Integer id;
	@Column(name="name")
	private String name;
	@Column(name="pass")
	private String pass;
	@Column(name="create_date")
	private Date create_date;
	@Column(name="aid")
	private String aid;
	
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", pass=" + pass
				+ ", create_date=" + create_date + ", aid=" + aid + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

}
