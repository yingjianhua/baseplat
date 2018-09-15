package irille.action;

import irille.core.sys.SysTable;
import irille.core.sys.SysUser;
import irille.gl.gs.GsGoods;
import irille.pub.ClassTools;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanInt;
import irille.pub.bean.BeanLong;
import irille.pub.bean.BeanMain;
import irille.pub.bean.BeanStr;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduEnabled;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduPage;
import irille.pub.idu.IduUnEnabled;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpd;
import irille.pub.inf.IExtName;
import irille.pub.svr.Env;
import irille.pub.svr.LoginUserMsg;
import irille.pub.tb.Fld;
import irille.pub.tb.FldEnumByte;
import irille.pub.tb.FldLongTbObj;
import irille.pub.tb.FldOptCust;
import irille.pub.tb.FldOutKey;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Infs.IOptLine;
import irille.pub.tb.OptBase;
import irille.pub.tb.OptCust;
import irille.pub.tb.Tb;
import irille.view.BaseView;
import irille.view.ResultView;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionSupport;

public abstract class ExtAction<THIS extends BeanMain> extends ActionSupport implements RequestAware, SessionAware,
    ApplicationAware {
	public static final Log LOG = new Log(ExtAction.class);
	private static final long serialVersionUID = 1L;

	public static final String QUERY_PROPERTY = "property"; // EXT查询的JSON参数的NAME
	public static final String QUERY_VALUE = "value";
	public static final String BEAN_SPLIT = "##"; // OUTKEY对象键值分割符
	public static final String STORE_ROOT = "items";
	public static final String STORE_TOTAL = "total";

	protected Map<String, Object> request;
	protected Map<String, Object> session;
	protected Map<String, Object> application;
	protected THIS _bean = null;
	protected String _result = null;
	protected String _sarg1 = null;
	private Serializable _pkey = null;
	private String _pkeys = null;
	private short _rowVersion = -1;
	private String _rowVersions = null;
	private int _page;
	private int _start;
	private int _limit;
	private String _query;
	private String _filter;
	private boolean _insFlag;
	// 公共导出相关
	private JSONArray _expJa; // 待导出的数据
	// 对应Action的方法，默认为list；像职员与金融组织会不同如listDw listQy
	private String _expMethod;
	// 对应前台GRID列名如：code##代码,name##名称
	private String _expFields;

	public static final String LOGIN = "login";
	public static final String LOGINCO = "loginco";
	public static final String TRENDS = "trends";
	public static final String RTRENDS = "rtrends";

	public Tb tb() {
		if (_bean != null)
			return (Tb) _bean.gtTB();
		return Bean.tb(beanClazz());
	}

	//重构后取消
	//	public final String beanClazz() {
	//		String code = getClass().getName().replace(".action.", ".bean.");
	//		return code.substring(0, code.length() - 6);
	//	}

	public abstract Class beanClazz();

	public final THIS newBean() throws Exception {
		return (THIS) ClassTools.newInstance(beanClazz());
	}

	public final IduIns newIns() throws Exception {
		Class c;
		try {
			c = Class.forName(beanClazz().getName() + "DAO$Ins");
		} catch (ClassNotFoundException e) {
			c = Class.forName("irille.pub.idu.IduBase$Ins");
		}
		IduIns ins = (IduIns) c.newInstance();
		ins.setClazz(beanClazz());
		return ins;
	}

	public final IduUpd newUpd() throws Exception {
		Class c;
		try {
			c = Class.forName(beanClazz().getName() + "DAO$Upd");
		} catch (ClassNotFoundException e) {
			c = Class.forName("irille.pub.idu.IduBase$Upd");
		}
		IduUpd upd = (IduUpd) c.newInstance();
		upd.setClazz(beanClazz());
		return upd;
	}

	public final IduDel newDel() throws Exception {
		Class c;
		try {
			c = Class.forName(beanClazz().getName() + "DAO$Del");
		} catch (ClassNotFoundException e) {
			c = Class.forName("irille.pub.idu.IduBase$Del");
		}
		IduDel del = (IduDel) c.newInstance();
		del.setClazz(beanClazz());
		return del;
	}

	public final IduEnabled newEnabled() throws Exception {
		Class c;
		try {
			c = Class.forName(beanClazz().getName() + "DAO$Enabled");
		} catch (ClassNotFoundException e) {
			c = Class.forName("irille.pub.idu.IduBase$Enabled");
		}
		IduEnabled act = (IduEnabled) c.newInstance();
		act.setClazz(beanClazz());
		return act;
	}

	public final IduUnEnabled newUnEnabled() throws Exception {
		Class c;
		try {
			c = Class.forName(beanClazz().getName() + "DAO$UnEnabled");
		} catch (ClassNotFoundException e) {
			c = Class.forName("irille.pub.idu.IduBase$UnEnabled");
		}
		IduUnEnabled act = (IduUnEnabled) c.newInstance();
		act.setClazz(beanClazz());
		return act;
	}

	public final IduPage newPage() throws Exception {
		Class c;
		try {
			c = Class.forName(beanClazz().getName() + "DAO$Page");
		} catch (ClassNotFoundException e) {
			c = Class.forName("irille.pub.idu.IduBase$Page");
		}
		IduPage page = (IduPage) c.newInstance();
		page.setClazz(beanClazz());
		return page;
	}

	public final IduApprove newApprove() throws Exception {
		Class c;
		try {
			c = Class.forName(beanClazz().getName() + "DAO$Approve");
		} catch (ClassNotFoundException e) {
			throw LOG.err("notDefine", "[{0}]类未定义审核方法", beanClazz().getName());
		}
		IduApprove appr = (IduApprove) c.newInstance();
		appr.setClazz(beanClazz());
		return appr;
	}

	public final IduUnapprove newUnapprove() throws Exception {
		Class c;
		try {
			c = Class.forName(beanClazz().getName() + "DAO$Unapprove");
		} catch (ClassNotFoundException e) {
			throw LOG.err("notDefine", "[{0}]类未定义弃审方法", beanClazz().getName());
		}
		IduUnapprove act = (IduUnapprove) c.newInstance();
		act.setClazz(beanClazz());
		return act;
	}
	
	/**
	 * 获取文件上传路径的 项目相对路径
	 * @return
	 */
	public static String getUploadPath() {
		return getUploadPath(false);
	}
	/**
	 * 获取文件上传路径，绝对路径或相对路径
	 * @param real
	 * @return
	 */
	public static String getUploadPath(boolean real) {
		String path = ServletActionContext.getServletContext().getInitParameter("uploadPath");
		if(Str.isEmpty(path)) 
			path = "uploads";
		if(path.indexOf(":")>0)
			return path;
		if(real) {
			return ServletActionContext.getServletContext().getRealPath("/") + path;
		} else {
			return path;
		}
	}
	// 取下载路径--有绝对路径的情况
	public String getDownPath() {
		return getUploadPath(true);
	}

	// ext.model.load方法 默认固定参数名为id
	public Serializable getId() {
		return _pkey;
	}

	public void setId(String pkey) {
		setPkey(pkey);
	}

	public Serializable getPkey() {
		return _pkey;
	}
	
	/**
	 * 配合longPkey选择器使用
	 * @throws JSONException 
	 * @throws IOException 
	 * */
	public void getLongPkey() throws JSONException, IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		Long longPkey = Bean.gtLongPkey((Integer)getPkey(), SysTable.gtTable(beanClazz()).getPkey());
		JSONObject json = new JSONObject();
		json.put("success", true);
		json.put("longPkey", longPkey);
		response.getWriter().print(json.toString());
	}

	public void setPkey(String pkey) {
		Tb tb = tb();
		if (BeanInt.class.isAssignableFrom(tb.getClazz()))
			_pkey = Integer.parseInt(pkey);
		else if (BeanStr.class.isAssignableFrom(tb.getClazz()))
			_pkey = pkey;
		else if (BeanLong.class.isAssignableFrom(tb.getClazz()))
			_pkey = Long.parseLong(pkey);
		else
			throw LOG.err();
	}

	public String getPkeys() {
		return _pkeys;
	}

	public void setPkeys(String pkeys) {
		_pkeys = pkeys;
	}
	
	public short getRowVersion() {
		return _rowVersion;
	}

	public void setRowVersion(short rowVersion) {
		_rowVersion = rowVersion;
	}
	
	public String getRowVersions() {
		return _rowVersions;
	}
	
	public void setRowVersions(String rowVersions) {
		_rowVersions = rowVersions;
	}
	
	public String getResult() {
		return _result;
	}

	public void setResult(String result) {
		_result = result;
	}

	public String getSarg1() {
		return _sarg1;
	}

	public void setSarg1(String sarg1) {
		this._sarg1 = sarg1;
	}

	public static String toDateJson(Date date) {
		return date == null ? null : new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	public static String toTimeJson(Date date) {
		return date == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	public int getPage() {
		return _page;
	}

	public void setPage(int page) {
		_page = page;
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

	public String getQuery() {
		return _query;
	}

	public void setQuery(String query) {
		_query = query;
	}

	public String getFilter() {
		return _filter;
	}

	public void setFilter(String filter) {
		_filter = filter;
	}

	public Map<String, Object> getRequest() {
		return request;
	}

	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public Map<String, Object> getApplication() {
		return application;
	}

	public void setApplication(Map<String, Object> application) {
		this.application = application;
	}

	public boolean isInsFlag() {
		return _insFlag;
	}

	public void setInsFlag(boolean insFlag) {
		_insFlag = insFlag;
	}

	public String getExpMethod() {
		return _expMethod;
	}

	public void setExpMethod(String expMethod) {
		_expMethod = expMethod;
	}

	public String getExpFields() {
		return _expFields;
	}

	public void setExpFields(String expFields) {
		_expFields = expFields;
	}

	public String crtAll() {
		return "1=1";
	}

	public SysUser getLoginSys() {
		HttpServletResponse response = ServletActionContext.getResponse();
		Object obj = getSession().get(LOGIN);
		return ((LoginUserMsg) obj).getUser();
	}

	// 产生查询语句 OR关系 -- 外键选择器调用
	public String crtQuery() throws JSONException {
		if (Str.isEmpty(getQuery()))
			return crtQueryAll() + orderBy();
		// {"property":"flds","value":"1=1"} //初始化选择器的情况
		// [{"property":"flds","value":"pkey,name"},{"property":"param","value":"1111"}]
		JSONArray ja = new JSONArray(getQuery());
		String[] flds = null;
		String param = null;
		String diy = ""; // 获取前台EXT动态写死的SQL条件
		for (int i = 0; i < ja.length(); i++) {
			String pro = ja.getJSONObject(i).getString(QUERY_PROPERTY);
			if (pro.equals("flds")) {
				if (ja.getJSONObject(i).getString(QUERY_VALUE).equals("1=1") == false)
					flds = ja.getJSONObject(i).getString(QUERY_VALUE).split(",");
			} else if (pro.equals("param"))
				param = ja.getJSONObject(i).getString(QUERY_VALUE);
			else
				diy = ja.getJSONObject(i).getString(QUERY_VALUE);
		}
		if (flds == null && Str.isEmpty(diy)) {
			if (tb().chk("enabled")) 
				return crtQueryAll() + " AND enabled = 1" + orderBy();
			return crtQueryAll() + orderBy();
		}
		String sql = "";
		Tb tb = tb();
		if (flds != null) {
			for (String line : flds)
				if (line.equals("goods"))//TODO goods字段做特殊处理，待日后规范再修改
					sql += " OR goods IN (SELECT " + GsGoods.T.PKEY.getFld().getCodeSqlField() + " FROM " + GsGoods.TB.getCodeSqlTb() + " WHERE " + GsGoods.T.CODE.getFld().getCodeSqlField() + " LIKE '%" + param + "%' OR " + GsGoods.T.NAME.getFld().getCodeSqlField() + " LIKE '%" + param + "%' OR " + GsGoods.T.SPEC.getFld().getCodeSqlField() + " LIKE '%" + param + "%')";
				else
					sql += " OR " + Env.INST.getDB().crtWhereSearch(tb.get(line), param);
			sql = sql.substring(4);
		}
		String where = crtQueryAll();
		if (Str.isEmpty(diy) == false)
			where += " AND " + diy;
		if (Str.isEmpty(sql) == false)
			where += " AND (" + sql + ")";
		if (tb().chk("enabled")) 
			where += " AND enabled = 1";
		return where + orderBy();
	}

	public String crtQueryAll() {
		return crtAll();
	}

	// 产生查询语句 AND关系
	public String crtFilter() throws JSONException {
		if (Str.isEmpty(getFilter()))
			return crtFilterAll() + orderBy();
		JSONArray ja = new JSONArray(getFilter());
		String sql = "";
		Tb tb = tb();
		for (int i = 0; i < ja.length(); i++) {
			JSONObject json = ja.getJSONObject(i);
			String fldName = json.getString(QUERY_PROPERTY);
			String param = json.getString(QUERY_VALUE);
			if (Str.isEmpty(param))
				continue;
			if (!tb.chk(fldName))
				continue;
			Fld fld = tb.get(fldName);
			if (fld == null)
				continue;
			sql += " AND " + Env.INST.getDB().crtWhereSearch(fld, param);
		}
		return crtFilterAll() + sql + orderBy();
	}

	public String crtFilterAll() {
		return crtAll();
	}

	public String orderBy() {
		return " ORDER BY PKEY DESC";
	}

	public String orderByAsc() {
		return " ORDER BY PKEY ASC";
	}

	public void list() throws Exception {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		// 目前过滤器的搜索，是肯定会带初始条件的
		String where = Str.isEmpty(getQuery()) ? crtFilter() : crtQuery();
		IduPage page = newPage();
		page.setStart(getStart());
		page.setLimit(getLimit());
		page.setWhere(where);
		page.commit();
		List<THIS> list = page.getList();
		JSONObject lineJson = null;
		for (THIS line : list) {
			lineJson = crtJsonByBean(line);
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		json.put(STORE_TOTAL, page.getCount());
		writerOrExport(json);
	}

	public void writerOrExport(JSONObject json) throws Exception {
		if (Str.isEmpty(getExpFields()))
			ServletActionContext.getResponse().getWriter().print(json.toString());
		else
			_expJa = json.getJSONArray(STORE_ROOT);
	}

	public void insBefore() {
	}

	public void updBefore() {
	}

	public void insAfter() {
	}

	public void updAfter() {
	}

	public void ins() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = crtJsonByBean(insRun(), "bean.");
		json.put("success", true);
		response.getWriter().print(json.toString());
	}

	public THIS insRun() throws Exception {
		insBefore();
		IduIns ins = newIns();
		ins.setB(_bean);
		ins.commit();
		insAfter();
		return (THIS) ins.getB();
	}

	public void upd() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = crtJsonByBean(updRun(), "bean.");
		json.put("success", true);
		response.getWriter().print(json.toString());
	}

	public THIS updRun() throws Exception {
		updBefore();
		IduUpd upd = newUpd();
		upd.setB(_bean);
		upd.commit();
		updAfter();
		return (THIS) upd.getB();
	}

	// 如自增型PKEY，还需要返回作处理
	//	public void iu() throws Exception {
	//		iuBefore();
	//		HttpServletResponse response = ServletActionContext.getResponse();
	//		Bean rtn = null;
	//		if (isInsFlag())
	//			rtn = ins();
	//		else
	//			rtn = upd();
	//		JSONObject json = crtJsonByBean(rtn, "bean.");
	//		json.put("success", true);
	//		iuAfter();
	//		response.getWriter().print(json.toString());
	//	}

	public void load() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		Bean bean = newBean().load(getPkey());
		JSONObject lineJson = crtJsonByBean(bean);
		response.getWriter().print(lineJson.toString());
	}

	public JSONObject crtJsonByBean(Bean bean) throws Exception {
		return crtJsonByBean(bean, "");
	}

	/**
	 * 将POJO对象中转为对应的JSONOBJECT对象 列表数据与，修改加载的数据字段无前缀，为字段代码
	 * 新增或修改操作返回时，带前缀bean.，用于EXT的new xxxModel()去替换list内容
	 * @param bean 实体
	 * @param pref 字段前缀
	 * @return
	 * @throws Exception
	 */
	public JSONObject crtJsonByBean(Bean bean, String pref) throws Exception {
		JSONObject lineJson = new JSONObject();
		for (Fld fld : bean.gtTB().getFlds()) {
			String fldcode = fld.getCode();
			//			if (fld instanceof FldLines || fld instanceof FldV)
			//				continue;
			if (fld.isDatabaseField() == false)
				continue;
			Object obj = bean.propertyValue(fld);
			if (obj == null)
				continue;
			if (fld instanceof FldOutKey) {
				if (obj instanceof String && Str.isEmpty(obj.toString()))
					continue;
				BeanMain outkey = (BeanMain) bean.propertyValueOBJ(fld);
				String show = outkey.getPkey().toString();
				if (outkey instanceof IExtName)
					show = ((IExtName) outkey).getExtName();
				lineJson.put(pref + fldcode, outkey.getPkey() + BEAN_SPLIT + show);
				continue;
			}
			if (fld instanceof FldLongTbObj) {
				if (obj instanceof String && Str.isEmpty(obj.toString()))
					continue;
				Tb tb = Tb.getTBByBean(Bean.gtLongClass(Long.parseLong(obj.toString())));
				lineJson.put(pref + fldcode, obj.toString() + BEAN_SPLIT + tb.getShortName());
				continue;
			}
			if (fld instanceof FldOptCust) {
				OptBase opt = ((FldOptCust) fld).getOpt(true);
				String optname = "错误的值";
				IOptLine lineI = opt.chk(obj.toString());
				if (lineI != null)
					optname = lineI.getName();
				lineJson.put(pref + fldcode, obj + BEAN_SPLIT + optname);
				continue;
			}
			if (fld.getSqlType() == Types.DATE) {
				lineJson.put(pref + fldcode, toDateJson((Date) obj));
				continue;
			}
			if (fld.getSqlType() == Types.TIME) {
				lineJson.put(pref + fldcode, toTimeJson((Date) obj));
				continue;
			}
			lineJson.put(pref + fldcode, obj);
		}
		//外部扩展重载用
		crtJsonExt(lineJson, bean, pref);
		return lineJson;
	}

	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		return json;
	}

	public void del() throws Exception {
		IduDel del = newDel();
		del.setBKey(getPkey());
		del.commit();
		writeSuccess();
	}

	public void delMulti() throws Exception {
		Serializable[] svs = tranMuliKeys(getPkeys().split(","));
		for (Serializable line : svs) {
			IduDel act = newDel();
			act.setBKey(line);
			act.commit();
		}
		writeSuccess();
	}

	public void doEnabled() throws Exception {
		Serializable[] svs = tranMuliKeys(getPkeys().split(","));
		for (Serializable line : svs) {
			IduEnabled act = newEnabled();
			act.setBKey(line);
			act.commit();
		}
		writeSuccess();
	}

	public void unEnabled() throws Exception {
		Serializable[] svs = tranMuliKeys(getPkeys().split(","));
		for (Serializable line : svs) {
			IduUnEnabled act = newUnEnabled();
			act.setBKey(line);
			act.commit();
		}
		writeSuccess();
	}

	private Serializable[] tranMuliKeys(String[] ary) {
		Tb tb = tb();
		Class clazz = null;
		if (BeanInt.class.isAssignableFrom(tb.getClazz()))
			clazz = Integer.class;
		else if (BeanStr.class.isAssignableFrom(tb.getClazz()))
			clazz = String.class;
		else if (BeanLong.class.isAssignableFrom(tb.getClazz()))
			clazz = Long.class;
		Serializable[] svs = new Serializable[ary.length];
		for (int i = 0; i < ary.length; i++) {
			String line = ary[i];
			Serializable obj = line;
			if (clazz.equals(Integer.class))
				obj = Integer.parseInt(line);
			else if (clazz.equals(Long.class))
				obj = Long.parseLong(line);
			svs[i] = obj;
		}
		return svs;
	}

	public void writeSuccess() throws IOException, JSONException {
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = new JSONObject().put("success", true);
		response.getWriter().print(json.toString());
	}

	public void writeSuccess(Bean bean) throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = crtJsonByBean(bean, "bean.").put("success", true);
		response.getWriter().print(json.toString());
	}
	
	public static final byte RET_CODE_SUCCESS = 1;//成功
	public static final byte RET_CODE_FAiLURE = 0;//失败
	public static final byte RET_CODE_TIMEOUT = -1;//失败
	private static final String DEFAULT_CONTENT_TYPE = "application/json;charset=utf-8";
	
	private static final ObjectMapper oMapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
	
	/**
	 * 登录超时或者未登录
	 * @throws IOException
	 */
	public void writeTimeout() throws IOException {
		writeAsJson(RET_CODE_TIMEOUT, null, null);
	}
	public void writeErr(int code, String msg) throws IOException {
		writeAsJson(code, msg, null);
	}
	public void writeErr(String msg) throws IOException {
		writeAsJson(RET_CODE_FAiLURE, msg, null);
	}
	public void write() throws IOException {
		writeAsJson(RET_CODE_SUCCESS, null, null);
	}
	public <T extends BaseView> void write(T view) throws IOException {
		writeAsJson(RET_CODE_SUCCESS, null, view);
	}
	public <T extends BaseView> void write(Collection<T> list) throws IOException {
		writeAsJson(RET_CODE_SUCCESS, null, list);
	}
	public <T extends BaseView> void write(irille.view.Page<T> page) throws IOException {
		writeAsJson(RET_CODE_SUCCESS, null, page);
	}
	private void writeAsJson(int ret, String msg, Object o) throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType(DEFAULT_CONTENT_TYPE);
		ResultView result = new ResultView();
		result.setRet(ret); 
		result.setMsg(msg); 
		result.setResult(o);
		response.getWriter().print(oMapper.writeValueAsString(result));
	}

	/**
	 * 对应EXT上的外键下拉框组件 在store加载调用
	 */
	public void getComboTrigger() throws Exception {
		String where = crtQueryAll();
		if (tb().chk("enabled")) {
			where += " AND enabled = 1";
		}
		if (!Str.isEmpty(getSarg1()))
			where += " AND (" + getSarg1() + ")";
		where += orderBy();
		IduPage page = newPage();
		page.setStart(getStart());
		page.setLimit(0); // 取所有数据，下拉框不分页
		page.setWhere(where);
		page.commit();
		List<THIS> list = page.getList();
		JSONArray ja = new JSONArray();
		JSONObject lineJson = null;
		boolean isinf = false;
		if (list.size() > 0 && list.get(0) instanceof IExtName)
			isinf = true;
		for (THIS line : list) {
			lineJson = new JSONObject();
			// 注意不论主键是否为STRING,都转成字符串
			// 前EXT组件初始化时,数字也是以字符形式判断其值
			lineJson.put("value", line.getPkey().toString());
			if (isinf)
				lineJson.put("text", ((IExtName) line).getExtName());
			else
				lineJson.put("text", line.getPkey());
			ja.put(lineJson);
		}
		JSONObject json = new JSONObject();
		json.put(STORE_ROOT, ja);
		ServletActionContext.getResponse().getWriter().print(json.toString());
	}

	/**
	 * 通用的XLS导出入口
	 * 
	 * @throws Exception
	 */
	public void exportGrid() throws Exception {
		// 获取允许导出数据
		if (getExpMethod() != null)
			ClassTools.run(this, getExpMethod(), null);
		else
			list();
		// 根据前台EXT获取需要导出的列
		JSONArray ja2 = new JSONArray(getExpFields());
		ArrayList<ExportColumn> cols = new ArrayList<ExtAction.ExportColumn>();
		int rownum = 1; //表头有几行
		for (int i = 0; i < ja2.length(); i++) {
			ExportColumn col = new ExportColumn(ja2.getJSONObject(i));
			cols.add(col);
			int tmp = col.getRowNum();
			if (tmp > rownum)
				rownum = tmp;
		}
		// 新建XLS对象
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFCellStyle setBorder = workbook.createCellStyle();
		setBorder.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont sfont = workbook.createFont();
		sfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		setBorder.setFont(sfont);
		setBorder.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		setBorder.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		setBorder.setBorderRight(HSSFCellStyle.BORDER_THIN);
		setBorder.setBorderTop(HSSFCellStyle.BORDER_THIN);
		HSSFSheet sheet = workbook.createSheet();
		sheet.setDefaultColumnWidth(18);
		HSSFRow row1 = sheet.createRow(0);// 设置列头信息、支持1、2、3级
		HSSFRow row2 = null;
		HSSFRow row3 = null;
		if (rownum > 1) {
			row2 = sheet.createRow(1);
		}
		if (rownum > 2) {
			row3 = sheet.createRow(2);
		}
		int index = 0;
		for (ExportColumn line : cols) {
			HSSFCell cell = row1.createCell(index);
			cell.setCellStyle(setBorder);
			cell.setCellValue(line._value);
			int oldindex = index;
			if (line._columnsTwo != null) { //第二列头处理
				for (String label2 : line._columnsTwo.values()) {
					HSSFCell cell2 = row2.createCell(index);
					cell2.setCellStyle(setBorder);
					cell2.setCellValue(label2);
					int oldindex2 = index;
					if (line._columnsThr != null) { //第三列头处理
						for (String label3 : line.getThrLabels(label2)) {
							HSSFCell cell3 = row3.createCell(index);
							cell3.setCellStyle(setBorder);
							cell3.setCellValue(label3);
							index++;
						}
						//第二列单元格合并
						if ((index - oldindex2) > 1) {
							exportAutoSet(1, 1, oldindex2, index - 1, row1, row2, row3, setBorder);
							CellRangeAddress range = new CellRangeAddress(1, 1, oldindex2, index - 1);
							sheet.addMergedRegion(range);
						}
					} else {
						index++;
					}
					//第二列单元格合并
					if (rownum > 2 && line._columnsThr == null) {
						exportAutoSet(1, rownum - 1, oldindex2, index - 1, row1, row2, row3, setBorder);
						CellRangeAddress range = new CellRangeAddress(1, rownum - 1, oldindex2, index - 1);
						sheet.addMergedRegion(range);
					}
				}
				//第一列单元格合并
				if ((index - oldindex) > 1) {
					exportAutoSet(0, 0, oldindex, index - 1, row1, row2, row3, setBorder);
					CellRangeAddress range = new CellRangeAddress(0, 0, oldindex, index - 1);
					sheet.addMergedRegion(range);
				}
			} else {
				index++;
			}
			//第一列单元格合并
			if (rownum > 1 && line._columnsTwo == null) {
				exportAutoSet(0, rownum - 1, oldindex, index - 1, row1, row2, row3, setBorder);
				CellRangeAddress range = new CellRangeAddress(0, rownum - 1, oldindex, index - 1);
				sheet.addMergedRegion(range);
			}
		}
		//============下面开始写入数据===============
		String value;
		Fld tbfield;
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		ArrayList<String> allkeys = exportAllKey(cols);
		for (int i = 0; i < _expJa.length(); i++) { //开始循环LIST的JSON数据<所有的>
			HSSFRow rowline = sheet.createRow(i + rownum); //XLS新增一行
			JSONObject jsondata = _expJa.getJSONObject(i);
			index = 0;
			for (String fld : allkeys) { //开始循环EXT展示的列<部分的>、列数据填充
				if (jsondata.has(fld)) { //A列有数据
					value = jsondata.getString(fld);
					if (tb().chk(fld)) { //排除自定义字段
						//处理外键值、选项值
						tbfield = tb().get(fld);
						if (tbfield.isOutKey())
							value = value.split("\\##")[1];
						else if (tbfield.isOpt()) {
							if (tbfield.getOpt() instanceof OptCust)
								value = value.split("\\##")[1];
							else {
								IEnumOpt ie = ((FldEnumByte)tbfield).getEnum();
								if (	ie.getLine().chk(Integer.parseInt(value)) == null)
									value = null;
								else
									value = ((IEnumOpt)ie.getLine().chk(Integer.parseInt(value))).getLine().getName();
							}
						}
					}
					HSSFCell cell = rowline.createCell(index);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(value);
				} else {
					//无数据--仅设置样式
					HSSFCell cell = rowline.createCell(index);
					cell.setCellStyle(cellStyle);
				}
				index++;
			}
		}
		// 将新建的XLS对象写入输出流中，下载的文件名使用默认的ISO编码
		HttpServletResponse response = ServletActionContext.getResponse();
		response.reset();
		response.setContentType("applicationnd.ms-excel");
		String fileName = tb().getName() + ".xls";
		response.setHeader("Content-Disposition", "attachment; filename="
		    + new String(fileName.getBytes("gb2312"), "ISO-8859-1"));
		OutputStream os = response.getOutputStream();
		workbook.write(os);
	}

	public String execute() throws Exception {
		return SUCCESS;
	}

	//返回所有值映射的KEY
	private ArrayList<String> exportAllKey(ArrayList<ExportColumn> cols) {
		ArrayList<String> ary = new ArrayList<String>();
		for (ExportColumn line : cols) {
			if (line._date != null) {
				ary.add(line._date);
				continue;
			}
			if (line._columnsThr != null) {
				for (String line3 : line._columnsThr.values()) {
					ary.add(line3.split("\\##")[0]);
				}
				continue;
			}
			for (String line2 : line._columnsTwo.keySet())
				ary.add(line2);
		}
		return ary;
	}

	//合并的单元格统一设置STYLE
	private void exportAutoSet(int x1, int x2, int y1, int y2, HSSFRow row1, HSSFRow row2, HSSFRow row3,
	    HSSFCellStyle style) {
		HSSFCell cell;
		for (int xx = x1; xx <= x2; xx++) {
			for (int yy = y1; yy <= y2; yy++) {
				if (xx == x1 && yy == y1) //起点不用设置
					continue;
				if (xx == 0) {
					cell = row1.createCell(yy);
					cell.setCellStyle(style);
				} else if (xx == 1) {
					cell = row2.createCell(yy);
					cell.setCellStyle(style);
				} else if (xx == 2) {
					cell = row3.createCell(yy);
					cell.setCellStyle(style);
				}
			}
		}
	}

	//前台EXT表头的显示类
	public static class ExportColumn {
		public String _value = null; //列名称
		public String _date = null; //列对应的fldCode
		public LinkedHashMap<String, String> _columnsTwo = null; //key value || value value
		public LinkedHashMap<String, String> _columnsThr = null; //value+序号 key##vlaue

		public ExportColumn(JSONObject json) throws JSONException {
			_value = json.getString(QUERY_VALUE);
			if (json.has(QUERY_PROPERTY)) { //一级表头
				_date = json.getString(QUERY_PROPERTY);
			} else { //二级表头
				_columnsTwo = new LinkedHashMap<String, String>();
				JSONArray ja2 = json.getJSONArray("columns");
				for (int i = 0; i < ja2.length(); i++) {
					JSONObject line2 = ja2.getJSONObject(i);
					String value2 = line2.getString(QUERY_VALUE);
					if (line2.has(QUERY_PROPERTY)) {
						_columnsTwo.put(line2.getString(QUERY_PROPERTY), value2);
					} else { //三级表头
						initThr();
						_columnsTwo.put(value2, value2);
						JSONArray ja3 = line2.getJSONArray("columns");
						for (int tr = 0; tr < ja3.length(); tr++) {
							JSONObject line3 = ja3.getJSONObject(tr);
							_columnsThr.put(value2 + tr, line3.getString(QUERY_PROPERTY) + "##" + line3.getString(QUERY_VALUE));
						}
					}
				}
			}
		}

		public ArrayList<String> getThrLabels(String label) {
			ArrayList<String> ary = new ArrayList<String>();
			for (String key : _columnsThr.keySet()) {
				if (key.contains(label))
					ary.add(_columnsThr.get(key).split("\\##")[1]);
			}
			return ary;
		}

		private void initThr() {
			if (_columnsThr == null)
				_columnsThr = new LinkedHashMap<String, String>();
		}

		public int getRowNum() {
			if (_columnsThr != null)
				return 3;
			if (_columnsTwo != null)
				return 2;
			return 1;
		}
	}
	
	/**
	 * 搜索和高级搜索外表非关联字段搜索
	 * @param ja 前台传来的JSON
	 * @param tb 本表
	 * @param outkeyfld 主表外键关联用的FLD
	 * @param outTb 所关联的外表
	 * @param prefix 前缀，没有填null
	 * @param outFlds 搜索用的外表字段
	 * @return
	 * @throws JSONException
	 */
	public String crtOutWhereSearch(JSONArray ja, Tb tb, Fld outkeyfld, Tb outTb, String prefix, Fld...outFlds) throws JSONException {
		String sql = "";
		String subSql = "SELECT pkey FROM " + outTb.getCodeSqlTb() + " WHERE ";
		int pcount = 0;
		for (Fld outFld : outFlds) {
			for (int i = 0; i < ja.length(); i++) {
				JSONObject json = ja.getJSONObject(i);
				String fldName = json.getString(QUERY_PROPERTY);
				String param = json.getString(QUERY_VALUE);
				if (Str.isEmpty(param))
					continue;
				if (tb.chk(fldName))
					break;
				if (prefix != null)
					fldName = fldName.substring(prefix.length()).substring(0, 1).toLowerCase() + fldName.substring(prefix.length()).replaceFirst("\\w","");
				if (fldName.equals(outFld.getCode())) {
					Fld fld = outTb.get(fldName);
					if (fld == null)
						continue;
					if (pcount > 0)
						subSql += " AND ";
					subSql += Env.INST.getDB().crtWhereSearch(fld, param);
					pcount++;
				}
			}
		}
		if (pcount > 0)
			sql = " AND " + outkeyfld.getCodeSqlField() + " IN (" + subSql + ")";
		return sql;
	}
}
