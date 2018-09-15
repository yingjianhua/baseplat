package irille.pub.inf;

import irille.pub.Log;
import irille.pub.svr.DbMysql;

/**
 * 系统安装初始化类
 * @author surface1
 *
 */
public class AStartInit {
	private static final Log LOG = new Log(AStartInit.class);
	
	private IDb db=new DbMysql();  //当前数据库为MySql
	private String workDir="c:\\irille\\wisdom\\";// 工作目录
	
	public void init() {
		//初始化环境
		//TODO whx
		//Env.ENV=new Env(workDir,db);
	}
	
}
