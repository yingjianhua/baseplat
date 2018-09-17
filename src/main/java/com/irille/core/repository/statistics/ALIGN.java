package com.irille.core.repository.statistics;

import java.util.function.BiFunction;

public enum ALIGN {
	left((value, column)->{
		StringBuilder b = new StringBuilder();
		int length = value.toString().length(); 
		b.append(value);
		for(int i=0;i<column.width()-length;i++) {
			b.append(" ");
		}
		return b;
	}),
	center((value, column)->{
		StringBuilder b = new StringBuilder();
		int length = value.toString().length(); 
		int front = (column.width()-length)/2;
		int behind = column.width()-length-front;
		for(int i=0;i<front;i++) {
			b.append(" ");
		}
		b.append(value.toString());
		for(int i=0;i<behind;i++) {
			b.append(" ");
		}
		return b;
	}),
	right((value, column)->{
		StringBuilder b = new StringBuilder();
		int length = value.toString().length(); 
		for(int i=0;i<column.width()-length;i++) {
			b.append(" ");
		}
		b.append(value);
		return b;
	}),
	auto((value, column)->{
		StringBuilder b = new StringBuilder();
		int length = value.toString().length(); 
		if(value instanceof Number) {
			for(int i=0;i<column.width()-length;i++) {
				b.append(" ");
			}
			b.append(value);
		} else {
			b.append(value);
			for(int i=0;i<column.width()-length;i++) {
				b.append(" ");
			}
		}
		return b;
	});
	private BiFunction<Object, Column, StringBuilder> print;
	private ALIGN(BiFunction<Object, Column, StringBuilder> print) {
		this.print = print;
	}
	public void print2Console(Object v, Column c) {
		System.out.print(this.print2String(v, c));;
	}
	public StringBuilder print2String(Object v, Column c) {
		return this.print.apply(v, c);
	}
}