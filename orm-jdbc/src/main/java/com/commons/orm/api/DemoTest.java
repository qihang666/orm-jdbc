package com.commons.orm.api;

import java.util.ArrayList;
import java.util.List;

import com.commons.orm.DruidPools;
import com.commons.orm.dao.BaseDaoTemplate;
import com.commons.orm.entity.Age;

/**
 * @Description: orm test
 * @author hang
 * @date 2016-6-15 上午10:16:17
 * @version V1.7
 */
public class DemoTest {
	public static void main(String[] args) {
		List<Age> ls = new ArrayList<Age>();
		String url ="jdbc:mysql://127.0.0.1:3306/db_game?useUnicode=true&characterEncoding=UTF8&autoReconnect=true";
		String user ="root";
		String password ="root";
		BaseDaoTemplate dao = new BaseDaoTemplate(new DruidPools(url,user,password));
		try {
			long st = System.currentTimeMillis();
			Age u = null;
			for (int i = 0; i < 100; i++) {
				u = new Age();
				u.setId(i);
				u.setSid("id"+i);
				u.setName("name" + i);
				u.setPass("pass" + i);
				ls.add(u);
			}
			dao.batchInsert(ls);
			long end = System.currentTimeMillis() - st;
			System.err.println("耗时 :" + end);

			System.err.println("------------------------");
			List<Age> rs = dao.selectAll(Age.class);
			
			for (Age us : rs) {
				System.err.println(us);
				us.setName("4655448");
			}
			dao.batchUpdate(rs);
			
			List<Age> rs2 = dao.selectList("select * from age",Age.class);
			
			for (Age us : rs2) {
				System.err.println(us);
			}
//			dao.batchDelete(rs2);
			// int i = SQLDao.executeUpdate(conn, u.updateSql,
			// u.getUpdateParams());
			// List<User> ls = SQLDao.query(conn, "SELECT * FROM user",
			// User.classs, null);
			// User i = BaseDao.getInstance().save(u);
			// boolean f = BaseDao.getInstance().delete(u);

			// System.err.println(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
