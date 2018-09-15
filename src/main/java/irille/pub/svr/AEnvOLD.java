package irille.pub.svr;

import irille.pub.FileTools;

import java.util.Hashtable;


/**
 * 环境信息类，包括工作环境、开发环境、数据库环境等信息
 * 为了方便开发，对于环境有关的参数都集中通过此类来存取
 * @author whx
 * 
 */
public class AEnvOLD {
	public static final EnvWork WORK = new EnvWork();
	public static final EnvDep DEP = new EnvDep();
	
	// 初始化 工作相关目录
	public void main(String[] args) { // XXX
		FileTools.mkDir(WORK.MIDDLE_FILE_ROOT, WORK.CSS_SPRITE_DIR);
	}

	public static class EnvWork {
		public final String FILE_SEPARATOR = System.getProperty("file.separator");
		public final String WORK_DIR=""; //XXX
		public final String TMP_DIR=""; //XXX
		public final String MIDDLE_FILE_ROOT = "D:/irilleRoot/";// 系统 中间数据目录
		public final String CSS_SPRITE_DIR = MIDDLE_FILE_ROOT + "cssSprite/"; // 小图片文件合并的目录
		public final Hashtable<String, Object> CSS_SPRITE_BUF = new Hashtable();

		private EnvWork() {
		}

		public String getSystemUser() {
			return System.getProperty("user.name");
		}
		
		public String getUserHome() {
			return System.getProperty("user.home");
		}
		
		
	}

	/**
	 * 开发环境的参数
	 * 
	 * @author whx
	 * 
	 */
	public static class EnvDep {
		private String _depDir=""; //XXX

		private EnvDep() {
		}

		public String getDepDir() {
			return _depDir;
		}
	}
}
