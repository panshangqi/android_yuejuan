
package com.app.webservice;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

import com.app.webservice.GetExamTaskInfoResponse.Datas;
import com.app.yuejuan.Public;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class GetTestPaperSignResponse {

  
	protected String codeid;
	protected String message;
    
	public static class Datas{
		public String signid;
		public String signname;
	}
	public List<Datas> dataList;
    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public GetTestPaperSignResponse(String result){
		try {
			dataList = new ArrayList();
			JsonParser parser=new JsonParser();  //创建JSON解析器
			JsonObject object=(JsonObject) parser.parse(result);  //创建JsonObject对象
			this.codeid = object.get("codeid").getAsString();
			if(Public.responseIDOK.equals(this.codeid)){
				JsonArray jsonArr = object.get("message").getAsJsonArray();
				for(int i=0;i<jsonArr.size();i++){
					Datas data = new Datas();
	                JsonObject subObject=jsonArr.get(i).getAsJsonObject();
	                data.signid = subObject.get("signid").getAsString();
	                data.signname = subObject.get("signname").getAsString();
	                dataList.add(data);
	            }
				this.message = "";
			}else{
				this.message = object.get("message").getAsString();
			}
			
		} catch (JsonIOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
