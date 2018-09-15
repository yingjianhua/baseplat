/**
 * 
 */
package irille.pub.bean;

import irille.gl.gs.GsGoods;
import irille.gl.gs.GsUom;

import java.math.BigDecimal;

/**
 * @author surface1
 * 
 */
public interface IPurLine extends IGoodsPrice{
  public Long getPkey();
  public void setPkey(Long pkey);
  public Integer getGoods();
  public void setGoods(Integer goods);
  public GsGoods gtGoods();
  public void stGoods(GsGoods goods);
  public Integer getUom();
  public void setUom(Integer uom);
  public GsUom gtUom();
  public void stUom(GsUom uom);
  public BigDecimal getQty();
  public void setQty(BigDecimal qty);
  public BigDecimal getPrice();
  public void setPrice(BigDecimal price);
  public BigDecimal getAmt();
  public void setAmt(BigDecimal amt);
  public BigDecimal getCostPur();
  public void setCostPur(BigDecimal costPur);
}
