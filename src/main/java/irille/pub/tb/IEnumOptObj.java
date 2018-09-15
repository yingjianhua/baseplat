/**
 * 
 */
package irille.pub.tb;

/**
 * 有附加属性的Opt类
 * @author surface1
 *
 */
public interface IEnumOptObj<OBJ extends Object> extends IEnumOpt{
	public OBJ getObj();
}
