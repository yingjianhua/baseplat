package irille.pub.gl;

import irille.core.sys.SysCell;
import irille.core.sys.Sys.OYn;
import irille.gl.gl.GlGoodsLine;
import irille.gl.gl.GlJournal;
import irille.gl.gl.GlSubject;
import irille.gl.gl.Gl.OTallyFlag;
import irille.gl.gl.Gl.OTallyState;
import irille.pub.Log;
import irille.pub.gl.TallyLineClasses.TallyLine;
import irille.pub.gl.TallyLineClasses.TallyLineAlias;
import irille.pub.gl.TallyLineClasses.TallyLineAlias2;
import irille.pub.gl.TallyLineClasses.TallyLineJournal;
import irille.pub.gl.TallyLineClasses.TallyLineSubject;
import irille.pub.gl.TallyLineClasses.TallyLineZh;
import irille.pub.svr.ISvrVars;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

/**
 * 记账分录组 Bill与Note 都需要产生此对象，用于记账
 * 
 * @author whx
 * 
 */
public class TallyLines<T extends TallyLines> implements ISvrVars {
	public static final String NAME_DEFAULT = ITallyBean.NAME_DEFAULT;

	private static final Log LOG = new Log(TallyLines.class);
	private Vector<TallyLine> _lines = new Vector();

	public Vector<TallyLine> getLines() {
		return _lines;
	}

	private <LINE extends TallyLine> LINE add(LINE line) {
		_lines.add(line);
		return line;
	}

	/**
	 * 对单据合并记账的分录进行合并，并校验合法性，更改入账状态(区别每日汇总的类型)
	 * @param ls
	 */
	public void mergeAndVerify(TallyLines ls) {
		for (TallyLine line : (Vector<TallyLine>) ls.getLines())
			mergeAndVerify(line);
	}

	private void mergeAndVerify(TallyLine line) {
		//		GlSubjectMap.chk
//		if (line.getAmt().signum() == -1 && !line.isNegativeAble()) {
//			throw LOG.err("amtSignum", "当前分录【{0}】金额【{1}】不允许为负数。", line.getName(), line.getAmt());
//		}
		GlJournal jl = line.getJournal();
		OTallyFlag flag = jl.gtTallyFlag();
		if (line.gtTallyState() == OTallyState.INIT) { // 初始状态
			if (jl.gtTallyFlag() == OTallyFlag.BILL) {// 记明细汇总标志 <OTallyFlag> BYTE。
				                                        // ONE:1,每日汇总成一笔 FORM:2,按单据汇总 AUTO:9,逐笔
				for (TallyLine l : _lines) {
					if (l.getJournal().getPkey().equals(jl.getPkey())) {
						if (l.gtDirect() == line.gtDirect())
							l.setAmt(l.getAmt().add(line.getAmt()));
						else
							l.setAmt(l.getAmt().add(line.getAmt().negate()));
						l.setDocNum(null);
						l.setSummary("单据合并入账");
						copyGoodsLine(l, line);
						return;
					}
				}
			}
		}
		TallyLine newLine = addByJournal(null, jl) //不知基什么原因，重新构造TALLYLINE对象
		    .set(line.getAmt(), line.getDirect(), line.getDocNum(), line.getSummary());
		newLine.setGoodsLine(line.getGoodsLine());
		if (line.gtTallyState() != OTallyState.INIT) { // 非初始状态
			newLine.stTallyState(line.gtTallyState());
			return;
		}
		switch (jl.gtTallyFlag()) {
		case ONE:
		case BILL:
			newLine.stTallyState(OTallyState.DONE_ONE);
			return;
		case TOTAL:
			newLine.stTallyState(OTallyState.WAIT_TOTAL);
			return;
		}
	}
	private void copyGoodsLine(TallyLine desc, TallyLine sour) {
		if(sour.getGoodsLine()==null) return ;
		if(desc.getGoodsLine()==null) {
			desc.setGoodsLine(sour.getGoodsLine());
		} else {
			List<GlGoodsLine> lines = desc.getGoodsLine();
			for(GlGoodsLine line: (List<GlGoodsLine>)sour.getGoodsLine()) {
				lines.add(line);
			}
		}
	}

	/**
	 * 分录借贷平衡检查 合并记账与表外的分录不参与平衡！
	 */
	public void balanceCheck() {
		BigDecimal jf = ZERO;
		BigDecimal df = ZERO;
		GlJournal jl;
		for (TallyLine line : _lines) {
			jl = line.getJournal();
			if (line.gtTallyState() == OTallyState.DONE_TOTAL // 汇总入账
			    || jl.gtInFlag().equals(OYn.NO)) // 表外
				continue;
			if (line.getDirect() == DF)
				df = df.add(line.getAmt());
			else
				jf = jf.add(line.getAmt());
		}
		if (jf.compareTo(df) != 0)
			throw LOG.err("notBalance", "分录【借:{0}】【贷:{1}】不平衡，差额【{2}】", jf, df, jf.subtract(df));
	}
	
	public TallyLineZh addByZh(String zh) {
		return addByZh(NAME_DEFAULT, zh);
	}

	public TallyLineZh addByZh(String name, String zh) {
		return add(new TallyLineZh(zh).setName(name));
	}

	public TallyLineJournal addByJournal(GlJournal jl) {
		return addByJournal(NAME_DEFAULT, jl);
	}

	public TallyLineJournal addByJournal(String name, GlJournal jl) {
		return add(new TallyLineJournal(jl).setName(name));
	}

	public TallyLineSubject addBySubject(SysCell cell, ITallyBean tallyBean, GlSubject subject) {
		return addBySubject(NAME_DEFAULT, cell, tallyBean, subject);
	}

	public TallyLineSubject addBySubject(String name, SysCell cell, ITallyBean tallyBean, GlSubject subject) {
		return add(new TallyLineSubject(cell, tallyBean, subject).setName(name));
	}

	public TallyLineAlias addByAlias(SysCell cell, ITallyBean tallyBean, String alias) {
		return addByAlias(NAME_DEFAULT, cell, tallyBean, alias);
	}

	public TallyLineAlias addByAlias(String name, SysCell cell, ITallyBean tallyBean, String alias) {
		return add(new TallyLineAlias(cell, tallyBean, alias).setName(name));
	}

	public TallyLineAlias2 addByAlias2(SysCell cell, ITallyBean objs, String aliasOrSubjectCode, String targetAilas) {
		return addByAlias2(NAME_DEFAULT, cell, objs, aliasOrSubjectCode, targetAilas);
	}

	public TallyLineAlias2 addByAlias2(String name, SysCell cell, ITallyBean objs, String aliasOrSubjectCode,
	    String targetAilas) {
		return add(new TallyLineAlias2(cell, objs, aliasOrSubjectCode, targetAilas).setName(name));
	}
}
