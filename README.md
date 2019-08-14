# orm-jdbc
基于JDBC封装的ORM框架，解析实体生成insert，update，dalete，selectAll语句
<pre>
<code>
String url ="jdbc:mysql://127.0.0.1:3306/db_game;
String user ="root";
String password ="root";
BaseDaoTemplate dao = new BaseDaoTemplate(new DruidPools(url,user,password));
Age a = new Age();
a.setId(1);
a.setSid("id"+22);
a.setName("哈哈");
a.setPass("aa");
a.setC(122);
a.getBs().add("1");
a.getBs().add("2");
a.getBs().add("3");
a.getBs().add("4");
a.getAs().put("1", "1");
a.getAs().put("2", "2");
a.getAs().put("3", "3");
try {
		a = dao.insert(a);
} catch (SQLException e1) {
		e1.printStackTrace();
}
ry {
		TimeUnit.SECONDS.sleep(3);
} catch (InterruptedException e) {
		e.printStackTrace();
}
		
a.getBs().clear();
a.getBs().add("ccc");
try {
	dao.update(a);
} catch (SQLException e) {
	e.printStackTrace();
}
System.err.println(a);
</code>
</pre>
