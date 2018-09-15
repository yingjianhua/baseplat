package irille.action;

import irille.pub.Log;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanMain;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

import java.util.List;

/**
 * 支持诠释图的表格数据提交
 * @author whx
 * @param <MAIN>
 */
public abstract class ActionSync<MAIN extends BeanMain> extends ActionBase<MAIN> {
	public static final Log LOG = new Log(ActionSync.class);
	private String _mainPkey; // 主表的主键值
	protected List<MAIN> _insLines; //新增的记录集合
	protected List<MAIN> _updLines; //修改的记录集合
	protected List<MAIN> _delLines; //删除的记录集合

	public String getMainPkey() {
		return _mainPkey;
	}

	public void setMainPkey(String mainPkey) {
		_mainPkey = mainPkey;
	}

	public void syncBefore() {
	}

	public void syncAfter() {
	}

	/**
	 * 表格数据同步入口
	 * 新增、修改、删除操作
	 * @throws Exception
	 */
	public void sync() throws Exception {
		syncBefore();
		if (_insLines != null)
			for (MAIN line : _insLines) {
				IduIns ins = newIns();
				ins.setB(line);
				ins.commit();
			}
		if (_updLines != null)
			for (MAIN line : _updLines) {
				//非自增情况，前台EXT暂时无法区分新增与修改
				if (tb().isAutoIncrement() == false && BeanBase.chk(line.clazz(), line.getPkey()) == null) {
					IduIns ins = newIns();
					ins.setB(line);
					ins.commit();
					continue;
				}
				IduUpd upd = newUpd();
				upd.setB(line);
				upd.commit();
			}
		if (_delLines != null)
			for (MAIN line : _delLines) {
				IduDel del = newDel();
				del.setB(line);
				del.commit();
			}
		syncAfter();
		writeSuccess();
	}
	
}
