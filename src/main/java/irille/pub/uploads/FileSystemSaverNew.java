/*
 *  FileSystemSaver.java
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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import irille.pub.Log;
import irille.pub.Str;
import irille.pub.svr.DepConstants;

/**
 * 将照片保存在文件系统中
 * 
 * @author Winter Lau
 */
public class FileSystemSaverNew {
	private static final Log LOG = new Log(FileSystemSaverNew.class);

	public Photo saveFile(String realPath, File fileTmp, String fileName, String module)
	    throws IOException {
		String extendName = Str.getFileExtend(fileName).toLowerCase();
		// 原图
		String root = getUploadPath(realPath);
		String url = this.createNewPhotoURI(root, extendName, module);
		String oldFile = root + url;
		// 临时文件 拷贝到 uploads目录下
		ImageUtils.writeToFile(fileTmp, oldFile);
		Photo photo = new Photo();
		photo.setFileName(fileName);
		photo.setPreviewURL(DepConstants.FILE_SEPA + url);
		photo.setSize((int)fileTmp.length());
		return photo;
	}

	//根据要求的最大高度和最大宽度，计算出压缩后图片的宽度和高度,do的值为，0：表示不压缩，1：表示压缩
	private static Map getNewSize(int oldWidth, int oldHeight, int maxWidth, int maxHeight) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("height", oldHeight);
		result.put("width", oldWidth);
		result.put("do", 0);
		if(oldWidth>0&oldHeight>0&&maxWidth>0&&maxHeight>0) {
			double wscale = (double)maxWidth/oldWidth;
			double hscale = (double)maxHeight/oldHeight;
			double scale = wscale<hscale?wscale:hscale;
			if(scale<1) {
				result.put("width", (int)(oldWidth*scale));
				result.put("height", (int)(oldHeight*scale));
				result.put("do", 1);
			}
		}
		return result;
	}
	public Photo savePic(String realPath, File fileTmp, String fileName, String module, int maxWidth, int maxHeight) throws IOException {
		if(!ImageUtils.isImage(fileName)) {
			throw LOG.err("state", "不允许的文件格式");
		}
		BufferedImage buffer = ImageIO.read(fileTmp);
		//判断是否需要压缩,并计算出最终图片的宽度和高度
		Map<String, Integer> wh_map = getNewSize(buffer.getWidth(), buffer.getHeight(), maxWidth, maxHeight);
		boolean compress = wh_map.get("do")==0?false:true;
		int width = wh_map.get("width");
		int height = wh_map.get("height");
		Photo photo = new Photo();
		photo.setWidth(width);
		photo.setHeight(height);
		String extendName = Str.getFileExtend(fileName).toLowerCase();
		if(ImageUtils.isJPG(extendName)) {
			//将图片的元数据保存到photo中
		} else if(ImageUtils.isBMP(extendName)) {
			compress = true;
			extendName = "jpg";
		}
		// 原图
		System.out.println("realPath:"+realPath);
		String root = getUploadPath(realPath);
		System.out.println("root:"+root);
		String url = createNewPhotoURI(root, extendName, module);
		System.out.println("url:"+url);
		String imageURL = root + url;
		if(compress) {
			System.out.println("compress:"+compress);
			ImageUtils.createThumbnail(buffer, new File(imageURL), width, height);
			photo.setSize((int) new File(imageURL).length());
		} else {
			System.out.println("compress:"+compress);
			ImageUtils.writeToFile(fileTmp, imageURL);
		}
		//图片的原文件名
		photo.setFileName(fileName);
		//图片的绝对路径
		photo.setImageURL(imageURL);
		//图片在realPath下的相对路径
		
		photo.setPreviewURL(DepConstants.FILE_SEPA + url);
		photo.setSize((int)fileTmp.length());
		return photo;
	}
	public Photo savePic2(String realPath, File fileTmp, String fileName, String module, int maxWidth,
	    int maxHeight) throws IOException {
		String extendName = Str.getFileExtend(fileName).toLowerCase();
		// 原图
		String root = getUploadPath(realPath);
		String url = this.createNewPhotoURI(root, extendName, module);
		String oldFile = root + url;
		// 临时文件 拷贝到 uploads目录下
		ImageUtils.writeToFile(fileTmp, oldFile);

		Photo photo = new Photo();
		//假如该文件是jpg格式
		if (ImageUtils.isJPG(extendName)) {
			try {
				//将图片的元数据保存到photo中
			} catch (Exception e) {
				throw LOG.err("upload", "Exception occur when reading EXIF of " + oldFile);
			}
		//假如该文件是bmp格式
		} else if (ImageUtils.isBMP(extendName)) {
			//将图片转换成jpg格式
			String jpgName = ImageUtils.BMP_TO_JPG(oldFile);
			if (jpgName != null) {
				// 删除bmp文件
				if (new File(oldFile).delete())
					oldFile = jpgName;
			}
		}

		// 获取图片的基本信息，例如大小尺寸象素等
		File fOrigionalImage = new File(oldFile);
		photo.setSize((int) fOrigionalImage.length());
		BufferedImage oldImage = (BufferedImage) ImageIO.read(fOrigionalImage);
		int old_width = oldImage.getWidth();
		int old_height = oldImage.getHeight();
		photo.setWidth(old_width);
		photo.setHeight(old_height);
		photo.setColorBit(oldImage.getColorModel().getPixelSize());
		photo.setFileName(fileName);
		photo.setPreviewURL(DepConstants.FILE_SEPA + url);
		photo.setImageURL(oldFile);
		if (maxWidth > 0 && maxHeight > 0) {
			// 将原图片限定在1280*1024的范围内
			int ori_width = maxWidth, ori_height = maxHeight;
			boolean regenerate_img = true;
			if (old_width <= maxWidth && old_height <= maxHeight) {
				ori_width = old_width;
				ori_height = old_height;
				regenerate_img = false;
			} else {
				System.out.println("1");
				if (old_width >= old_height) {
					System.out.println("2");
					ori_width = Math.min(maxWidth, photo.getWidth());
					// 按比例对图像高度进行压缩
					ori_height = photo.getHeight() * ori_width / photo.getWidth();
					// }
				} else {
					System.out.println("3");
					System.out.println("maxHeight:"+maxHeight);
					System.out.println("photo.getHeight():"+photo.getHeight());
					System.out.println("ori_height:"+ori_height);
					System.out.println("photo.getWidth():"+photo.getWidth());
					System.out.println("maxWidth:"+maxWidth);
					ori_height = Math.min(maxHeight, photo.getHeight());
					// 按比例对图像高度进行压缩
					ori_width = photo.getWidth() * ori_height / photo.getHeight();
					// }
				}
			}
			System.out.println("ori_width:"+ori_width);
			System.out.println("ori_height:"+ori_height);
			if (regenerate_img) {
				photo.setWidth(ori_width);
				photo.setHeight(ori_height);
				ImageUtils.createThumbnail(oldFile, oldFile, ori_width, ori_height);
				// ImageUtils.createPreviewImage(new FileInputStream(oldFile), oldFile,
				// ori_width, ori_height);
				photo.setSize((int) new File(oldFile).length());
			}
		}
		return photo;
	}

	public void saveNew(Photo photo, int prev_width, int prev_height, int index, boolean write)
	    throws IOException {

		String extendName = Str.getFileExtend(photo.getImageURL()).toLowerCase();
		String previewPath = photo.getImageURL().replaceAll("." + extendName,
		    "_" + index + "." + extendName);
		// 计算略缩图的最适合尺寸
		int preview_width, preview_height, width, height;
		width = photo.getWidth();
		height = photo.getHeight();
		if (width >= height) {
			preview_width = Math.min(prev_width, photo.getWidth());
			// 按比例对图像高度进行压缩
			preview_height = photo.getHeight() * preview_width / photo.getWidth();
			// }
		} else {
			preview_height = Math.min(prev_height, photo.getHeight());
			// 按比例对图像高度进行压缩
			preview_width = photo.getWidth() * preview_height / photo.getHeight();
			// }
		}
		// 生成略缩图
		previewPath = ImageUtils.createThumbnail(photo.getImageURL(), previewPath, preview_width,
		    preview_height);
		// previewPath = ImageUtils.createPreviewImage(
		// new FileInputStream(photo.getImageURL()), previewPath, preview_width,
		// preview_height);

		// 水印
		// if (write && photo.getWidth() > 30 && photo.getHeight() > 30) {
		// String shuiying = getUploadPath(servlet) + "images/shuiyin.png";
		// ImageSet.pressImage(shuiying, previewPath, 10, 10);
		// }
	}

	public Photo cut(String realPath, Photo photo1, int point, int srcCount) {
		return cut(realPath, photo1, point, point, srcCount);
	}

	public Photo cut(String realPath, Photo photo1, int pointx, int pointy, int srcCount) {
		try {
			String extendName = Str.getFileExtend(photo1.getImageURL()).toLowerCase();
			String url = photo1.getImageURL().replaceAll("." + extendName, "_9." + extendName);
			String oldurl = photo1.getImageURL();
			String pach = getUploadPath(realPath);
			BufferedImage bi = ImageIO.read(new File(oldurl));
			int srcWidth = bi.getWidth();
			int srcHeight = bi.getHeight();

			int descY, descWidth, descX, descHeight;

			double s1 = (double) srcWidth / srcHeight;
			double s2 = (double) pointx / pointy;

			if (s1 > s2) {
				descY = 0;
				descX = (int) ((srcWidth - srcHeight * s2) / 2);
				descHeight = srcHeight;
				descWidth = (int) (srcHeight * s2);
			} else {
				descX = 0;
				descY = (int) ((srcHeight - srcWidth / s2) / 2);
				descWidth = srcWidth;
				descHeight = (int) (srcWidth / s2);
			}

			ImageFilter cropFilter = new CropImageFilter(descX, descY, descWidth, descHeight);

			Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
			Image img = Toolkit.getDefaultToolkit().createImage(
			    new FilteredImageSource(image.getSource(), cropFilter));
			BufferedImage tag = new BufferedImage(descWidth, descHeight, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(img, 0, 0, null);
			g.dispose();
			ImageIO.write(tag, "JPEG", new File(url));

			String newurl = url.replaceAll("_9." + extendName, "_10." + extendName);
			Photo photo = new Photo();
			photo.setPreviewURL(newurl.replace(pach, ""));
			photo.setImageURL(newurl);
			photo.setWidth(pointx);
			photo.setHeight(pointy);

			ImageUtils.createThumbnail(url, newurl, pointx, pointy);

			// ImageUtils.createPreviewImage(st, newurl, point, point);

			return photo;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
		}
		return null;
	}

	public Photo cut(String realPath, String src, int x, int y, int w, int h, int souce) {
		try {
			String extendName = Str.getFileExtend(src).toLowerCase();
			String root = getUploadPath(realPath);
			String url = this.createNewPhotoURI(root, extendName);
			String pach = getUploadPath(realPath);
			BufferedImage bi = ImageIO.read(new File(pach + src));
			int srcWidth = bi.getWidth();
			int srcHeight = bi.getHeight();

			int tmpSouce;
			int descY, descWidth, descX, descHeight;

			if (srcWidth <= souce && srcHeight <= souce) {
				descY = y;
				descWidth = w;
				descX = x;
				descHeight = h;
				souce = srcWidth;
			}

			if (srcWidth / srcHeight > 1) {
				tmpSouce = souce * srcHeight / srcWidth;
				descY = y * srcHeight / tmpSouce;
				descWidth = w * srcWidth / souce;
				descX = x * srcWidth / souce;
				descHeight = h * srcHeight / tmpSouce;
			} else {
				tmpSouce = souce * srcWidth / srcHeight;
				descY = y * srcHeight / souce;
				descWidth = w * srcWidth / tmpSouce;
				descX = x * srcWidth / tmpSouce;
				descHeight = h * srcHeight / souce;
			}
			ImageFilter cropFilter = new CropImageFilter(descX, descY, descWidth, descHeight);

			Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
			Image img = Toolkit.getDefaultToolkit().createImage(
			    new FilteredImageSource(image.getSource(), cropFilter));
			BufferedImage tag = new BufferedImage(descWidth, descHeight, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(img, 0, 0, null);
			g.dispose();
			ImageIO.write(tag, "JPEG", new File(pach + url));
			Photo photo = new Photo();
			photo.setPreviewURL(DepConstants.FILE_SEPA + url);
			photo.setImageURL(pach + url);
			photo.setWidth(descWidth);
			photo.setHeight(descHeight);
			return photo;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
			// TODO: handle exception
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.liusoft.dlog4j.photo.PhotoSaver#delete(java.lang.String)
	 */
	public boolean delete(String realPath, String imgURL) throws IOException {
		String uploadPath = this.getUploadPath(realPath);
		if (imgURL.startsWith(DepConstants.FILE_SEPA)) {
			imgURL = imgURL.substring(DepConstants.FILE_SEPA.length());
		}
		// String baseURI = this.getPhotoBaseURI(ctx);
		String path = uploadPath + imgURL.replaceAll(DepConstants.FILE_SEPA, File.separator);
		File f = new File(path);
		if (f.exists() && f.isFile())
			return f.delete();
		return false;
	}

	public InputStream read(String realPath, String imgURL) throws IOException {
		String uploadPath = this.getUploadPath(realPath);
		String baseURI = this.getPhotoBaseURI(realPath);
		String path = uploadPath
		    + imgURL.substring(baseURI.length()).replaceAll(DepConstants.FILE_SEPA, File.separator);
		File f = new File(path);
		if (f.exists() && f.isFile())
			return new FileInputStream(f);
		return null;
	}

	public OutputStream write(String realPath, String imgURL) throws IOException {
		String uploadPath = this.getUploadPath(realPath);
		String baseURI = this.getPhotoBaseURI(realPath);
		String path = uploadPath
		    + imgURL.substring(baseURI.length()).replaceAll(DepConstants.FILE_SEPA, File.separator);
		File f = new File(path);
		if (f.exists() && f.isFile())
			return new FileOutputStream(f, false);
		return null;
	}

	public static String createNewPhotoURI(String root, String ext) throws IOException {
		return createNewPhotoURI(root, ext, null);
	}

	public static String createNewPhotoURI(String root, String ext, String module) throws IOException {
		Calendar cal = Calendar.getInstance();
		StringBuffer dir = new StringBuffer();
		dir.append(cal.get(Calendar.YEAR));
		String tmp = ""+(cal.get(Calendar.MONTH) + 1);
		tmp = tmp.length() == 1 ? "0"+tmp : tmp; 
		dir.append(tmp);
		tmp = "" + cal.get(Calendar.DATE);
		tmp = tmp.length() == 1? "0"+tmp : tmp;
		dir.append(tmp + DepConstants.FILE_SEPA);
		if (Str.isEmpty(module) == false)
			dir.append(module + DepConstants.FILE_SEPA);
		File file = new File(root + dir.toString());
		if (!file.exists()) {
			// 如果目录不存在则创建目录
			synchronized (FileSystemSaverNew.class) {
				if (!file.mkdirs())
					return null;
			}
		}
		file = null;
		int times = 0;
		// 生成独一无二的文件名
		do {
			String str = String.valueOf(System.currentTimeMillis());
			StringBuffer fn_u = new StringBuffer();
			fn_u.append(cal.get(Calendar.MONTH) + 1);
			fn_u.append(cal.get(Calendar.DATE));
			fn_u.append(str);
			// str.substring(str.length()-4,str.length);
			fn_u.append(".");
			fn_u.append(ext);
			String fn_uu = String.valueOf(fn_u);
			File f = new File(root + dir + fn_uu);
			if (!f.exists()) {
				try {
					if (f.createNewFile())
						return dir + fn_uu;
				} catch (SecurityException e) {
				} catch (IOException e) {
				} finally {
					f = null;
				}
			}
			times++;
		} while (times < 10);
		return null;
	}

	/**
	 * 返回保存图像的物理路径
	 * @param ctx
	 * @return
	 */
	private String getUploadPath(String dir) {
		if (upload_path != null)
			return upload_path;
		if (dir.startsWith(DepConstants.LOCAL_PATH_PREFIX))
			upload_path = dir.substring(DepConstants.LOCAL_PATH_PREFIX.length());
		else
			upload_path = dir;
		if (!upload_path.endsWith(DepConstants.FILE_SEPA))
			upload_path += DepConstants.FILE_SEPA;

		return upload_path;
	}

	/**
	 * 返回图像对应的BaseURL
	 * 
	 * @param ctx
	 * @return
	 */
	private String getPhotoBaseURI(String realPath) {
		if (upload_uri != null)
			return upload_uri;
		upload_uri = realPath;
		if (!upload_uri.endsWith(DepConstants.FILE_SEPA))
			upload_uri += DepConstants.FILE_SEPA;
		return upload_uri;
	}

	private String upload_path = null;
	private String upload_uri = null;

}
