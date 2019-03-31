
package com.app.webservice;
import java.util.List;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class UserLoginResponse {

  
	protected String codeid;
	protected String message;
	protected String authtoken = "";
    

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public UserLoginResponse(String result){
		try {
			JsonParser parser=new JsonParser();  //创建JSON解析器
			JsonObject object=(JsonObject) parser.parse(result);  //创建JsonObject对象
			this.codeid = object.get("codeid").getAsString();
			this.message = object.get("message").getAsString();
			this.authtoken = object.get("authtoken").getAsString();
		} catch (JsonIOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        List<UserLoginResponse> jsonListObject = gs.fromJson(result, new TypeToken<List<UserLoginResponse>>(){}.getType());  
//        Log.v("YJ", jsonListObject.get(0).toString());
//        if(jsonListObject.size() > 0){
//        	this.codeid = jsonListObject.get(0).codeid;
//        	this.message = jsonListObject.get(0).message;
//        	this.authtoken = jsonListObject.get(0).authtoken;
//        }
	}
    public String getCodeID() {
        return this.codeid;
    }
    public String getMessage() {
        return this.message;
    }
    public String getAuthtoken() {
        return this.authtoken;
    }
    @Override
    public String toString(){
    	return "codeid="+this.codeid+",message="+this.message+",authtoken="+this.authtoken;
    }
}
