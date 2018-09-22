package com.irille.core.repository;

import com.irille.core.repository.orm.Column;
import com.irille.core.repository.orm.ColumnBuilder;
import com.irille.core.repository.orm.ColumnTemplate;
import com.irille.core.repository.orm.Entity;
import com.irille.core.repository.orm.IColumnField;
import com.irille.core.repository.orm.IColumnTemplate;
import com.irille.core.repository.orm.Table;
import com.irille.core.repository.orm.TableFactory;

public class NormalBean extends Entity {
	
	public static final Table<NormalBean> table = TableFactory.entity(NormalBean.class).column(field.values()).create();
	
	public enum field implements IColumnField {
		PKEY(ColumnTemplate.PKEY),
		ID(ColumnTemplate.INT__11.length(20)),
		NAME(ColumnTemplate.STR__200.columnName("user_name").nullable(true))
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
	private Integer pkey; // pkey INT(11)
	private Integer id; // id INT(20)
	private String name; // name VARCHAR(200)<null>

	@Override
	public NormalBean init() {
		super.init();
		id = null; // id INT(20)
		name = null; // name VARCHAR(200)
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	// <<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
