package com.commons.orm.dao;

import java.sql.Connection;

import com.commons.orm.sql.EntityParse;

/**
 * @Description: Connection接口
 * @author hang
 * @date 2016-6-29 下午2:14:43
 * @version V1.7
 */
public interface IConnection {
	
	 Connection getConnection();
	 
	 EntityParse getEntityParse();
	 
	 
}
