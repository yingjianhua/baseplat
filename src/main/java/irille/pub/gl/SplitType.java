/**
 * 
 */
package irille.pub.gl;

import irille.pub.tb.EnumLine;
import irille.pub.tb.IEnumOpt;

/**
 * @author surface1
 * 
 */
public enum SplitType implements IEnumOpt {
	AVG(1, "各项平均分"), WEIGHT(2, "按重量分摊"), AMT(3, "按金额"), NUMBER(4, "按数量");
	public static final String NAME = "分摊方式	";
	public static final SplitType DEFAULT = WEIGHT; // 定义缺省值
	private EnumLine _line;

	private SplitType(int key, String name) {
		_line = new EnumLine(this, key, name);
	}

	public EnumLine getLine() {
		return _line;
	}
} // @formatter:on

