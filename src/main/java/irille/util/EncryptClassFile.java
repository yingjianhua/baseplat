package irille.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class EncryptClassFile {
	private static final String origFile = "F:/eclipse-workspace/testJava/bin/testJava/HelloWorld.class";
	private static final String destFile = "F:/eclipse-workspace/testJava/bin2/testJava/HelloWorld.class";
	private static final String ss = "F:/eclipse-workspace/irilleBasePlat/WebContent/WEB-INF/classes/irille/pub/svr";
	private static final String skey = "3";
	
	public static void main(String[] args) throws FileNotFoundException, IOException, URISyntaxException {
		URL url = EncryptClassFile.class.getResource("/");
		System.out.println(url.getPath());
		//encryptClassFileInDir(new File(url.getPath().substring(1)), skey);
		encryptClassFileInDir(new File(ss), skey);
	}
	
	private static void encryptClassFileInDir(File origDir, String skey) {
		if(origDir.isDirectory()) {
			for(File file: origDir.listFiles()) 
				encryptClassFileInDir(file, skey);
		} else {
			String origFile = origDir.getPath();
			if(origFile.indexOf(".class") == -1)
				return;
			//System.out.println(origFile);
			try {
				encryptClassFile(origFile, origFile.substring(0, origFile.lastIndexOf(".class")).concat(".irille"), skey);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private static void decryptClassFileInDir(File origDir, String skey) {
		if(origDir.isDirectory()) {
			for(File file: origDir.listFiles()) 
				decryptClassFileInDir(file, skey);
		} else {
			String origFile = origDir.getPath();
			if(origFile.indexOf(".irille") == -1)
				return;
			//System.out.println(origFile);
			try {
				decryptClassFile(origFile, origFile.substring(0, origFile.lastIndexOf(".irille")).concat(".class"), skey);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private static void encryptClassFile(String origFile, String destFile, String skey) throws FileNotFoundException, IOException {
		try(FileInputStream in = new FileInputStream(origFile);
				FileOutputStream out = new FileOutputStream(destFile)) {
			int key = Integer.parseInt(skey);
			int ch;
			while((ch = in.read())!=-1) {
				byte c= (byte)(ch - key);
				out.write(c);
			}
		}
	}
	private static void decryptClassFile(String origFile, String destFile, String skey) throws FileNotFoundException, IOException {
		try(FileInputStream in = new FileInputStream(origFile);
				FileOutputStream out = new FileOutputStream(destFile)) {
			int key = Integer.parseInt(skey);
			int ch;
			while((ch = in.read())!=-1) {
				byte c= (byte)(ch + key);
				out.write(c);
			}
		}
	}
}
