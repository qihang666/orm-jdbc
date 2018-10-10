package com.commons.orm.entity;

import java.util.Date;

import com.commons.orm.sql.SqlAnnotation.Column;
import com.commons.orm.sql.SqlAnnotation.Id;
import com.commons.orm.sql.SqlAnnotation.Table;
@Table(name = "user")
public class User{
	
	@Id(name="id")
	private Long id;
	@Column(name="username")
	private String username;
	@Column(name="password")
	private String password;
	@Column(name="createdate")
	private Date createdate;
	
	
	public User(Long id, String username, String password, Date createdate) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.createdate = createdate;
	}


	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password="
				+ password + ", createdate=" + createdate + "]";
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


	public Date getCreatedate() {
		return createdate;
	}


	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	



}
