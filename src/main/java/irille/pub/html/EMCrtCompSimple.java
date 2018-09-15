/**
 * 
 */
package irille.pub.html;

import irille.pub.bean.CmbGoods;
import irille.pub.tb.Fld;
import irille.pub.tb.FldStr;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

/**
 * @author surface1
 *
 */
/**
 * 最基础的菜单功能
 * 
 * @author surface1
 * 
 */
public class EMCrtCompSimple<T extends EMCrtCompSimple> extends EMCrt<T> {
	private VFlds  _outVflds, _mainFlds;
	private VFlds _nowOutKeyLineVflds, _nowOutKeyModelVflds;
	private EMZipListMain _listMain;
	protected EMForm _form;
	private EMWin _win;
	protected EMWinSearch _winSearch;

	public EMCrtCompSimple(Tb tb, VFlds[] vflds,VFlds[] mainvflds, VFlds[] searchVflds,
			 VFlds[] outVflds) {
		super(tb, vflds, searchVflds);
		_mainFlds = VFlds.newVFlds(mainvflds);
		_outVflds = VFlds.newVFlds(outVflds);
	}

	@Override
	public T newExts() {
		super.newExts();
		VFlds vflds,vflds_link;
		addExt(newZipListMain());
			for (VFld fld : getOutVflds().getVFlds()) {
				vflds=new VFlds().addAll(fld.getFld().getTb());
				//EXT上的技术问题待解决，暂只能把单位移动到数量之后，避免货物编辑时"TAB"造成的单位数据冲突
				if (vflds.chk("goods")) {
					vflds.moveAfter(CmbGoods.T.UOM, CmbGoods.T.QTY);
					Fld fldName = new FldStr("goodsName", "货物名称", 100, true);
					Fld fldSpec = new FldStr("goodsSpec", "货物规格", 100, true);
					vflds_link = new VFlds("link").add(fldName.getVFld()).add(fldSpec.getVFld());
					setNowOutKeyModelVFlds(vflds, vflds_link);
				}else {
					setNowOutKeyModelVFlds(vflds);
				}
				setNowOutKeyLineVflds(vflds);
				crtOutFld(fld);
			}
		return (T)this;
	}

	/**
	 * 建立明细的相关控件
	 * 
	 * @param fld
	 */
	public void crtOutFld(VFld fld) {
		addExt(new EMZipListLine(getTb(), fld, getNowOutKeyLineVflds()));
		addExt(new EMModel((Tb) fld.getFld().getTb(), getNowOutKeyModelVflds()));
		addExt(new EMStore((Tb) fld.getFld().getTb()));
	}

	public ExtFile newZipListMain() {
		if (_listMain == null)
			_listMain = new EMZipListMain(getTb(), getVfldsList());
		return _listMain;
	}

	@Override
	public ExtFile newList() {
		return new EMZipList(getTb(), getMainFlds()).setOutVFlds(_outVflds)
				.setSearchVFlds(getSearchVflds());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.EMCrts.EMCrt#newWin()
	 */
	@Override
	public ExtFile newWin() {
		if (_win == null)
			_win = new EMWin(getTb());
		return _win;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.EMCrts.EMCrt#newForm()
	 */
	@Override
	public ExtFile newForm() {
		if (_form == null)
			_form = new EMForm(getTb(), getVfldsForm());
		return _form;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.EMCrts.EMCrt#newWinSearch()
	 */
	@Override
	public ExtFile newWinSearch() {
		if (_winSearch == null)
			_winSearch = new EMWinSearch(getTb(), getVfldsModel());
		return _winSearch;
	}

	/**
	 * @return the outVflds
	 */
	public VFlds getOutVflds() {
		return _outVflds;
	}

	/**
	 * @param outVflds
	 *          the outVFlds to set
	 */
	public T setOutVflds(VFlds... outVflds) {
		_outVflds = VFlds.newVFlds(outVflds);
		return (T) this;
	}

	/**
	 * @param mainFlds
	 *          the mainFlds to set
	 */
	public void setMainFlds(VFlds... mainFlds) {
		_mainFlds = VFlds.newVFlds(mainFlds);
	}

	/**
	 * @return the mainFlds
	 */
	public VFlds getMainFlds() {
		return _mainFlds;
	}

	/**
	 * @return the nowOutKeyVflds
	 */
	public VFlds getNowOutKeyLineVflds() {
		return _nowOutKeyLineVflds;
	}

	/**
	 * @param nowOutKeyLineVflds
	 *          the nowOutKeyVflds to set
	 */
	public T setNowOutKeyLineVflds(VFlds... nowOutKeyLineVflds) {
		_nowOutKeyLineVflds = VFlds.newVFlds(nowOutKeyLineVflds);
		return (T)this;
	}

	/**
	 * @return the nowOutKeyModelVflds
	 */
	public VFlds getNowOutKeyModelVflds() {
		return _nowOutKeyModelVflds;
	}

	/**
	 * @param nowOutKeyModelVflds
	 *          the nowOutKeyModelVflds to set
	 */
	public T setNowOutKeyModelVFlds(VFlds... nowOutKeyModelVflds) {
		_nowOutKeyModelVflds = VFlds.newVFlds(nowOutKeyModelVflds);
		return (T)this;
	}

	public void setMainFlds(VFlds mainFlds) {
		_mainFlds = mainFlds;
	}
	
}
