/**
 * 
 */
package irille.pub.print;

import irille.pub.view.VFld.OAlign;


/**
 * 表格处理的常量定义接口
 * @author whx
 *
 */
public interface SheetVarsInf {
	public static final float AVG = -1;
	public static final float NOSET = -99999; // 未设置，用于边框等，一般情况下没设置取上级的值
	public static final float DEFAILT_GAP_LEFT_RIGHT = (float) 0.2;
	public static final float DEFAILT_GAP_UP_DOWN = (float) 0.1;
	public static final OAlign ALIGN=OAlign.MIDDLE_LEFT;
}
