/**
 * 
 */
package irille.pub.html;

import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.view.VFlds;

/**
 * @author surface1
 *
 */
/**
 * 产生外键选择器
 * 
 * @author surface1
 * 
 */
public class EMCrtTrigger extends EMCrt<EMCrtTrigger> {
	private IEnumFld _dispFld;
	private VFlds _combFlds;
	private EMListTrigger _listTrigger;

	public EMCrtTrigger(Tb tb, VFlds[] vflds, IEnumFld dispFld, VFlds combFlds) {
		super(tb, vflds, null);
		_dispFld = dispFld;
		_combFlds = combFlds;
	}

	public EMCrtTrigger(Tb tb, VFlds vflds, IEnumFld dispFld, VFlds combFlds) {
		super(tb, vflds, null);
		_dispFld = dispFld;
		_combFlds = combFlds;
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMCrts.EMCrt#newList()
	 */
	@Override
	public ExtFile newList() {
		if (_listTrigger == null)
			_listTrigger = new EMListTrigger(getTb(), _dispFld, getVfldsList()).setCombFlds(_combFlds);
		return _listTrigger;
	}

	public boolean isCrtWinAndForm() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMCrts.EMCrt#newWin()
	 */
	@Override
	public ExtFile newWin() {
		return new EMWinTrigger(getTb());
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMCrts.EMCrt#newForm()
	 */
	@Override
	public ExtFile newForm() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMCrts.EMCrt#newWinSearch()
	 */
	@Override
	public ExtFile newWinSearch() {
		return null;
	}

	@Override
	public ExtFile newModel() {
		return null;
	}

	@Override
	public ExtFile newStore() {
		return null;
	}

}
