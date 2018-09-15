//Created on 2005-12-26
package irille.pub.doc;

import irille.pub.ClassTools;
import irille.pub.FileText;
import irille.pub.Log;
import irille.pub.html.EMCrt;
import irille.pub.html.Node;
import irille.pub.html.Nodes.Div;
import irille.pub.html.Nodes.Span;
import irille.pub.html.Nodes.Table;
import irille.pub.html.Nodes.Td;
import irille.pub.html.Nodes.Tr;
import irille.pub.svr.Act;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

import java.util.Hashtable;
import java.util.Vector;

/**
 * 产生交易帮助文档的基类
 * 
 * @author whx
 * 
 * @param <T>
 */
public class DocTran<T extends DocTran> extends DocBase<T> {
	private static final Log LOG = new Log(DocTran.class);
	private EMCrt _ext;
	private Tb _tb;
	private Hashtable<String, DocAct> _actsMap = new Hashtable(); // 功能列表
	private Hashtable<String, DocFld> _fldsMap = new Hashtable();
	private Vector<DocInf> _docNodes = new Vector(); // 记录所有的可引用节点,用于产生代码变量
	private Vector<DocTb> _tbs = new Vector();
	private Node _divTbs = new Div();
	private Node _divActs = new Div();

	@Override
	public T init() {
		_ext = (EMCrt) ClassTools.getStaticProerty(getClass(), "EXT");
		_tb = _ext.getTb();
		setHtml("TRAN_" + _tb.getCode(),"[交易]:" + _tb.getName());
		super.init();
		_docNodes.add(this); // 用于产生自动代码
		body().add(_divTbs);
		body().add(_divActs);

		DocTb mainTb = addTb(_tb, "m", "主表", _ext.getVflds());
		_docNodes.add(mainTb); // 用于输出自动代码
		initTbs();
		initActs(_divActs);
		initFoot();
		return (T)this;
	}

	@Override
	public void initHead(Node n) {
		n.H2().add(getAnchorName());
		// 增加功能菜单
		DocAct docAct;
		Span menu = new Span();
		n.add(menu);
		for (Act act : (Vector<Act>) _tb.getActs()) {
			docAct = new DocAct(getClass(), act);
			_actsMap.put(act.getCode(), docAct);
			_docNodes.add(docAct);
			menu.add(" ");
			menu.add(getUrl(docAct));
			menu.add(" ");
		}
		n.p();
		super.initHead(n);
	}

	/**
	 * 
	 */
	public void initTbs() {
	}

	public DocTb addTb(Tb tb, VFlds... vfldss) {
		return addTb(tb, _tb.getCode(), _tb.getName(), vfldss);
	}

	public DocTb addTb(Tb tb, String code, VFlds... vfldss) {
		return addTb(tb, code, _tb.getName(), vfldss);
	}

	public DocTb addTb(Tb tb, String code, String name, VFlds... vfldss) {
		DocTb docTb = new DocTb(tb, this, code, name, vfldss);
		_tbs.add(docTb);
		return docTb;
	}

	public void initActs(Node n) {
		// 增加功能信息
		for (Act act : (Vector<Act>) _tb.getActs()) {
			n.add(getAct(act.getCode()).getDocNode());
		}
	}

	/* (non-Javadoc)
	 * @see irille.pub.dep.DocBase#saveToFileBeforeDo()
	 */
	@Override
	public void saveToFileBeforeDo() {
		super.saveToFileBeforeDo();
		for (DocTb tb : _tbs)
			_divTbs.add(tb.getDocNode());
	}

	private static String TAB = "\t";

	/**
	 * 输出Doc产生程序自动产生的代码段 clazz 源代码的类
	 * 
	 * @return
	 */
	public void outSourceCode() {
		StringBuilder buf = new StringBuilder();
		for (DocInf doc : _docNodes) {
			doc.outSourceCode(buf, TAB);
		}
		FileText file=new FileText(getClass());
		file.replace("Doc文档", buf.toString());
		file.save();
	}

	/* (non-Javadoc)
	 * @see irille.pub.dep.DocBase#countTestCases()
	 */
	@Override
	public int countTestCases() {
	  ((DocTran) ClassTools.getStaticProerty(getClass(), "DOC")).outSourceCode(); // 产生本文件最后的自动产生代码
		return super.countTestCases();
	}
	
	/**
	 * 取交易功能
	 * 
	 * @param code
	 *          代码
	 * @return 结果
	 */
	public final DocAct getAct(String code) {
		DocAct act = _actsMap.get(code);
		if (act == null)
			throw LOG.err("actNotFind", "交易功能[{0}]没找到!", code);
		return act;
	}

	/**
	 * 取字段
	 * 
	 * @param code
	 *          代码
	 * @return 结果
	 */
	public final DocFld getFld(String code) {
		DocFld fld = _fldsMap.get(code);
		if (fld == null)
			throw LOG.err("fldNotFind", "列表中无字段[{0}]!", code);
		return fld;
	}


	public final DocTb getTb(String code) {
		for (DocTb tb : _tbs)
			if (tb.getAnchor().getCode().equals(code))
				return tb;
		throw LOG.err("tbNotFind", "表对象[{0}]没找到!", code);
	}

	public EMCrt getExt() {
		return _ext;
	}

	/**
	 * 交易中的表
	 * 
	 * @author whx
	 * 
	 */
	public static class DocTb extends Div implements DocInf<Table> {
		private Tb _tb;
		private String _name;
		private String _code;
		private VFlds[] _vfldss;
		private Table _table = new Table();
		private DocTran _doc;

		public DocTb(Tb tb, DocTran doc, String code, String name, VFlds... vfldss) {
			_tb = tb;
			_vfldss = vfldss;
			_name = name;
			_code = code;
			_doc = doc;
			setAnchor(doc.getClass(), "TB_" + code,_name);
			_table.add(this);
			_table.setClazz(CSS.PROPERTY);
			DocFld docFld;
			for (VFlds vflds : _vfldss)
				for (VFld fld : vflds.getVFlds()) {
					docFld = new DocFld(doc.getClass(), code, fld);
					doc._fldsMap.put(docFld.getCode(), docFld);
					doc._docNodes.add(docFld);
				}
		}

		private boolean _crtNoded = false;

		/*
		 * (non-Javadoc)
		 * 
		 * @see irille.pub.dep.TranDoc.XDocInf#getDocNode()
		 */
		@Override
		public Table getDocNode() {
			if (!_crtNoded) {
				_crtNoded = true;
				// 输出表头
				_table.br().hr();
				_table.H4(_name + "栏目说明");
				Tr tr = _table.addTr();
				tr.addTh().setClazz(CSS.PROPERTY_NAME).add("名  称");
				tr.addTh().setClazz(CSS.PROPERTY_NOTNULL).add("可空");
				tr.addTh().setClazz(CSS.PROPERTY_EXPLAIN).add("说    明");

				DocFld docFld;
				for (VFlds vflds : _vfldss)
					for (VFld fld : vflds.getVFlds()) {
						_table.add(_doc.getFld(_code + "_" + fld.getCode()).getDocNode());
					}
			}
			return _table;
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see irille.pub.dep.DocInf#outSourceCode(java.lang.StringBuilder, String)
		 */
		@Override
		public void outSourceCode(StringBuilder buf, String tab) {
			buf.append(tab + "public static DocTb " + getAnchorCode()
					+ "=DOC.getTb(\"" + getAnchorCode() + "\");	//" + _tb.getName()
					+ LN);
		}
	}

	/**
	 * 交易字段
	 * 
	 * @author whx
	 * 
	 */
	public static class DocFld extends Td implements DocInf<Tr> {
		private VFld _vfld;
		private Tr _tr = new Tr();
		private String _code;

		public DocFld(Class clazz, String tbCode, VFld vfld) {
			_vfld = vfld;
			_code = tbCode + "_" + vfld.getCode();
			setAnchor(clazz, "FLD_" + _code, vfld.getName());
			setClazz(CSS.PROPERTY_EXPLAIN).add(vfld.getHelp());
		}

		public String getCode() {
			return _code;
		}

		private boolean _crtNoded = false;

		/*
		 * (non-Javadoc)
		 * 
		 * @see irille.pub.dep.TranDoc.XDocInf#getDocNode()
		 */
		@Override
		public Tr getDocNode() {
			if (!_crtNoded) {
				_crtNoded = true;
				_tr.addTd().setClazz(CSS.PROPERTY_NAME).add(getAnchorName());
				_tr.addTd().setClazz(CSS.PROPERTY_NOTNULL).set("align", "center")
						.add(_vfld.isNull() ? "是" : "否");
				_tr.add(this);
			}
			return _tr;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see irille.pub.dep.DocInf#outSourceCode(java.lang.StringBuilder,
		 * java.lang.String)
		 */
		@Override
		public void outSourceCode(StringBuilder buf, String tab) {
			buf.append(tab + "public static DocFld " + getAnchorCode()
					+ "=DOC.getFld(\"" + getAnchorCode().substring(4) + "\");	//"
					+ _vfld.getName() + LN);
		}
	}

	/**
	 * 交易字段
	 * 
	 * @author whx
	 * 
	 */
	public static class DocAct extends Td implements DocInf<Div> {
		private Act _act;
		private Td _input = new Td(); // 输入内容
		private Td _check = new Td(); // 数据检查说明
		private Td _proc = new Td(); // 处理过程与结果说明
		private Td _output = new Td(); // 输出信息或跳转页面
		private Div _div = new Div(); // 主对象

		public DocAct(Class anchorClazz, Act act) {
			_act = act;
			setAnchor(anchorClazz, "ACT_" + act.getCode(), act.getName());
			_div.br().hr();
			_div.H4("[操作]").add(getAnchorName());
		}

		public DocAct addToInput(String str) {
			_input.add(str);
			return this;
		}

		public DocAct addToCheck(String str) {
			_check.add(str);
			return this;
		}

		public DocAct addToProc(String str) {
			_proc.add(str);
			return this;
		}

		public DocAct addToOutput(String str) {
			_output.add(str);
			return this;
		}

		private boolean _crtNoded = false;

		/*
		 * (non-Javadoc)
		 * 
		 * @see irille.pub.dep.TranDoc.XDocInf#getDocNode()
		 */
		@Override
		public Div getDocNode() {
			if (!_crtNoded) {
				_crtNoded = true;
				Table table = new Table();
				_div.add(this);
				_div.add(table).setClazz(CSS.PROPERTY);
				Tr tr = table.addTr();
				tr.addTh().setClazz(CSS.PROPERTY_NAME).add("名  称");
				tr.addTh().setClazz(CSS.PROPERTY_EXPLAIN).add("说  明");
				table.add(newRem("输入内容", _input));
				table.add(newRem("数据检查说明", _check));
				table.add(newRem("处理过程与结果说明", _proc));
				table.add(newRem("输出信息或跳转页面", _output));
			}
			return _div;
		}

		private Tr newRem(String name, Td node) {
			Tr tr = new Tr();
			tr.addTd().setClazz(CSS.PROPERTY_NAME).add(name);
			tr.add(node).setClazz(CSS.PROPERTY_EXPLAIN);
			return tr;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see irille.pub.dep.DocInf#outSourceCode(java.lang.StringBuilder,
		 * java.lang.String)
		 */
		@Override
		public void outSourceCode(StringBuilder buf, String tab) {
			buf.append(tab + "public static DocAct ACT_" + _act.getCode()
					+ "=DOC.getAct(\"" + _act.getCode() + "\");	//" + _act.getName() + LN);
		}

		/**
		 * @return the act
		 */
		public Act getAct() {
			return _act;
		}

		/**
		 * @return the input
		 */
		public Td getInput() {
			return _input;
		}

		/**
		 * @return the check
		 */
		public Td getCheck() {
			return _check;
		}

		/**
		 * @return the proc
		 */
		public Td getProc() {
			return _proc;
		}

		/**
		 * @return the output
		 */
		public Td getOutput() {
			return _output;
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.dep.DocInf#outSourceCode(java.lang.StringBuilder,
	 * java.lang.String)
	 */
	@Override
	public void outSourceCode(StringBuilder buf, String tab) {
		// buf.append(tab + "public static DocTranHead TRAN_" + _tb.getCode() +
		// "=DOC.getTranHead();	//交易："
		// +_tb.getName() + LN);
	}

}
