package com.irille.core.commons.i18n.translateLanguage;

import java.util.Date;
import java.util.stream.Stream;

import irille.pub.bean.BeanInt;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

/**
 * Created by IntelliJ IDEA.
 * User: lijie@shoestp.cn
 * Date: 2018/9/7
 * Time: 13:26
 */
public class PubTrantslate extends BeanInt<PubTrantslate> {
    public static final Tb TB = new Tb(PubTrantslate.class, "翻译缓存类").setAutoIncrement();

    public enum T implements IEnumFld {
        PKEY(Tb.crtIntPkey()),
        HASHCODE(SYS.STR__100_NULL),
        SOURCE_TEXT(SYS.TEXT__20000_NULL),
        TARGET(SYS.STR__10),
        TARGET_TEXT(SYS.TEXT__20000_NULL),
        CREATED_TIME(SYS.CREATED_DATE_TIME),
        ROW_VERSION(SYS.ROW_VERSION),
        //>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
        //<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
        ;
        //>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
        //<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		private Fld<?> _fld;

		private T(Class<?> clazz, String name, boolean... isnull) {
			_fld = IEnumFld.crtFld(this, clazz, name, isnull);
		}

		private T(IEnumFld fld, boolean... isnull) {
			_fld = IEnumFld.crtFld(this, fld, null, isnull);
		}

		private T(IEnumFld fld, String name, boolean... null1) {
			_fld = IEnumFld.crtFld(this, fld, name, null1);
		}

		private T(IEnumFld fld, String name, int strLen) {
			_fld = IEnumFld.crtFld(this, fld, name, strLen);
		}

		private T(Fld<?> fld) {
			_fld = IEnumFld.crtFld(this, fld);
		}

		public Fld<?> getFld() {
			return _fld;
		}

    }


    static { //在此可以加一些对FLD进行特殊设定的代码
    	Stream.of(T.values()).forEach(f->TB.add(f.getFld()));
		TB.lockAllFlds();
//        PubTrantslate.T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
    }

    public static Fld fldOutKey() {
        return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
    }

    public static Fld fldOutKey(String code, String name) {
        return Tb.crtOutKey(TB, code, name);
    }
    //@formatter:on

    //>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private String _hashcode;	// 字符100  STR(100)<null>
  private String _sourceText;	// 字符20000  TEXT(200)<null>
  private String _target;	// 字符10  STR(10)
  private String _targetText;	// 字符20000  TEXT(200)<null>
  private Date _createdTime;	// 建档时间  TIME
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public PubTrantslate init(){
		super.init();
    _hashcode=null;	// 字符100  STR(100)
    _sourceText=null;	// 字符20000  TEXT(200)
    _target=null;	// 字符10  STR(10)
    _targetText=null;	// 字符20000  TEXT(200)
    _createdTime=Env.getTranBeginTime();	// 建档时间  TIME
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public String getHashcode(){
    return _hashcode;
  }
  public void setHashcode(String hashcode){
    _hashcode=hashcode;
  }
  public String getSourceText(){
    return _sourceText;
  }
  public void setSourceText(String sourceText){
    _sourceText=sourceText;
  }
  public String getTarget(){
    return _target;
  }
  public void setTarget(String target){
    _target=target;
  }
  public String getTargetText(){
    return _targetText;
  }
  public void setTargetText(String targetText){
    _targetText=targetText;
  }
  public Date getCreatedTime(){
    return _createdTime;
  }
  public void setCreatedTime(Date createdTime){
    _createdTime=createdTime;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
