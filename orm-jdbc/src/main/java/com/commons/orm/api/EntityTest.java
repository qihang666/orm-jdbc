package com.commons.orm.api;


/**
 * @Description: orm test
 * @author hang
 * @date 2016-6-15 涓婂崍10:16:17
 * @version V1.7
 */
public class EntityTest {
	public static void main(String[] args) {
/*
		String sql = EntityUtil.createInsert(Age.class);
		System.err.println(sql);
		String sql1 = EntityUtil.createUpdate(Age.class);
		System.err.println(sql1);
		String sql2 = EntityUtil.createDelete(Age.class);
		System.err.println(sql2);
		String sql3 = EntityUtil.createSelectALL(Age.class);
		System.err.println(sql3);
		String url ="jdbc:mysql://127.0.0.1:3306/dab_game?useUnicode=true&characterEncoding=UTF8&autoReconnect=true";
		String user ="root";
		String password ="root";
		BaseDaoTemplate dao = new BaseDaoTemplate(new DruidPools(url,user,password));
		
//		List<Age> ls = new ArrayList<Age>();
//		for(int i=0;i<30;i++){
//			Age a = new Age();
//			a.setId(""+i);
//			a.setName("鍝堝搱"+i);
//			a.setPass("passs"+i);
//			ls.add(a);
//		}
//		dao.batchInsert(ls);
//		List<Age> ls2 = dao.selectAll(Age.class);
//		for(Age a : ls2){
//			System.err.println(a);
//			a.setName("瀹�);
//		}
//		dao.batchUpdate(ls2);
		
//		try {
//			Age a = new Age();
//			a.setId("121");
//			a.setName("鍝堝搱");
//			a.setPass("passs");
//			
//			dao.save(a);
//
//			TimeUnit.SECONDS.sleep(3);
//
//			a.setName("123");
//			dao.update(a);
//			TimeUnit.SECONDS.sleep(3);
//			
//			dao.delete(a);

//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		List<Age> a = dao.selectList("select * from age where name=? and pass=?", Age.class, "鍝堝搱","22");
//		System.err.println(a);
//			Age a = new Age();
//			a.setId("111");
//			a.setName("鍝堝搱1");
//			a.setPass("passs");
//			dao.insert(a);
	
		int a = dao.insert("INSERT INTO age(id,name,pass)values(?,?,?)","4444","鍝堝搱","22");
		System.err.println(a);
		
		*/
	}
}
