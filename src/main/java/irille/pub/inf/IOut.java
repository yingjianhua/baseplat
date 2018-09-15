package irille.pub.inf;

import irille.gl.gs.GsOut;
import irille.pub.bean.Bean;
import irille.pub.bean.IGoods;

import java.util.List;

public interface IOut<E extends Bean> {

	public void outOk(GsOut out, E model);

	public void outCancel(GsOut out, E model);
	
	public List<IGoods> getOutLines(E model, int idx, int count);
	
	public int getOutLinesCount(E model);
}
