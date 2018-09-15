package irille.pub.html;


/**
 * 表达式或变量（不包括JAVA基本类型）, 输出格式为：“命令(aaa,'bbb',this)”
 * 
 * @author whx
 * 
 */
public class ExtParas  extends ExtDime {
	private String _cmd;
	/**
	 * 构造方法，输出格式为：“命令(aaa,'bbb',this)”
	 * @param cmd 命令
	 * @param exps 参数
	 */
	public ExtParas(String cmd, Object... exps) {
		super(exps);
		setCloseStrToParas();
		_cmd=cmd;
	}
	
	/* (non-Javadoc)
	 * @see irille.pub.html.ExtDime#out(int, java.lang.StringBuilder)
	 */
	@Override
	public void out(int tabs, StringBuilder buf) {
		buf.append(_cmd);
		super.out(tabs, buf);
	}
}
