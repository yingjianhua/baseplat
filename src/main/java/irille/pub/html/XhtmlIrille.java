package irille.pub.html;


public class XhtmlIrille<T extends XhtmlIrille> extends Xhtml<T>{
	public static final int WIDTH=IrillePublicCSS.WIDTH;
	public static final IrillePublicCSS PUBLIC_CSS=IrillePublicCSS.PUBLIC_CSS;
	
	public XhtmlIrille(String name) {
	  super(name);
  }
	public XhtmlIrille(String code,String name) {
	  super(code,name);
  }
}
