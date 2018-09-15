package irille.dep.gs;

import irille.dep.gs.EGsUnite.MyForm;
import irille.dep.gs.EGsUnite.myGoods;
import irille.dep.gs.EGsUnite.myUom;
import irille.gl.gs.GsSplit;
import irille.gl.gs.GsSplitLine;
import irille.gl.gs.GsUnite;
import irille.gl.gs.GsUnite.T;
import irille.pub.bean.CmbGoods;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtComp;
import irille.pub.html.EMCrtModelAndStore;
import irille.pub.html.EMForm;
import irille.pub.html.EMFormTwoRow;
import irille.pub.html.EMModel;
import irille.pub.html.EMZipWin;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtList;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class EGsSplit extends GsSplit
{

  public static void main(String[] args)
  {
    new EGsSplit().crtExt().crtFiles();
  }

  public EMCrt crtExt()
  {
    GsSplitLine.TB.getCode(); // 解决静态块初始化异常的问题。。。
    CmbGoods.TB.getCode();// 解决静态块初始化异常的问题。。。
    VFlds[] vflds = new VFlds[]
    { VFlds.newVFlds(new VFlds[]
        { new VFlds(TB), EMCrtModelAndStore.getGoodsVflds(T.GOODS.getFld()) }) };
    vflds[0].moveAfter("goodsSpec", "goods").moveAfter("goodsName", "goods");
    VFlds[] mflds = new VFlds[]
    { new VFlds(T.CODE, T.WAREHOUSE, T.GOODS, T.STATUS, T.ORG, T.DEPT) }; // 主表信息字段
    VFlds[] searchVflds = new VFlds[]
    { new VFlds(T.CODE, T.WAREHOUSE, T.GOODS, T.STATUS) }; // 搜索栏字段
    // 在上面改对MODEL、LIST、FROM是同时影响，下面则是各改各的-互不影响
    MyComp ext = new MyComp(TB, vflds, mflds, searchVflds, new VFlds[]
    { new VFlds(GsSplitLine.T.PKEY) });

    VFlds vs = ext.getVfldsForm();
    vs.setGoodsLink(T.GOODS);
    vs.del(T.PKEY, T.CODE, T.ORG, T.DEPT, T.STATUS, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME, T.AMT, T.AMT_COST);// FROM页面删除建档员等字段
    vs.moveAfter(T.QTY, T.UOM);// FORM页面的各字段位置调整
    vs.moveAfter(T.REM, T.QTY);// FORM页面的各字段位置调整
    // list字段排序调整
    VFlds vsl = ext.getVfldsList();
    vsl.moveLast(T.STATUS, T.ORG, T.DEPT, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME, T.REM);

    ext.newExts().init();
    return ext;
  }
  class MyForm extends EMFormTwoRow<MyForm>
  {

    public MyForm(Tb tb, VFlds... vFlds)
    {
      super(tb, vFlds);
    }

    @Override
    public void initFuns()
    {
      AddFun("onLoadGoodsForm", EGsPriceGoods.class).addFunParasExp("goodsCode,form");
      super.initFuns();
    }

    public void setFldAttr(VFld fld, ExtList fldList)
    {
      if (fld.getCode().equals(GsSplit.T.GOODS.getFld().getCode()))
      {
        fldList.add(new myGoods());
      } else if (fld.getCode().equals(GsSplit.T.UOM.getFld().getCode()))
      { fldList.add(new myUom());
      } else
      {
        super.setFldAttr(fld, fldList);
      }
    }
  }

  public static class myGoods implements IExtOut
  {

    @Override
    public void out(int tabs, StringBuilder buf)
    {
      buf.append(new EMModel(GsSplit.TB).loadFunCode(EGsSplit.class, "myGoods"));
    }

    @Override
    public String toString(int tabs)
    {
      return null;
    }
    // @formatter:off
    /** Begin myGoods ********
    xtype : 'comboauto',
    listConfig : {minWidth:250},
    fieldLabel : '货物',
    fields : ['pkey','code','name','spec'],//查询返回信息model
    valueField : ['pkey'],//提交值
    textField : 'code', //显示信息
    queryParam : 'code',//搜索使用
    name : 'bean.goods', //提交使用
    url : base_path + '/gs_GsGoods_autoComplete',
    urlExt : 'gs.GsGoods',
    hasBlur : false,
    afterLabelTextTpl : required,
    allowBlank : false,
    listeners : {
      scope : this,
      blur : function(field){
        this.onLoadGoodsForm(field.getRawValue(),this);
      }
    }
     *** End myGoods *********/
    /** Begin myUom ********
    xtype : 'beantrigger',
    name : 'bean.uom',
    fieldLabel : '计量单位',
    bean : 'GsUom',
    beanType : 'gs',
    emptyText : form_empty_text,
    afterLabelTextTpl : required,
    allowBlank : false,
    listeners : {
      scope : this,
      change : function(field, newv, oldv, eOpt){
        console.log("newv:"+newv);
        console.log("oldv:"+oldv);
        if(newv!=oldv) {
          field.diySql = 'uom_type = (select uom_type from gs_uom where pkey='+newv.split(bean_split)[0]+')';
        }
      }
    }
     *** End myUom *********/
    // @formatter:on
  }
  public static class myUom implements IExtOut
  {

    @Override
    public void out(int tabs, StringBuilder buf)
    {
      buf.append(new EMModel(GsSplit.TB).loadFunCode(EGsSplit.class, "myUom"));
    }

    @Override
    public String toString(int tabs)
    {
      return null;
    }
    // @formatter:off
    /** Begin myUom ********
    xtype : 'beantrigger',
    name : 'bean.uom',
    fieldLabel : '计量单位',
    bean : 'GsUom',
    beanType : 'gs',
    emptyText : form_empty_text,
    afterLabelTextTpl : required,
    allowBlank : false,
    listeners : {
      scope : this,
      change : function(field, newv, oldv, eOpt){
        console.log("newv:"+newv);
        console.log("oldv:"+oldv);
        if(newv!=oldv) {
          field.diySql = 'uom_type = (select uom_type from gs_uom where pkey='+newv.split(bean_split)[0]+')';
        }
      }
    }
     *** End myUom *********/
    // @formatter:on
  }
  class MyComp extends EMCrtComp<MyComp>
  {

    public MyComp(Tb tb, VFlds[] vflds, VFlds[] mainvflds, VFlds[] searchVflds, VFlds[] outVflds)
    {
      super(tb, vflds, mainvflds, searchVflds, outVflds);
      // TODO Auto-generated constructor stub
    }

    public ExtFile newForm()
    {
      EMFormTwoRow form = new MyForm(getTb(), getVfldsForm());
      form.setGoodsLink(true); // 货物的空间重写
      return form;
    }

    public ExtFile newZipWin(VFld fld)
    {
      EMZipWin win = new EMZipWin<EMZipWin>(getTb(), fld);
      win.setWidth(630);
      return win;
    }

  }

}
