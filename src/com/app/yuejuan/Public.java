package com.app.yuejuan;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.WindowManager;
public class Public extends Application {
	public boolean isDebug = true;
	public String userid;
	public String token;
	public String username;
	public String usernowproject;
	public String usersubjectid;
	public String usersubject;
	public String userpower;
	
	public static String responseID = "0001";
	public static int isMarkingActivity = 1; //默认显示正评列表
	public static Context context = null;
	public static LayoutInflater inflater = null;
	@Override
	public void onCreate(){
		super.onCreate();
		if(isDebug == true){
			userid = "d001";
			token = "mObAPFOnsFnGKuud747m4w==";
			username = "d001";
			usernowproject = "实验";
			usersubjectid = "100001";
			usersubject = "高一地理综合";
			userpower = "评卷老师";
		}
		context = getApplicationContext();
		inflater = LayoutInflater.from(context);
	
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
