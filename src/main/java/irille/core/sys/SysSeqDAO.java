//Created on 2005-9-27
package irille.core.sys;

import irille.gl.gs.GsGain;
import irille.pub.ClassTools;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.ISeq;
import irille.pub.idu.Idu;
import irille.pub.svr.Env;
import irille.pub.tb.Tb;

import java.util.Date;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class SysSeqDAO {
	public static final Log LOG = new Log(SysSeqDAO.class);

	public static void main(String[] args) {
		System.err.println(getSeqnumForm(GsGain.TB));
	}

	/**
	 * 初始化表的序号表信息，如果没有实现ISeq接口，则返回null
	 * 
	 * @param tb
	 * @return
	 */
	public static SysSeq init(Tb tb) {
		SysTable table = SysTable.gtTable(tb.getClazz());
		Bean bean = (Bean) ClassTools.newInstance(tb.getClazz());
		if (!(bean instanceof ISeq))
			return null;
		SysSeq seq;
		seq = new SysSeq();
		seq.init();
		((ISeq) bean).initSeq(seq);
		seq.setPkey(table.getPkey());
		if (BeanBase.chk(SysSeq.class, seq.getPkey()) == null)
			seq.ins();
		//更新暂时不处理 TODO
		return seq;
	}

	public static String getSeqnum(Tb tb, SysOrg org, boolean isForm) {
		SysTable table = SysTable.gtTable(tb.getClazz());
		SysSeq seq = BeanBase.chk(SysSeq.class, table.getPkey());
		if (seq == null) { // 没找到，初始化
			seq = init(tb);
			if (seq == null)
				throw LOG.err("err", "取类【{0}】的序号出错，没有实现接口【ISeq】", tb.getClazz());
		}
		if (seq.gtOrgFlag() && org == null)
			org = Idu.getOrg();
		Date now = Env.getSystemTime();
		String ymd = null, y, m, d;
		y = (now.getYear() + 1900) + "";
		m = "" + Str.right("0" + (now.getMonth() + 1), 2);
		d = "" + Str.right("0" + (now.getDate()), 2);
		switch (seq.gtType()) {
		case YYYY:
		case YY:
			ymd = y;
			break;
		case YYMM:
			ymd = Str.right(y, 2) + m;
			break;
		case DAY:
			ymd = y + m + d;
			break;
		}

		SysSeqLine line = getSeqnumLine(seq, org, ymd);
		line.setSeqnum(line.getSeqnum() + 1);
		line.upd();
		// 编号
		String str = "";
		if (Str.isEmpty(seq.getFirstStr()) == false)
			str += seq.getFirstStr();
		if (org != null)
			str += org.getCode() + "-";
		if (Str.isEmpty(ymd) == false)
			str += ymd + "-";
		if (isForm)
			str += Str.right("00000" + line.getSeqnum(), 6);
		else
			str += line.getSeqnum();
		return str;
	}

	/**
	 * 取普通序号
	 * 
	 * @param tb
	 * @return
	 */
	public static Integer getSeqnumInt(Tb tb) {
		String num = getSeqnum(tb,null, false);
		if (Str.isNum(num) == false)
			throw LOG.err("err", tb.getName() + "序号类型错误");
		return Integer.parseInt(num);
	}

	/**
	 * 取单据号
	 * 
	 * @param tb
	 * @return
	 */
	public static String getSeqnumForm(Tb tb) {
		return getSeqnum(tb,null, true);
	}

	private static SysSeqLine getSeqnumLine(SysSeq seq, SysOrg org, String ymd) {
		String pkey = seq.getPkey().toString();
		if (org != null)
			pkey += ":" + org.getPkey();
		SysSeqLine line;
		line = BeanBase.chk(SysSeqLine.class, pkey);
		if (line == null) {
			line = new SysSeqLine().init();
			line.setPkey(pkey);
			line.stOrg(org);
			line.setYmd(ymd);
			line.ins();
		} else if (ymd != null && !ymd.equals(line.getYmd())) { // 日期已变化，则序号复位
			line.setYmd(ymd);
			line.setSeqnum(0);
		}
		return line;
	}
}