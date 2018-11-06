package com.irille.core.repository;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import com.irille.core.repository.orm.Column;
import com.irille.core.repository.orm.Entity;
import com.irille.core.repository.orm.IColumnField;
import com.irille.core.repository.orm.Table;
import com.irille.core.repository.query.EntityQuery;

import irille.pub.Log;
import irille.pub.util.GenericsUtils;

public class EntityRepository<T extends Entity> extends Query2 {
	
	private static final Log LOG = new Log(EntityRepository.class);
	
	private Class<T> beanClass;

	@SuppressWarnings("unchecked")
	public EntityRepository() {
			beanClass = (Class<T>)GenericsUtils.getSuperClassGenricType(getClass());
	}
	public Class<T> beanClass() {
		return beanClass;
	}
	
	public static <R extends Entity> R saveWith(R entity, IColumnField... fields) {
		Class<? extends Entity> entityClass = entity.getClass();
		Table<?> table = Entity.table(entityClass);
		EntityQuery<?> q = insert(entity.getClass());
		for (IColumnField field : fields) {
			if (field.column().isPrimary())
				continue;
			try {
				q.values(field.params((Serializable) field.column().getterMethod().invoke(entity)));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw LOG.err(e, "setTo", "对象【{0}】赋值到数据库记录时出错!", entityClass);
			}
		}
		Serializable key = q.executeUpdateReturnGeneratedKey(table.primaryKey().type());
		try {
			table.primaryKey().setterMethod().invoke(entity, key);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw LOG.err(e, "setTo", "对象【{0}】赋值到数据库记录时出错!", entityClass);
		}
		return entity;
	}
	
	public static <R extends Entity> R save(R entity) {
		return saveWith(entity, Entity.fields(entity.getClass()));
	}
	
	public static <R extends Entity> R updateWith(R entity, IColumnField... fields) {
		Class<? extends Entity> entityClass = entity.getClass();
		EntityQuery<?> q = update(entityClass);
		try {
			IColumnField primaryField = null;
			for (IColumnField field : fields) {
				if (field.column().isPrimary()) {
					primaryField = field;
				} else {
					q.set(field, (Serializable) field.column().getterMethod().invoke(entity));
				}
			}
			q.where(primaryField, "=?", (Serializable) primaryField.column().getterMethod().invoke(entity));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw LOG.err(e, "getmetod", "取对象【{0}】字段时出错!", entityClass);
		}
		q.execute();
		return entity;
	}
	
	public static <R extends Entity> R update(R entity) {
		return updateWith(entity, Entity.fields(entity.getClass()));
	}

	public static <R extends Entity> void delete(R entity) {
		Class<? extends Entity> entityClass = entity.getClass();
		Table<?> table = Entity.table(entityClass);
		Column column = table.primaryKey();
		Serializable primaryKeyValue;
		try {
			primaryKeyValue = (Serializable) column.getterMethod().invoke(entity);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw LOG.err(e, "setTo", "对象【{0}】赋值到数据库记录时出错!", entityClass);
		}
		int row = delete(entityClass).where(column.field(), "=?", primaryKeyValue).execute();
		if (row == 0)
			throw LOG.err("deleteNotFound", "删除表【{0}】主键为【{1}】的记录不存在!", table.name(), primaryKeyValue);
	}
}
