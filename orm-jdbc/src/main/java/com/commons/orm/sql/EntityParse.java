package com.commons.orm.sql;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import com.commons.orm.sql.SqlAnnotation.Column;
import com.commons.orm.sql.SqlAnnotation.Id;
import com.commons.orm.sql.SqlAnnotation.Table;
import com.commons.util.ClassUtil;

/**
 * @Description: 实体解析类
 * @author hang
 * @date 2016-5-12 下午6:09:56
 * @version V1.7
 */
public class EntityParse {

	public String CLASS_PATH = "com";

	public enum SqlType {
		INSERT,//插入语句
		UPDATE,//修改语句
		DELETE,//删除语句
		SELECT_ALL,//查询所有语句
	}
	
	public Map<Class<?>,String> insertMap = new HashMap<Class<?>, String>();
	public Map<Class<?>,String> updateMap = new HashMap<Class<?>, String>();
	public Map<Class<?>,String> deleteMap = new HashMap<Class<?>, String>();
	public Map<Class<?>,String> selectAllMap = new HashMap<Class<?>, String>();

	public List<Class<?>> entityClass = new ArrayList<Class<?>>();

	public EntityParse() {
		registerEntity();
		initSql();
	}
	
	public static void main(String[] args) {
		new EntityParse();
	}
	
	public  void registerEntity() {
		Set<Class<?>> classes = ClassUtil.getAllClass(CLASS_PATH);
		for (Class<?> clas : classes) {
			if (clas.getAnnotation(Table.class) != null) {
				entityClass.add(clas);
			}
		}
	}


	public void initSql() {
		for (Class<?> clz : entityClass) {
			parse(clz);
		}
	}

	/**
	 * 解析
	 * 
	 * @param clz
	 */
	void parse(Class<?> clz) {
		insertMap.put(clz, createInsert(clz));
		updateMap.put(clz, createUpdate(clz));
		deleteMap.put(clz, createDelete(clz));
		selectAllMap.put(clz, createSelectALL(clz));
	}

	public String getSql(SqlType type, Class<?> clz) {
		String sql = null;
		switch (type) {
		case INSERT:  
			sql = insertMap.get(clz);
			break;
		case UPDATE:
			sql = updateMap.get(clz);
			break;
		case DELETE:
			sql = deleteMap.get(clz);
			break;
		case SELECT_ALL:
			sql = selectAllMap.get(clz);
			break;
		default:
			break;
		}
		return sql;
	}

	/**
	 * 创建删除
	 * 
	 * @param clz
	 * @return
	 */
	public static String createDelete(Class<?> clz) {
		String tableName = getTableName(clz);
		List<Field> fields = getFields(clz);
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("DELETE FROM `").append(tableName).append("` WHERE ");
		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers())) {
				Id id = field.getAnnotation(Id.class);
				if (null != id) {
					String idName = null;
					if(id.name().equals("")){
						idName = field.getName();
					}else{
						idName = id.name();
					}
					sqlBuffer.append("`").append(idName).append("`").append("=?");
				}

			}
		}
		return sqlBuffer.toString();
	}

	/**
	 * 创建更新新语句
	 * 
	 * @param clz
	 * @return
	 */
	public static String createUpdate(Class<?> clz) {
		String tableName = getTableName(clz);
		List<Field> fields = getFields(clz);
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("UPDATE ").append(tableName).append(" SET ");
		String idName = null;
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			if (!Modifier.isStatic(field.getModifiers())) {
				Id id = field.getAnnotation(Id.class);
				Column column = field.getAnnotation(Column.class);
				if(id == null && column == null){
					continue;
				}
				if (id == null) {
					String name = null;
					if(column == null || column.name().equals("")){
						name = field.getName();
					}else{
						name = column.name();
					}
					sqlBuffer.append("`").append(name).append("`").append("=?");
					sqlBuffer.append(",");
				} else {
					if(id.name().equals("")){
						idName = field.getName();
					}else{
						idName = id.name();
					}
				}
			}
		}

		if (idName == null) {
			throw new RuntimeException("not found of " + clz + "'s ID");
		}
		int length = sqlBuffer.length();
		sqlBuffer.delete(length - 1, length);
		sqlBuffer.append(" WHERE ").append("`").append(idName).append("`").append("=?");

		return sqlBuffer.toString();
	}

	/**
	 * 创建插入语句
	 * 
	 * @param clz
	 * @return
	 */
	public static String createInsert(Class<?> clz) {
		String tableName = getTableName(clz);
		List<Field> fields = getFields(clz);
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("INSERT INTO ").append(tableName).append("(");
		String fieldName = null;
		int fielNum = 0;
		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers())) {
				Id id = field.getAnnotation(Id.class);
				Column column = field.getAnnotation(Column.class);
				if(id == null && column == null){
					continue;
				}
				if(id != null && !id.name().equals("")){
					fieldName  =id.name();
				}else if(column!=null && !column.name().equals("")){
					fieldName  = column.name();
				}
				sqlBuffer.append("`").append(fieldName).append("`,");
				fielNum++;
			}
		}
		int length = sqlBuffer.length();
		sqlBuffer.delete(length - 1, length).append(")values(");
		for (int i = 0; i < fielNum; i++) {
			if (i != 0) {
				sqlBuffer.append(",");
			}
			sqlBuffer.append("?");
		}
		sqlBuffer.append(")");

		return sqlBuffer.toString();
	}

	/**
	 * 创建查询所有语句
	 * @param clz
	 * @return
	 */
	public static String createSelectALL(Class<?> clz) {
		String tableName = getTableName(clz);
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("SELECT * FROM ").append("`").append(tableName).append("`");
		return sqlBuffer.toString();
	}
	/**
	 * 获取insert参数
	 * 
	 * @param target
	 * @return
	 */
	public static List<Object> getInsterParams(Object target) {
		List<Object> params = new ArrayList<Object>();
		List<Field> fields = getFields(target.getClass());
		Field field = null;
		try {
			for (int i = 0; i < fields.size(); i++) {
				field = fields.get(i);
				if (!Modifier.isStatic(field.getModifiers())) {
					Object param = FieldUtils.readField(field, target, true);
					if(param == null){
						if(field.getType() == Integer.class){
							param = 0;
						}
					}
					params.add(param);
				}
			}
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		return params;
	}

	/**
	 * 获取update参数
	 * 
	 * @param target
	 * @return
	 */
	public static List<Object> getUpdateParams(Object target) {
		List<Object> params = new ArrayList<Object>();
		List<Field> fields = getFields(target.getClass());
		Field field = null;
		Object idParam = null;
		try {
			Object param = null;
			for (int i = 0; i < fields.size(); i++) {
				field = fields.get(i);

				if (!Modifier.isStatic(field.getModifiers())) {
					Id id = field.getAnnotation(Id.class);
					param = FieldUtils.readField(field, target, true);
					if (id != null) {
						idParam = param;
						continue;
					}
					if(param == null){
						if(field.getType() == Integer.class){
							param = 0;
						}
					}
					params.add(param);
				}

			}
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		params.add(idParam);
		return params;
	}

	/**
	 * 获取Delete参数
	 * 
	 * @param target
	 * @return
	 */
	public static List<Object> getDeleteParams(Object target) {
		List<Object> params = new ArrayList<Object>();
		List<Field> fields = getFields(target.getClass());
		Field field = null;
		try {
			for (int i = 0; i < fields.size(); i++) {
				field = fields.get(i);
				if (!Modifier.isStatic(field.getModifiers())) {
					Object param = FieldUtils.readField(field, target, true);
					Id id = field.getAnnotation(Id.class);
					if (id != null) {
						params.add(param);
						break;
					}
				}
			}
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		return params;
	}

	/**
	 * 获取字段
	 * 
	 * @param clazz
	 * @return
	 */
	public static List<Field> getFields(Class<?> clazz) {
		if (Object.class.equals(clazz)) {
			return null;
		}
		Field[] fieldArray = clazz.getDeclaredFields();
		List<Field> fields = new ArrayList<Field>();
		for (Field file : fieldArray) {
			if (null == file.getAnnotation(Column.class) && null == file.getAnnotation(Id.class)) {
				//过滤字段
				continue;
			}
			fields.add(file);
		}
		return fields;
	}

	/**
	 * 根据注解获取表名
	 * */
	private static String getTableName(Class<?> clazz) {
		return getTableNameForClass(clazz);
	}

	protected static Object readField(Object target, Field field) {
		try {
			return FieldUtils.readField(field, target, true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据注解取表明
	 * 
	 * @param clazz
	 * @return
	 */
	private static String getTableNameForClass(Class<?> clazz) {
		String tableName;
		Table table = clazz.getAnnotation(Table.class);
		if (null != table) {
			tableName = table.name();
			if ("".equalsIgnoreCase(tableName)) {
				tableName = clazz.getSimpleName();
			}
		} else {
			tableName = clazz.getSimpleName();
		}
		return tableName;
	}

	@SuppressWarnings("unchecked")
	public static void setIdValue(Object target, Object value) {
		List<Field> fields = getFields(target.getClass());
		Field field = null;
		try {
			for (int i = 0; i < fields.size(); i++) {
				field = fields.get(i);
				if (!Modifier.isStatic(field.getModifiers())) {
					Id id = field.getAnnotation(Id.class);
					if (id != null
							&& value != null
							&& !field.getType().isAssignableFrom(
									value.getClass())) {
						if (field.getType().isAssignableFrom(Date.class)) {
							value = SqlDateUtil.parse(value.toString());
						} else if (field.getType().isEnum()) {
							@SuppressWarnings("rawtypes")
							Class<Enum> clazz = (Class<Enum>) field.getType();
							value = Enum.valueOf(clazz, value + "");
						} else {
							value = ConvertUtils.convert(value.toString(),
									field.getType());
						}
					}
					field.setAccessible(true);
					field.set(target, value);
					break;
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}