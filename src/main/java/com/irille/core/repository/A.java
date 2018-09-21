package com.irille.core.repository;

import java.text.MessageFormat;

import org.slf4j.helpers.MessageFormatter;

public class A {
	
	static final String TAB = "  ";
	static final String LN = "\r\n";
	
	public static void main(String[] args) {
		String p = TAB+"public {0} {1}() '{'"+LN+TAB+TAB+"return {2};"+LN+TAB+"'}'"+LN+TAB+"public void {3}({0} {2}) '{'"+LN+TAB+TAB+"this.{2}={2};"+LN+TAB+"'}'"+LN;
		System.out.println(p);
		new MessageFormat(p);
	}
}
