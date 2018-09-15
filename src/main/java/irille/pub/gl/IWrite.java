/**
 * 
 */
package irille.pub.gl;

import irille.gl.gl.GlNote;

/**
 * 销账计划的接口
 * 用于检查是否可以取消记账，如开始核销后需要抛出错误
 */
public interface IWrite {
	/**
	 * 记账处理
	 */
	//public void tally(GlNote note);

	/**
	 * 记账取消处理
	 */
	public void tallyCancel(GlNote note);

}
