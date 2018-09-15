package irille.pub.view;

import org.omg.stub.java.rmi._Remote_Stub;

import irille.pub.Str;
import irille.pub.html.ExtList;
import irille.pub.tb.Fld;
import oracle.net.jdbc.TNSAddress.AddressList;

public class VFldImg<T extends VFldImg> extends VFld<VFldImg> {
	private String _labelWidth = "100";

	public VFldImg(Fld fld) {
		super(fld,"imagefield","string");
	}

	@Override
	public T copyNew(Fld fld) {
		return (T) copy(new VFldImg(fld));
	}

	public String extModel() {
		return "{name : 'bean." + getCode() + "' , mapping : '" + getCode() + "' , type : 'string'},";
	}

	public String extForm(String lnt, boolean isComp) {
		String tmp = "" +
		/**/"{" + lnt +
		/**/"xtype : 'textareafield'," + lnt +
		/**/"name : 'bean." + getCode() + "'," + lnt + extFormNull(lnt,isComp) + extFormEmpty(lnt, isComp) +
		/**/"fieldLabel : '" + getName() + "'" + lnt +
		/**/"}";
		return tmp;
	}
	
	public String extWinSearch(String lnt, boolean isComp) {
		String tmp = "" +
		/**/"{" + lnt +
		/**/"	xtype : 'label'," + lnt +
		/**/"	text : '" + getName() + "'" + lnt +
		/**/"},mvc.Tools.crtComboBase(false,{" +
		/**/"	sotre : strstore," + lnt +
		/**/"	name : 'optcht_'" + getCode() + "," + lnt +
		/**/"	value : 1," + lnt +
		/**/"	width : 80" + lnt +
		/**/"}),{" + lnt +
		/**/"	xtype : 'textfield'," + lnt +
		/**/"	name : '" + getCode() + "'," + lnt + extFormNull(lnt,isComp) + extFormEmpty(lnt, isComp) +
		/**/"}";
		return tmp;
	}
	
	public String extEdit(String lnt) {
		String tmp = "" +
		/**/"{" + lnt +
		/**/"	xtype : 'textareafield'," + lnt +
		/**/"	value: this.record.get('bean." + getCode() + "')," + lnt +
		/**/"	fieldLabel : '" + getName() + "'" + lnt +
		/**/"}";
		return tmp;
	}
	
	public String extEditList(String lnt) {
		return "editor: {}";
	}
	/* (non-Javadoc)
	 * @see irille.pub.view.VFld#editList(irille.pub.html.ExtList)
	 */
	@Override
	public void editList(ExtList v) {
	}
	
	@Override
	protected void winSearchOptcht(ExtList l) {
		super.winSearchOptcht(l);
		l.add(VALUE, 1)
		.addExp(STORE, "strstore");
	}
	
	@Override
	public void renderer(ExtList v) {
		v.addExp(RENDERER, "mvc.Tools.imgRenderer()");
	}
	
	@Override
	public void form(ExtList v, boolean isComp) {
		super.form(v, isComp);
		addLabelWidth(v);
	}

	public void addLabelWidth(ExtList v) {
		v.addExp(LABEL_WIDTH, _labelWidth);
	}
}
