
package com.app.webservice;
import java.util.List;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UserLogoutResponse {

  
	protected String codeid;
	protected String message;
    

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public UserLogoutResponse(String result){
		Gson gs = new Gson();  
        List<UserLogoutResponse> jsonListObject = gs.fromJson(result, new TypeToken<List<UserLogoutResponse>>(){}.getType());  
        Log.v("YJ", jsonListObject.get(0).toString());
        if(jsonListObject.size() > 0){
        	this.codeid = jsonListObject.get(0).codeid;
        	this.message = jsonListObject.get(0).message;
        }
	}
    public String getCodeID() {
        return this.codeid;
    }
    public String getMessage() {
        return this.message;
    }
    
    @Override
    public String toString(){
    	return "codeid="+this.codeid+",message="+this.message;
    }
}
