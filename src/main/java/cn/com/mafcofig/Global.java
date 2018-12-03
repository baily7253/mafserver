package cn.com.mafcofig;

import java.io.FileInputStream;
import java.util.Properties;

public class Global {
	
	public  String getSysPath() {
        String ret = getClass().getResource("/").getPath();
        ret = ret.replace("\\", "/");
        ret=ret.replace("classes/", "");
        return ret;
    }

    public Properties prop = getProperties();
     
     
     
     public  Properties getProperties() {
         Properties prop = new Properties();
         try {
        	 String filePath=getSysPath()+"prop.properties";
             FileInputStream file = new FileInputStream(filePath);
             prop.load(file);
             file.close();
         } catch (Exception e) {
             e.printStackTrace();
         }
         return prop;
     }
     
     public  String getProperty(String Property) {
         return prop.getProperty(Property);
     }

}
