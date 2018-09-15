/*
 *  ImageUtils.java
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Library General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *  
 *  Author: Winter Lau (javayou@gmail.com)
 *  http://dlog4j.sourceforge.net
 */
package irille.pub.uploads;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import irille.pub.Log;
import irille.pub.Str;

public class ImageUtils {
	private ImageUtils(){};

	private static final Log LOG = new Log(ImageUtils.class);

	public static String createThumbnail(String src, String dist, int width, int height) {
		File file_src = new File(src);
		if(!file_src.exists()) {
			throw LOG.err("nofile", "文件不存在！");
		}
		return createThumbnail(file_src, new File(dist), width, height);
	}
	public static String createThumbnail(BufferedImage image, File dist, int width, int height) {
		try {
			System.out.println(image.getHeight());
			System.out.println(image.getWidth());
			BufferedImage bfImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			bfImage.getGraphics().drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH),
			    0, 0, null);
			FileOutputStream os = new FileOutputStream(dist);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
			encoder.encode(bfImage);
			os.close();
			System.out.println("创建缩略图成功");
		} catch (Exception e) {
			System.out.println("创建缩略图发生异常" + e.getMessage());
			return null;
		}
		return dist.getPath();
	}
	public static String createThumbnail(File src, File dist, int width, int height) {
		try {
			System.out.println("src:"+src);
			BufferedImage image = ImageIO.read(src);
			BufferedImage bfImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			bfImage.getGraphics().drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH),
			    0, 0, null);
			FileOutputStream os = new FileOutputStream(dist);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
			encoder.encode(bfImage);
			os.close();
			System.out.println("创建缩略图成功");
		} catch (Exception e) {
			System.out.println("创建缩略图发生异常" + e.getMessage());
			return null;
		}
		return dist.getPath();
	}

	public static String createPreviewImage(InputStream orig_img, String obj_filename, int p_width,
	    int p_height) throws IOException {
		String extendName = Str.getFileExtend(obj_filename).toLowerCase();
		FileOutputStream newimage = null;
		InputStream fis = orig_img;
		try {
			if ("gif".equalsIgnoreCase(extendName)) {
				fis.close();
				fis = null;
				// GifImage newGif = GifTransformer.resize(gifImage, p_width,p_height,
				// false);
				newimage = new FileOutputStream(obj_filename);
				// GifEncoder.encode(newGif, newimage);
			} else {
				BufferedImage orig_portrait = (BufferedImage) ImageIO.read(fis);
				fis.close();
				fis = null;
				BufferedImage bi = new BufferedImage(p_width, p_height, BufferedImage.TYPE_INT_RGB);
				bi.getGraphics().drawImage(orig_portrait, 0, 0, p_width, p_height, null);
				obj_filename = obj_filename.substring(0, obj_filename.length() - 1 - extendName.length())
				    + "." + extendName;
				// if(!obj_filename.endsWith(".jpg"))
				// obj_filename += ".jpg";
				newimage = new FileOutputStream(obj_filename);
				ImageIO.write(bi, extendName, newimage);
			}
		} finally {
			if (newimage != null)
				newimage.close();
			if (fis != null)
				fis.close();
		}
		return obj_filename;
	}

	public static void writeToFile(File file, String outPath) throws IOException {
		FileOutputStream out = null;
		InputStream in = null;
		byte[] data = new byte[8192];
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			out = new FileOutputStream(outPath);
			do {
				int rc = in.read(data);
				if (rc == -1)
					break;
				out.write(data, 0, rc);
				if (rc < data.length)
					break;
			} while (true);
		} finally {
			data = null;
			if (out != null)
				out.close();
			if (in != null)
				in.close();
		}
	}

	public static boolean rotateImage(String img_fn, int orient) throws IOException {
		return rotateImage(img_fn, orient, img_fn);
	}

	public static boolean rotateImage(String img_fn, int orient, String dest_fn) throws IOException {
		double radian = 0;
		switch (orient) {
		case 3:
			radian = 180.0;
			break;
		case 6:
			radian = 90.0;
			break;
		case 8:
			radian = 270.0;
			break;
		default:
			return false;
		}
		BufferedImage old_img = (BufferedImage) ImageIO.read(new File(img_fn));
		int width = old_img.getWidth();
		int height = old_img.getHeight();

		BufferedImage new_img = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = new_img.createGraphics();

		AffineTransform origXform = g2d.getTransform();
		AffineTransform newXform = (AffineTransform) (origXform.clone());
		// center of rotation is center of the panel
		double xRot = 0;
		double yRot = 0;
		switch (orient) {
		case 3:
			xRot = width / 2.0;
			yRot = height / 2.0;
		case 6:
			xRot = height / 2.0;
			yRot = xRot;
			break;
		case 8:
			xRot = width / 2.0;
			yRot = xRot;
			break;
		default:
			return false;
		}
		newXform.rotate(Math.toRadians(radian), xRot, yRot);

		g2d.setTransform(newXform);
		// draw image centered in panel
		g2d.drawImage(old_img, 0, 0, null);
		// Reset to Original
		g2d.setTransform(origXform);

		FileOutputStream out = new FileOutputStream(dest_fn);
		try {
			ImageIO.write(new_img, "JPG", out);
		} finally {
			out.close();
		}
		return true;
	}

	public static boolean isImage(String fn) {
		fn = fn.toLowerCase();
		return fn.endsWith("jpg") || fn.endsWith("png") || fn.endsWith("gif") || fn.endsWith("bmp")
		    || fn.endsWith("jpeg");
	}

	public static boolean isJPG(String fn) {
		if (fn == null)
			return false;
		String s_fn = fn.toLowerCase();
		return s_fn.endsWith("jpg") || s_fn.endsWith("jpge");
	}

	/**
	 * 锟叫讹拷锟角凤拷为JPG图片
	 * 
	 * @param fn
	 * @return
	 */
	public static boolean isBMP(String fn) {
		if (fn == null)
			return false;
		String s_fn = fn.toLowerCase();
		return s_fn.endsWith("bmp");
	}

	public static String BMP_TO_JPG(File file) {
		try {
			BufferedImage oldImage = (BufferedImage) ImageIO.read(file);
			String jpgName = file.getPath() + ".jpg";
			FileOutputStream newimage = new FileOutputStream(jpgName);
			try {
				if (ImageIO.write(oldImage, "jpg", newimage))
					return jpgName;
			} finally {
				if (newimage != null)
					newimage.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String BMP_TO_JPG(String imgPath) throws IOException {
		File file = new File(imgPath);
		return BMP_TO_JPG(file);
	}
	
	/**
	 * 是否横图
	 * @param image
	 * @return
	 * @throws IOException
	 */
	public static boolean isHorizontal(File image) throws IOException {
		return getImageShape(image) == 1;
	}
	
	/**
	 * 是否竖图
	 * @param image
	 * @return
	 * @throws IOException
	 */
	public static boolean isVertical(File image) throws IOException {
		return getImageShape(image) == 2;
	}
	
	/**
	 * 是否方图
	 * @param image
	 * @return
	 * @throws IOException
	 */
	public static boolean isSquare(File image) throws IOException {
		return getImageShape(image) == 0;
	}
	
	/**
	 * 获取图片类型
	 * @param image
	 * @return 0表示方图、1表示横图、2表示竖图
	 * @throws IOException
	 */
	public static int getImageShape(File image) throws IOException {
		BufferedImage img = (BufferedImage) ImageIO.read(image);
		if (img.getWidth() > img.getHeight())
			return 1;
		else if (img.getWidth() < img.getHeight())
			return 2;
		else
			return 0;
	}
	
	/**
	 * 获取图片宽度
	 * @param image
	 * @return
	 * @throws IOException
	 */
	public static int getImageWidth(File image) throws IOException {
		BufferedImage img = (BufferedImage) ImageIO.read(image);
		return img.getWidth();
	}
	
	/**
	 * 获取图片高度
	 * @param image
	 * @return
	 * @throws IOException
	 */
	public static int getImageHeight(File image) throws IOException {
		BufferedImage img = (BufferedImage) ImageIO.read(image);
		return img.getHeight();
	}
}
