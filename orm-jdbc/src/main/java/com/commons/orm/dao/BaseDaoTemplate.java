package com.commons.orm.dao;

import java.sql.Connection;

import com.commons.orm.sql.EntityParse;



/**
 * @Description: dao基类
 * @author hang
 * @date 2016-5-12 下午5:43:05
 * @version V1.7
 */
public class BaseDaoTemplate extends BaseSQLTemplate{
	
	
	protected IConnection iConnection;//数据库连接
	
	public BaseDaoTemplate(IConnection conne){
		iConnection = conne;
	}
	
	@Override
	public Connection getConnection() {
		return iConnection.getConnection();
	}

	@Override
	public EntityParse getEntityParse() {
		return iConnection.getEntityParse();
	}
	
	


}
