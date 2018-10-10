package com.commons.orm.api;

import java.util.List;

import com.commons.orm.DruidPools;
import com.commons.orm.dao.BaseDaoTemplate;
import com.commons.orm.entity.Age;
import com.commons.orm.model.Page;

/**
 * @Description: orm test
 * @author hang
 * @date 2016-6-15 上午10:16:17
 * @version V1.7
 */
public class PageTest {
	public static void main(String[] args) {
//		List<Age> ls = new ArrayList<Age>();
		String url ="jdbc:mysql://127.0.0.1:3306/db_game?useUnicode=true&characterEncoding=UTF8&autoReconnect=true";
		String user ="root";
		String password ="root";
		BaseDaoTemplate dao = new BaseDaoTemplate(new DruidPools(url,user,password));
		try {
//			long st = System.currentTimeMillis();
//			Age u = null;
//			for (int i = 1; i < 100; i++) {
//				u = new Age();
//				u.setId(i);
//				u.setSid("id"+i);
//				u.setName("name" + i);
//				u.setPass("pass" + i);
//				ls.add(u);
//			}
//			dao.batchInsert(ls);
//			long end = System.currentTimeMillis() - st;
//			System.err.println("耗时 :" + end);

			List<Age> rs = dao.selectList("select * from age",Age.class, new Page(2, 10));
			for(Age a : rs){
                System.err.println(a);
			}
			System.err.println(rs.size());
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
