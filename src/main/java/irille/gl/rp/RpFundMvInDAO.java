package irille.gl.rp;

import irille.pub.Log;

/**
 * 审核后，状态更改为‘已审核’
 * 调入后，状态更改为‘可记账’(检查审核标识为是)
 * 作调入时，界面上检查审核标识，输入出纳账，并后台检查审核标识、检查是当前用户是否为出纳员
 * 
 * 手动新增时：
 * 初始审核标识-否、是发起方；审核后，自动产生调出单；
 * 
 * 由调出单审核产生时：
 * 初始审核标识-是、否发起方； 审核后，更改来源调出单的审核标识
 * 
 * @author whx
 * @version 创建时间：2015年8月27日 下午3:10:48
 */
public class RpFundMvInDAO {
	public static final Log LOG = new Log(RpFundMvInDAO.class);

}
