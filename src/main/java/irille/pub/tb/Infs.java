/**
 * 
 */
package irille.pub.tb;

import java.io.Serializable;
import java.util.Vector;

/**
 * @author whx
 *
 */
public class Infs {
	
	public static interface IEnabled {
		public Byte getEnabled();

		public void setEnabled(Byte enabled);

		public Boolean gtEnabled() ;

		public void stEnabled(Boolean enabled);
	}

	/**
	 * 外健接口，用于取外键数据对象类型
	 * @author whx
	 *
	 */
	public static interface IOutTbClass {
		public Class getOutTbClazz();
	}
	
	/**
	 * 被其他表引用的表接口，用于显示对象
	 * @author surface1
	 *
	 */
	public static interface ITbOutKey {
		public String getName();
		public String getCode();
		public String getShortName();
		/**
		 * 在Bean基类中定义了如下方法，来配合使用
		 */
		public void assertImplementsFromITbOutKey();

		public String gtIOutKeyTbAndName() ;

		public String gtIOutKeyTbAndShortName() ;
	}

	public static interface IFld {
		public String getName();
		public TbBase getTb();
		public String getCode();
		public boolean isOpt();
		public OptBase getOpt();

	}

	/**
	 * Title: 外键字段接口<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * @version 1.0
	 */
	public static interface IFldKey<KFLD extends Fld>  {

		/**
		 * 列出明细，以“、”分隔
		 * @return 结果
		 */
		public abstract KFLD getOutKeyFld();
		
	}

	/**
	 * Title: 选项字段接口<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * @version 1.0
	 */
	public static interface IFldOpt<T extends IFldOpt>  {
		/**
		 * 取选项
		 * @return 结果
		 */
		public IOpt getOpt();
	}

	/**
	 * Title: 选项接口<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * @version 1.0
	 */
	public static interface IOpt<T extends IOpt> extends Serializable {
		public enum TYPE {
			OPT,  //选项
			OPT_CUST, //自定义选项
			TABLE  //表格
		}
		/**
		 * 取code 
		 * @return code
		 */
		public abstract String getCode();

		/**
		 * 取name
		 * @return name
		 */
		public abstract String getName();

		/**
		 * 取Type
		 * @return Type
		 */
		public abstract TYPE getType();

		/**
		 * 值是否必须在明细中
		 * @return 结果
		 */
		public abstract boolean isInCheck();
		
		/**
		 * 取optLine
		 * @return optLine
		 */
		public abstract IOptLine get(int id);

		/**
		 * 取optLine
		 * @return optLine
		 */
		public abstract IOptLine get(String value);
		
		/**
		 * 取optLine 返回null
		 * @param value
		 * @return
		 */
		public abstract IOptLine chk(String value);

		/**
		 * 取明细行
		 * @return 结果
		 */
		public abstract IOptLine[] getLines();

		/**
		 * 取明细行
		 * @return 结果
		 */
		public abstract Vector<IOptLine> getLineVector();

		/**
		 * 取明细数量
		 * @return 结果
		 */
		public abstract int size();

		/**
		 * 确认值在选项行的所有列表中
		 * @param fieldName 字段，用于出错时显示名称
		 * @param value
		 *          值
		 */
		public abstract void assertValid(String fieldName,Object value) ;

		/**
		 * 确认值在选项行列表中
		 * @param fieldName 字段，用于出错时显示名称
		 * @param value
		 *          值
		 * @param lineOrValues
		 *          选项行列表
		 */
		public abstract void assertIn(String fieldName,Object value, Object... lineOrValues) ;

		/**
		 * 确认值不在选项行列表中，并且有效
		 * @param String fieldName 字段，用于出错时显示名称
		 * @param value
		 *          值
		 * @param lineOrValues
		 *          选项行列表,
		 */
		public abstract void assertNotIn(String fieldName, Object value, Object... lineOrValues);
		/**
		 * 判断值在选项行列表中
		 * 
		 * @param value
		 *          值
		 * @param lineOrValues
		 *          选项行列表, 为空表示从所有包括的行中检查
		 */
		public abstract boolean isIn(Object value, Object... lineOrValues) ;


		/**
		 * 列出明细，以“、”分隔
		 * @return 结果
		 */
		public abstract String listToString();
	}
	

	/**
	 * Title: 选项行接口<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * @version 1.0
	 */
	public static interface IOptLine extends Serializable {
		/**
		 * 取选项
		 * 
		 * @return 结果
		 */
		public IOpt getOpt();

		/**
		 * 取key
		 * 
		 * @return key
		 */
		public int getKeyInt();
		/**
		 * 取key
		 * 
		 * @return key
		 */
		public byte getKeyByte();
		/**
		 * 取key
		 * 
		 * @return key
		 */
		public String getKey();

		public char getKeyChar();

		/**
		 * 取name
		 * 
		 * @return name
		 */
		public String getName();
		/**
		 * 取enable
		 * 
		 * @return enable
		 */
		public boolean isEnable() ;

		/**
		 * 取shortcutKey
		 * 
		 * @return shortcutKey
		 */
		public String getShortcutKey();
		
	  public String getRem();
	}

}
