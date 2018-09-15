/**
 * 
 */
package irille.pub.html;

import irille.pub.tb.Tb;
import irille.pub.view.VFlds;

/**
 * @author surface1
 *
 */
/**
	 * 最基础的菜单功能--FORM表单为两列情况
	 * 以解决字段过多的排版问题
 * @author surface1
 * 
 */
public class EMCrtSimpleTwo<T extends EMCrtSimpleTwo> extends EMCrt<T> {
	protected EMWinTwoRow _win;
	protected EMFormTwoRow _form;
	protected EMList _list;
	protected EMWinSearch _winSearch;
	
	public EMCrtSimpleTwo(Tb tb, VFlds[] vflds, VFlds[] searchVflds) {
		super(tb, vflds, searchVflds);
	}

	public EMCrtSimpleTwo(Tb tb, VFlds vflds, VFlds searchVflds) {
		super(tb, vflds, searchVflds);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.EMCrts.EMCrt#newList()
	 */
	@Override
	public ExtFile newList() {
		if (_list == null)
			_list = new EMList(getTb(), getVfldsList()).setSearchVFlds(getSearchVflds());
		return _list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.EMCrts.EMCrt#newWin()
	 */
	@Override
	public ExtFile newWin() {
		if (_win == null)
			_win = new EMWinTwoRow(getTb());
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
			_form = new EMFormTwoRow(getTb(), getVfldsForm());
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
}
