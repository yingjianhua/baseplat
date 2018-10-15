package com.irille.core.repository.query;

import java.io.Serializable;

/**
 * 用于产生Predicate
 * 
 * @author Jianhua Ying
 *
 */
public interface IPredicateBuilder {

	public Predicate as(String alias);

	public Predicate eq(Serializable param);

	public Predicate ne(Serializable param);

	public Predicate gt(Serializable param);

	public Predicate ge(Serializable param);

	public Predicate lt(Serializable param);

	public Predicate le(Serializable param);

	public Predicate isNull();

	public Predicate notNull();

	public Predicate between(Serializable param, Serializable param2);

	public Predicate like(Serializable param);
	
	public Predicate desc();
	
	public Predicate asc();
	
	public Predicate params(Serializable... params);
	
	public Predicate value(Serializable value);
	
}
