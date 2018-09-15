package irille.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class IrilleClassLoader extends ClassLoader {
	
	public static void main(String[] args) throws MalformedURLException, ClassNotFoundException {
		String pool = "irille.pub.svr.DbPool";
		new IrilleClassLoader(3).findClass(pool);
		
		Class.forName("irille.pub.svr.DbPool", true, new IrilleClassLoader(3));
		Class.forName("irille.pub.bean.Bean");
	}
	
	private int key;
	
	public IrilleClassLoader(int k) {
        // this is to make the stack depth consistent with 1.1
        key = k;
    }

	/**
     * Finds and loads the class with the specified name from the URL search
     * path. Any URLs referring to JAR files are loaded and opened as needed
     * until the class is found.
     *
     * @param name the name of the class
     * @return the resulting class
     * @exception ClassNotFoundException if the class could not be found,
     *            or if the loader is closed.
     * @exception NullPointerException if {@code name} is {@code null}.
     */
	/**
	 * 加载并解密class文件的bytes
	 * @param name 类文件的名称
	 * @return 包含了class文件bytes的数组
	 */
	private byte[] loadClassBytes(URL url) throws IOException  {
		InputStream in = url.openStream();
		byte[] bytes = new byte[in.available()];
		in.read(bytes);
		for(int i=0;i<bytes.length;i++) {
			bytes[i] = (byte)(bytes[i]+key);
		}
		return bytes;
	}
	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			String rname = name.replace('.', '/')+".irille";
			System.out.println(rname);
			URL url = this.findResource(rname);
			if(url == null) {
				return super.findClass(name);
			} else {
				System.out.println("findClass.start:"+name);
				System.out.println("url-----"+url);
				byte[] classBytes = null;
				classBytes = loadClassBytes(url);
				Class<?> cls = null;
				try {
					cls = defineClass(name, classBytes, 0, classBytes.length);
				} catch (ClassFormatError e) {
					System.err.println("classformatError:"+rname);
				}
				System.out.println("findClass.end1:");
				if(cls == null) throw new ClassNotFoundException(name);
				System.out.println("findClass.end2:");
				return cls;
			}
		} catch (IOException e) {
			throw new ClassNotFoundException(name);
		}
	}
}
