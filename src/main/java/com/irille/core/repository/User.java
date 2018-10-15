package com.irille.core.repository;


import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.irille.core.repository.orm.Column;
import com.irille.core.repository.orm.ColumnBuilder;
import com.irille.core.repository.orm.ColumnFactory;
import com.irille.core.repository.orm.ColumnTemplate;
import com.irille.core.repository.orm.ColumnTypes;
import com.irille.core.repository.orm.Entity;
import com.irille.core.repository.orm.IColumnField;
import com.irille.core.repository.orm.IColumnTemplate;
import com.irille.core.repository.orm.Table;
import com.irille.core.repository.orm.TableFactory;

import irille.pub.tb.EnumLine;
import irille.pub.tb.IEnumOpt;
import irille.view.BaseView;

public class User extends Entity {
	
	public static void main(String[] args) {
		System.out.println(User.class.getName());
	}
	
	static public class UserView implements BaseView {
		private String name2;
		private String username2;
		private Integer id2;
		public String getName2() {
			return name2;
		}
		public void setName2(String name2) {
			this.name2 = name2;
		}
		public String getUsername2() {
			return username2;
		}
		public void setUsername2(String username2) {
			this.username2 = username2;
		}
		public Integer getId2() {
			return id2;
		}
		public void setId2(Integer id2) {
			this.id2 = id2;
		}
	}

	public static final Table<User> table = TableFactory.entity(User.class).column(T.values()).index(true, T.ID).index(false, T.ID, T.NAME).create();

	public enum OptType implements IEnumOpt{
		admin(0, "管理员"),
		anonymous(1, "匿名用户"),
		normal(2, "普通用户")
		;
		private EnumLine line;
		private OptType(int key, String name) {line=new EnumLine(this,key,name);}
		@Override
		public EnumLine getLine() {
			return line;
		}
		
	}
	public enum T implements IColumnField {
		PKEY(ColumnTemplate.PKEY),
		ID(ColumnTemplate.INT__11),
		NORMAL_BEAN(ColumnFactory.manyToOne(NormalBean.class)),
		NAME(ColumnTemplate.STR__200.nullable(true).showName("名字").defaultValue("")),
		TYPE(ColumnFactory.opt(OptType.anonymous).showName("用户类型")),
		ENABLED(ColumnFactory.type(ColumnTypes.BOOLEAN).showName("是否启用")),
		USERNAME(ColumnTemplate.STR__200.showName("用户名")),
		PASSWORD(ColumnTemplate.STR__200.nullable(true).showName("密码")),
		BILL_ADDR(ColumnTemplate.STR__200.nullable(true)),
		EMAIL(ColumnTemplate.EMAIL),
		PRODUCT_NAME(ColumnTemplate.I18N),
		IS_VALID(ColumnTemplate.BOOLEAN.defaultValue(true).showName("是否合法"))
		;
		private Column column;

		T(IColumnTemplate template) {
			this.column = template.builder().create(this);
		}

		T(ColumnBuilder builder) {
			this.column = builder.create(this);
		}

		@Override
		public Column column() {
			return column;
		}
	}
	// >>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>

	// 实例变量定义-----------------------------------------
	private Integer pkey; // pkey INT(11)
	private Integer id; // id INT(11)
	private Integer normalBean; // normalBean<表主键:NormalBean> INT(11)
	private String name; // 名字 VARCHAR(200)<null>
	private Byte type; // 用户类型<OptType> TINYINT(4)
	// admin:0,管理员
	// anonymous:1,匿名用户
	// normal:2,普通用户
	private Boolean enabled; // 是否启用 TINYINT(1)
	private String username; // 用户名 VARCHAR(200)
	private String password; // 密码 VARCHAR(200)<null>
	private String billAddr; // billAddr VARCHAR(200)<null>
	private String email; // 邮件 VARCHAR(50)<null>
	private String productName; // productName JSON(0)
	private Boolean isValid; // 是否合法 TINYINT(1)

	@Override
	public User init() {
		super.init();
		id = null; // id INT(11)
		normalBean = null; // normalBean INT(11)
		name = ""; // 名字 VARCHAR(200)
		type = OptType.anonymous.getLine().getKey(); // 用户类型<OptType> TINYINT(4)
		enabled = null; // 是否启用 TINYINT(1)
		username = null; // 用户名 VARCHAR(200)
		password = null; // 密码 VARCHAR(200)
		billAddr = null; // billAddr VARCHAR(200)
		email = null; // 邮件 VARCHAR(50)
		productName = null; // productName JSON(0)
		isValid = true; // 是否合法 TINYINT(1)
		return this;
	}

	// 方法------------------------------------------------
	public Integer getPkey() {
		return pkey;
	}
	public void setPkey(Integer pkey) {
		this.pkey = pkey;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getNormalBean() {
		return normalBean;
	}
	public void setNormalBean(Integer normalBean) {
		this.normalBean = normalBean;
	}
	public NormalBean gtNormalBean() {
		return selectFrom(NormalBean.class, getNormalBean());
	}
	public void stNormalBean(NormalBean normalBean) {
		this.normalBean = normalBean.getPkey();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public OptType gtType() {
		return (OptType)(OptType.anonymous.getLine().get(type));
	}
	public void stType(OptType type) {
		this.type = type.getLine().getKey();
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
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
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductName(Locale locale) throws JSONException {
		return gtProductName().has(locale.getLanguage())?gtProductName().getString(locale.getLanguage()):"";
	}
	public void setProductName(String productName, Locale locale) throws JSONException {
		stProductName(gtProductName().put(locale.getLanguage(), productName));
	}
	public JSONObject gtProductName() throws JSONException {
		return (getProductName()==null?new JSONObject():new JSONObject(getProductName()));
	}
	public void stProductName(JSONObject productName) {
		this.setProductName(productName==null?null:productName.toString());
	}
	public Boolean getIsValid() {
		return isValid;
	}
	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	// <<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
