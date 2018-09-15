/**
 * 
 */
package irille.pub.gl;

import irille.gl.gl.GlNote;


/**
 * 销账计划-核算扩展表的接口，用于实现记账时同时更改核销对象的余额值
 */
public interface IWriteoff {
	/**
	 * 记账处理
	 */
	public void tallyWriteoff(GlNote note);

	/**
	 * 记账取消处理
	 */
	public void tallyWriteoffCancel(GlNote note);

}
