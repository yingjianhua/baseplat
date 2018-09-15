/**
 * 
 */
package irille.pub.gl;

import irille.gl.gl.GlDaybookLine;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 分户账明细的接口
 * @author whx
 * 
 */
public interface IJournalLine  {
	public <T extends IJournalLine> T init();
	
	public GlDaybookLine gtDaybookLine();

	public void stDaybookLine(GlDaybookLine daybookLine);

	public Long getPkey();

	public void setPkey(Long pkey);

	public Long getMainPkey();

	public void setMainPkey(Long mainPkey);

	public BigDecimal getBalance();

	public void setBalance(BigDecimal balance);

	public Date getTallyDate();

	public void setTallyDate(Date tallyDate);
}
