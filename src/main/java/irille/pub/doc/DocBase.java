//Created on 2005-12-26
package irille.pub.doc;

import irille.pub.ClassTools;
import irille.pub.FileTools;
import irille.pub.Log;
import irille.pub.html.Node;
import irille.pub.html.Xhtml;
import irille.pub.html.Nodes.A;
import irille.pub.html.Nodes.Div;
import irille.pub.html.Nodes.Img;
import irille.pub.html.Nodes.Td;
import irille.pub.html.Nodes.Tr;
import irille.pub.html.Xhtml.Body;

import java.io.File;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestResult;

/**
 * 产生交易帮助文档的基类
 * 
 * @author whx
 * 
 * @param <T>
 */
public abstract class DocBase<T extends DocBase> extends Div implements DocInf,
		Test {
	private static final Log LOG = new Log(DocBase.class);

	public static final String ROOT = ("\\".equals(File.separator) ? "C:": File.separator + "Users" + File.separator + "wanghaixiao"
					+ File.separator + "Desktop") + File.separator + "irille" + File.separator + "doc" + File.separator; // 输出的根目录
	public static final String DIR_SUB = "irille" + File.separator + "dep"
			+ File.separator; // 存放Doc的文件名相对于ROOT要去掉的前缀。路径来自EXT2的类
	public static final String CSS_FILE = ROOT + "irille.css";
	public static final DocIrilleCss CSS = new DocIrilleCss();
	public static int STEP = 2;
	private Vector<DocBase> _docList = new Vector();

	private Node _divHead = new Div();
	private Vector<DocNoun> _nouns = new Vector(); // 名词列表
	private Node _divNouns = new Div();
	private Node _divFoot = new Div().br().hr();

	private Xhtml _html;

	/**
	 * 初始化 继承的方法必须用如下方法初始化相关变量： setXhtml,setSpan
	 * 
	 * @return
	 */
	public T init() {
		_html.linkCss(CSS_FILE);

		body().add(_divHead);
		body().add(_divNouns);// 名词解释,
								// 需在子类的静态变量中定义，调用newNoun(code,name)或newNoun(code,name,rem)来设定
		initHead(_divHead);

		return (T) this;
	}

	/**
	 * 初始化帮助文件的内容说明，所有的帮助文档说明请在继承此方法之中进行说明
	 */
	public void initMsg() {
	}

	public void initHead(Node n) {
		n.br().add(this);
	}

	public Xhtml getHtml() {
		return _html;
	}

	public void setHtml(String code, String name) {
		_html = new Xhtml(code, name);
		setAnchor(getClass(), "", name);
	}

	public void initFoot() {
		body().add(_divFoot);
		_divFoot.br(2).set("align", "center")
				.add("Copyright 2010-2014 irille.com All Rights Reserved");
	}

	private boolean _saveToFileBeforeDo = false;

	/**
	 * 在保存文件之前的数据处理
	 */
	public void saveToFileBeforeDo() {
		if (!_saveToFileBeforeDo) {
			_saveToFileBeforeDo = true;
			for (DocNoun noun : _nouns)
				_divNouns.add(noun.getDocNode());
			initMsg();
		}
	}

	/**
	 * 取存储的文件名，相于对ROOT常量
	 * 
	 * @return
	 */
	public String getHtmlFileName() {
		return FileTools.tranClassToFileDir(getClass()).substring(
				DIR_SUB.length())
				+ ".htm";
	}

	/**
	 * 输出帮助信息到文件
	 */
	public void saveToFile() {
		// System.err.println(getHtmlFileName());
		saveToFileBeforeDo();
		_html.saveToFile(ROOT + getHtmlFileName(), STEP);
	}

	/**
	 * 建立图片对象
	 * 
	 * @param name
	 *            图片名称
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public final DocImg newImg(String name, String fileName) {
		return new DocImg(getClass(), name, fileName);
	}

	/**
	 * 增加名词解释
	 * 
	 * @param code
	 *            代码
	 * @param name
	 *            名称
	 * @param rem
	 *            说明
	 * @return
	 */
	public final DocNoun newNoun(String code, String name, String rem) {
		DocNoun noun = newNoun(code, name);
		noun.add(rem);
		return noun;
	}

	private boolean _outNounHeaded = false;

	/**
	 * 增加名词解释
	 * 
	 * @param code
	 *            代码
	 * @param name
	 *            名称
	 * @return
	 */
	public final DocNoun newNoun(String code, String name) {
		DocNoun noun = new DocNoun(getClass(), code, name);
		_nouns.add(noun);
		// _docNodes.add(noun);
		if (!_outNounHeaded) {
			_outNounHeaded = true;
			_divNouns.br().hr();
		}
		return noun;
	}

	/**
	 * 取名词解释
	 * 
	 * @param code
	 *            代码
	 * @return 结果
	 */
	public final DocNoun getNoun(String code) {
		for (DocNoun noun : _nouns)
			if (noun.getAnchor().getCode().equals(code))
				return noun;
		throw LOG.err("nounNotFind", "名词解释[{0}]没找到!", code);
	}

	public Body body() {
		return _html.body();
	}

	public final A getUrl(Node node) {
		return (A) new A().setHref(node.assertAnchor().getUrl(getClass())).add(
				node.getAnchorName());
	}

	/**
	 * 名词解释
	 * 
	 * @author whx
	 * 
	 */
	public static class DocNoun extends Td implements DocInf<Tr> {
		private String _code, _name;
		private Tr _tr = new Tr();

		public DocNoun(Class anchorClazz, String code, String name) {
			_code = code;
			_name = name;
			setAnchor(anchorClazz, "NOUN_" + code, name);
			setClazz(CSS.PROPERTY_EXPLAIN);
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
				_tr.addTd().H4("[名词]" + getAnchorName());
				_tr.add(this);
			}
			return _tr;
		}

		/**
		 * @return the code
		 */
		public String getCode() {
			return _code;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return _name;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see irille.pub.dep.DocInf#outSourceCode(java.lang.StringBuilder,
		 * java.lang.String)
		 */
		@Override
		public void outSourceCode(StringBuilder buf, String tab) {
			buf.append(tab + "public static DocNoun NOUN_" + _code
					+ "=DOC.getNoun(\"" + _code + "\");	//" + _name + LN);
		}
	}

	/**
	 * 追加到列表中，用于一次性产生所有文件的帮助文档
	 * 
	 * @param doc
	 */
	public void addToDocList(DocBase doc) {
		_docList.add(doc);
	}

	public void saveAllToFile() {
		saveToFile();
		for (DocBase doc : _docList)
			doc.saveToFile();
	}

	/**
	 * 图片
	 * 
	 * @author whx
	 * 
	 */
	public static class DocImg extends Img implements DocInf<Img> {

		public DocImg(Class clazz, String name, String fileName) {
			super(fileName, name);
			setAnchor(clazz, name, name);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see irille.pub.dep.TranDoc.XDocInf#getDocNode()
		 */
		@Override
		public Img getDocNode() {
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see irille.pub.dep.DocInf#outSourceCode(java.lang.StringBuilder,
		 * String)
		 */
		@Override
		public void outSourceCode(StringBuilder buf, String tab) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.dep.DocInf#getDocNode()
	 */
	@Override
	public Div getDocNode() {
		return this;
	}

	/*
	 * 输出帮助文件变量定义的源代码
	 * 
	 * @see irille.pub.dep.DocInf#outSourceCode(java.lang.StringBuilder,
	 * java.lang.String)
	 */
	@Override
	public void outSourceCode(StringBuilder buf, String tab) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.Test#run(junit.framework.TestResult)
	 */
	@Override
	public void run(TestResult arg0) {
	}

	public final void testCase() {
	}

	/**
	 * 此代码用于产生帮助文档及帮助文档相关的自动代码
	 * 
	 * @return
	 * @see junit.framework.Test#countTestCases()
	 */
	@Override
	public int countTestCases() {
		DocBase doc = (DocBase) ClassTools.getStaticProerty(getClass(), "DOC");
		doc.saveToFile();// 产生帮助文档
		return 0;
	}

	/**
	 * @return the docList
	 */
	public Vector<DocBase> getDocList() {
		return _docList;
	}
}
