package irille.pub.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

public class CompressTool {
	int linebreakpos = -1;
	boolean munge = true;
	boolean verbose = false;
	boolean preserveAllSemiColons = false;
	boolean disableOptimizations = false;

	private static String path = new File(System.getProperty("user.dir")+"/WebContent").getPath().replace("\\", "/");
	private static String srcDir = path+"";
	private static String targetDir="D:/Users/whx/Desktop/js";
	private static List<File> files = new ArrayList<File>();
	private static List<File> error = new ArrayList<File>();
	private static String[] extensions = { "js", "css" };

	public static void main(String[] args) {
	  new CompressTool();
  }
	public CompressTool() {
		run();
	}
	public void run() {
		initFiles();
		try {
	    compress();
    } catch (Exception e) {
	    e.printStackTrace();
    }
		result();
	}
	public void result() {
		System.out.println("共"+files.size()+"个文件");
		System.out.println("实际压缩"+(files.size()-error.size())+"个");
		if(error.size()>0) {
			System.out.println("其中有"+error.size()+"个文件有异常");
			System.out.println("-----------------------错误文件列表-----------------------");
			int i=1;
			for(File file:error) {
				System.out.println(i+":"+file.getPath());
				i++;
			}

		}
	}
	private void compress() throws Exception {
		Reader in = null;
		Writer out = null;
		for (File file : files) {
			String fileName = file.getPath();
			File tf = new File(fileName.replace("\\", "/").replaceAll(srcDir, targetDir));
			if(tf.exists()) continue;
			try {
	      in = new FileReader(file);
	      out = new FileWriter(tf);
	      if (fileName.endsWith(".js")) {
	      	JavaScriptCompressor jscompressor = new JavaScriptCompressor(in, new ErrorReporter() {

	      		public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
	      			if (line < 0) {
	      				System.err.println("\n[WARNING] " + message);
	      			} else {
	      				System.err.println("\n[WARNING] " + line + ':' + lineOffset + ':' + message+"|"+sourceName+"|"+lineSource);
	      			}
	      		}

	      		public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
	      			if (line < 0) {
	      				System.err.println("\n[ERROR] " + message);
	      			} else {
	      				System.err.println("\n[ERROR] " + line + ':' + lineOffset + ':' + message+"|"+sourceName+"|"+lineSource);
	      			}
	      		}

	      		public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource,
	      		    int lineOffset) {
	      			error(message, sourceName, line, lineSource, lineOffset);
	      			return new EvaluatorException(message);
	      		}
	      	});

	      	jscompressor.compress(out, linebreakpos, munge, verbose, preserveAllSemiColons, disableOptimizations);
	      } else if (fileName.endsWith(".css")) {
	      	CssCompressor csscompressor = new CssCompressor(in);
	      	csscompressor.compress(out, linebreakpos);
	      }
	     
      } catch (EvaluatorException e) {
      	byte[] buf = new byte[2048];
      	in.close();
      	out.close();
      	FileInputStream fis=null;
        FileOutputStream fos=null;
        try {
          fis = new FileInputStream(file);
          fos = new FileOutputStream(tf);
	        fis.read(buf);
	        fos.write(buf);
        } finally {
        	if(fis!=null) fis.close();
        	if(fos!=null) fos.close();	
        }
      	System.err.println(e.getMessage());
	      error.add(file);
      } finally {
      	 if(in!=null)in.close();
 	    	 if(out!=null)out.close();
      }
		}
	}

	public void initFiles() {
		File rootDir = new File(targetDir);
		if (rootDir.exists()) {
			System.out.println("目标目录已存在：正在删除");
			System.out.println(rootDir.delete());
		}
		rootDir.mkdirs();
		getSubFile(srcDir);
	}

	public void getSubFile(String pathname) {
		File parent = new File(pathname);
		for (File file : parent.listFiles()) {
			if (file.isDirectory()) {
				new File(file.getPath().replace("\\", "/").replaceAll(srcDir, targetDir)).mkdir();
				getSubFile(file.getPath());
			} else {
				for (String extension : extensions) {
					if (file.getName().endsWith("." + extension)) {
						files.add(file);
						break;
					}
				}
			}

		}
	}
}
