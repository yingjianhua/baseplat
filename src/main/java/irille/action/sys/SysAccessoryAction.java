package irille.action.sys;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import irille.action.ActionUpload;
import irille.core.sys.SysAccessory;
import irille.pub.Str;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;
import irille.pub.uploads.ImageUtils;
import irille.pub.uploads.Photo;

public class SysAccessoryAction extends ActionUpload<SysAccessory> {
	private int _widthLimit;

	public int getWidthLimit() {
		return _widthLimit;
	}

	public void setWidthLimit(int widthLimit) {
		_widthLimit = widthLimit;
	}

	public SysAccessory getBean() {
		return _bean;
	}

	public void setBean(SysAccessory bean) {
		this._bean = bean;
	}
	
	public int getHeightLimit(File image, int widthLimit) throws IOException {
		return widthLimit * ImageUtils.getImageHeight(image) / ImageUtils.getImageWidth(image);
	}

	@Override
	public Class beanClazz() {
		return SysAccessory.class;
	}

	public void uploadImage() throws IOException, JSONException {
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = new JSONObject();
		boolean isImage = ImageUtils.isImage(getFileFileName());
		int width = getWidthLimit() == 0 ? ImageUtils.getImageWidth(getFile()) : getWidthLimit();
		int height = getWidthLimit() == 0 ? ImageUtils.getImageHeight(getFile()) : getHeightLimit(getFile(), getWidthLimit());
		if(!isImage) {
			json.put("sucess", false);
			json.put("state", "不允许的文件格式");
			response.getWriter().print(json);
		} else {
			Photo file = saveImage("Sys", width, height);
			String originalName = file.getFileName();
			int size = file.getSize();
			String state = "SUCCESS";
			String url = getUploadPath(false)+file.getPreviewURL();
			String type = url.substring(url.lastIndexOf(".")+1);
			String name = url.substring(url.lastIndexOf("/")+1, url.lastIndexOf("."));
			
			json.put("state", state);
			json.put("name", name);
			json.put("originalName", originalName);
			json.put("size", size);
			json.put("url", url);
			json.put("type", type);
			json.put("success", true);
			response.getWriter().print(json);
		}
	}
	public void uploadFile() throws JSONException, IOException  {
		Photo file = saveFile("Sys");
		String url = getUploadPath(false)+file.getPreviewURL();
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = new JSONObject();
		json.put("url", url);
		json.put("success", true);
		response.getWriter().print(json);
	}
	
	@Override
	public void insBefore() {
		super.insBefore();
		Photo file = saveFile("Sys");
		getBean().setPath(file.getPreviewURL());
		getBean().setSize(
				new BigDecimal(file.getSize()).divide(new BigDecimal(1024), 4,
						BigDecimal.ROUND_HALF_DOWN));
	}

/*	public void listForComm() throws Exception {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		List<SysAccessory> list = SysAccessoryDAO.listByComm(((Integer) getPkey()).longValue());
		JSONObject lineJson = null;
		for (SysAccessory line : list) {
			lineJson = crtJsonByBean(line);
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		writerOrExport(json);
	}
*/
	public String crtFilter() throws JSONException {
		if (Str.isEmpty(getFilter()))
			return crtFilterAll() + orderBy();
		// [{"property":"code","value":"aaa"},{"property":"name","value":"bbb"}]
		JSONArray ja = new JSONArray(getFilter());
		String sql = "";
		Tb tb = tb();
		for (int i = 0; i < ja.length(); i++) {
			JSONObject json = ja.getJSONObject(i);
			String fldName = json.getString(QUERY_PROPERTY);
			String param = json.getString(QUERY_VALUE);
			if (fldName.equals("planPkey")) {
				sql += " AND tbandkey_pkey in(select pkey from tp_plan_log where plan = "
						+ param + ")";
				continue;
			}
			if (!tb.chk(fldName))
				continue;
			Fld fld = tb.get(fldName);
			if (fld == null)
				continue;
			sql += " AND " + Env.INST.getDB().crtWhereSearch(fld, param);
		}
		System.err.println(sql);
		return crtFilterAll() + sql + orderBy();
	}

}
