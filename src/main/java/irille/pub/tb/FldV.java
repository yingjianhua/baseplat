package irille.pub.tb;

import irille.pub.Log;

public abstract class FldV<T extends FldV> extends Fld<T> {
	private static final Log LOG = new Log(FldV.class);
	private short _width=30;

	public FldV(String code, String name) {
	  super( code, name);
  }

	@Override
	public int getSqlType() {
		throw LOG.err();
	}
	@Override
	protected T copy(Fld newObj) {
	  return (T) super.copy(newObj).setWidth(_width);
	}
	@Override
  public short getWidth() {
	  return _width;
  }
	public T setWidth( int width) {
		_width=(short)width;
		return (T)this;
	}
	
	@Override
	public String getTypeName() {
		throw LOG.err();
	}

	@Override
	public KIND getKind() {
	  return KIND.UNION;
	}
	
	@Override
	public String outSrcVarDefine() {
	  return "";
	}

}
