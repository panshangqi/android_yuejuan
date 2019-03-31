package com.app.webservice;
import android.util.Log;
import java.io.IOException;
import java.util.Properties;
import java.util.Iterator;
import java.io.*;

public class AppCookies {
	
	protected static String token = "";
	protected static String configPath = "/data/data/com.app.yuejuan/cache/token.cfg";
	public static String getToken(){
		File file = new File(AppCookies.configPath);
		if(file.isFile() && file.exists()){
			try{
				FileInputStream fileInputStream = new FileInputStream(file);
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				StringBuffer sb = new StringBuffer();
                String text = null;
                while((text = bufferedReader.readLine()) != null){
                    sb.append(text);
                }
                return sb.toString();
                
			}catch(IOException e){
				Log.v("YJ","get token fail. ");
				e.printStackTrace();
			}
		}
		return "";
		
	}
	public static void setToken(String _token){
		File file = new File(AppCookies.configPath);
		if(!file.exists()){
			try{
				file.createNewFile();
				
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		try{
			FileWriter fw = new FileWriter(file, false);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(_token);
			bw.flush();
			fw.flush();
			bw.close();
			fw.close();
		}
		catch(Exception e){
			Log.v("YJ","set token fail. ");
			e.printStackTrace();
		}
	}
	public static void clearToken(){
		File file = new File(AppCookies.configPath);
		if(!file.exists()){
			try{
				file.createNewFile();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		try{
			FileWriter fw = new FileWriter(file, false);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("");
			bw.flush();
			fw.flush();
			bw.close();
			fw.close();
		}
		catch(Exception e){
			Log.v("YJ","set token fail. ");
			e.printStackTrace();
		}
	}
	
}
