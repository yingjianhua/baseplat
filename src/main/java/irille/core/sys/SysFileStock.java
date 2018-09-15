//Created on 2005-10-20
package irille.core.sys;

import java.util.Date;

import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

public class SysFileStock extends BeanInt<SysFileStock> {
	private static final Log LOG = new Log(SysFileStock.class);
	public static final Tb TB = new Tb(SysFileStock.class, "文件存储").setAutoIncrement();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		MD5(SYS.STR__50, "md5"),
		PATH(SYS.STR__1000, "路径"),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_MD5 = TB.addIndex("md5", true,T.MD5);
		private Fld _fld;
		private T(Class clazz,String name,boolean... isnull) 
			{_fld=TB.addOutKey(clazz,this,name,isnull);	}
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld,this); }
		public Fld getFld(){return _fld;}
	}
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(),TB.getShortName());
	}
	public static Fld fldOutKey(String code,String name) {
		return Tb.crtOutKey(TB, code, name);
	}
	public static Fld fldOneToOne() {
		return fldOneToOne(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOneToOne(String code, String name) {
		return Tb.crtOneToOne(TB, code, name);
	}
	
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private String _md5;	// md5  STR(50)
  private String _path;	// 路径  STR(1000)
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysFileStock init(){
		super.init();
    _md5=null;	// md5  STR(50)
    _path=null;	// 路径  STR(1000)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static SysFileStock loadUniqueMd5(boolean lockFlag,String md5) {
    return (SysFileStock)loadUnique(T.IDX_MD5,lockFlag,md5);
  }
  public static SysFileStock chkUniqueMd5(boolean lockFlag,String md5) {
    return (SysFileStock)chkUnique(T.IDX_MD5,lockFlag,md5);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public String getMd5(){
    return _md5;
  }
  public void setMd5(String md5){
    _md5=md5;
  }
  public String getPath(){
    return _path;
  }
  public void setPath(String path){
    _path=path;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
