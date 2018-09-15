//Created on 2005-9-17
package irille.pub.tb;

import irille.core.sys.SysTable;
import irille.pub.ClassTools;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.svr.Act;
import irille.pub.svr.ActOpt;
import irille.pub.svr.Env;
import irille.pub.svr.Act.OAct;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;
import java.util.stream.Stream;

/**
 * Title: 表<br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class Tb<THIS extends Tb> extends TbBase<THIS> {
	private static final Log LOG = new Log(Tb.class);

	private String _codeSqlTb; // 数据表名
	private String[] _dbFldCodes = null; // 内容如：user_name
	private Fld[] _dbFlds = null;
	private transient Class _classDao = null;
	private transient Vector<Index> _indexes = new Vector<Index>(); // 索引
	private Vector<Act> _acts = new Vector(); // 功能
	private TbBase _baseTb = null;
	private boolean _autoIncrement = false; // 主键自增长标志
	private boolean _autoLocal = false; // 主键自增长标志-本地控制
	private String _sqlIns = null; // Insert 的SQL语句，类似insert into
	                               // user(id,name,birthday,money)
	                               // values(?,?,?,?)
	private String _sqlUpd = null; // update user set money=? where id=?
	private String _sqlDel = null; // delete from user where id=?
	private String _sqlSelect = null; // select * from user where id=?
	private String _sqlSelectLock = null; // select * from user where id=? FOR

	public Tb(Class classBean, String tbName, String shortName) {
		super(classBean, Str.getClazzLastCode(classBean), tbName, shortName);
		String className = classBean.getName();
		if(className.indexOf(".domain.")!=-1) {
			String[] strs = className.substring(className.indexOf(".entity.")+".entity.".length()).split("\\.");
			_codeSqlTb = Str.tranFieldToLineLower(strs[strs.length-2]+strs[strs.length-1].split("$")[0]).substring(0);// 去掉前的"_"
		} else {
			_codeSqlTb = Str.tranFieldToLineLower(Str.getClazzLastCode(classBean)).substring(0);// 去掉前的"_"
		}
	}

	public Tb(Class classBean, String tbName) {
		this(classBean, tbName, tbName);
	}

	public Tb(Class classBean, String tbName, String shortName, Tb baseTb) {
		this(classBean, tbName, shortName);
		for (Fld fld : baseTb.getFlds()) {
			add(fld);
		}
		_indexes.addAll(baseTb.getIndexes());
		_baseTb = baseTb;
	}

	public Tb(Class classBean, String tbName, Tb baseTb) {
		this(classBean, tbName, tbName, baseTb);
	}

	public final int getID() {
		return SysTable.gtTable(getClazz()).getPkey();
	}

	public static final int getID(Class beanClass) {
		// TODO 待优化
		return SysTable.gtTable(beanClass).getPkey();
	}

	public static final Integer chkID(Class beanClass) {
		SysTable st = SysTable.chkTable(beanClass);
		if (st == null)
			return null;
		return SysTable.chkTable(beanClass).getPkey();
	}

	public static final Class getTableClass(Integer id) {
		// TODO 待优化
		return ClassTools.getClass(SysTable.gtTable(id).getCode());
	}

	public String getSqlSelect() {
		if (_sqlSelect == null)
			_sqlSelect = "SELECT * FROM " + _codeSqlTb + " WHERE pkey=?";
		return _sqlSelect;
	}

	public String getSqlSelectLock() {
		if (_sqlSelectLock == null)
			_sqlSelectLock = getSqlSelect() + Bean.lockSqlStr(true);
		return _sqlSelectLock;
	}

	public String getSqlIns() {
		if (_sqlIns == null) {
			int firstFldId = Env.INST.getDB().getInsFirstFldId(this);
			_sqlIns = "INSERT INTO " + _codeSqlTb + "(" + toStringFldCode(getDbFlds(), firstFldId, "", ",") + ") VALUES("
			    + outMark(getDbFlds(), firstFldId) + ")";
		}
		return _sqlIns;
	}

	public String getSqlUpd() {
		if (_sqlUpd == null)
			_sqlUpd = "UPDATE " + _codeSqlTb + " SET " + toStringFldCode(getDbFlds(), 1, "=?", ",") + " WHERE pkey=?";
		return _sqlUpd;
	}

	public String getSqlUpd(Fld... flds) {
		return "UPDATE " + _codeSqlTb + " SET " + toStringFldCode(flds, 1, "=?", ",") + " WHERE pkey=?";

	}

	public String getSqlDel() {
		if (_sqlDel == null)
			_sqlDel = "DELETE FROM " + _codeSqlTb + " WHERE pkey=?";
		return _sqlDel;
	}

	/**
	 * 将字段名转换成列表串，第个字段名后+middleStr，分隔符为split 如：name=?,code=?
	 * 
	 * @param flds
	 *          字段数组
	 * @param firstFldId
	 *          起始字段的ID，对于主键有时不用处理，所以会是1，否则从0开始
	 * @param middleStr
	 *          中间串，如"=?"
	 * @param split
	 *          分隔符，如","
	 * @return 结果
	 */
	public static String toStringFldCode(Fld[] flds, int firstFldId, String middleStr, String split) {
		StringBuilder buf = new StringBuilder();
		Fld fld;
		for (int i = firstFldId; i < flds.length; i++) {
			fld = flds[i];
			if (fld.isDatabaseField()) // 是数据库的字段
				buf.append(split + fld.getCodeSqlField() + middleStr);
		}
		return buf.toString().substring(1);
	}

	public static String toStringFldCode(Fld fld, String middleStr, String split) {
		StringBuilder buf = new StringBuilder();
		if (fld.isDatabaseField()) // 是数据库的字段
			buf.append(split + fld.getCodeSqlField() + middleStr);
		return buf.toString().substring(1);
	}

	private String outMark(Fld[] flds, int firstFldId) {
		StringBuilder buf = new StringBuilder();
		Fld fld;
		for (int i = firstFldId; i < flds.length; i++) {
			fld = flds[i];
			if (fld.isDatabaseField()) // 是数据库的字段
				buf.append(",?");
		}
		return buf.toString().substring(1);
	}

	/**
	 * 取SQL的表名，由小写与“_”组成，如“base_user"
	 * 
	 * @return
	 */
	public String getCodeSqlTb() {
		return _codeSqlTb;
	}

	public final Class getClassDao() {
		if (_classDao == null) {
			_classDao = ClassTools.getClassDaoByBean(this.getClazz().getName());
		}
		return _classDao;
	}

	// 存Tb的Hash表
	private static Hashtable<String, Tb> _tbHash = new Hashtable<String, Tb>();

	public static Tb getTBByBean(Class bean) {
		Tb tb = _tbHash.get(bean.getName());
		if (tb == null) {
			tb = (Tb) ClassTools.getStaticProerty(bean, "TB");
			_tbHash.put(bean.getName(), tb);
		}
		return tb;
	}

	public static Tb getTBByDao(Class dao) {
		Tb tb = _tbHash.get(dao.getName());
		if (tb == null) {
			tb = (Tb) ClassTools.getStaticProerty(ClassTools.getClassBeanByDao(dao.getName()), "TB");
			_tbHash.put(dao.getName(), tb);
		}
		return tb;
	}

	public final Fld[] getFldSqlWithout(Fld... fields) {
		Vector v = (Vector) _fields.clone();
		for (Fld field : fields)
			v.removeElement(field);
		for (int i = v.size() - 1; i >= 0; i--)
			// 去掉虚拟字段
			if (!((Fld) v.elementAt(i)).isDatabaseField())
				v.removeElement(v.elementAt(i));
		Fld[] flds = new Fld[v.size()];
		for (int i = 0; i < flds.length; i++) {
			flds[i] = (Fld) v.elementAt(i);
		}
		return flds;
	}

	public String[] getFldSqlCodes() {
		if (_dbFldCodes == null) {
			_dbFlds = getFldSqlWithout();
			_dbFldCodes = new String[_dbFlds.length];
			for (int i = 0; i < _dbFlds.length; i++) {
				_dbFldCodes[i] = _dbFlds[i].getCodeSqlField();
			}
		}
		return _dbFldCodes;
	}

	public Fld[] getFldsSql() {
		if (_dbFldCodes == null)
			getFldSqlCodes();// 初始化 _dbFlds与_dbFldCodes
		return _dbFlds;
	}

	private Fld[] getDbFlds() {
		if (_dbFlds == null)
			getFldsSql();
		return _dbFlds;
	}

	/**
	 * 取数据库字段的数量
	 * 
	 * @return
	 */
	public int sizeDbFlds() {
		return getFldSqlCodes().length;
	}

	public Vector<Index> getIndexes() {
		return _indexes;
	}

	public TbBase getBaseTb() {
		return _baseTb;
	}

	public String outMsgHead() {
		return LINE + getCode() + ":" + getName() + LN + LINE;
	}

	@Override
	public String toString() {
		return "[" + getCode() + "]" + getName();
	}

	/**
	 * 置indexes的值
	 * 
	 * @param indexes
	 */
	public Index addIndex(String code, boolean unique, IIndexFld... fields) {
		Index index = new Index(this, code, unique, fields);
		_indexes.add(index);
		return index;
	}

	/**
	 * 置indexes的值
	 * 
	 * @param indexes
	 */
	public Index addIndexUnique(String code, IIndexFld... fields) {
		return addIndex(code, true, fields);
	}

	/**
	 * 置indexes的值
	 * 
	 * @param indexes
	 */
	public Index addIndex(String code, boolean unique, IEnumFld... fields) {
		Index index = new Index(this, code, unique, fields);
		_indexes.add(index);
		return index;
	}

	/**
	 * 置indexes的值
	 * 
	 * @param indexes
	 */
	public Index addIndexUnique(String code, IEnumFld... fields) {
		return addIndex(code, true, fields);
	}

	/**
	 * 索引字段接口，用于建立索引对象
	 * Fld实现本接口，isIndexAscending()方法默认值为true，如要返回降序的对象，则调用Fld类的indexDesc()方法
	 * 
	 * @author whx
	 * 
	 */
	public static interface IIndexFld {
		public String getCodeSqlField();

		public String getCode();

		public String getName();

		public Class getJavaType();

		public boolean isIndexAscending(); // 是否升序

	}

	/**
	 * 索引降序类
	 * 
	 * @author whx
	 * 
	 */
	public static class IndexDescending implements IIndexFld {
		private Fld _fld;

		public IndexDescending(Fld fld) {
			_fld = fld;
		}

		@Override
		public String getCodeSqlField() {
			return _fld.getCodeSqlField();
		}

		@Override
		public String getCode() {
			return _fld.getCode();
		}

		@Override
		public String getName() {
			return _fld.getName();
		}

		@Override
		public boolean isIndexAscending() {
			return false;
		}

		@Override
		public Class getJavaType() {
			return _fld.getJavaType();
		}

	}

	public Tb addActUpd() {
		return addAct(new Act(this, OAct.UPD));
	}

	public Tb addActIns() {
		return addAct(new Act(this, OAct.INS));
	}

	public Tb addActDel() {
		return addAct(new Act(this, OAct.DEL));
	}

	public Tb addActList() {
		return addAct(new Act(this, OAct.LIST));
	}

	public Tb addActPrint() {
		return addAct(new Act(this, OAct.PRINT));
	}

	public Tb addActEnabled() {
		addAct(new Act(this, OAct.DO_ENABLED));
		addAct(new Act(this, OAct.UN_ENABLED));
		return this;
	}

	public Tb addActReport() {
		return addAct(new Act(this, OAct.REPORT));
	}
	public Tb addActEdit() {
		return addAct(new Act(this, OAct.EDIT));
	}
	public Tb addActEdit(String name) {
		Act act = new Act(this, OAct.EDIT);
		act.setName(name);
		return addAct(act);
	}

	public Tb addActIUDL() {
		addActList();
		addActIns();
		addActUpd();
		addActDel();
		return this;
	}

	// 附件管理的增、删、改、查、下载功能
	public Tb addActFileIUDL() {
		addActOpt("fileIns", "附件新增");
		addActOpt("fileUpd", "附件修改");
		addActOpt("fileDel", "附件删除");
		addActOpt("fileList", "附件查询");
		addActOpt("fileDown", "附件下载");
		return this;
	}

	// 导出功能
	public Tb addActExport() {
		addActOpt("export", "导出");
		return this;
	}

	// 审核弃审功能
	public Tb addActApprove() {
		addAct(new Act(this, OAct.DO_APPR));
		addAct(new Act(this, OAct.UN_APPR));
		return this;
	}
	
	public Tb addActTally() {
		addAct(new Act(this, OAct.DO_TALLY));
		addAct(new Act(this, OAct.UN_TALLY));
		return this;
	}
	
	public Tb addActNote() {
		addAct(new Act(this, OAct.DO_NOTE));
		return this;
	}
	
	public Tb addActApproveDo() {
		addAct(new Act(this, OAct.DO_APPR));
		return this;
	}
	
	public Tb addActApproveUn() {
		addAct(new Act(this, OAct.UN_APPR));
		return this;
	}

	public Tb addActFileList() {
		addActOpt("fileList", "附件查询");
		addActOpt("fileDown", "附件下载");
		return this;
	}

	public Tb addActOpt(String code, String name, String icon) {
		return addAct(new ActOpt(this, code, name, icon,true));
	}

	public Tb addActOpt(String code, String name) {
		return addActOpt(code, name, true);
	}
	public Tb addActOpt(String code, String name, boolean sel) {
		Act act = new ActOpt(this, code, name, "请在Tb.accActOpt()中指定ICON");
		act.setNeedSel(sel);
		return addAct(act);
	}
	public Tb addActOpt(String code, String name,String icon, boolean sel) {
		Act act = new ActOpt(this, code, name, icon);
		act.setNeedSel(sel);
		return addAct(act);
	}


	public Tb addAct(Act act) {
		if (checkAct(act.getCode()) != null) // 是Bean对象允许重复
			throw LOG.err("actError", "表[{0}]的Act[{1}]重复!", getCode(), act.getCode());
		_acts.add(act);
		return this;
	}

	public final Act getAct(String code) {
		Act act = checkAct(code);
		if (act != null)
			return act;
		throw LOG.err("ActNotInTable", "表[{0}]中无Act[{1}]!", getCode(), code);
	}

	public final Act getAct(IEnumOpt act) {
		return getAct(Str.tranLineToField(act.name()));
	}

	public final Act checkAct(IEnumOpt act) {
		return checkAct(Str.tranLineToField(act.name()));
	}

	public final Act checkAct(String code) {
		for (Act act : _acts) {
			if (act.getCode().equals(code))
				return act;
		}
		return null;
	}

	public final Act getActApprove() {
		return getAct(OAct.DO_APPR);
	}

	public final Act getActUnapprove() {
		return getAct(OAct.UN_APPR);
	}

	public final Act getActUpd() {
		return getAct(OAct.UPD);
	}

	public final Act getActIns() {
		return getAct(OAct.INS);
	}

	public final Act getActDel() {
		return getAct(OAct.DEL);
	}
	
	public final Act getActEdit() {
		return getAct(OAct.EDIT);
	}

	public final Act getActList() {
		return getAct(OAct.LIST);
	}

	public final Act getActPrint() {
		return getAct(OAct.PRINT);
	}

	public final Act getActEnabled() {
		return getAct(OAct.DO_ENABLED);
	}

	public final Act getActReport() {
		return getAct(OAct.REPORT);
	}

	public final Vector<Act> getActs() {
		return _acts;
	}

	/**
	 * Title: 索引类<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public static class Index {
		private String _code;
		private boolean _unique;
		private IIndexFld[] _fields;
		private Tb _tb;
		private String _selectSql = null;

		/**
		 * 构造方法
		 * 
		 * @param tb
		 * @param code
		 * @param unique
		 *          唯一索引标志
		 * @param fields
		 *          字段清单
		 */
		public Index(Tb tb, String code, boolean unique, IIndexFld... fields) {
			_tb = tb;
			_code = code;
			_unique = unique;
			_fields = fields;
		}

		/**
		 * 构造方法
		 * 
		 * @param tb
		 * @param code
		 * @param unique
		 *          唯一索引标志
		 * @param fields
		 *          字段清单
		 */
		public Index(Tb tb, String code, boolean unique, IEnumFld... fields) {
			_tb = tb;
			_code = code;
			_unique = unique;
			_fields = new Fld[fields.length];
			for (int i = 0; i < fields.length; i++)
				_fields[i] = fields[i].getFld();
		}

		/**
		 * 取索引的SELECT语句，如：SELECT * FROM base_user WHERE code=? AND name=?
		 * 
		 * @return
		 */
		public String getSelectSql() {
			if (_selectSql == null) {
				StringBuilder buf = new StringBuilder();
				for (IIndexFld fld : _fields)
					buf.append(" AND " + fld.getCodeSqlField() + "=?");
				_selectSql = "SELECT * FROM " + _tb._codeSqlTb + " WHERE " + buf.toString().substring(5);
			}
			return _selectSql;
		}

		/**
		 * 取code
		 * 
		 * @return code
		 */
		public String getCode() {
			return this._code;
		}

		public String getCodeUpper() {
			return Str.tranFieldToLineUpper(_code);
		}

		/**
		 * 返回大写的字段名，以“,”分隔
		 * 
		 * @return
		 */
		public String getFldsCodeUpper() {
			StringBuilder buf = new StringBuilder();
			IIndexFld fld;
			for (int i = 0; i < _fields.length; i++) {
				fld = _fields[i];
				buf.append("," + Str.tranFieldToLineUpper(fld.getCode()));
			}
			return buf.toString().substring(1);
		}

		public String getCodeSql() {
			return Str.tranFieldToLineLower(Str.getClazzLastCode(getCode()));
		}

		/**
		 * 取fields
		 * 
		 * @return fields
		 */
		public IIndexFld[] getFields() {
			return this._fields;
		}

		/**
		 * 取unique
		 * 
		 * @return unique
		 */
		public boolean isUnique() {
			return this._unique;
		}

		public Tb getTb() {
			return _tb;
		}

		/**
		 * 返回格式为: fldName1="value1",fldName2="value2"类似的文本
		 * 
		 * @param values
		 * @return
		 */
		public String toString(Serializable... values) {
			StringBuilder buf = new StringBuilder();
			IIndexFld fld;
			for (int i = 0; i < _fields.length; i++) {
				fld = _fields[i];
				buf.append("," + fld.getName() + "=\"" + values[i] + "\"");
			}
			return buf.toString().substring(1);
		}
	}

	/**
	 * 设定表主键为自增长类型
	 * 
	 * @return 当前对象
	 */
	public THIS setAutoIncrement() {
		_autoIncrement = true;
		return (THIS) this;
	}

	public boolean isAutoIncrement() {
		return _autoIncrement;
	}

	/**
	 * 设定表主键为自增长类型--本地控制
	 */
	public THIS setAutoLocal() {
		_autoLocal = true;
		return (THIS) this;
	}

	public boolean isAutoLocal() {
		return _autoLocal;
	}
	@Override
	public OBeanType getBeanType() {
		return OBeanType.TB;
	}


}
