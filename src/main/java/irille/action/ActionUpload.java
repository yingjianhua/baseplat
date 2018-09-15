package irille.action;

import irille.core.sys.SysFileStock;
import irille.pub.Log;
import irille.pub.bean.BeanMain;
import irille.pub.uploads.FileSystemSaverNew;
import irille.pub.uploads.Photo;
import irille.util.MD5Util;

import java.io.File;
import java.io.IOException;

public class ActionUpload<MAIN extends BeanMain> extends ActionBase<MAIN> {
	public static final Log LOG = new Log(ActionUpload.class);

	private File _logo;
	private String _logoContentType;
	private String _logoFileName;
	private File _file;
	private File upfile;
	public File getUpfile() {
		return upfile;
	}

	public void setUpfile(File upfile) {
		this.upfile = upfile;
	}
	private String _fileContentType;
	private String _fileFileName;

	public Class beanClazz() {
		throw LOG.err("err","action中beanClazz未重写");
	}
	
	public File getLogo() {
		return _logo;
	}

	public void setLogo(File logo) {
		this._logo = logo;
	}

	public String getLogoContentType() {
		return _logoContentType;
	}

	public void setLogoContentType(String logoContentType) {
		this._logoContentType = logoContentType;
	}

	public String getLogoFileName() {
		return _logoFileName;
	}

	public void setLogoFileName(String logoFileName) {
		this._logoFileName = logoFileName;
	}

	public File getFile() {
		return _file;
	}

	public void setFile(File file) {
		_file = file;
	}

	public String getFileContentType() {
		return _fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		_fileContentType = fileContentType;
	}

	public String getFileFileName() {
		return _fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		_fileFileName = fileFileName;
	}

	public Photo saveFile(String module) {
		return saveFile(getFile(), getFileFileName(), module);
	}
	public Photo saveImage(String module, int maxWidth, int maxHeight) {
		return saveImage(getFile(), getFileFileName(), module, maxWidth, maxHeight);
	}

	public Photo saveLogo(String module) {
		return saveLogo(getLogo(), getLogoFileName(), module, 800, 600);
	}

	public Photo saveLogo() {
		return saveLogo(getLogo(), getLogoFileName(), "WEB", 800, 600);
	}
 
	public Photo saveFile(File file, String fileName, String module) {
		if (file == null)
			return null;
		FileSystemSaverNew uploadSys = new FileSystemSaverNew();
		try {
			Photo photo = uploadSys.saveFile(getUploadPath(true), file, fileName, module);
			photo.setImageURL(getUploadPath(true) + photo.getPreviewURL());
			String md5 = MD5Util.getMD5(photo.getImageURL());
			SysFileStock stock = SysFileStock.chkUniqueMd5(false, md5);
			if(stock==null) {
				stock = new SysFileStock();
				stock.setMd5(md5);
				stock.setPath(photo.getPreviewURL());
				stock.setRowVersion((short)1);
				stock.ins();
			} else {
				new File(photo.getImageURL()).delete();
				photo.setImageURL(getUploadPath(true)+stock.getPath());
				photo.setPreviewURL(stock.getPath());
			}
			return photo;
		} catch (IOException e) {
			e.printStackTrace();
			throw LOG.err("upload", "上传失败");
		}
	}
	public Photo saveImage(File file, String fileName, String module, int maxWidth, int maxHeight) {
		if (file == null)
			return null;
		FileSystemSaverNew uploadSys = new FileSystemSaverNew();
		try {
			Photo photo = uploadSys.savePic(getUploadPath(true), file, fileName, module, maxWidth, maxHeight);
			String md5 = MD5Util.getMD5(photo.getImageURL());
			SysFileStock stock = SysFileStock.chkUniqueMd5(false, md5);
			if(stock==null) {
				stock = new SysFileStock();
				stock.setMd5(md5);
				stock.setPath(photo.getPreviewURL());
				stock.setRowVersion((short)1);
				stock.ins();
			} else {
				System.out.println("delete tmpfile ["+photo.getImageURL()+"]:"+new File(photo.getImageURL()).delete());;
				photo.setImageURL(getUploadPath(true)+stock.getPath());
				photo.setPreviewURL(stock.getPath());
			}
			return photo;
		} catch (IOException e) {
			e.printStackTrace();
			throw LOG.err("upload", "上传失败");
		}
	}
	public Photo saveLogo(File file, String fileName, String module, int maxW, int maxH) {
		if (file == null)
			return null;
		FileSystemSaverNew uploadSys = new FileSystemSaverNew();
		try {
			Photo photo = uploadSys.savePic(getUploadPath(true), file, fileName, module, maxW, maxH);
			String md5 = MD5Util.getMD5(photo.getImageURL());
			SysFileStock stock = SysFileStock.chkUniqueMd5(false, md5);
			if(stock==null) {
				stock = new SysFileStock();
				stock.setMd5(md5);
				stock.setPath(photo.getPreviewURL());
				stock.setRowVersion((short)1);
				stock.ins();
			} else {
				new File(photo.getImageURL()).delete();
				photo.setImageURL(getUploadPath(true)+stock.getPath());
				photo.setPreviewURL(stock.getPath());
			}
			return photo;
		} catch (IOException e) {
			e.printStackTrace();
			throw LOG.err("upload", "上传失败");
		}
	}

}
