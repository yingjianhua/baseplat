package irille.pub.idu;

import irille.pub.Cn;
import irille.pub.bean.Bean;
import irille.pub.svr.ProvDataCtrl;

import java.util.List;

/**
 * 数据库功能操作：查询
 * 用于查询性的操作，也需要特定权限的，需要通过此类
 * 在子类中必须声明常量CN，用类似的语句：public static Cn CN = new Cn("功能 代码", "功能名称");
 * @author whx
 * 
 * @param <BEAN>
 */
public class IduPage<T extends IduPage, BEAN extends Bean> extends Idu<T, BEAN> {
	public static Cn CN = new Cn("list", "查询");
	protected int _start;
	protected int _limit;
	protected String _where;
	protected List<BEAN> _list;
	protected int _count;

	@Override
	public final void commit() {
		before();
		valid();
		run();
		//资源控制
		String sql = getWhere();
		String countSql = sql;
		int index = sql.toUpperCase().indexOf("ORDER BY");
		if (index > -1)
			countSql = sql.substring(0, index);

		if (ProvDataCtrl.needControl(clazz()) && getUser().getLoginName().equals("admin") == false) {
			String prv = ProvDataCtrl.getWhere(Idu.getUser(), clazz());
			if (index > -1) {
				sql = sql.substring(0, index) + " AND " + prv + sql.substring(index - 1);
				countSql += " AND " + prv;
			} else {
				sql += " AND " + prv;
				countSql = sql;
			}
		}
		System.err.println("资源权限-" + clazz().getSimpleName() + "：" + sql);
		_list = list(getStart(), getLimit(), sql);
		_count = countWhere(countSql);
		after();
	}

	public static void main(String[] args) {
		String aa = "1=1 order by pkey";
		String ww = " AND 1=2 ";
		int index = aa.indexOf("order by");
		System.err.println(aa.substring(0, index) + ww + aa.substring(index));
	}

	public int getStart() {
		return _start;
	}

	public void setStart(int start) {
		_start = start;
	}

	public int getLimit() {
		return _limit;
	}

	public void setLimit(int limit) {
		_limit = limit;
	}

	public String getWhere() {
		return _where;
	}

	public void setWhere(String where) {
		_where = where;
	}

	public List<BEAN> getList() {
		return _list;
	}

	public void setList(List<BEAN> list) {
		_list = list;
	}

	public int getCount() {
		return _count;
	}

	public void setCount(int count) {
		_count = count;
	}

}
