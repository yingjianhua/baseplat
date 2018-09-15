package irille.pub.tools;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ReadTxtUtil {
	public  String[] writeToDat(String path){
		File file = new File(path);
		List list = new ArrayList();
		String []str = null;
		try {
			BufferedReader bw = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
			String line = null;
			while ((line = bw.readLine()) != null) {
				list.add(line);
			}
			bw.close();
			str = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				String s = (String)list.get(i);
				str[i] = s;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	* 读取指定文件夹下的文件名或全路径
	* @Title: getFile  
	* @Description: TODO 
	* @param @param path     
	* @return void     
	* @throws  
	 */
	private static void getFile(String path){    
        File file = new File(path);    
        File[] array = file.listFiles();      
        for(int i=0;i<array.length;i++){    
            if(array[i].isFile()){    
                // 获取文件名称    
                System.out.println("获取文件名称  " + array[i].getName());    
                // 获取完成的文件路径    
                System.out.println("获取文件名称" + array[i]);    
                // 获取完成的文件路径  
                System.out.println("获取完成的文件路径" + array[i].getPath());    
            }else if(array[i].isDirectory()){    
                getFile(array[i].getPath());    
            }    
        }    
    }
	
	/** 
	 * * 
	 * 读取数据 
	 * */
	public static void ReadDate() {
		String url = "e:/B01002CFR20130925.txt";
		try {
			FileReader read = new FileReader(new File(url));
			StringBuffer sb = new StringBuffer();
			char ch[] = new char[1024];
			int d = read.read(ch);
			while (d != -1) {
				String str = new String(ch, 0, d);
				System.out.println(str);
				sb.append(str);
				d = read.read(ch);
			}
//			System.out.print(sb.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		String path = "D:/uploads/XDYSDR/20131120/A06030/xd_py_pyhxg/201311181228/A02006CON20131118.txt";
//		String path="C:/Users/pc/Desktop/201311181228/A02006CUS20131118.txt";
		String pathFiles = "e:/201309251103";
		String[] str = new ReadTxtUtil().writeToDat(path);
//		System.out.println("str.length == " +str.length);
		for (int i = 0; i < str.length; i++) { 
//			System.out.println(str[i]);
//			String[] spiltStr = str[i].split("\\|"); //竖线分割
//			for (int k = 0; k < spiltStr.length; k++) {
//				System.out.println(spiltStr[k]);
//			}
		}
//		getFile(pathFiles);
	}
}
