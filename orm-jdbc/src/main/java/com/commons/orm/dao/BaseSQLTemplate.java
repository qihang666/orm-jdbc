package com.commons.orm.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.commons.orm.model.Page;
import com.commons.orm.sql.EntityParse;
import com.commons.orm.sql.EntityParse.SqlType;
import com.commons.orm.sql.SqlDateUtil;

/**
 * @Description: jdbc封装实体模板
 * @author hang
 * @date 2016-6-14 下午3:39:50
 * @version V1.7
 */
@SuppressWarnings("unchecked")
public abstract class BaseSQLTemplate implements ISQLtemplate {

	protected static Logger log = LoggerFactory
			.getLogger(BaseSQLTemplate.class);

	/** 批量修改大小 */
	private final static int BATCH_UPDATE_SIZE = 1000;
	/** 批量插入大小 */
	private final static int BATCH_INSERT_SIZE = 1000;
	/** 批量删除大小 */
	private final static int BATCH_DELETE_SIZE = 1000;

	/**
	 * 获取解析对象
	 * 
	 * @return
	 */
	public abstract EntityParse getEntityParse();

	/**
	 * 获取连接
	 * 
	 * @return
	 */
	public abstract Connection getConnection();

	/**
	 * 关闭连接
	 */
	public void closeConnection(Connection conn) {
		if (conn == null) {
			return;
		}
		try {
			conn.close();
		} catch (SQLException e) {
			log(e);
		}
	}

	/**
	 * 关闭结果集
	 * 
	 * @param rs
	 */
	public static void closeResultSet(ResultSet rs) {
		if (rs == null) {
			return;
		}
		try {
			rs.close();
		} catch (SQLException e) {
			log(e);
		}
	}

	/**
	 * 关闭Statement
	 * 
	 * @param statement
	 */
	public static void closeStatement(Statement statement) {
		if (statement == null) {
			return;
		}
		try {
			statement.close();
		} catch (Exception e) {
			log(e);
		}

	}

	public static void log(Object msg) {
		// log_static.error("error", msg);
		log.error("error", msg);
		// System.err.println("error:"+msg);
	}

	/**
	 * 保存实体
	 * 
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <T> T insert(T model) throws SQLException {
		String sql = getEntityParse().getSql(SqlType.INSERT, model.getClass());
		List<Object> params = EntityParse.getInsterParams(model);
		return executeInsert(sql, model, params);
	}

	/**
	 * 创建，返回自增主键
	 * 
	 * @throws SQLException
	 * 
	 * */
	@Override
	public Long insert(String sql) throws SQLException {
		Connection conn = getConnection();

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		Long generatedKey = null;
		try {
			statement = conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);

			statement.executeUpdate();

			resultSet = statement.getGeneratedKeys();

			if (resultSet.next()) {
				generatedKey = (Long) resultSet.getObject(1);
			}

		} catch (SQLException e) {
			log(e);
			throw e;
		} finally {
			closeConnection(conn);
			closeResultSet(resultSet);
			closeStatement(statement);
		}
		return generatedKey;
	}

	/**
	 * 返回执行成功的行数
	 * 
	 * @throws SQLException
	 */
	@Override
	public int insert(String sql, Object... params) throws SQLException {
		Connection conn = getConnection();
		int result = 0;
		try {
			QueryRunner qr = new QueryRunner();
			result = qr.update(conn, sql, params);
		} catch (SQLException e) {
			log(e);
			throw e;
		} finally {
			closeConnection(conn);
		}
		return result;
	}

	/**
	 * 修改实体
	 * 
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <T> boolean update(T model) throws SQLException {
		String sql = getEntityParse().getSql(SqlType.UPDATE, model.getClass());
		List<Object> params = EntityParse.getUpdateParams(model);
		return execute(sql, params) > 0;
	}

	/**
	 * 更新
	 * 
	 * @throws SQLException
	 */
	@Override
	public Integer update(String sql) throws SQLException {
		Connection conn = getConnection();
		Integer result = null;
		try {
			QueryRunner qr = new QueryRunner();
			result = qr.update(conn, sql);
		} catch (SQLException e) {
			log(e);
			throw e;
		} finally {
			closeConnection(conn);
		}
		return result;
	}

	/**
	 * 更新
	 * 
	 * @throws SQLException
	 */
	@Override
	public Integer update(String sql, Object... params) throws SQLException {
		Connection conn = getConnection();
		Integer result = null;
		try {
			QueryRunner qr = new QueryRunner();
			result = qr.update(conn, sql, params);
		} catch (SQLException e) {
			log(e);
			throw e;
		} finally {
			closeConnection(conn);
		}
		return result;
	}

	/**
	 * 删除
	 * 
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <T> boolean delete(T model) throws SQLException {
		String sql = getEntityParse().getSql(SqlType.DELETE, model.getClass());
		List<Object> params = EntityParse.getDeleteParams(model);
		return execute(sql, params) > 0;
	}

	/**
	 * 删除
	 * 
	 * @throws SQLException
	 * */
	@Override
	public void delete(String sql) throws SQLException {
		update(sql);
	}

	/**
	 * 执行sql语句
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int execute(String sql, List<Object> params) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement statement = null;
		try {
			statement = conn.prepareStatement(sql);
			if (params != null && params.size() > 0) {
				for (int i = 0; i < params.size(); i++) {
					statement.setObject(i + 1, params.get(i));
				}
			}
			return statement.executeUpdate();
		} catch (SQLException e) {
			log(e);
			throw e;
		} finally {
			closeConnection(conn);
			closeStatement(statement);
		}
	}

	/**
	 * 执行插入语句
	 * 
	 * @param sql
	 * @param model
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public <T extends Object> T executeInsert(String sql, T model,
			List<Object> params) throws SQLException {
		Connection conn = getConnection();
		ResultSet rs = null;
		PreparedStatement statement = null;
		try {
			statement = conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			// for (int i = 0; i < params.size(); i++) {
			// statement.setObject(i + 1, params.get(i));
			// }
			fillStatement(statement, params.toArray());
			statement.executeUpdate();
			rs = statement.getGeneratedKeys();
			Object id = 0;// 保存生成的ID
			if (rs != null && rs.next()) {
				id = rs.getObject(1);
				EntityParse.setIdValue(model, id);
			}
			return model;
		} catch (SQLException e) {
			log(e);
			throw e;
		} finally {
			closeConnection(conn);
			closeResultSet(rs);
			closeStatement(statement);
		}
	}

	/**
	 * 查询:有反射 注:1.基础表专用 2.T为Integer,Long,String等基础数据类型时 clazz传null
	 * 
	 * @param sql
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <T> T selectOne(String sql, Class<T> clazz) throws SQLException {

		Connection conn = getConnection();
		T results = null;
		try {

			QueryRunner qr = new QueryRunner();

			if (clazz == null) {
				results = (T) qr.query(conn, sql, new MapHandler());
			} else {
				ResultSetHandler<T> rsh = new BeanHandler<T>(clazz);
				results = qr.query(conn, sql, rsh);
			}

		} catch (SQLException e) {
			log(e);
			throw e;
		} finally {
			closeConnection(conn);
		}

		return results;
	}

	/**
	 * 查询:有反射 注:1.基础表专用 2.T为Integer,Long,String等基础数据类型时 clazz传null
	 * 
	 * @param sql
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <T> T selectOne(String sql, Class<T> clazz, Object... params)
			throws SQLException {
		Connection conn = getConnection();
		T results = null;
		try {
			QueryRunner qr = new QueryRunner();
			if (clazz == null) {
				results = (T) qr.query(conn, sql, new MapHandler(), params);
			} else {
				ResultSetHandler<T> rsh = new BeanHandler<T>(clazz);
				results = qr.query(conn, sql, rsh, params);
			}
		} catch (SQLException e) {
			log(e);
			throw e;
		} finally {
			closeConnection(conn);
		}

		return results;
	}

	/**
	 * 查询:无反射 注：返回map
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <T> T selectOne(String sql, Object... params) throws SQLException {

		Connection conn = getConnection();
		T results = null;
		try {
			QueryRunner qr = new QueryRunner();
			ResultSetHandler<T> rsh = (ResultSetHandler<T>) new MapHandler();
			results = qr.query(conn, sql, rsh, params);
		} catch (SQLException e) {
			log(e);
			throw e;
		} finally {
			closeConnection(conn);
		}

		return results;
	}

	/**
	 * 查询:无反射 注:返回List<Map<String,Object>>
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <T> List<T> selectList(String sql) throws SQLException {
		Connection conn = getConnection();
		List<T> results = null;
		try {
			QueryRunner qr = new QueryRunner();
			results = (List<T>) qr.query(conn, sql, new MapListHandler());
		} catch (SQLException e) {
			log(e);
			throw e;
		} finally {
			closeConnection(conn);
		}
		return results;
	}

	/**
	 * 查询:有反射 注:1.基础表专用 2.T为Integer,Long,String等基础数据类型时 clazz传null
	 * 
	 * @param sql
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <T> List<T> selectList(String sql, Class<T> clazz)
			throws SQLException {
		Connection conn = getConnection();
		List<T> results = null;
		try {

			QueryRunner qr = new QueryRunner();
			if (clazz == null) {
				results = qr.query(conn, sql, new ColumnListHandler<T>());
			} else {
				ResultSetHandler<List<T>> rsh = new BeanListHandler<T>(clazz);
				results = qr.query(conn, sql, rsh);
			}

		} catch (SQLException e) {
			log(e);
			throw e;
		} finally {
			closeConnection(conn);
		}

		return results;
	}

	/**
	 * 有反射 分页查询
	 * 
	 * @throws SQLException
	 */
	@Override
	public <T> List<T> selectList(String sql, Class<T> clazz, Page page)
			throws SQLException {
		StringBuffer sqlbuff = new StringBuffer(sql);
		sqlbuff.append(" LIMIT ").append(page.getIndex()).append(",")
				.append(page.getRows());
		return selectList(sqlbuff.toString(), clazz);
	}

	/**
	 * 查询:有反射 注:1.基础表专用 2.T为Integer,Long,String等基础数据类型时 clazz传null
	 * 
	 * @param sql
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
	@Override
	public <T> List<T> selectList(String sql, Class<T> clazz, Object... params)
			throws SQLException {
		Connection conn = getConnection();
		List<T> results = null;
		try {

			QueryRunner qr = new QueryRunner();
			if (clazz == null) {
				results = qr.query(conn, sql, new ColumnListHandler<T>(),
						params);
			} else {
				ResultSetHandler<List<T>> rsh = new BeanListHandler<T>(clazz);
				results = qr.query(conn, sql, rsh, params);
			}

		} catch (SQLException e) {
			log(e);
			throw e;
		} finally {
			closeConnection(conn);
		}

		return results;
	}

	@Override
	public <T> List<T> selectList2(String sql, Class<T> clazz, Object... params)
			throws Exception {
		Connection conn = getConnection();
		List<T> list = new ArrayList<T>();
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = conn.prepareStatement(sql);
			fillStatement(statement, params);
			rs = statement.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int col = metaData.getColumnCount();
			List<Field> fields = new ArrayList<Field>();
			Field field;
			String columnName;
			for (int i = 1; i <= col; i++) {
				columnName = metaData.getColumnLabel(i);
				field = clazz.getDeclaredField(columnName);
				if (field != null) {
					fields.add(field);
				}
			}
			while (rs.next()) {
				T obj = clazz.newInstance();
				for (int i = 0; i < fields.size(); i++) {
					field = fields.get(i);
					Object v = ConvertUtils.convert(
							rs.getString(field.getName()), field.getType());
					setFieldValue(obj, field, v);
				}
				list.add(obj);
			}
		} catch (Exception e) {
			log(e);
			throw e;
		} finally {
			closeConnection(conn);
			closeResultSet(rs);
			closeStatement(statement);
		}
		return list;
	}

	@Override
	public <T> List<T> selectAll(Class<T> clazz) throws SQLException {
		Connection conn = getConnection();
		List<T> results = null;
		String sql = getEntityParse().getSql(SqlType.SELECT_ALL, clazz);
		try {
			QueryRunner qr = new QueryRunner();
			ResultSetHandler<List<T>> rsh = new BeanListHandler<T>(clazz);
			results = qr.query(conn, sql, rsh);

		} catch (SQLException e) {
			log(e);
			throw e;
		} finally {
			closeConnection(conn);
		}

		return results;
	}

	@SuppressWarnings({ "rawtypes" })
	public static void setFieldValue(final Object object, final Field field,
			Object value) throws Exception {
		if (field != null) {
			field.setAccessible(true);
			try {
				if (value != null
						&& !field.getType().isAssignableFrom(value.getClass())) {
					if (field.getType().isAssignableFrom(Date.class)) {
						value = SqlDateUtil.parse(value.toString());
					} else if (field.getType().isEnum()) {
						Class<Enum> clazz = (Class<Enum>) field.getType();
						value = Enum.valueOf(clazz, value + "");
					} else {
						value = ConvertUtils.convert(value.toString(),
								field.getType());
					}
				}
				field.set(object, value);
			} catch (Exception e) {
				log(e);
				throw e;
			}
		}
	}

	/**
	 * 批量插入
	 * 
	 * @throws SQLException
	 */
	@Override
	public <T> void batchInsert(List<T> entityList) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement statement = null;
		String sql = getEntityParse().getSql(SqlType.INSERT,
				entityList.get(0).getClass());
		try {
			statement = conn
					.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			int n = 0;

			for (Iterator<?> iterator = entityList.iterator(); iterator
					.hasNext();) {
				Object object = iterator.next();
				List<Object> params = EntityParse.getInsterParams(object);
				for (int i = 0; i < params.size(); i++) {
					statement.setObject(i + 1, params.get(i));
				}
				statement.addBatch();
				n++;
				if (n % BATCH_INSERT_SIZE == 0 || n == entityList.size()) {
					statement.executeBatch();
				}
			}
		} catch (SQLException e) {
			log(e);
			throw e;
		} finally {
			closeConnection(conn);
			closeStatement(statement);
		}
	}

	/**
	 * 批量更新
	 * 
	 * @throws SQLException
	 */
	@Override
	public <T> void batchUpdate(List<T> entityList) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement statement = null;
		String sql = getEntityParse().getSql(SqlType.UPDATE,
				entityList.get(0).getClass());
		try {
			statement = conn
					.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			int n = 0;
			for (Iterator<?> iterator = entityList.iterator(); iterator
					.hasNext();) {
				Object object = iterator.next();
				List<Object> params = EntityParse.getUpdateParams(object);
				for (int i = 0; i < params.size(); i++) {
					statement.setObject(i + 1, params.get(i));
				}
				statement.addBatch();
				n++;
				if (n % BATCH_UPDATE_SIZE == 0 || n == entityList.size()) {
					statement.executeBatch();
				}
			}
		} catch (SQLException e) {
			log(e);
			throw e;
		} finally {
			closeConnection(conn);
			closeStatement(statement);
		}
	}

	/**
	 * 批量删除
	 * 
	 * @throws SQLException
	 */
	@Override
	public <T> void batchDelete(List<T> entityList) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement statement = null;
		String sql = getEntityParse().getSql(SqlType.DELETE,
				entityList.get(0).getClass());
		try {
			statement = conn
					.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			int n = 0;
			for (Iterator<?> iterator = entityList.iterator(); iterator
					.hasNext();) {
				Object object = iterator.next();
				List<Object> params = EntityParse.getDeleteParams(object);
				for (int i = 0; i < params.size(); i++) {
					statement.setObject(i + 1, params.get(i));
				}
				statement.addBatch();
				n++;
				if (n % BATCH_DELETE_SIZE == 0 || n == entityList.size()) {
					statement.executeBatch();
				}
			}
		} catch (SQLException e) {
			log(e);
			throw e;
		} finally {
			closeConnection(conn);
			closeStatement(statement);
		}
	}

	/**
	 * 设置prepareStatement的参数
	 * 
	 * @param pstm
	 * @param params
	 *            查询参数
	 * @throws SQLException
	 */
	private void fillStatement(PreparedStatement pstm, Object[] params)
			throws SQLException {
		if (params == null || params.length == 0)
			return;

		Object value;
		for (int i = 0, len = params.length; i < len; i++) {
			value = params[i];

			if (value instanceof String) {
				pstm.setString(i + 1, (String) value);
			} else if (value instanceof Integer) {
				pstm.setInt(i + 1, (Integer) value);

			} else if (value instanceof Long) {
				pstm.setLong(i + 1, (Long) value);

			} else if (value instanceof Double) {
				pstm.setDouble(i + 1, (Double) value);

			} else if (value instanceof Float) {
				pstm.setFloat(i + 1, (Float) value);

			} else if (value instanceof Short) {
				pstm.setShort(i + 1, (Short) value);

			} else if (value instanceof Byte) {
				pstm.setShort(i + 1, (Byte) value);

			} /*
			 * else if(converter != null){ pstm.setObject(i+1,
			 * converter.fieldToColumn(value));
			 * 
			 * }
			 */else {
				pstm.setObject(i + 1, value);
			}
		}
	}
	
	


}
