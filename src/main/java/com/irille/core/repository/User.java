package com.irille.core.repository;

import org.json.JSONObject;

import com.irille.core.repository.orm.Column;
import com.irille.core.repository.orm.ColumnBuilder;
import com.irille.core.repository.orm.ColumnTemplate;
import com.irille.core.repository.orm.Entity;
import com.irille.core.repository.orm.IColumnField;
import com.irille.core.repository.orm.IColumnTemplate;
import com.irille.core.repository.orm.Table;
import com.irille.core.repository.orm.TableFactory;

public class User extends Entity {

	public static final Table<User> table = TableFactory.entity(User.class).column(field.values()).index(true, field.ID).index(false, field.ID, field.NAME).create();

	enum OptType {
		admin,anonymous,normal
		
	}
	public enum field implements IColumnField {
		ID(ColumnTemplate.INT__11.length(11)),
		NAME(ColumnTemplate.STR__200.nullable(true).showName("名字")),
		USERNAME(ColumnTemplate.STR__200.showName("用户名")),
		PASSWORD(ColumnTemplate.STR__200.nullable(true).showName("密码")),
		BILL_ADDR(ColumnTemplate.STR__200.nullable(true)),
		EMAIL(ColumnTemplate.EMAIL),
		PRODUCT_NAME(ColumnTemplate.I18N)
		;
		private Column column;

		field(IColumnTemplate template) {
			this.column = template.builder().create(this);
		}

		field(ColumnBuilder builder) {
			this.column = builder.create(this);
		}

		@Override
		public Column column() {
			return column;
		}
	}
	// >>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>

	// 实例变量定义-----------------------------------------
	private Integer id; // id INT(11)<>
	private String name; // 名字 STR(200)<null>
	private String username; // 用户名 STR(200)<>
	private String password; // 密码 STR(200)<null>
	private String billAddr; // billAddr STR(200)<null>
	private String email; // 邮箱地址 STR(100)<null>
	private JSONObject productName; // productName JSONOBJECT(0)<>

	@Override
	public User init() {
		super.init();
		id = null; // id INT(11)
		name = null; // 名字 STR(200)
		username = null; // 用户名 STR(200)
		password = null; // 密码 STR(200)
		billAddr = null; // billAddr STR(200)
		email = null; // 邮箱地址 STR(100)
		productName = null; // productName JSONOBJECT(0)
		return this;
	}

	// 方法------------------------------------------------
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getBillAddr() {
		return billAddr;
	}
	public void setBillAddr(String billAddr) {
		this.billAddr = billAddr;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public JSONObject getProductName() {
		return productName;
	}
	public void setProductName(JSONObject productName) {
		this.productName = productName;
	}

	// <<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
