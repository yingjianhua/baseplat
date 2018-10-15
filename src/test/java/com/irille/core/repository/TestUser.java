package com.irille.core.repository;

import java.util.Locale;

import org.json.JSONException;
import org.junit.Before;

import com.irille.core.controller.JsonWriter;
import com.irille.core.repository.User.OptType;
import com.irille.core.repository.User.T;
import com.irille.core.repository.User.UserView;
import com.irille.core.repository.db.Transcation;

public class TestUser extends Query2 {

	@Before
	public void init() {
		User.table.getClass();
	}

	public void test() throws JSONException {
		Integer pkey = testIns();
		testLoad(pkey);
		testUpd(pkey);
		testView(pkey);
		testLoad(pkey);
		testDel(pkey);
		testLoad(pkey);
		// ConnectionManager.commitConnection();
	}
	public static void main(String[] args) {
		System.out.println(1);
	}

	@Transcation
	public Integer testIns() {
		User user = new User();
		user.setId(999);
		user.setNormalBean(1);
		user.setName("名字");
		user.stType(OptType.admin);
		user.setEnabled(true);
		user.setUsername("用户名");
		user.setPassword("密码");
		user.setBillAddr("zhangdandizhi");
		user.setEmail("a86291151@163.com");
		user.setProductName("{}");
		user.setIsValid(true);
		user.ins();
		return user.getPkey();
	}

	public void testView(Integer pkey) {
		JsonWriter.toConsole(
				select(
						T.NAME.as("name2"), 
						T.USERNAME.as("username2"), 
						T.ID.as("id2"))
				.FROM(User.class)
				.where(T.PKEY.eq(pkey))
				.orderBy(T.PKEY, T.ID.asc())
				.query(UserView.class)
				);
	}

	public void testLoad(Integer pkey) {
		// JsonWriter.toConsole(User.SELECT(User.class).queryMap());
		JsonWriter.toConsole(
				selectFrom(User.class).
				where(T.PKEY.eq(pkey)).
				query()
				);
	}

	public void testUpd(Integer pkey) throws JSONException {
		User user = selectFrom(User.class)
				.where(T.PKEY.eq(pkey))
				.query();
		user.setName("新名字");
		user.setProductName("red shoes", Locale.ENGLISH);
		user.setProductName("红蝎子", Locale.CHINESE);
		user.upd();
	}

	public void testDel(Integer pkey) {
		selectFrom(User.class, pkey)
		.del();
	}
}
