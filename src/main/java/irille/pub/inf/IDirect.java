package irille.pub.inf;

import irille.pub.bean.Bean;

public interface IDirect<E extends Bean> {

	public void directOk(E model);

	public void directCancel(E model);
}
