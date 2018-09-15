/**
 * 
 */
package irille.pub.bean;

import irille.core.sys.SysSeq;

/**
 * 顺序号接口
 * 对要取顺序号的Bean要实现此接口
 * BeanForm已实现此接口，其子类就不用实现
 * @author whx
 *
 */
public interface ISeq {
	public void initSeq(SysSeq seq);

}
