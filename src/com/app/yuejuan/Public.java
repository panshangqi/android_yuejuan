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
	public String userid = "d001";
	public String token = "aJ7HAIWLh2qg7ut7f25DOQ==";
	public String username;
	public String usernowproject;
	public String usersubjectid = "100001";
	public String usersubject;
	public String userpower;
	public static String responseID = "0001";
	
	public static Context context = null;
	public static LayoutInflater inflater = null;
	@Override
	public void onCreate(){
		super.onCreate();
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
