/**
 * 
 */
package irille.pub.html;

import java.util.Vector;

import irille.pub.FileTools;
import irille.pub.Log;
import irille.pub.ext.Ext.IExtVars;
import irille.pub.svr.Act.OAct;
import irille.pub.tb.Fld;
import irille.pub.tb.FldEnumByte;
import irille.pub.tb.FldOptCust;
import irille.pub.tb.FldStr;
import irille.pub.tb.OptBase;
import irille.pub.tb.Tb;
import irille.pub.view.VFlds;

/**
 * 各种类型交易的JS模块组合产生器的基类
 * @author whx
 * 
 */
public abstract class EMCrt<T extends EMCrt> implements IExtVars {
	private static final Log LOG = new Log(EMCrt.class);
	private Tb _tb;
	private VFlds _vflds; //主屏字段集
	private VFlds _vfldsModel, //Model字段集
	    _vfldsList, //列表对象的字段集
	    _vfldsForm, //Form中的字段集
	    _searchVflds; //搜索器的中字段集
	private Vector<ExtFile> _files = new Vector(); //产生的文件列表
	private Tb[] _optTbs;
	private int _errCount = 0; //累计出错数
	public static final String LOGIN_USER = "lg_user_pkey+bean_split+lg_user_name";
	public static final String LOGIN_DEPT = "lg_dept_pkey+bean_split+lg_dept_name";
	public static final String LOGIN_CELL = "lg_cell_pkey+bean_split+lg_cell_name";
	public static final String LOGIN_ORG = "lg_org_pkey+bean_split+lg_org_name";

	/**
	 * 构造方法
	 * @param tb Tb对象
	 * @param vflds 字段对象
	 * @param searchVflds 搜索对象
	 *          列表界面上的查询字段
	 */
	public EMCrt(Tb tb, VFlds[] vflds, VFlds[] searchVflds) {
		super();
		_tb = tb;
		_vflds = VFlds.newVFlds(vflds);
		_vfldsModel = VFlds.newVFlds(_vflds);
		//	_vflds = _vfldsForm = _vfldsList = vflds;
		_vfldsForm = VFlds.newVFlds(_vflds);
		_vfldsList = VFlds.newVFlds(_vflds);
		_searchVflds = VFlds.newVFlds(searchVflds);
		setOptTbs();
		if (VFlds.newVFlds(vflds).chk("goods"))
			getVfldsList().del("goodsSpec").del("goodsName");
	}

	EMCrt() {
	}

	/**
	 * 复制VFlds对象
	 * @param vflds
	 * @return
	 */
	public static VFlds[] copyVflds(VFlds... vflds) {
		VFlds[] nvs = new VFlds[vflds.length];
		for (int i = 0; i < vflds.length; i++) {
			nvs[i] = new VFlds(vflds[i], true);
		}
		return nvs;
	}

	public static VFlds getGoodsVflds() {
		Fld fldName = new FldStr("goodsName", "货物名称", 100, true);
		Fld fldSpec = new FldStr("goodsSpec", "货物规格", 100, true);
		VFlds vflds = new VFlds("link").add(fldName.getVFld()).add(fldSpec.getVFld());
		return vflds;
	}

	public static VFlds getGoodsVflds(Fld fld) {
		Fld fldName = new FldStr(fld.getCode() + "Name", fld.getName() + "名称", 100, true);
		Fld fldSpec = new FldStr(fld.getCode() + "Spec", fld.getName() + "规格", 100, true);
		VFlds vflds = new VFlds("link").add(fldName.getVFld()).add(fldSpec.getVFld());
		return vflds;
	}

	/**
	 * 
	 * @param tb
	 * @param vflds
	 * @param vfldsList
	 *          列表界面上的查询字段
	 */
	public EMCrt(Tb tb, VFlds vflds, VFlds vfldsList) {
		this(tb, new VFlds[] { vflds }, vfldsList == null ? new VFlds[] {} : new VFlds[] { vfldsList });
	}

	/**
	 * 产生节点中的所有文件，并加到列表中
	 * 
	 * @param file
	 */
	public final void addExt(ExtFile file) {
		_files.add(file);
	}

	public T newExts() {
		addExt(newModel());
		addExt(newStore());
		addExt(newList());
		addExt(newWinSearch());
		if (isCrtWinAndForm()) {
			addExt(newWin());
			addExt(newForm());
		}
		return (T) this;
	}

	public final T backupFiles() {
		for (ExtFile file : _files) {
			if (file == null)
				continue;
			file.backupFiles(false);
		}
		return (T) this;
	}

	/**
	 * 产生文件
	 * 
	 * @return
	 */
	public final T crtFiles() {
		return crtFiles(false);
	}

	/**
	 * 产生文件, 并与原来看文件进行比较
	 * 
	 * @return
	 */
	public final T crtFilesAndCompBackup() {
		return crtFiles(true);
	}

	public T init() {
		for (ExtFile file : _files) {
			if (file == null)
				continue;
			file.init();
		}
		return (T) this;
	}

	private final T crtFiles(boolean compBakFile) {
		for (ExtFile file : _files) {
			if (file == null)
				continue;
			file.crtFile();
			if (!compBakFile)
				continue;
			if (!file.cmpFileIgnoreBlank())
				_errCount++;
		}
		if (_errCount == 0) {
			// 建立选项与用户选项
			crtOpt(getTb());
			for (Tb tb : getOptTbs())
				crtOpt(tb);
			return (T) this;
		}
		System.err.println("以上共有[" + _errCount + "]个文件不一致，请认真核实！");
		return (T) this;
	}

	public static final void crtOpt(Tb tb) {
		OptBase opt;
		for (Fld fld : tb.getFlds()) {
			if (!fld.isOpt())
				continue;
			if (fld instanceof FldEnumByte)
				new EMOpt(((FldEnumByte) fld).getEnum().getClass()).init().crtFile();
			else if (fld instanceof FldOptCust) {
				opt = fld.getOpt();
				new EMOptCust(opt.getCode()).init().crtFile();
			} else
				throw LOG.err("crtOptErr", "表【{0}】的字段【{1}】选项类型不能识别！", tb.getCode(), fld.getCode());
		}
	}

	public final T delBackupFiles() {
		for (ExtFile file : _files)
			FileTools.del(ExtFile.getBackupFile(file.getFileName()));
		return (T) this;
	}

	public ExtFile newModel() {
		return new EMModel(getTb(), getVfldsModel());
	}

	public ExtFile newStore() {
		return new EMStore(getTb());
	}

	public abstract ExtFile newList();

	public abstract ExtFile newWin();

	public abstract ExtFile newForm();

	public abstract ExtFile newWinSearch();

	public boolean isCrtWinAndForm() {
		return getTb().checkAct(OAct.INS) != null || getTb().checkAct(OAct.UPD) != null;
	}

	/**
	 * @return the vfldses
	 */
	public VFlds getVflds() {
		return _vflds;
	}

	/**
	 * @param vflds
	 *          the vfldses to set
	 */
	public void setVflds(VFlds... vflds) {
		_vflds = VFlds.newVFlds(vflds);
	}

	/**
	 * @return the vfldsModel
	 */
	public VFlds getVfldsModel() {
		return _vfldsModel;
	}

	/**
	 * @param vfldsModel
	 *          the vfldsModel to set
	 */
	public void setVfldsModel(VFlds... vfldsModel) {
		_vfldsModel = VFlds.newVFlds(vfldsModel);
	}

	/**
	 * @return the vfldsList
	 */
	public VFlds getVfldsList() {
		return _vfldsList;
	}

	/**
	 * @param vfldsList
	 *          the vfldsList to set
	 */
	public void setVfldsList(VFlds... vfldsList) {
		_vfldsList = VFlds.newVFlds(vfldsList);
	}

	/**
	 * @return the vfldsForm
	 */
	public VFlds getVfldsForm() {
		return _vfldsForm;
	}

	/**
	 * @param vfldsForm
	 *          the vfldsForm to set
	 */
	public void setVfldsForm(VFlds... vfldsForm) {
		_vfldsForm = VFlds.newVFlds(vfldsForm);
	}

	/**
	 * @return the tb
	 */
	public Tb getTb() {
		return _tb;
	}

	/**
	 * @return the optTbs
	 */
	public Tb[] getOptTbs() {
		return _optTbs;
	}

	/**
	 * @param optTbs
	 *          the optTbs to set
	 */
	public T setOptTbs(Tb... optTbs) {
		_optTbs = optTbs;
		return (T) this;
	}

	/**
	 * @return the searchVflds
	 */
	public VFlds getSearchVflds() {
		return _searchVflds;
	}
}
