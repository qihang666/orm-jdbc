package com.commons.orm.api;

import java.util.Date;

import com.commons.orm.DruidPools;
import com.commons.orm.dao.BaseDaoTemplate;
import com.commons.orm.entity.User;

/**
 * @Description: orm test
 * @author hang
 * @date 2016-6-15 上午10:16:17
 * @version V1.7
 */
public class UserTest {
	public static void main(String[] args) {
		
		String url ="jdbc:mysql://127.0.0.1:8066/cat?useUnicode=true&characterEncoding=UTF8&autoReconnect=true";
		String user ="user";
		String password ="user";
		BaseDaoTemplate dao = new BaseDaoTemplate(new DruidPools(url,user,password));
		try {
//			List<User> ls = dao.selectList2("select * from user where id >?", User.class,14997);
//			System.err.println(ls.size());
			User u = null;
			for(int i=1;i<600001;i++){
				 u = new User(Long.valueOf(i), "name"+i, "pass"+i,new Date());
				 if(i%10000 == 0){
					 System.err.println("====num====="+i);
				 }
		        dao.insert(u);
			}
			System.err.println("end--------------");
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
