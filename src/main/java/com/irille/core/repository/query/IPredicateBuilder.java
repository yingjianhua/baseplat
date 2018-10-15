package com.irille.core.repository.query;

import java.io.Serializable;
import java.util.Collection;

import com.irille.core.repository.sql.EntitySQL;

/**
 * 用于产生Predicate
 * 
 * @author Jianhua Ying
 *
 */
public interface IPredicateBuilder {

	public Predicate as(String alias);

	public Predicate eq(Serializable param);

	public Predicate eq(EntitySQL sql);

	public Predicate ne(Serializable param);

	public Predicate ne(EntitySQL sql);

	public Predicate gt(Serializable param);

	public Predicate gt(EntitySQL sql);

	public Predicate ge(Serializable param);

	public Predicate ge(EntitySQL sql);

	public Predicate lt(Serializable param);

	public Predicate lt(EntitySQL sql);

	public Predicate le(Serializable param);

	public Predicate le(EntitySQL sql);

	public Predicate in(Serializable... params);

	public Predicate in(Collection<Serializable> params);

	public Predicate in(String params);

	public Predicate in(EntitySQL sql);

	public Predicate isNull();

	public Predicate notNull();

	public Predicate between(Serializable param, Serializable param2);

	public Predicate like(Serializable param);

	public Predicate like(EntitySQL sql);

	public Predicate desc();

	public Predicate asc();

	public Predicate params(Serializable... params);

	public Predicate value(Serializable value);

}
