package irille.pub.html;

import irille.pub.IPubVars;
import irille.pub.Log;
import irille.pub.svr.Env;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

/**
 * 图片合并类：通过合并产生一个大图片文件，并缓冲到“Base.CSS_SPRITE_BUF”中，主要被类"Style"中的方法“
 * BackgroudMerge”，“ImgMerge”调用。
 * 
 * @see Style
 * @author whx
 * 
 */
public class ImgMerge  implements IPubVars{
	private static final int STEP_HEIGHT=0; //图片之间的间隔点数
	private static final Log LOG= new Log(ImgMerge.class);
	private String _outFile;
	private Hashtable<String, PostSize> _postSizes;
	static Logger _logger = Logger.getLogger(ImgMerge.class.getName());
	private final static Hashtable<String, Object> _cssSpriteBuf = new Hashtable(); 

	public static void main(String[] args) {
		try {
			ImgMerge m = getMerge("log.gif",
			    "D:\\workspace\\irille\\webapp\\images\\logo\\",  "dlog.gif",
			    "dlog4j.gif", "dlog4j_links.gif");

			m = getMerge("log.gif", "D:\\workspace\\irille\\webapp\\images\\logo\\",
			     "dlog.gif", "dlog4j.gif", "dlog4j_links.gif");
			m.getPostSize("dlog4j_links.gif");
			// m.getPostSize("dlog4j_links.gif1");
			_logger.info("ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 取合并文件中的指定文件信息
	 * 
	 * @param mergeFile
	 *          合并的文件名
	 * @param imgFile 要合并的文件列表
	 * @return 返回ImgMerge对象，每次应用程序启动时将重新产生文件，这后就存到缓冲区中使用
	 */
	public static final ImgMerge getMerge(String outFileName, String inDir,
	  String... inFiles) {
		ImgMerge imgs;
		synchronized ("") {
			imgs = (ImgMerge) _cssSpriteBuf.get(outFileName);
			if (imgs != null)
				return imgs;
			imgs = merge(outFileName, inDir, inFiles);
			_cssSpriteBuf.put(outFileName, imgs);
		}
		return imgs;
	}

	private ImgMerge(String outFile, Hashtable<String, PostSize> postSizes) {
		super();
		_outFile = outFile;
		_postSizes = postSizes;
	}

	/**
	 * 最图片的位置
	 * @param imgFile 图片的文件名
	 * @return PostSize 位置
	 */
	public PostSize getPostSize(String imgFile) {
		PostSize ps = _postSizes.get(imgFile);
		if (ps != null)
			return ps;
		throw LOG.err("getPostSize","图片文件[{0}]没有定义在[{1}]中", imgFile, _outFile);
	}

	/**
	 * 合并指定的小图片文件为一个大文件
	 * 
	 * @param outFileName
	 *          要输出到的文件名, 将存在 Base.CSS_SPRITE_DIR 目录下
	 * @param inDir
	 *          小图片文件的目录
	 * @param inFiles
	 *          要合并的小图片文件列表
	 * @return 所有小图片的位置信息HashMap
	 */
	private static ImgMerge merge(String outFileName, String inDir,
	  String... inFiles) {
		outFileName = Env.INST.getCssSprateDir() + outFileName;
		Vector<BufferedImage> imgs = new Vector<BufferedImage>();
		File outFile;
		Hashtable<String, PostSize> postSizes = new Hashtable<String, PostSize>();
		BufferedImage img;
		PostSize pos;
		int y = 0;
		int maxWidth = 0;
		String logStr = "";
		for (String file : inFiles) {
			try {
				img = ImageIO.read(new File(inDir + file));
			} catch (Exception e) {
				throw LOG.err(e,"fileOpen","打开文件[{0}]出错!",inDir + file);
			}
			imgs.add(img);
			pos = new PostSize(img.getWidth(), img.getHeight(), y);
			y += img.getHeight() + STEP_HEIGHT; // 累加高度
			if (img.getWidth() > maxWidth) // 最大宽度
				maxWidth = img.getWidth();
			postSizes.put(file, pos);
			logStr += "	" + inDir + file + LN;
		}
		_logger.info("合并以下小图片文件->：" + outFileName + LN + logStr);
		try {
			outFile = new File(outFileName);
		} catch (Exception e) {
			throw LOG.err(e,"fileOpen","打开文件[{0}]出错!",outFileName);
		}

		BufferedImage imageSaved = new BufferedImage(maxWidth, y - STEP_HEIGHT,
		    BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = imageSaved.createGraphics();
		y = 0;
		for (int i = 0; i < imgs.size(); i++) {
			g2d.drawImage(imgs.get(i), null, 0, y);
			y += imgs.get(i).getHeight() + STEP_HEIGHT; // 累加高度
		}
		try {
			ImageIO.write(imageSaved, "gif", outFile);
		} catch (Exception e) {
			throw LOG.err(e,"fileOpen","打开文件[{0}]出错!",outFileName);
		}
		return new ImgMerge(outFileName, postSizes);
	}

	/**
	 * 小图片的位置信息
	 * 
	 * @author whx
	 * 
	 */
	public static class PostSize {
		private int _width;
		private int _height;
		private int _y;

		public PostSize(int width, int height, int y) {
			_width = width;
			_height = height;
			_y = y;
		}

		public int getWidth() {
			return _width;
		}

		public int getHeight() {
			return _height;
		}

		public int getY() {
			return _y;
		}
		public int getX() {
			return 0;
		}
	}

	/**
	 * 取输出的文件名
	 * @return
	 */
	public String getOutFile() {
		return _outFile;
	}

	/**
	 * 取各图片的位置信息
	 * @return
	 */
	public Hashtable<String, PostSize> getPostSizes() {
		return _postSizes;
	}
}
