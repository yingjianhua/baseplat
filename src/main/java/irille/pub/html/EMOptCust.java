package irille.pub.html;

import irille.gl.gl.GlNoteWriteoff;
import irille.pub.ClassTools;
import irille.pub.Str;
import irille.pub.ext.Ext;
import irille.pub.tb.EnumLine;
import irille.pub.tb.Fld;
import irille.pub.tb.FldEnumByte;
import irille.pub.tb.FldOptCust;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;

import java.util.Vector;

/**
 * 属性类的基类
 * 
 * @author whx
 * 
 */
public class EMOptCust<T extends EMOptCust> extends ExtFile<T> {
	private String _optCode;

	/**
	 * @param fileName
	 */
	public EMOptCust(String optCode) {
		_optCode = optCode;
	}

	public String getOptCode() {
		return _optCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFile#getFileName()
	 */
	@Override
	public String getFileName() {
		return Ext.getPathApp() + "comboCust/"
				+ _optCode + ".js";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFile#getDefineName()
	 */
	@Override
	public String getDefineName() {
		return "mvc.comboCust." 
				+ _optCode;
	}

	@Override
	public void initAttrs() {
		super.initAttrs();
		add(EXTEND, "Ext.data.Store");
		AddDime("fields", "value", "text");
		add("autoLoad", true);
		AddList(PROXY)
				.add(TYPE, "ajax")
				.addExp(
						URL,
						"base_path+'/sys_SysCtype_getCombo?ctype="
								+_optCode+"'").AddList(READER)
				.add(TYPE, "json").add(ROOT, "items");
	}
}
