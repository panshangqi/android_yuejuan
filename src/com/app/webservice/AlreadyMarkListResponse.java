
package com.app.webservice;
import java.util.List;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AlreadyMarkListResponse {

  
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
	public AlreadyMarkListResponse(String result){
		Gson gs = new Gson();  
        List<AlreadyMarkListResponse> jsonListObject = gs.fromJson(result, new TypeToken<List<AlreadyMarkListResponse>>(){}.getType());  
        Log.v("YJ", jsonListObject.get(0).toString());
        if(jsonListObject.size() > 0){
        	this.codeid = jsonListObject.get(0).codeid;
        	this.message = jsonListObject.get(0).message;
        	this.authtoken = jsonListObject.get(0).authtoken;
        }
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
