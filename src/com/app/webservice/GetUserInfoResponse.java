
package com.app.webservice;
import java.io.FileNotFoundException;
import java.util.List;

import android.util.Log;

import com.app.yuejuan.Public;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
class UserInfoMessage{
	
}
public class GetUserInfoResponse {

	public String codeid;
	public String message;
	
	public String userid;
	public String username;
	public String usernowproject;
	public String usersubjectid;
	public String usersubject;
	public String userpower;
    

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	
	public GetUserInfoResponse(String result){
		
		try {
			JsonParser parser=new JsonParser();  //创建JSON解析器
			JsonObject object=(JsonObject) parser.parse(result);  //创建JsonObject对象
			this.codeid = object.get("codeid").getAsString();
			JsonArray jsonArr = object.get("message").getAsJsonArray();
			if(jsonArr.size()>0){
                
                JsonObject subObject=jsonArr.get(0).getAsJsonObject();
                this.userid = subObject.get("userid").getAsString();
                this.username = subObject.get("username").getAsString();
                this.usernowproject = subObject.get("usernowproject").getAsString();
                this.usersubjectid = subObject.get("usersubjectid").getAsString();
                this.usersubject = subObject.get("usersubject").getAsString();
            	this.userpower = subObject.get("userpower").getAsString();
            	Log.v("YJ", "userid: " + this.userid);
            	Log.v("YJ", "username: " + this.username);
            	Log.v("YJ", "usernowproject: " + this.usernowproject);
            	Log.v("YJ", "usersubjectid: " + this.usersubjectid);
            	Log.v("YJ", "usersubject: " + this.usersubject);
            	Log.v("YJ", "userpower: " + this.userpower);
            }
		} catch (JsonIOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}
	public String getCodeID(){
		return this.codeid;
	}
    @Override
    public String toString(){
    	return "codeid="+this.codeid;
    }
}
