package com.app.yuejuan;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.WindowManager;
public class Public extends Application {
	public boolean isDebug = true;
	public static String userid;
	public static String token;
	public static String username;
	public static String usernowproject;
	public static String usersubjectid;
	public static String usersubject;
	public static String userpower;
	
	public static String imageHost = "49.4.48.115";///exam/appshowimage
	
	public static String responseID = "0001";
	public static String responseIDOK = "0001";
	
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

}
