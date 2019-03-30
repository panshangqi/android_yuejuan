package com.app.webservice;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Iterator;
import java.io.*;

public class AppCookies {
	
	protected static String token = "";
	protected static String configPath = "/data/data/com.app.yuejuan/cache/config.properties";
	public static String getToken(){
		
		Properties props = new Properties();
		
		try{
			InputStream in = new BufferedInputStream (new FileInputStream(AppCookies.configPath));
			props.load(in);     ///加载属性列表
			Iterator<String> it=props.stringPropertyNames().iterator();
			String s_token = "";
			while(it.hasNext()){
               String key=it.next();
               //System.out.println(key+":"+prop.getProperty(key));
               //Log.v("YJ","key = " + key);
               if("token".equals(key)){
            	   s_token = props.getProperty("token","");
            	   Log.v("YJ","token = " + s_token);
            	   break;
               }
            }
            in.close();
			return s_token;
			
		}catch(IOException e){
			Log.v("YJ","get token fail. ");
			e.printStackTrace();
		}
		return "";
		
	}
	public static void setToken(String _token){
		
		Properties props = new Properties();
		File file = new File(AppCookies.configPath);
		if(!file.exists()){
			try{
				file.createNewFile();
            } catch (IOException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
            }
		}
		try{
			FileOutputStream oFile = new FileOutputStream(AppCookies.configPath, true);//true表示追加打开
			props.setProperty("token", _token);
			props.store(oFile, "Comment");
			oFile.close();
			Log.v("YJ","token write success.");
        } catch (IOException e) {
           // TODO Auto-generated catch block
        	Log.v("YJ","token write fail.");
           e.printStackTrace();
        }

	}
	
}
