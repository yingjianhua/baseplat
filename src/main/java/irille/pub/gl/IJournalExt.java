/**
 * 
 */
package irille.pub.gl;

import irille.gl.gl.GlDaybook;
import irille.gl.gl.GlDaybookLine;

/**
 * 分户账扩展表的接口，用于实现记账及记账取消相关的操作
 * @author whx
 *
 */
public interface IJournalExt {
	/**
	 * 记账处理
	 * @param daybook
	 * @param daybookLine
	 */
	public void tallyExt(GlDaybook daybook,GlDaybookLine daybookLine);

	/**
	 * 记账取消处理
	 * @param daybook
	 * @param daybookLine
	 */
	public void tallyExtCancel(GlDaybook daybook,GlDaybookLine daybookLine);

	/**
	 * 记明细行
	 * @param daybook
	 * @param daybookLine
	 */
	public void tallyLine(GlDaybook daybook,GlDaybookLine daybookLine);
	
	/**
	 * 记明细行取消
	 * @param daybook
	 * @param daybookLine
	 */
	public void tallyLineCancel(GlDaybook daybook,GlDaybookLine daybookLine);
	
}
