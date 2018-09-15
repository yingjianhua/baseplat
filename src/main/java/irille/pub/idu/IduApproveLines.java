package irille.pub.idu;

import irille.pub.bean.Bean;

import java.util.List;

public class IduApproveLines<T extends IduApproveLines, BEAN extends Bean, LINES extends Bean>
		extends IduApprove<T, BEAN> {
	private List<LINES> _lines;

	public List<LINES> getLines() {
		return _lines;
	}

	public void setLines(List<LINES> lines) {
		_lines = lines;
	}

}
