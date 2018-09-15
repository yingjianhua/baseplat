/**
 * 
 */
package irille.pub.gl;

/**
 * @author whx
 * 
 */
public interface ITallyBean {
	public static final String NAME_DEFAULT="nameDefault";
	/**
	 * 取账户关联信息 取记账自动建账户的关联对象 对一些可以自动建立的账户，如关联机构、部门、用户、核算单元等时，需要获取相关的对象，通过些接口来获取
	 * Bill与Note的扩展表的子类都要实现此接口
	 * 
	 * @param name
	 *          对于有多条分录的Bill或Note,
	 *          取得对象可能会有区别，用name进行区分，以返回不同的内容,它与initTallyLines方法中分录行的name要对应
	 * @param objs
	 *          已初始化过的对象
	 * @return
	 */
	public void getAccObjs(String name, AccObjs objs);

	/**
	 * 初始化记账分录
	 * 
	 * @param ls 其分录行的name要与getAccObjs方法中的name相对对应
	 */
	public void initTallyLines(TallyLines ls);

}
