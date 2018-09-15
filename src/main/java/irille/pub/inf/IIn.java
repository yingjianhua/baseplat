package irille.pub.inf;

import irille.gl.gs.GsIn;
import irille.pub.bean.Bean;
import irille.pub.bean.IGoods;

import java.util.List;

/**
 * 产生入库单的回调接口
 * @author whx
 * @version 创建时间：2014年11月21日 下午4:23:32
 */
public interface IIn<E extends Bean> {

	public void inOk(GsIn in, E model);

	public void inCancel(GsIn in, E model);
	
	public List<IGoods> getInLines(E model, int idx, int count);
	
	public int getInLinesCount(E model);
}
