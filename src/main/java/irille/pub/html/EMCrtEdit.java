/**
 * 
 */
package irille.pub.html;

import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

/**
 * 最基础的菜单功能
 * 
 * @author surface1
 * 
 */
public class EMCrtEdit<T extends EMCrtEdit> extends EMCrt<T> {
	private VFlds[] _outVflds;
	private VFld[] _outs;

	public EMCrtEdit(Tb tb, VFlds vflds, VFlds[] outVflds, VFld[] outs) {
		super(tb, vflds, null);
		_outVflds = outVflds;
		_outs = outs;
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMCrt#crtFiles()
	 */
	@Override
	public T newExts() {
		addExt(newWinEdit());
		for (int i = 0; i < _outs.length; i++) {
			crtOutFld(_outs[i], _outVflds[i]);
		}
		return (T) this;
	}

	/**
	 * 建立明细的相关控件
	 * 
	 * @param flds
	 */
	public void crtOutFld(VFld out, VFlds flds) {
		addExt(new EMListEdit((Tb)out.getTb(), out, flds));
		addExt(new EMModel((Tb) out.getFld().getTb(), new VFlds().addAll(out.getFld().getTb())));
		addExt(new EMStore((Tb) out.getFld().getTb()));
		crtOpt((Tb) out.getFld().getTb());
	}

	public ExtFile newWinEdit() {
		return new EMWinEdit(getTb(), new VFlds[] {getVflds()}, getOuts());
	}

	/**
	 * @return the outVflds
	 */
	public VFlds[] getOutVflds() {
		return _outVflds;
	}
	
	public VFld[] getOuts() {
		return _outs;
	}

	/**
	 * @param outVflds
	 *          the outVFlds to set
	 */
	public T setOutVflds(VFlds... outVflds) {
		_outVflds = outVflds;
		return (T) this;
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMCrt#newList()
	 */
	@Override
	public ExtFile newList() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMCrt#newWin()
	 */
	@Override
	public ExtFile newWin() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMCrt#newForm()
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
}
