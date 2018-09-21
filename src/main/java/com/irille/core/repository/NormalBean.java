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

	private Integer id;
	private String name;
	
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
	
}
