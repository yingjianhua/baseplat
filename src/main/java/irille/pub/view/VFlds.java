//Created on 2005-9-17
package irille.pub.view;

import irille.core.sys.SysTemplat;
import irille.pub.IPubVars;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.TbBase;
import irille.pub.view.VFld.OAlign;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Title: 表<br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class VFlds<T extends VFlds> implements IPubVars {
	private static final Log LOG = new Log(VFlds.class);
	private String _code;
	protected Vector<VFld> _fields = new Vector<VFld>();
	protected VFld[] _flds = null;
	private transient Hashtable<String, VFld> _fieldHash = new Hashtable<String, VFld>();

	public static void main(String[] args) {
		VFlds flds = new VFlds(SysTemplat.TB);
		System.out.println(flds.toString());
		// flds.moveBefore("code", "type");
		flds.moveAfter("code", "name");
		System.out.println(flds.toString());
	}

	public VFlds(String code, IEnumFld... flds) {
		if (code == null || code.length() == 0)
			_code = "";
		else
			_code = code + ".";
		add(flds);
	}

	// by whx
	public void setCode(String code) {
		_code = code;
	}

	public VFlds() {
		this("bean");
	}

	public VFlds(IEnumFld... flds) {
		this("bean", flds);
	}

	public VFlds(Tb tb) {
		this("bean");
		addAll(tb);
	}

	public VFlds(VFlds vflds) {
		this("bean");
		setCode(vflds.getCode());
		addAll(vflds);
	}

	public VFlds(VFlds vflds, boolean copy) {
		this("bean");
		setCode(vflds.getCode());
		if (copy) {
			for (VFld fld : vflds.getVFlds())
				add(fld.copyNew());
		}
	}

	public T setWidths(double... listWidths) {
		for (int i = 0; i < _fields.size(); i++) {
			//设置列表的各列宽度
			_fields.get(i).setWidthList(listWidths[i]);
		}
		return (T) this;
	}

	public final String getCode() {
		return _code;
	}

	/**
	 * 取类实例变量名称，如: userName
	 * 
	 * @return 结果
	 */
	public final String[] getVFldCodes() {
		String[] fieldCodes = new String[_fields.size()];
		for (int i = 0; i < _fields.size(); i++) {
			fieldCodes[i] = get(i).getCode();
		}
		return fieldCodes;
	}

	/**
	 * 取line, 从0开始
	 * 
	 * @return line
	 */
	public final VFld get(int id) {
		if (id >= _fields.size() || id < 0)
			throw LOG.err("idNotValid", "表[{0}]中字段序号范围为[0-{1}], 引用值[{2}]超范围!", getCode(), new Integer(_fields.size() - 1),
			    new Integer(id));
		return _fields.elementAt(id);
	}

	public VFlds moveBefore(String moveObj, String toBefore) {
		VFld fld = get(moveObj);
		_fields.remove(fld);
		int i = getId(toBefore);
		_fields.add(i, fld);
		return this;
	}

	public VFlds moveBefore(IEnumFld moveObj, IEnumFld toBefore) {
		return moveBefore(moveObj.getFld().getCode(), toBefore.getFld().getCode());
	}

	public VFlds moveAfter(String moveObj, String toAfter) {
		VFld fld = get(moveObj);
		_fields.remove(fld);
		int i = getId(toAfter);
		if (i >= _fields.size())
			add(fld);
		else
			_fields.add(i + 1, fld);
		return this;
	}
	
	public VFlds moveAfter(IEnumFld moveObj, IEnumFld toBefore) {
		return moveAfter(moveObj.getFld().getCode(), toBefore.getFld().getCode());
	}

	public VFlds moveAfter(IEnumFld[] moveObjs, IEnumFld toBefore) {
		for (int i = moveObjs.length - 1; i >= 0; i--)
			moveAfter(moveObjs[i].getFld().getCode(), toBefore.getFld().getCode());
		return this;
	}

	public VFlds moveLast(IEnumFld... iflds) {
		for (IEnumFld ifld : iflds) {
			VFld fld = get(ifld.getFld().getCode());
			_fields.remove(fld);
			add(fld);
		}
		return this;
	}

	public final VFlds del(String code) {
		VFld fld = get(code);
		_fields.remove(fld);
		_fieldHash.remove(code);
		_flds = null;
		return this;
	}

	public final VFlds del(IEnumFld ifld) {
		return del(ifld.getFld().getCode());
	}

	public final VFlds del(IEnumFld... iflds) {
		for (IEnumFld line : iflds)
			del(line);
		return this;
	}

	public final String toString() {
		String s = "";
		int i = 0;
		for (VFld fld : _fields) {
			s += fld.getCode() + ":" + fld.getName() + LN;
			i++;
		}
		return s + "共有[" + i + "]个字段。";
	}

	/**
	 * 取字段
	 * 
	 * @param code
	 *          代码
	 * @return 结果
	 */
	public final VFld get(String code) {
		VFld fld = _fieldHash.get(code);
		if (fld == null)
			throw LOG.err("fieldNotInTable", "字段集中无字段[{0}]!", code);
		return fld;
	}

	public final VFld get(IEnumFld fld) {
		return get(fld.getFld().getCode());
	}

	public final boolean chk(String code) {
		VFld fld = _fieldHash.get(code);
		if (fld == null)
			return false;
		return true;
	}

	public final T setHelp(String code, Object helpStringOrHtml) {
		get(code).setHelp(helpStringOrHtml);
		return (T) this;
	}

	public final T setHelp(IEnumFld fld, Object helpStringOrHtml) {
		return setHelp(fld.getFld().getCode(), helpStringOrHtml);
	}

	/**
	 * 取字段Id号，从0开始
	 * 
	 * @param code
	 *          代码
	 * @return 结果
	 */
	public final int getId(String code) {
		for (int i = 0; i < _fields.size(); i++)
			if (_fields.get(i).getCode().equals(code))
				return i;
		throw LOG.err("fieldNotInTable", "字段集中无字段[{0}]!", code);
	}

	/**
	 * 取明细数量
	 * 
	 * @return 结果
	 */
	public final int size() {
		return _fields.size();
	}

	public final VFlds add(Fld... flds) {
		for (Fld fld : flds) {
			if (fld.getVFld() == null) // 虚拟字段的值为NULL
				continue;
			if (_fieldHash.get(fld.getCode()) != null) // 是Bean对象允许重复
				throw LOG.err("fldError", "字段集中字段[{0}]重复!", fld.getCode());
			add(fld.getVFld().copyNew());
		}
		return this;
	}

	public final VFlds add(VFld vfld) {
		return add(vfld, getCode());
	}

	public final VFlds add(VFld vfld, String beanCode) {
		_fieldHash.put(vfld.getCode(), vfld);
		_fields.addElement(vfld);
		vfld.setBeanCode(beanCode);
		return this;
	}

	public final VFlds addAll(VFlds vflds) {
		if (vflds != null)
			for (VFld fld : vflds.getVFlds())
				add(fld, vflds.getCode());
		return this;
	}

	public static final VFlds newVFlds(VFlds... vflds) {
		VFlds vs = new VFlds();
		if (vflds == null)
			return vs;
		for (VFlds v : vflds) {
			//			vs.addAll(v);
			for (VFld fld : v.getVFlds())
				vs.add(fld.copyNew(), Str.isEmpty(fld.getBeanCode()) ? vs.getCode() : fld.getBeanCode());
		}
		return vs;
	}

	public VFlds add(IEnumFld... flds) {
		for (IEnumFld fld : flds)
			add(fld.getFld());
		return this;
	}

	public VFlds add(TbBase tb, String... codes) {
		for (String code : codes)
			add(tb.get(code));
		return this;
	}

	public VFlds add(TbBase tb, IEnumFld... flds) {
		String[] codes = new String[flds.length];
		int i = 0;
		for (IEnumFld fld : flds) {
			codes[i++] = fld.getFld().getCode();
		}
		return add(tb, codes);
	}

	public VFlds addAll(TbBase tb) {
		add(tb.getFlds());
		return this;
	}

	/**
	 * 增加表除指定字段外的所有字段
	 * 
	 * @param tb
	 * @param flds
	 * @return
	 */
	public VFlds addWithout(Tb tb, IEnumFld... flds) {
		String[] codes = new String[flds.length];
		int i = 0;
		for (IEnumFld fld : flds) {
			codes[i++] = fld.getFld().getCode();
		}
		return addWithout(tb, codes);
	}

	public VFlds addWithout(Tb tb, VFlds vflds) {
		String[] codes = new String[vflds.size()];
		int i = 0;
		for (VFld fld : vflds.getVFlds()) {
			codes[i++] = fld.getFld().getCode();
		}
		return addWithout(tb, codes);
	}

	/**
	 * 增加表除指定字段外的所有字段
	 * 
	 * @param tb
	 * @param fldCodes
	 * @return
	 */
	public VFlds addWithout(Tb tb, String... fldCodes) {
		HashSet<String> set = new HashSet();
		for (String code : fldCodes) {
			set.add(code);
		}
		for (Fld fld : tb.getFlds()) {
			if (set.contains(fld.getCode()))
				continue;
			add(fld.copyNew());
		}
		return this;
	}

	/**
	 * 增加表除指定字段外的所有字段
	 * 
	 * @param tb
	 * @param fldCodes
	 * @return
	 */
	public VFlds addWithout(VFlds flds, String... fldCodes) {
		HashSet<String> set = new HashSet();
		for (String code : fldCodes) {
			set.add(code);
		}
		for (VFld fld : flds.getVFlds()) {
			if (set.contains(fld.getCode()))
				continue;
			add(fld);
		}
		return this;
	}

	public VFlds addWithout(VFlds flds, IEnumFld... withoutFlds) {
		String[] codes = new String[withoutFlds.length];
		int i = 0;
		for (IEnumFld fld : withoutFlds) {
			codes[i++] = fld.getFld().getCode();
		}
		return addWithout(flds, codes);
	}

	public int compareTo(Tb o) {
		return getCode().compareTo(o.getCode());
	}

	/**
	 * 对象相等与否比较
	 * 
	 * @param obj
	 *          对象
	 * @return 结果
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	// @Override
	public final boolean equals(Object obj) {
		if (!(obj instanceof Tb))
			return false;
		return ((Tb) obj).getCode().equals(getCode());
	}

	/**
	 * 取fields
	 * 
	 * @return fields
	 */
	public final VFld[] getVFlds() {
		if (_flds == null || _flds.length != _fields.size()) {
			VFld[] flds = new VFld[_fields.size()];
			_fields.toArray(flds);
			_flds = flds;
		}
		return _flds;
	}

	public final VFlds setNull(boolean isnull, IEnumFld... flds) {
		for (IEnumFld line : flds)
			if (isnull)
				get(line.getFld().getCode()).setNullTrue();
			else
				get(line.getFld().getCode()).setNullFalse();
		return this;
	}

	public final VFlds setReadOnly(String exp, IEnumFld... flds) {
		for (IEnumFld line : flds)
			get(line.getFld().getCode()).setReadOnly(exp);
		return this;
	}

	public final VFlds setHidden(String exp, IEnumFld... flds) {
		for (IEnumFld line : flds)
			get(line.getFld().getCode()).setHidden(exp);
		return this;
	}

	public final VFlds setExpand(String exp, IEnumFld... flds) {
		for (IEnumFld line : flds)
			get(line.getFld().getCode()).setExpandCol(exp);
		return this;
	}

	public final VFlds setExpandAndHidden(String exp, IEnumFld... flds) {
		setExpand(exp, flds);
		setHidden(exp, flds);
		return this;
	}

	public final VFlds setReadOnly(String exp, String... codes) {
		for (String code : codes)
			get(code).setReadOnly(exp);
		return this;
	}

	public final VFlds setReadOnlyNotToInsFlag(String... codes) {
		return setReadOnly("!this.insFlag", codes);
	}

	public final VFlds setAlignToMiddleRight(IEnumFld... flds) {
		for (IEnumFld fld : flds)
			get(fld).setAlign(OAlign.MIDDLE_RIGHT);
		return this;
	}

	public final VFlds setAlignToMiddleCenter(IEnumFld... flds) {
		for (IEnumFld fld : flds)
			get(fld).setAlign(OAlign.MIDDLE_CENTER);
		return this;
	}

	public final VFlds setAlignToTopRight(IEnumFld... flds) {
		for (IEnumFld fld : flds)
			get(fld).setAlign(OAlign.TOP_RIGHT);
		return this;
	}

	public final VFlds setAlignToTopCenter(IEnumFld... flds) {
		for (IEnumFld fld : flds)
			get(fld).setAlign(OAlign.TOP_CENTER);
		return this;
	}

	public final VFlds setAlign(OAlign align, IEnumFld... flds) {
		for (IEnumFld fld : flds)
			get(fld).setAlign(align);
		return this;
	}

	public final VFlds setAlignTitle(OAlign align, IEnumFld... flds) {
		for (IEnumFld fld : flds)
			get(fld).setAlignTitle(align);
		return this;
	}

	public final VFlds setGoodsLink(IEnumFld... flds) {
		for (IEnumFld fld : flds) {
			get(fld.getFld().getCode() + "Name").setReadOnly("true").attrs().addExp("disabled", "true")
			    .addExp("disabledCls", "''");
			get(fld.getFld().getCode() + "Spec").setReadOnly("true").attrs().addExp("disabled", "true")
			    .addExp("disabledCls", "''");
		}
		return this;
	}
}
