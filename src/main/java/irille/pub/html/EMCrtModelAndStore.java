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
 * 产生最基础的JS文件，Model与Store
 * 
 * @author surface1
 * 
 */
public class EMCrtModelAndStore<T extends EMCrtModelAndStore> extends EMCrt<T> {
	public EMCrtModelAndStore(Tb tb, VFlds... modelVFlds ) {
		super(tb, modelVFlds, null);
	}
	public EMCrtModelAndStore(){
	}
	
	
	public static void crtModelAndStore(Tb tb, VFlds... modelVFlds  ) {
		EMCrtModelAndStore ext=new EMCrtModelAndStore(tb,modelVFlds);
		ext.newExts().init();
		ext.crtFiles();
	}
	public static void crtModelAndStore_CompBackup(Tb tb, VFlds... modelVFlds  ) {
		EMCrtModelAndStore ext=new EMCrtModelAndStore(tb,modelVFlds);
		ext.newExts();
	  ext.backupFiles();
		ext.crtFilesAndCompBackup();
	}
	

	/* (non-Javadoc)
	 * @see irille.pub.html.EMCrt#newExts()
	 */
	@Override
	public T newExts() {
		addExt(newModel());
		addExt(newStore());
		return (T)this;
	}
	
	/* (non-Javadoc)
	 * @see irille.pub.html.EMCrt#newList()
	 */
	@Override
	public ExtFile newList() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see irille.pub.html.EMCrt#newWin()
	 */
	@Override
	public ExtFile newWin() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see irille.pub.html.EMCrt#newForm()
	 */
	@Override
	public ExtFile newForm() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.EMCrts.EMCrt#newWinSearch()
	 */
	@Override
	public ExtFile newWinSearch() {
		return null;
	}

	
}
