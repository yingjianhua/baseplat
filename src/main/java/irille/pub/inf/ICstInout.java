package irille.pub.inf;

import irille.pub.bean.IGoods;

import java.util.List;

public interface ICstInout {
	public final static int TYPE_IN = 1;
	public final static int TYPE_OUT = 2;
	public final static int TYPE_IN_RED = 3;
	public final static int TYPE_OUT_RED = 4;

	public List<IGoods> getCstInoutLines(int type);

}
