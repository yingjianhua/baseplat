package irille.action.dataimport.excel;

import java.io.File;
import java.io.IOException;
/**
 * 
* @ClassName: ExcelProcessor 
* @Description: Excel处理器，兼容Excel-2003与Excel-2007 
* @author zhangwanxi
* @date 2013-7-22 上午11:02:01 
*
 */
public abstract class ExcelProcessor implements ExcelRowProcessor{
	private ExcelRowProcessor processor;
	
	public ExcelProcessor(String fileName) throws Exception{
		if(fileName==null||fileName.equals("")){
			throw new Exception("构造Excel导入器失败，未指定文件全名。");
		}
		File file=new File(fileName);
		if(!file.exists()){
			throw new Exception("构造Excel导入器失败，指定的文件不存在："+fileName);
		}
		if(fileName.endsWith("xls")){
			processor=new MyExcel2003RowProcessor(fileName);
		}else{
			processor=new MyExcel2007RowProcessor(fileName);
		}
	}

	public void processByRow() throws Exception {
		processor.processByRow();
	}

	public void processByRow(String excelPath, String sheetName) throws Exception {
		 
		
		ExcelUtils.getAllData(excelPath, sheetName);
		
		
		//processor.processByRow(sheetIndex);
	}
	
	public void processByRowFormula(String excelPath, String sheetName) throws Exception {
		ExcelUtils.getAllDataCell(excelPath, sheetName);
	}
	public void stop() throws IOException {
		processor.stop();
	}

	public abstract void processRow(XRow row);
	
	private class MyExcel2003RowProcessor extends Excel2003RowProcessor{
		public MyExcel2003RowProcessor(String filename) throws Exception {
			super(filename);
		}

		@Override
		public void processRow(XRow row) {
			ExcelProcessor.this.processRow(row);
		}
	}
	
	private class MyExcel2007RowProcessor extends Excel2007RowProcessor{
		public MyExcel2007RowProcessor(String filename) throws Exception {
			super(filename);
		}

		@Override
		public void processRow(XRow row) {
			ExcelProcessor.this.processRow(row);
		}
	}
}
