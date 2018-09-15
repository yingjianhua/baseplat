package irille.pub.svr;

import irille.pub.ClassTools;
import irille.pub.bean.Bean;
import irille.pub.tb.Fld;
import irille.pub.tb.FldOutKey;

import java.io.PrintWriter;
import java.util.List;
import java.util.zip.DeflaterOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;


public class GenXmlData {

	// 方法简要说明
	// 1. GenNodeXmlData：产生报表需要的XML节点类型数据
	// 2.
	// GenFullReportData：根据RecordsetQuerySQL产生提供给报表生成需要的XML数据，并同时将ParameterPart中的报表参数数据一起打包，参数ToCompress指定是否压缩数据
	// 3. GenReportParameterData：根据ParameterQuerySQL获取的报表参数数据一起打包

	// ///////////////////////////////////////////////////////////////////////////////////////
	// 产生报表需要的XML节点类型数据，节点类型数据产生数据量比属性类型数据的要大

	public static void GenNodeXmlData(HttpServletResponse response, List list, boolean ToCompress) {
		try {
			try {
				response.resetBuffer();
				StringBuffer XmlText = new StringBuffer("<xml>\n");
				for (Object obj : list) {
					Bean bean = (Bean) obj;
					XmlText.append("<row>");
					for (Fld fld : bean.gtTB().getFlds()) {
						XmlText.append("<");
						XmlText.append(fld.getCode());
						XmlText.append(">");
						XmlText.append(ClassTools.getProperty(bean, fld.getCode()));
						XmlText.append("</");
						XmlText.append(fld.getCode());
						XmlText.append(">");
					}
					XmlText.append("</row>\n");
				}
				XmlText.append("</xml>\n");

				if (ToCompress) {
					byte[] RawData = XmlText.toString().getBytes();

					// 写入特有的压缩头部信息，以便报表客户端插件能识别数据
					response.addHeader("gr_zip_type", "deflate"); // 指定压缩方法
					response.addIntHeader("gr_zip_size", RawData.length); // 指定数据的原始长度
					response.addHeader("gr_zip_encode", response.getCharacterEncoding()); // 指定数据的编码方式
					                                                                      // utf-8
					                                                                      // utf-16
					                                                                      // ...

					// 压缩数据并输出
					ServletOutputStream bos = response.getOutputStream();
					DeflaterOutputStream zos = new DeflaterOutputStream(bos);
					zos.write(RawData);
					zos.close();
					bos.flush();
				} else {
					PrintWriter pw = response.getWriter();
					pw.print(XmlText.toString());
					pw.close(); // 终止后续不必要内容输出
				}
			} catch (Exception e) {
				// output error message
				PrintWriter pw = response.getWriter();
				pw.print(e.toString());
			}
		} catch (Exception e) {
		}
	}

	public static void GenNodeXmlData(HttpServletResponse response, List list, String ParameterPart,
	    boolean ToCompress) {
		try {
			try {
				response.resetBuffer();
				StringBuffer XmlText = new StringBuffer("<report>\n<xml>\n");
				for (Object obj : list) {
					Bean bean = (Bean) obj;
					XmlText.append("<row>");
					for (Fld fld : bean.gtTB().getFlds()) {
						XmlText.append("<");
						XmlText.append(fld.getCode());
						XmlText.append(">");
						XmlText.append(ClassTools.getProperty(bean, fld.getCode()));
						XmlText.append("</");
						XmlText.append(fld.getCode());
						XmlText.append(">");
					}
					XmlText.append("</row>\n");
				}

				XmlText.append("</xml>\n");
				XmlText.append(ParameterPart);
				XmlText.append("</report>");

				if (ToCompress) {
					byte[] RawData = XmlText.toString().getBytes();

					// 写入特有的压缩头部信息，以便报表客户端插件能识别数据
					response.addHeader("gr_zip_type", "deflate"); // 指定压缩方法
					response.addIntHeader("gr_zip_size", RawData.length); // 指定数据的原始长度
					response.addHeader("gr_zip_encode", response.getCharacterEncoding()); // 指定数据的编码方式
					                                                                      // utf-8
					                                                                      // utf-16
					                                                                      // ...

					// 压缩数据并输出
					ServletOutputStream bos = response.getOutputStream();
					DeflaterOutputStream zos = new DeflaterOutputStream(bos);
					zos.write(RawData);
					zos.close();
					bos.flush();
				} else {
					PrintWriter pw = response.getWriter();
					pw.print(XmlText.toString());
					pw.close(); // 终止后续不必要内容输出
				}
			} catch (Exception e) {
				// output error message
				PrintWriter pw = response.getWriter();
				pw.print(e.toString());
			}
		} catch (Exception e) {
		}
	}

	// 产生报表参数的XML节点类型数据
	// 根据ParameterQuerySQL获取的报表参数数据一起打包
	public static String GenReportParameterData(Bean bean) {
		StringBuffer XmlText = new StringBuffer("<_grparam>\n");

		for (Fld fld : bean.gtTB().getFlds()) {
			XmlText.append("<");
			XmlText.append(fld.getCode());
			XmlText.append(">");
			if (fld instanceof FldOutKey) 
				XmlText.append(ClassTools.getPropertyIfNullReturnZeroStr(
				    ClassTools.getProperty(bean, fld.getCode()+"OBJ"), "name"));
			else
				XmlText.append(ClassTools.getProperty(bean, fld.getCode()));
			XmlText.append("</");
			XmlText.append(fld.getCode());
			XmlText.append(">");
		}

		XmlText.append("</_grparam>\n");

		return XmlText.toString();
	}

	public static void GenParameterXmlData(HttpServletResponse response, Bean bean) {
		try {
			response.resetBuffer();

			StringBuffer XmlText = new StringBuffer("<report>\n");
			String ParameterPart = GenReportParameterData(bean);
			XmlText.append(ParameterPart);
			XmlText.append("</report>");

			PrintWriter pw = response.getWriter();
			pw.print(XmlText.toString());
			pw.close(); // 终止后续不必要内容输出
		} catch (Exception e) {
		}
	}
}
