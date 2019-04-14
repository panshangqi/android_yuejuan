package com.app.yuejuan;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
public class Public extends Application {
	public boolean isDebug = false;
	public static String userid;
	public static String token;
	public static String username;
	public static String usernowproject;
	public static String usersubjectid;
	public static String usersubject;
	public static String userpower;
	
	public static String imageHost = "";// "49.4.48.115";///exam/appshowimage
	
	public static String responseID = "0001";
	public static String responseIDOK = "0001";
	public String cachePath;
	public static int isMarkingActivity = 1; //默认显示正评列表
	public static Context context = null;
	public static LayoutInflater inflater = null;
	
	@Override
	public void onCreate(){
		super.onCreate();
		if(isDebug == true){
			Public.userid = "1001";
			Public.token = "Hz4ybVyXi2aBJIUrQjLnrg==";
			Public.username = "1001";
			Public.usernowproject = "实验";
			Public.usersubjectid = "100001";
			Public.usersubject = "高一地理综合";
			Public.userpower = "评卷老师";
		}
		context = getApplicationContext();
		inflater = LayoutInflater.from(context);
		cachePath = getCacheDir().getAbsolutePath();
		Log.v("YJ cache", cachePath);
	}
	public static String imageUrl(String imgPath){
		return "http://" + Public.imageHost + "/exam/appshowimage?path=" + imgPath;
	}
	public void setUserID(String _userid){
		this.userid = _userid;
	}
	
	public void setToken(String _token){
		this.token = _token;
	}
	
	public String getUserID(){
		return this.userid;
	}
	public String getToken(){
		return this.token;
	}
	public static boolean checkJson(String jsonStr){
 	   
        JsonElement jsonElement;
        try {
            jsonElement = new JsonParser().parse(jsonStr);
        } catch (Exception e) {
            return false;
        }
        if (jsonElement == null) {
            return false;
        }
        if (!jsonElement.isJsonObject()) {
            return false;
        }
        return true;
    }
	public String mergePath(String prePath, String nextPath){
		if(prePath.length() > 0){
			String ch = prePath.substring(prePath.length()-1, prePath.length());
			if("/".equals(ch) || "\\".equals(ch)){
				return prePath + nextPath;
			}
		}
		return prePath + "/" + nextPath;
	}
	public void writeFile(String filename, String content){
		BufferedWriter out = null;   
        try {   
        	
        	String filepath = mergePath(this.cachePath, filename);
        	File file = new File(filepath);
        	if(!file.exists()){
        		file.createNewFile();
        	}
        	
        	Log.v("YJ filename", filepath);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false)));   
            out.write(username);   
            out.flush();
            out.close();   
        } catch (Exception e) {   
            e.printStackTrace();   
        } 
	}
	public String readFile(String filename){
		try{
			String filepath = mergePath(this.cachePath, filename); 
	        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"));
	        String line = "";
	        while ((line = br.readLine()) != null) {
	            // 如果 t x t文件里的路径 不包含---字符串       这里是对里面的内容进行一个筛选
	        	break;
	        }
	        br.close();
	        return line;
		}catch (Exception e) {   
            e.printStackTrace();
            return "";
        }
	}
	public void setUserName(String username){
		writeFile("username.txt", username);
		
	}
	public void setHostIP(String ip){
		writeFile("ip.txt", username);
	}
	public String getUserName(){
		return readFile("username.txt");
	}
	public String getHostIP(){
		return readFile("ip.txt");
	}
}
