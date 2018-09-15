package irille.core.prv;

import irille.pub.bean.BeanInt;
import irille.pub.tb.EnumLine;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.tb.TbCmb;

/**
 * 起止日期
 * 
 * @author whx
 */
public class CmbFldRule extends BeanInt<CmbFldRule> {
	public static final Tb TB = new TbCmb(CmbFldRule.class, "字段数据规则");
	
	public enum OPrvFldType implements IEnumOpt {//@formatter:off
		ORG(1,"机构"),CELL(2,"核算单元"),DEPT(3,"部门"),
		USER(4,"用户"),DATE(5,"日期"),DATETIME(6,"日期时间"),CUST(7,"自定义");
		public static final String NAME="权限数据类型";
		public static final OPrvFldType DEFAULT = CELL; // 定义缺省值
		private EnumLine _line;
		private OPrvFldType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OPrvFldRule implements IEnumOpt {//@formatter:off
		ORG_OWNER(OPrvFldType.ORG, 1,"本机构"),
		ORG_GROUP(OPrvFldType.ORG,15,"指定机构组"),
		CELL_OWNER(OPrvFldType.CELL,16 ,"当前核算单元"),
		CELL_GROUP(OPrvFldType.CELL,30,"指定核算单元组"),
		DEPT_OWNER(OPrvFldType.DEPT,31 ,"当前部门"),
		DEPT_GROUP(OPrvFldType.DEPT,45,"指定部门组"),
		USER_OWNER(OPrvFldType.USER,46 ,"当前用户"),
		USER_GROUP(OPrvFldType.USER,60,"指定用户组"),
		;
		public static final String NAME="权限字段规则";
		public static final OPrvFldRule DEFAULT = CELL_OWNER; // 定义缺省值
		private EnumLine _line;
		private OPrvFldType _type;
		private OPrvFldRule(OPrvFldType type,int key, String name)
			{_line=new EnumLine(this,key,name);_type=type;	}
		public OPrvFldType getType(){return _type; };
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum T implements IEnumFld {//@formatter:off
		FIELD(SYS.STR__40,"字段代码",true),
		TYPE(TB.crt(OPrvFldRule.CELL_GROUP,"type","权限字段规则").setNull()),
		EXP(SYS.STR__100_NULL,"表达式",true);
		
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		private Fld _fld;
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.FIELD.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	public static Fld fldFlds() {
		return Tb.crtCmbFlds(TB);
	}
	public static Fld fldCmb(String code,String name) {
		return TB.crtCmb(code, name, TB);
	}
	
	//@formatter:on


	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private String _field;	// 字段代码  STR(40)<null>
  private Byte _type;	// 权限字段规则 <OPrvFldRule>  BYTE<null>
	// ORG_OWNER:1,本机构
	// ORG_GROUP:15,指定机构组
	// CELL_OWNER:16,当前核算单元
	// CELL_GROUP:30,指定核算单元组
	// DEPT_OWNER:31,当前部门
	// DEPT_GROUP:45,指定部门组
	// USER_OWNER:46,当前用户
	// USER_GROUP:60,指定用户组
  private String _exp;	// 表达式  STR(100)<null>

	@Override
  public CmbFldRule init(){
		super.init();
    _field=null;	// 字段代码  STR(40)
    _type=OPrvFldRule.DEFAULT.getLine().getKey();	// 权限字段规则 <OPrvFldRule>  BYTE
    _exp=null;	// 表达式  STR(100)
    return this;
  }

  //方法----------------------------------------------
  public String getField(){
    return _field;
  }
  public void setField(String field){
    _field=field;
  }
  public Byte getType(){
    return _type;
  }
  public void setType(Byte type){
    _type=type;
  }
  public OPrvFldRule gtType(){
    return (OPrvFldRule)(OPrvFldRule.CELL_GROUP.getLine().get(_type));
  }
  public void stType(OPrvFldRule type){
    _type=type.getLine().getKey();
  }
  public String getExp(){
    return _exp;
  }
  public void setExp(String exp){
    _exp=exp;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
