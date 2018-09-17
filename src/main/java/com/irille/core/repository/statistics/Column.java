package com.irille.core.repository.statistics;


public class Column {

	private String name;
	private Integer width;
	private ALIGN align = ALIGN.auto;
	
	public String name() {
		return name;
	}
	public void name(String name) {
		this.name = name;
	}
	public Integer width() {
		return width;
	}
	public void width(Integer width) {
		this.width = width;
	}
	public ALIGN align() {
		return align;
	}
	public void align(ALIGN align) {
		this.align = align;
	}
	public void printLabel2Console() {
		this.align.print2Console(this.name, this);
	}
	public void printValue2Console(Object value) {
		this.align.print2Console(value, this);
	}
	public void printSplitLine2Console() {
		StringBuilder b = new StringBuilder();
		for(int i=0;i<width;i++) {
			b.append("-");
		}
		this.align.print2Console(b.toString(), this);
	}
	public StringBuilder printLabel2String() {
		return this.align.print2String(this.name, this);
	}
	public StringBuilder printValue2String(Object value) {
		return this.align.print2String(value, this);
	}
	public StringBuilder printSplitLine2String() {
		StringBuilder b = new StringBuilder();
		for(int i=0;i<width;i++) {
			b.append("-");
		}
		return this.align.print2String(b.toString(), this);
	}
}
