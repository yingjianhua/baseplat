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
 * 最基础的菜单功能
 * 
 * @author surface1
 * 
 */
public class EMCrtSimple<T extends EMCrtSimple> extends EMCrt<T> {
	protected EMList _list;
	protected EMWin _win;
	protected EMForm _form;
	protected EMWinSearch _winSearch;
	
	public EMCrtSimple(Tb tb, VFlds[] vflds, VFlds[] searchVflds) {
		super(tb, vflds, searchVflds);
	}

	public EMCrtSimple(Tb tb, VFlds vflds, VFlds searchVflds) {
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

}
