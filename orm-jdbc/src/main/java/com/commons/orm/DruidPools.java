package com.commons.orm;

import java.sql.Connection;

import com.alibaba.druid.pool.DruidDataSource;
import com.commons.orm.dao.IConnection;
import com.commons.orm.sql.EntityParse;

/**
 * @Description: Druid连接池1
 * @author hang
 * @date 2016-5-11 上午10:33:48
 * @version V1.7
 */
public class DruidPools implements IConnection {

	private DruidDataSource dataSource = null;

	private EntityParse parse;
	
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url;// 数据库链接
	private static String user;// 数据库用户名
	private static String password;// 数据库密码
	// String driver ="com.mysql.jdbc.Driver";
	// String url ="jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF8&autoReconnect=true";
	// String user ="root";
	// String password ="root";
	// String driver ="com.mysql.jdbc.Driver";

	

	public DruidPools(String url, String user, String password) {
		parse = new EntityParse();
		DruidPools.url = url;
		DruidPools.user = user;
		DruidPools.password = password;
		try {
			
	
			dataSource = new DruidDataSource();
			dataSource.setDriverClassName(DruidPools.driver);
			dataSource.setUrl(DruidPools.url);
			dataSource.setUsername(DruidPools.user);
			dataSource.setPassword(DruidPools.password);
			dataSource.setInitialSize(10);
			dataSource.setMinIdle(1);
			dataSource.setMaxActive(20);
			dataSource.setPoolPreparedStatements(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	@Override
	public EntityParse getEntityParse() {
		return parse;
	}

	public static String getUrl() {
		return url;
	}

	public static void setUrl(String url) {
		DruidPools.url = url;
	}

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		DruidPools.user = user;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		DruidPools.password = password;
	}

	

	
}
