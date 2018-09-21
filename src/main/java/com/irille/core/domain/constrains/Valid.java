package com.irille.core.domain.constrains;

import java.util.function.BiConsumer;

import com.irille.core.web.exception.FieldValidateException;

import irille.pub.Log;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.Bean;
import irille.pub.tb.IEnumFld;

public class Valid extends BaseValid{
	
	private static final Log LOG = new Log(Valid.class);
	
	public enum Message implements IMsg {
		notNull("{0}不能为空"),
        ;
        private String _msg;
        private Message(String msg) {_msg = msg;}
        public String getMsg() {return _msg;}
    } 
	
	private IEnumFld[] validFlds = null;
	
	public Valid valid(IEnumFld... flds) {
		validFlds = flds;
		return this;
	}
	
	public static Valid bean(Bean<?, ?> bean) {
		return new Valid(bean);
	}

	private Valid(Bean<?, ?> bean) {
		super(bean);
	}
	
	public void assertFalse(IEnumFld... flds) {
		apply((name,value)->{
			
		}, flds);
	}
	
	public void assertTrue() {
		
	}
	public void DecimalMax() {
		
	}
	public void DecimalMin() {
		
	}
	public void Digits() {
		
	}
	public void Email() {
		
	}
	public void Future() {
		
	}
	public void FutureOrPresent() {
		
	}
	public void Max() {
		
	}
	public void Min() {
		
	}
	public void Negative() {
		
	}
	public void NegativeOrZero() {
		
	}
	public void Null() {
		
	}
	public void Past() {
		
	}
	public void PastOrPresent() {
		
	}
	public void Pattern() {
		
	}
	public void Positive() {
		
	}
	public void PositiveOrZero() {
		
	}
	public void Size() {
		
	}
	
	/**
	 * 不能为空
	 */
	public void notNull(IEnumFld... flds) {
		apply((value, name)-> {
			if (value == null)
				throw new FieldValidateException("{0}不能为空", name);
		}, flds);
	}
	/**
	 * 不能为空并且长度要大于0 
	 */
	public void notEmpty(IEnumFld... flds) {
		apply((value, name)-> {
			if(value == null || (value instanceof String && ((String)value).length() == 0))
				throw LOG.err(Message.notNull, name);
		}, flds);
	}
	/**
	 * 不能为空并且trim之后的长度要大于0 
	 */
	public void notBlank(IEnumFld... flds) {
		apply((value, name)-> {
			if(value == null || (value instanceof String && ((String)value).trim().length() == 0))
				throw LOG.err(Message.notNull, name);
		}, flds);
	}
	
	private void apply(BiConsumer<Object, String> consumer, IEnumFld... flds) {
		if (flds != null && flds.length > 0)
			validFlds = flds;
		for (IEnumFld fld : validFlds)
			consumer.accept(getValue(fld), getName(fld));
	}
}
