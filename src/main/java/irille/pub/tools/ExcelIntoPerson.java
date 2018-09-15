package irille.pub.tools;

import java.io.FileInputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import freemarker.template.SimpleDate;

public class ExcelIntoPerson {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 找到导入的文件

		String fileToBeRead = "D:\\irille-jbk\\login2.xls";
		String cell_value = new String();
		int iRowCount, iColCount; // 得到Excel表的行数，列数
		try {
			// 创建对Excel工作簿文件的

			FileInputStream fs_xls = new FileInputStream(fileToBeRead);
			POIFSFileSystem fs_pio = new POIFSFileSystem(fs_xls);
			HSSFWorkbook workbook = new HSSFWorkbook(fs_pio);
			HSSFSheet sheet = workbook.getSheetAt(0);

			iRowCount = sheet.getPhysicalNumberOfRows();
			iColCount = sheet.getRow(0).getPhysicalNumberOfCells();


			// 开始从第二行读取这个Excel表
			for (int i = 2; i < iRowCount; i++) {
				// 创建空的变量接受exl的值
				String xxname = null;
				String xxdep = null;
				String xxjob = null;
				String xxorg = null;
				Date date = new Date();//获得系统时间.
		        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);//将时间格式转换成符合Timestamp要求的格式.
				HSSFRow row = sheet.getRow(i);

				if (sheet.getRow(i) != null) {
					for (short j = 0; j < iColCount; j++) {

						HSSFCell cell = row.getCell(j);
						if (cell != null) {

							switch (cell.getCellType()) // 判断类型
							{
							case HSSFCell.CELL_TYPE_NUMERIC:

								String newD = ((Double) cell
										.getNumericCellValue()).toString();
								cell_value = newD.substring(0,
										newD.indexOf("."));
								break;
							case HSSFCell.CELL_TYPE_STRING:
								cell_value = cell.getStringCellValue();
								break;
							case HSSFCell.CELL_TYPE_FORMULA:
								cell_value = cell.getCellFormula();
								break;
							default:
								cell_value = "f";
							} 
							if (!"f".equals(cell_value)) {

								if (xxname == null) {
									xxname = new String(
											(cell_value).getBytes("utf-8"),
											"utf-8");
									continue;
									// xid = cell_value;
								}
								if (xxdep == null) {
									xxdep = new String(
											(cell_value).getBytes("utf-8"),
											"utf-8");
									continue;
									// xxname =cell_value;
								}
								if (xxjob == null) {
									xxjob = new String(
											(cell_value).getBytes("utf-8"),
											"utf-8");
									continue;
									// xxname =cell_value;
								}
								if (xxorg == null) {
									xxorg = new String(
											(cell_value).getBytes("utf-8"),
											"utf-8");
									// xxname =cell_value;
								}

								String sql = "insert into sys_em(em_name,dept_pkey,em_post,em_sta,em_crcode,em_crtime,org_code) values('" + xxname + "',"+xxdep+",'"+xxjob+"','01','1','"+nowTime+"','"+xxorg+"');";
								System.out.println(sql);
								// 执行sql插入

							} 

							cell_value = " ";
						}
					}

				} 
				else
					iRowCount++; 

			}

			fs_xls.close(); // 关闭文件流

		} catch (Exception e) {
			System.out.println("出错了   :   " + e);
		}

	}

}
