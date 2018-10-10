package com.commons.orm.sql;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * @Description: 自定义SQL注解
 * @author hang
 * @date 2016-5-12 下午5:41:43
 * @version V1.7
 */
public class SqlAnnotation {
	
	   /**
	    * 表名
	    * @author hang
	    *
	    */
	   @Target(value={java.lang.annotation.ElementType.TYPE})  
	   @Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)  
	   public @interface Table {  
	        String name() default "";  
	   }  
	   
	   /**
	    * 主键ID
	    * @author hang
	    *
	    */
	   @Target(value={java.lang.annotation.ElementType.FIELD})  
	   @Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)  
	   public @interface Id{  
		   String name() default "";
	   }  
	   
	   
	   /**
	    * 字段名字
	    * @author hang
	    *
	    */
	   @Target(value={java.lang.annotation.ElementType.FIELD})  
	   @Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)  
	   public @interface Column{  
		   String name() default "";
	   }  
	    
	   /**
	    * 过滤字段
	    * @author hang
	    *
	    */
//	   @java.lang.annotation.Target(value={java.lang.annotation.ElementType.FIELD})  
//	   @java.lang.annotation.Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)  
//	   public @interface Transient{  
//	   }  
}
