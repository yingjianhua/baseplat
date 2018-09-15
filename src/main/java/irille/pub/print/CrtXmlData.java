/**
 * 
 */
package irille.pub.print;

import irille.gl.gs.GsGoods;
import irille.pub.ClassTools;
import irille.pub.IPubVars;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanMain;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.FldEnumByte;
import irille.pub.tb.FldOutKey;
import irille.pub.tb.FldVOneToOne;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * 产生报表的数据文件，XML格式
 * @author whx
 * 
 */
public class CrtXmlData<T extends CrtXmlData> implements IPubVars {
	private static final Log LOG = new Log(CrtXmlData.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		msg("");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	private boolean _compress = true;
	private HttpServletResponse _response;
	private StringBuffer _buf = new StringBuffer("<report>" + LN);

	public CrtXmlData(HttpServletResponse response) {
		_response = response;
	}

	/**
	 * 输出主表数据
	 * 
	 * @param bean
	 * @param iflds
	 * @return
	 */
	public T crtParameterData(Bean bean, IEnumFld... iflds) {
		Fld[] flds = Fld.fromIEnumFlds(iflds);
		if (flds.length == 0)
			flds = bean.gtTB().getFlds();
		crtBeanData("_grparam", bean, flds);
		return (T) this;
	}

	/**
	 * 输出明细表数据
	 * 
	 * @param list
	 * @param iflds
	 * @return
	 */
	public CrtXmlData crtListXmlData(List list, IEnumFld... iflds) {
		_buf.append("<xml>" + LN);
		Fld[] flds = Fld.fromIEnumFlds(iflds);
		for (Bean bean : (List<Bean>) list) {
			crtBeanData("row", bean, flds);
		}
		_buf.append("</xml>\n");
		return (T) this;
	}

	/**
	 * 发送结果
	 */
	public void send() {
		_response.resetBuffer();
		PrintWriter pw = null;
		try {
			pw = _response.getWriter();
			// 如是压缩，则进行压缩处理 TODO
			pw.print(_buf.toString() + "</report>" + LN);
			System.err.println(_buf.toString());
		} catch (Exception e) {
			throw LOG.err(e);
		} finally {
			if (pw == null)
				pw.close(); // 终止后续不必要内容输出
		}
	}

	private void crtBeanData(String label, Bean bean, Fld... flds) {
		GsGoods goods = null;
		Boolean isGoods = false;
		_buf.append("<" + label + ">");
		for (Fld fld : flds) {
			if (fld instanceof FldVOneToOne)
				continue;
			if (fld.isDatabaseField() == false)
				continue;
			Object obj = bean.propertyValue(fld);
			_buf.append("<");
			_buf.append(fld.getCode());
			_buf.append(">");
			if (obj == null || obj instanceof String && Str.isEmpty(obj.toString()))
				;
			else if (fld instanceof FldEnumByte) {
				Object optkey = ClassTools.gtProperty(bean, fld.getCode());
				if (optkey instanceof Boolean)
					_buf.append(optkey);
				else {
					_buf.append(((IEnumOpt) optkey).getLine().getName());
				}
			} else if (fld instanceof FldOutKey) {
				BeanMain outkey = (BeanMain) bean.propertyValueOBJ(fld);
				String show = outkey.getPkey().toString();
				if (outkey instanceof IExtName) {
					if (outkey instanceof GsGoods) { // TODO
																						// 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
						isGoods = true; // TODO 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
						goods = (GsGoods) outkey; // TODO 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
						show = goods.getCode(); // TODO 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
					} else
						// TODO 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
						show = ((IExtName) outkey).getExtName();
				}
				_buf.append(show);
			} else
				_buf.append(obj);
			_buf.append("</");
			_buf.append(fld.getCode());
			_buf.append(">");
			if (isGoods) { // TODO 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
				_buf.append("<goodsDesc>"); // TODO 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
				_buf.append(goods.getName() + " " + goods.getSpec()); // TODO
																															// 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
				_buf.append("</goodsDesc>"); // TODO 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
				isGoods = false; // TODO 临时用来判断是否货物，今后可能使用其他方式实现，届时删除此行
			}
		}
		_buf.append("</" + label + ">");
	}

	/**
	 * 输出数据
	 * 
	 * @param label
	 * @param fldCodes
	 * @param vals
	 */
	private void crtLine(String label, String[] fldCodes, String[] vals) {
		_buf.append("<" + label + ">");
		int i = 0;
		for (String code : fldCodes) {
			_buf.append("<" + code + ">" + vals[i] + "</" + code + ">");
			i++;
		}
		_buf.append("</" + label + ">" + LN);
	}

}
