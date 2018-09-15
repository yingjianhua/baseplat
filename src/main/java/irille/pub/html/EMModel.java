package irille.pub.html;

import irille.core.sys.SysCom;
import irille.core.sys.SysOrg;
import irille.pub.ext.Ext;
import irille.pub.tb.FldLines;
import irille.pub.tb.FldV;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

/**
 * 属性类的基类
 * 
 * @author whx
 * 
 */
public class EMModel<T extends EMModel> extends EMBase<T> {

	/**
	 * @param fileName
	 */
	public EMModel(Tb tb, VFlds... vflds) {
		super(tb,(vflds==null || vflds.length==0)? new VFlds[]{new VFlds(tb)}:vflds);
	}

	public static void main(String[] args) {
		Tb tb = SysOrg.TB;
		VFlds flds = new VFlds().addAll(tb);

	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFile#getFileName()
	 */
	@Override
	public String getFileName() {
		return Ext.getPathApp() + "model/" + getTbPackage(getTb()) + "/"
				+ getTbClazz(getTb()) + ".js";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFile#getDefineName()
	 */
	@Override
	public String getDefineName() {
		return "mvc.model." + getTbPackage(getTb()) + "." + getTbClazz(getTb());
	}
	/* (non-Javadoc)
	 * @see irille.pub.html.EMBase#init()
	 */
	@Override
	public T init() {
		initAttrs();
		return (T)this;
	}

	@Override
	public void initAttrs() {
		super.initAttrs();
		//@formatter:off
		add(EXTEND,"Ext.data.Model");
		add("idProperty","bean.pkey");
		AddList(PROXY)
		.add(TYPE, "ajax")
		.addExp(
				URL,
				"base_path+'/"+getTbPackage(getTb())+"_"+getTbClazz(getTb())+"_load'");
		setFields(AddDime("fields"));
	}
	public void setFields(ExtDime v) {
		for (VFld fld : getVFlds().getVFlds())
			addFld(fld,v);
		
	}
	
	public void addFld(VFld fld, ExtDime v) {
		if (fld.getFld() instanceof FldLines || fld.getFld() instanceof FldV)
			return;
		fld.model(v.AddList(null).setTabs(false));		
	}
}
