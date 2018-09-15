package irille.pub.html;

import irille.core.sys.SysOrg;
import irille.pub.FileTools;
import irille.pub.ext.Ext;
import irille.pub.svr.Env;
import irille.pub.tb.Tb;

/**
 * 属性类的基类
 * 
 * @author whx
 * 
 */
public class EMStore<T extends EMStore> extends ExtFile<T> {
	private Tb _tb;
	private int _pageSize;

	/**
	 * @param fileName
	 */
	public EMStore(Tb tb, int pageSize) {
		_tb = tb;
		_pageSize = pageSize;
	}

	/**
	 * @param fileName
	 */
	public EMStore(Tb tb) {
		this(tb, Env.getConfParaInt(Env.SysConf.PAGE_SIZE));
	}

	public static void main(String[] args) {
		Tb tb = SysOrg.TB;
		new EMStore(tb).backupFiles().crtFile().cmpFileIgnoreBlank();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFile#getFileName()
	 */
	@Override
	public String getFileName() {
		return Ext.getPathApp() + "store/" + getTbPackage(getTb()) + "/"
				+ getTbClazz(getTb()) + ".js";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFile#getDefineName()
	 */
	@Override
	public String getDefineName() {
		return "mvc.store." + getTbPackage(getTb()) + "." + getTbClazz(getTb());
	}

	@Override
	public void initAttrs() {
		super.initAttrs();
		add(EXTEND, "Ext.data.Store");
		add(REQUIRES, "mvc.model." + getTbPackage(getTb()) + "."
				+ getTbClazz(getTb()));
		add(MODEL, "mvc.model." + getTbPackage(getTb()) + "." + getTbClazz(getTb()));
		add(PAGE_SIZE, _pageSize);
		add(REMOTE_SORT, false);
		add(AUTO_LOAD, false);
		AddList(PROXY)
				.add(TYPE, "ajax")
				.addExp(
						URL,
						"base_path+'/" + getTbPackage(getTb()) + "_" + getTbClazz(getTb())
								+ "_list'").AddList(READER).add(TYPE, "json")
				.add(ROOT, "items").add(TOTAL_PROPERTY, "total")._R()
				.add(SIMPLE_SORT_MODE, true);
	}
	public Tb getTb() {
		return _tb;
	}
}
