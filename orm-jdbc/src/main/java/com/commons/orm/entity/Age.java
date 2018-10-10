package com.commons.orm.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.commons.orm.sql.SqlAnnotation.Column;
import com.commons.orm.sql.SqlAnnotation.Id;
import com.commons.orm.sql.SqlAnnotation.Table;

@Table(name = "age")
public class Age{
	
	@Id(name = "id")
	private int id;
	@Column(name = "sid")
	private String sid;
	@Column(name="name")
	private String name;
	@Column(name="pass")
	private String pass;
	

	@Override
	public String toString() {
		return "Age [id=" + id + ", sid=" + sid + ", name=" + name + ", pass="
				+ pass + ", bs=" + bs + ", c=" + c + ", as=" + as + "]";
	}
	private List<String> bs = new ArrayList<String>();
	private Integer c; 
	private Map<String,String> as = new HashMap<String, String>();
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
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
	public List<String> getBs() {
		return bs;
	}
	public void setBs(List<String> bs) {
		this.bs = bs;
	}
	public Integer getC() {
		return c;
	}
	public void setC(Integer c) {
		this.c = c;
	}
	public Map<String, String> getAs() {
		return as;
	}
	public void setAs(Map<String, String> as) {
		this.as = as;
	}



}
