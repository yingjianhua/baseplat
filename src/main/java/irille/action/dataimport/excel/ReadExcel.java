package irille.action.dataimport.excel;

import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
* @ClassName: TestReadExcel 
* @Description: 解析Excel测试类
* @author zhangwanxi
* @date 2013-7-22 上午10:59:36 
*
 */
public class ReadExcel extends ExcelProcessor {
	
	/**
	 * Integer:行号
	 * XRow ：行item
	 */
	public static  Map<Integer , XRow> xrowMap;
	/**
	 * 
	* <p>Title: </p>
	* <p>Description:构造方法</p>
	* @param fileName
	* @throws Exception
	 */
	public ReadExcel(String fileName) throws Exception {
		super(fileName);
	}

	@Override
	public void processByRow(int sheetIndex) throws Exception {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 
	 */
	@Override
	public void processRow(XRow row) {
		/*for (int i = 0; i < row.getCellsSize(); i++) {
			System.out.print("[" + row.getRowIndex() + ","
					+ (char) row.getCell(i).getColumnIndex() + ","
					+ row.getCell(i).getValue() + "]");
		}
		System.out.println();*/
		
		xrowMap.put(row.getRowIndex(), row);
	}
	/**
	 * 
	* @Title: main 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param args
	* @param @throws Exception    设定文件 
	* @return void    返回类型 
	* @author zhangwanxi 
	* @throws
	 */
	public static void main(String[] args) throws Exception {
		ReadExcel reader=new ReadExcel("c:/小贷-利润表_指标项编码.xls");
		//reader.processByRow();//处理所有的sheet
		//reader.stop();//运行一半需要停止调用此方法，释放文件流，正常运行完毕会自动释放
		reader.xrowMap = new TreeMap();
		reader.processByRow("c:/小贷-利润表_指标项编码.xls","G04");//处理第一个sheet，sheet索引从1开始
		JSONArray list = new JSONArray();
		for(Object o : ReadExcel.xrowMap.keySet()){
			XRow row = ReadExcel.xrowMap.get(o);
			if(row.getRowIndex()<3){
				continue;
			}
			JSONObject obj = new JSONObject();
			for (int i = 0; i < row.getCellsSize(); i++) {
				if(i==1){
					obj.put("1",row.getCell(i).getValue());
				}else if(i==2){
					obj.put("2",row.getCell(i).getValue());
				}/*else if(i==8){
					obj.put("8",row.getCell(i).getValue());
				}*/
			}
			list.put(obj);
		} 
		System.out.println(list.toString());
	
		
	}

	
}
