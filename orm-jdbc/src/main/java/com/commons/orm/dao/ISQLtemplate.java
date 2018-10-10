package com.commons.orm.dao;

import java.sql.SQLException;
import java.util.List;

import com.commons.orm.model.Page;

/**
 * @Description: sql接口
 * @author hang
 * @date 2016-6-17 上午11:38:28
 * @version V1.7
 */
public interface ISQLtemplate {
	
	// insert
	public <T> T insert(T model) throws SQLException ;
	public Long insert(String sql) throws SQLException;
	public int insert(String sql,Object... params) throws SQLException;
	
	// update
	public <T> boolean update(T model) throws SQLException;
	public Integer update(String sql) throws SQLException;
	public Integer update(String sql,Object... params) throws SQLException;

	// delete
	public <T> boolean delete(T model) throws SQLException;
	public void delete(String sql) throws SQLException;

	// select model
	public <T> T selectOne(String sql,Object... params) throws SQLException;
	public <T> T selectOne(String sql, Class<T> clazz) throws SQLException;
	public <T> T selectOne(String sql, Class<T> clazz, Object... params) throws SQLException;
	
	// select list
	public <T> List<T> selectList(String sql) throws SQLException;
	public <T> List<T> selectList(String sql, Class<T> clazz) throws SQLException;
	public <T> List<T> selectList(String sql,Class<T> clazz,Page page) throws SQLException;
	public <T> List<T> selectList(String sql, Class<T> clazz, Object... params) throws SQLException;
	public <T> List<T> selectList2(String sql, Class<T> clazz, Object... params) throws Exception;
	public <T> List<T> selectAll(Class<T> clazz) throws SQLException;


	// batch批量操作
	public <T> void batchInsert(List<T> entityList) throws SQLException;
	public <T> void batchUpdate(List<T> entityList) throws SQLException;
	public <T> void batchDelete(List<T> entityList) throws SQLException;
}
