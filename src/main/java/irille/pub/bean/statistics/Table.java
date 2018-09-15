package irille.pub.bean.statistics;

import irille.pub.bean.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
	private List<Map<String,Object>> data;
	private List<Column> columns;
	private Map<String, Column> mapColumns;

	public Table(List<Map<String,Object>> data) {
		this.data = data;
		this.initColumns(this.data);
	}
	public Table(String sql) {
		this.data = Query.sql(sql).queryMaps();
		this.initColumns(this.data);
	}
	
	public Table column(String key, ALIGN align) {
		this.mapColumns.get(key).align(align);
		return this;
	}
	private List<Column> initColumns(List<Map<String,Object>> result) {
		this.columns = new ArrayList<>();
		this.mapColumns = new HashMap<>();
		result.get(0).keySet().forEach(column-> {
			Column c = new Column(){{
				name(column);
				width(column.length());
			}};
			mapColumns.put(c.name(), c);
			columns.add(c);
		});
		result.stream().forEach(map->{
			columns.forEach(column->{
				int length = map.get(column.name()).toString().length();
				if(column.width()<length)
					column.width(length);
			});
		});
		return columns;
	}
	public void print() {
		System.out.print(getHead());
		System.out.print(getBody());
	}
	public String toString() {
		return getHead().append(getBody()).toString();
	}
	
	private StringBuilder getHead() {
		StringBuilder b = new StringBuilder();
		for(Column column: columns) {
			b.append("|");
			b.append(column.printLabel2String());
		}
		b.append("|\r\n");
		for(Column column: columns) {
			b.append("|");
			b.append(column.printSplitLine2String());
		}
		return b.append("|\r\n");
	}
	private StringBuilder getBody() {
		StringBuilder b = new StringBuilder();
		data.stream().forEach(map->{
			columns.forEach(column->{
				b.append("|");
				b.append(column.printValue2String(map.get(column.name())));
			});
			b.append("|\r\n");
		});
		return b;
	}
}
