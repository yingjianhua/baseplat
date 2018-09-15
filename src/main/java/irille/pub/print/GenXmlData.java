package irille.pub.print;

import irille.core.sys.SysDept;
import irille.core.sys.SysEm;
import irille.core.sys.SysPersonLink;
import irille.core.sys.SysPersonLinkDAO;
import irille.core.sys.Sys.OLinkType;
import irille.gl.gs.GsGain;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsIn;
import irille.gl.gs.GsLoss;
import irille.gl.gs.GsMovement;
import irille.gl.gs.GsOut;
import irille.gl.gs.GsSplit;
import irille.gl.gs.GsStockLine;
import irille.gl.gs.GsUnite;
import irille.pss.pur.PurMvIn;
import irille.pss.pur.PurPresent;
import irille.pss.pur.PurRev;
import irille.pss.pur.PurRtn;
import irille.pss.sal.SalMvOut;
import irille.pss.sal.SalPresent;
import irille.pss.sal.SalRtn;
import irille.pss.sal.SalSale;
import irille.pub.ClassTools;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanMain;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.FldEnumByte;
import irille.pub.tb.FldOutKey;
import irille.pub.tb.FldVOneToOne;
import irille.pub.tb.IEnumOpt;

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
				for (Object row : list) {
					Bean bean = (Bean) row;
					XmlText.append("<row>");
					for (Fld fld : bean.gtTB().getFlds()) {
						if (fld instanceof FldVOneToOne)
							continue;
						Object obj = bean.propertyValue(fld);
						XmlText.append("<");
						XmlText.append(fld.getCode());
						XmlText.append(">");
						if (obj == null || obj instanceof String && Str.isEmpty(obj.toString()))
							;
						else if (fld instanceof FldEnumByte) {
							Object optkey = ClassTools.gtProperty(bean, fld.getCode());
							if (optkey instanceof Boolean)
								XmlText.append(optkey);
							else {
								XmlText.append(((IEnumOpt) optkey).getLine().getName());
							}
						} else if (fld instanceof FldOutKey) {
							BeanMain outkey = (BeanMain) bean.propertyValueOBJ(fld);
							String show = outkey.getPkey().toString();
							if (outkey instanceof IExtName)
								show = ((IExtName) outkey).getExtName();
							XmlText.append(show);
						} else
							XmlText.append(obj);
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
			e.printStackTrace();
		}
	}

	public static void GenNodeXmlData(HttpServletResponse response, List list, String ParameterPart, boolean ToCompress) {
		try {
			try {
				response.resetBuffer();
				StringBuffer XmlText = new StringBuffer("<report>\n<xml>\n");
				for (Object row : list) {
					Bean bean = (Bean) row;
					XmlText.append("<row>");
					for (Fld fld : bean.gtTB().getFlds()) {
						if (fld instanceof FldVOneToOne)
							continue;
						Object obj = bean.propertyValue(fld);
						XmlText.append("<");
						XmlText.append(fld.getCode());
						XmlText.append(">");
						if (obj == null || obj instanceof String && Str.isEmpty(obj.toString()))
							;
						else if (fld instanceof FldEnumByte) {
							Object optkey = ClassTools.gtProperty(bean, fld.getCode());
							if (optkey instanceof Boolean)
								XmlText.append(optkey);
							else {
								XmlText.append(((IEnumOpt) optkey).getLine().getName());
							}
						} else if (fld instanceof FldOutKey) {
							BeanMain outkey = (BeanMain) bean.propertyValueOBJ(fld);
							String show = outkey.getPkey().toString();
							if (outkey instanceof IExtName)
								show = ((IExtName) outkey).getExtName();
							XmlText.append(show);
						} else
							XmlText.append(obj);
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
			e.printStackTrace();
		}
	}

	public static String GenListXmlData(List list) {
		try {
			try {
				StringBuffer XmlText = new StringBuffer("<xml>\n");
				for (Object row : list) {
					Bean bean = (Bean) row;
					boolean isGoods = false; // TODO
					// 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
					GsGoods goods = null; // TODO 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
					XmlText.append("<row>");
					for (Fld fld : bean.gtTB().getFlds()) {
						if (fld instanceof FldVOneToOne)
							continue;
						if (fld.isDatabaseField() == false)
							continue;
						Object obj = bean.propertyValue(fld);
						XmlText.append("<");
						XmlText.append(fld.getCode());
						XmlText.append(">");
						if (obj == null || obj instanceof String && Str.isEmpty(obj.toString()))
							;
						else if (fld instanceof FldEnumByte) {
							Object optkey = ClassTools.gtProperty(bean, fld.getCode());
							if (optkey instanceof Boolean)
								XmlText.append(optkey);
							else {
								XmlText.append(((IEnumOpt) optkey).getLine().getName());
							}
						} else if (fld instanceof FldOutKey) {
							BeanMain outkey = (BeanMain) bean.propertyValueOBJ(fld);
							String show = outkey.getPkey().toString();
							if (outkey instanceof IExtName) {
								if (outkey instanceof GsGoods) { // TODO
									// 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
									isGoods = true; // TODO
									// 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
									goods = (GsGoods) outkey; // TODO
									// 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
									show = goods.getCode(); // TODO
									// 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
								} else
									// TODO 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
									show = ((IExtName) outkey).getExtName();
							}
							XmlText.append(show);
						} else
							XmlText.append(obj);
						XmlText.append("</");
						XmlText.append(fld.getCode());
						XmlText.append(">");
						if (isGoods) { // TODO 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
							XmlText.append("<code>"); // TODO
							// 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
							XmlText.append(goods.getCode()); // TODO
							// 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
							XmlText.append("</code>"); // TODO
							// 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
							XmlText.append("<name>"); // TODO
							// 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
							XmlText.append(goods.getName()); // TODO
							// 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
							XmlText.append("</name>"); // TODO
							// 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
							XmlText.append("<spec>"); // TODO
							// 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
							XmlText.append(goods.getSpec()); // TODO
							// 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
							XmlText.append("</spec>"); // TODO
							// 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
							isGoods = false; // TODO
							// 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
						} // TODO 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
					}
					XmlText.append("</row>\n");
				}
				XmlText.append("</xml>\n");
				return XmlText.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String GenListXmlData(List list, String[] params) {// TODO
		try {
			int seq = 1;
			StringBuffer XmlText = new StringBuffer("<xml>\n");
			for (Object row : list) {
				Bean bean = (Bean) row;
				XmlText.append("<row>");
				for (int i = 0; i < params.length; i++) {
					if (params[i].equals("gsTime")) {
						String createdTime = bean.propertyValue(bean.gtTB().get("gsTime")).toString();
						XmlText.append("<gsTime>" + createdTime.substring(0, createdTime.lastIndexOf(" ")) + "</gsTime>");
						continue;
					}
					if (params[i].equals("gsForm")) {
						String gsform = ((GsStockLine) bean).gtGsForm().gtTB().getName();
						XmlText.append("<gsForm>" + gsform.substring(0,gsform.length()-1) + "</gsForm>");
						continue;
					}
					if (params[i].equals("origForm")) {
						String origform = ((GsStockLine) bean).gtOrigForm().gtTB().getName();
						XmlText.append("<origForm>" + origform.substring(0,2) + "</origForm>");
						continue;
					}
					if (params[i].equals("pkey")) {
						XmlText.append("<");
						XmlText.append(params[i]);
						XmlText.append(">");
						XmlText.append(seq);
						XmlText.append("</");
						XmlText.append(params[i]);
						XmlText.append(">");
						continue;
					}

//					String subFlds[] = params[i].split("\\.");
//					if (subFlds.length > 1) {
//						// 有点点点
//						Bean subBean = bean;
//						String code = "";
//						String show = "";
//						for (int j = 0; j < subFlds.length; j++) {
//							if (subBean == null)
//								continue;
//							Fld fld = subBean.gtTB().get(subFlds[j]);
//							if (j < subFlds.length - 1) {
//								code += subFlds[j] + ".";
//								subBean = (BeanMain) subBean.propertyValueOBJ(fld);
//							}
//							if (j == subFlds.length - 1) {
//								code += subFlds[j];
//								show = subBean.propertyValue(fld).toString();
//							}
//						}
//						XmlText.append("<");
//						XmlText.append(code);
//						XmlText.append(">");
//						XmlText.append(show);
//						XmlText.append("</");
//						XmlText.append(code);
//						XmlText.append(">");
//						continue;
//					}
					String subFlds[] = params[i].split("\\.");//外键字段
					if (subFlds.length > 1) {// 有点点点,直接外键关联
						Object show = getFldValue(bean, subFlds);
						if (show == null)
							continue;
						XmlText.append("<");
						XmlText.append(params[i]);
						XmlText.append(">");
						XmlText.append(show.toString());
						XmlText.append("</");
						XmlText.append(params[i]);
						XmlText.append(">");
						continue;
					}
					Fld fld = bean.gtTB().get(params[i]);
					if (fld instanceof FldVOneToOne)
						continue;
					if (fld.isDatabaseField() == false)
						continue;
					Object obj = bean.propertyValue(fld);
					XmlText.append("<");
					XmlText.append(fld.getCode());
					XmlText.append(">");
					if (obj == null || obj instanceof String && Str.isEmpty(obj.toString()))
						;
					else if (fld instanceof FldEnumByte) {
						Object optkey = ClassTools.gtProperty(bean, fld.getCode());
						if (optkey instanceof Boolean)
							XmlText.append(optkey);
						else {
							XmlText.append(((IEnumOpt) optkey).getLine().getName());
						}
					} else if (fld instanceof FldOutKey) {
						BeanMain outkey = (BeanMain) bean.propertyValueOBJ(fld);
						String show = outkey.getPkey().toString();
						if (outkey instanceof IExtName) {
							show = ((IExtName) outkey).getExtName();
						}
						XmlText.append(show);
					} else
						XmlText.append(obj);
					XmlText.append("</");
					XmlText.append(fld.getCode());
					XmlText.append(">");
				}
				XmlText.append("</row>\n");
				seq++;
			}
			XmlText.append("</xml>\n");
			return XmlText.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	// 产生报表参数的XML节点类型数据
	// 根据ParameterQuerySQL获取的报表参数数据一起打包
	public static String GenReportParameterData(Bean bean) {
		StringBuffer XmlText = new StringBuffer("<_grparam>\n");

		for (Fld fld : bean.gtTB().getFlds()) {
			if (fld instanceof FldVOneToOne)
				continue;
			if (fld.isDatabaseField() == false)
				continue;
			Object obj = bean.propertyValue(fld);
			XmlText.append("<");
			XmlText.append(fld.getCode());
			XmlText.append(">");
			if (obj == null || obj instanceof String && Str.isEmpty(obj.toString()))
				;
			else if (fld instanceof FldEnumByte) {
				Object optkey = ClassTools.gtProperty(bean, fld.getCode());
				if (optkey instanceof Boolean)
					XmlText.append(optkey);
				else {
					XmlText.append(((IEnumOpt) optkey).getLine().getName());
				}
			} else if (fld instanceof FldOutKey) {
				BeanMain outkey = (BeanMain) bean.propertyValueOBJ(fld);
				String show = outkey.getPkey().toString();
				if (outkey instanceof IExtName)
					show = ((IExtName) outkey).getExtName();
				XmlText.append(show);
			} else
				XmlText.append(obj);
			XmlText.append("</");
			XmlText.append(fld.getCode());
			XmlText.append(">");
		}

		XmlText.append("</_grparam>\n");

		return XmlText.toString();
	}

	// 产生报表参数的XML节点类型数据
	// 根据ParameterQuerySQL获取的报表参数数据一起打包
	public static String GenReportParameterData(Bean bean, String[] params) throws Exception {

		StringBuffer XmlText = new StringBuffer("<_grparam>\n");
		OrigFormJudge(bean, XmlText);
		for (int i = 0; i < params.length; i++) {
			// 源单据字段获取单据名
			if (params[i].equals("origForm.tb.name")) {
				String origName = ((Bean) bean.propertyValueOBJ(bean.gtTB().get("origForm"))).gtTB().getName();
				XmlText.append("<origForm.tb.name>" + origName.replaceAll("单", "").replaceAll("(.{1})", "$1 ") + "</origForm.tb.name>");

				continue;
			}
			// 判断是否为出入库单是则截取时间里的日期，不是则去除时间中的毫秒
			if (bean instanceof GsIn || bean instanceof GsOut) {
				if (params[i].equals("createdTime")) {
					String createdTime = bean.propertyValue(bean.gtTB().get("createdTime")).toString();
					XmlText.append("<createdTime>" + createdTime.substring(0, createdTime.lastIndexOf(" ")) + "</createdTime>");
					continue;
				}
			} else {
				if (params[i].equals("createdTime")) {
					String createdTime = bean.propertyValue(bean.gtTB().get("createdTime")).toString();
					XmlText.append("<createdTime>" + createdTime.substring(0, createdTime.lastIndexOf(".")) + "</createdTime>");
					continue;
				}
			}
			String link[] = params[i].split("_");//传来的外链格式如字段名.字段名#表面.字段名
			if (link.length > 1) {//有关联表配置的情况
				Object obj = null;
				for (int j = 0; j < link.length; j++) {
					String[] subFlds = link[j].split("\\.");//外键字段
					if(j < link.length - 1) {
						obj = getFldValueOBJ(bean, subFlds);
						
					} else {
						if (subFlds[0].equals(SysPersonLink.TB.getCode())) {//SysPersonLink特殊处理
							SysPersonLink personLink = SysPersonLinkDAO.getDefault((Bean) obj, OLinkType.PUR);
							subFlds = removeFirst(subFlds);
							obj = getFldValue(personLink, subFlds);
						}
					}
				}
				if (obj == null)
					continue;
				XmlText.append("<");
				XmlText.append(params[i]);
				XmlText.append(">");
				XmlText.append(obj.toString());
				XmlText.append("</");
				XmlText.append(params[i]);
				XmlText.append(">");
				continue;
			} else {
				String subFlds[] = params[i].split("\\.");//外键字段
				if (subFlds.length > 1) {// 有点点点,直接外键关联
					Object show = getFldValue(bean, subFlds);
					if (show == null)
						continue;
					XmlText.append("<");
					XmlText.append(params[i]);
					XmlText.append(">");
					XmlText.append(show.toString());
					XmlText.append("</");
					XmlText.append(params[i]);
					XmlText.append(">");
					continue;
				}
			}
			Fld fld = bean.gtTB().get(params[i]);
			if (fld instanceof FldVOneToOne)
				continue;
			if (fld.isDatabaseField() == false)
				continue;
			Object obj = bean.propertyValue(fld);
			XmlText.append("<");
			XmlText.append(fld.getCode());
			XmlText.append(">");
			if (obj == null || obj instanceof String && Str.isEmpty(obj.toString()))
				;
			else if (fld instanceof FldEnumByte) {
				Object optkey = ClassTools.gtProperty(bean, fld.getCode());
				if (optkey instanceof Boolean)
					XmlText.append(optkey);
				else {
					XmlText.append(((IEnumOpt) optkey).getLine().getName());
				}
			} else if (fld instanceof FldOutKey) {
				BeanMain outkey = (BeanMain) bean.propertyValueOBJ(fld);
				String show = outkey.getPkey().toString();
				if (outkey instanceof IExtName)
					show = ((IExtName) outkey).getExtName();
				XmlText.append(show);
			} else
				XmlText.append(obj);
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
			System.err.println(XmlText.toString());
			pw.close(); // 终止后续不必要内容输出
			// SysOrg org = new SysOrg();
			// org.load(1);
			// System.err.println(org.gtOrgState().getLine().getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void GenParameterXmlData(HttpServletResponse response, Bean bean, List list) {
		try {
			response.resetBuffer();

			StringBuffer XmlText = new StringBuffer("<report>\n");
			String ParameterPart = GenReportParameterData(bean);
			String listPart = GenListXmlData(list);
			XmlText.append(listPart);
			XmlText.append(ParameterPart);
			XmlText.append("</report>");

			PrintWriter pw = response.getWriter();
			pw.print(XmlText.toString());
			System.err.println(XmlText.toString());
			pw.close(); // 终止后续不必要内容输出
			// SysOrg org = new SysOrg();
			// org.load(1);
			// System.err.println(org.gtOrgState().getLine().getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void GenParameterXmlData(HttpServletResponse response, Bean bean, List list, String[] mainFlds, String[] lineFlds) {
		try {
			response.resetBuffer();
			StringBuffer XmlText = new StringBuffer("<report>\n");
			String ParameterPart = GenReportParameterData(bean, mainFlds);
			String listPart = GenListXmlData(list, lineFlds);
			XmlText.append(listPart);
			XmlText.append(ParameterPart);
			XmlText.append("</report>");
			PrintWriter pw = response.getWriter();
			pw.print(XmlText.toString());
			System.err.println(XmlText.toString());
			pw.close(); // 终止后续不必要内容输出
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param bean 单据对象
	 * @param subFlds 关联字段名数组
	 * @return 获取点点点最后的字段的值，中途对象为null则返回null
	 */
	private static Object getFldValue(Bean bean, String[] subFlds) {
		return getFldV(bean, subFlds, false);
	}
	
	/**
	 * 
	 * @param bean 单据对象
	 * @param subFlds 关联字段名数组
	 * @return 获取点点点最后的字段的对象，中途对象为null则返回null
	 */
	private static Object getFldValueOBJ(Bean bean, String[] subFlds) {
		return getFldV(bean, subFlds, true);
	}
	
	private static Object getFldV(Bean bean, String[] subFlds, boolean isOBJ) {
		Bean subBean = bean;
		Object obj = null;
		for (int j = 0; j < subFlds.length; j++) {
			if (subBean == null) {
				break;
			}
			Fld fld = subBean.gtTB().get(subFlds[j]);
			if (j == subFlds.length - 1) {
				obj = isOBJ ? subBean.propertyValueOBJ(fld) : subBean.propertyValue(fld);
			} else {
				subBean = (BeanMain) subBean.propertyValueOBJ(fld);
			}
		}
		return obj;
	}

	/**
	 * @param bean
	 * @param XmlText
	 * 2015-6-25 15:46:01
	 * 由于出入库单的源单据太多所以写了下面这个判断方法
	 * @throws Exception 
	 */
	public static void OrigFormJudge(Bean bean, StringBuffer XmlText) throws Exception {
		if (bean instanceof GsIn || bean instanceof GsOut) {
			Bean gsin;
			String OrigFormNum;
			if (bean instanceof GsIn) {
				gsin = ((GsIn) bean).gtOrigForm();
				OrigFormNum = ((GsIn) bean).getOrigFormNum();
			} else {
				gsin = ((GsOut) bean).gtOrigForm();
				OrigFormNum = ((GsOut) bean).getOrigFormNum();
			}
			// 销售退货入库单
			if (gsin instanceof SalRtn) {
				String PeMobile = null, name = null;
				if (((SalRtn) gsin).gtOperator() != null) {
					PeMobile = ((SysEm) ((SalRtn) gsin).gtOperator().gtTbObj()).gtPerson().gtPe().getMobile();
					if (PeMobile != null && !PeMobile.equals("null")) {
						PeMobile = PeMobile;
					} else {
						PeMobile = "";
					}
					name = ((SalRtn) gsin).gtOperator().getName();
				}
				XmlText.append("<x12y1>" + "客户名称:" + ((SalRtn) gsin).getCustName() + "</x12y1>");
				XmlText.append("<x12y2>" + "业务员/手机:" + name + "/" + PeMobile + "</x12y2>");
				XmlText.append("<x3y1>" + ((SalRtn) gsin).gtTB().getName() + ":" + OrigFormNum + "</x3y1>");
			}
			// 调入入库单
			if (gsin instanceof PurMvIn) {
				XmlText.append("<x1y1>" + "调入单号:" + ((GsIn) bean).getCode() + "</x1y1>");
				XmlText.append("<x2y1>" + ((PurMvIn) gsin).gtTB().getName() + ":" + OrigFormNum + "</x2y1>");
			}
			// 调出出库单
			if (gsin instanceof SalMvOut) {
				String WarehouseOther;
				XmlText.append("<x1y1>" + "调出单号:" + ((GsOut) bean).getCode() + "</x1y1>");
				XmlText.append("<x2y1>" + ((SalMvOut) gsin).gtTB().getName() + ":" + ((GsOut) bean).getOrigFormNum() + "</x2y1>");
				if (((SalMvOut) gsin).gtWarehouseOther() != null) {
					WarehouseOther = ((SalMvOut) gsin).gtWarehouseOther().gtDept().getName();
				} else {
					WarehouseOther = "";
				}
				XmlText.append("<x1y2>" + "调入:" + WarehouseOther + "</x1y2>");
				XmlText.append("<x2y2>" + "调出仓库:" + ((SalMvOut) gsin).gtWarehouse().gtDept().getName() + "</x2y2>");
			}
			// 采购收货入库单
			if (gsin instanceof PurRev) {
				String PeMobile = ((SysEm) ((PurRev) gsin).gtBuyer().gtTbObj()).gtPerson().gtPe().getMobile();
				if (PeMobile != null && !PeMobile.equals("null")) {
					PeMobile = PeMobile;
				} else {
					PeMobile = "";
				}
				XmlText.append("<x12y1>" + "供应单位:" + ((PurRev) gsin).getSupname() + "</x12y1>");
				XmlText.append("<x3y1>" + ((PurRev) gsin).gtTB().getName() + ":" + OrigFormNum + "</x3y1>");
				XmlText.append("<x1y2>" + "采购员/电话:" + ((PurRev) gsin).gtBuyer().getName() + "/" + PeMobile + "</x1y2>");
			}
			// 采购受赠入库单
			if (gsin instanceof PurPresent) {
				String PeMobile = ((SysEm) ((PurPresent) gsin).gtCreatedBy().gtTbObj()).gtPerson().gtPe().getMobile();
				if (PeMobile != null && !PeMobile.equals("null")) {
					PeMobile = PeMobile;
				} else {
					PeMobile = "";
				}
				XmlText.append("<x12y1>" + "供应单位:" + ((PurPresent) gsin).getSupname() + "</x12y1>");
				XmlText.append("<x3y1>" + ((PurPresent) gsin).gtTB().getName() + ":" + OrigFormNum + "</x3y1>");
				XmlText.append("<x12y2>" + "业务员/电话:" + ((PurPresent) gsin).gtCreatedBy().getName() + "/" + PeMobile + "</x12y2>");
			}
			// 盘盈入库单
			if (gsin instanceof GsGain) {
				XmlText.append("<x1y1>" + ((GsGain) gsin).gtTB().getName() + ":" + OrigFormNum + "</x1y1>");
			}
			// 内部调拨出入库单
			if (gsin instanceof GsMovement) {
				String str;
				if (bean instanceof GsIn) {
					str = "调出仓库:";
					XmlText.append("<x3y1>" + ((GsMovement) gsin).gtTB().getName() + ":" + OrigFormNum + "</x3y1>");
				} else {
					str = "调入仓库:";
					XmlText.append("<x2y1>" + ((GsMovement) gsin).gtTB().getName() + ":" + ((GsOut) bean).getOrigFormNum() + "</x2y1>");
				}
				XmlText.append("<x1y1>" + str + ((GsMovement) gsin).gtWarehouseOut().gtDept().getName() + "</x1y1>");
			}
			// 拆分出入库单
			if (gsin instanceof GsSplit) {
				XmlText.append("<x1y1>" + ((GsSplit) gsin).gtTB().getName() + ":" + OrigFormNum + "</x1y1>");
			}
			// 合并出入库单
			if (gsin instanceof GsUnite) {
				XmlText.append("<x1y1>" + ((GsUnite) gsin).gtTB().getName() + ":" + OrigFormNum + "</x1y1>");
			}
			// 采购退货出库单
			if (gsin instanceof PurRtn) {
				String PeMobile = ((SysEm) ((PurRtn) gsin).gtCreatedBy().gtTbObj()).gtPerson().gtPe().getMobile();
				if (PeMobile != null && !PeMobile.equals("null")) {
					PeMobile = PeMobile;
				} else {
					PeMobile = "";
				}
				XmlText.append("<x12y1>" + "供应单位:" + ((PurRtn) gsin).getSupname() + "</x12y1>");
				XmlText.append("<x3y1>" + ((PurRtn) gsin).gtTB().getName() + ":" + OrigFormNum + "</x3y1>");
				XmlText.append("<x1y3>" + "采购员/电话:" + ((PurRtn) gsin).gtCreatedBy().getName() + "/" + PeMobile + "</x1y3>");
			}
			// 销售出库单
			if (gsin instanceof SalSale) {
				String addr, name, tel;
				String PeMobile = ((SysEm) ((SalSale) gsin).gtCreatedBy().gtTbObj()).gtPerson().gtPe().getMobile();
				if (PeMobile != null && !PeMobile.equals("null")) {
					PeMobile = PeMobile;
				} else {
					PeMobile = "";
				}
				if (((SalSale) gsin).gtShiping() != null) {
					addr = ((SalSale) gsin).gtShiping().getAddr();
					name = ((SalSale) gsin).gtShiping().getName();
					tel = ((SalSale) gsin).gtShiping().getTel();
				} else {
					addr = "";
					name = "";
					tel = "";
				}
				XmlText.append("<x12y1>" + "客户名称:" + ((SalSale) gsin).getCustName() + "</x12y1>");
				XmlText.append("<x12y2>" + "送货地/联系人/电话:" + addr + "/" + name + "/" + tel + "</x12y2>");
				XmlText.append("<x3y1>" + ((SalSale) gsin).gtTB().getName() + ":" + OrigFormNum + "</x3y1>");
				XmlText.append("<x3y2>" + "运输方式:" + ((SalSale) gsin).gtShipingMode() + "</x3y2>");
				XmlText.append("<x1y3>" + "业务员/手机:" + ((SalSale) gsin).gtCreatedBy().getName() + "/" + PeMobile + "</x1y3>");
			}
			// 销售赠送出库单
			if (gsin instanceof SalPresent) {
				String addr, name, tel;
				String PeMobile = ((SysEm) ((SalPresent) gsin).gtCreatedBy().gtTbObj()).gtPerson().gtPe().getMobile();
				if (PeMobile != null && !PeMobile.equals("null")) {
					PeMobile = PeMobile;
				} else {
					PeMobile = "";
				}
				if (((SalPresent) gsin).gtShiping() != null) {
					addr = ((SalPresent) gsin).gtShiping().getAddr();
					name = ((SalPresent) gsin).gtShiping().getName();
					tel = ((SalPresent) gsin).gtShiping().getTel();
				} else {
					addr = "";
					name = "";
					tel = "";
				}
				XmlText.append("<x12y1>" + "客户名称:" + ((SalPresent) gsin).getCustName() + "</x12y1>");
				XmlText.append("<x12y2>" + "送货地/联系人/电话:" + addr + "/" + name + "/" + tel + "</x12y2>");
				XmlText.append("<x3y1>" + ((SalPresent) gsin).gtTB().getName() + ":" + OrigFormNum + "</x3y1>");
				XmlText.append("<x3y2>" + "运输方式:" + ((SalPresent) gsin).gtShipingMode() + "</x3y2>");
				XmlText.append("<x1y3>" + "业务员/手机:" + ((SalPresent) gsin).gtCreatedBy().getName() + "/" + PeMobile + "</x1y3>");
			}
			// 盘亏入库单
			if (gsin instanceof GsLoss) {
				XmlText.append("<x1y1>" + ((GsLoss) gsin).gtTB().getName() + ":" + OrigFormNum + "</x1y1>");
			}
		}
	}

	
	private static String[] removeFirst(String[] array) {
		String[] result = new String[array.length - 1];
		int index = 0;
		System.arraycopy(array, 0, result, 0, index);
		System.arraycopy(array, index+1, result, index, result.length-index);
		return result;
	}
}
