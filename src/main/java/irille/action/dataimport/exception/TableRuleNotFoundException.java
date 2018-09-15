package irille.action.dataimport.exception;

public class TableRuleNotFoundException extends DataImportException{
	private static final long serialVersionUID = 1L;

	public TableRuleNotFoundException(String tableRuleId) {
		super("未找到指定的表规则，tableRuleId："+tableRuleId);
	}
}
