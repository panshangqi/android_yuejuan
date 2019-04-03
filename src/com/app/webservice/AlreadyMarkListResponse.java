
package com.app.webservice;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

import com.app.yuejuan.Public;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
/*
 * int secretid            密号
String firstmark        初评分数
String secondmark       复评分数
String flagmode         试卷类型
String signname         试卷标记
String submittime       提交时间
String quename          题目名称
 */
public class AlreadyMarkListResponse {

  
	protected String codeid;
	protected String message;
	public static class Datas{
		public String firstmark;
		public String secondmark;
		public String flagmode;
		public String signname;
		public String submittime;
		public String quename;
		public int secretid;
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
	public AlreadyMarkListResponse(String result){
		dataList = new ArrayList();
		try {
			JsonParser parser=new JsonParser();  //创建JSON解析器
			JsonObject object=(JsonObject) parser.parse(result);  //创建JsonObject对象
			this.codeid = object.get("codeid").getAsString();
			if(Public.responseIDOK.equals(this.codeid)){
				JsonArray jsonArr = object.get("message").getAsJsonArray();
				for(int i=0;i<jsonArr.size();i++){
					Datas data = new Datas();
	                JsonObject subObject=jsonArr.get(i).getAsJsonObject();
	                data.quename = subObject.get("quename").getAsString();
	                data.firstmark = subObject.get("firstmark").getAsString();
	                data.secondmark = subObject.get("secondmark").getAsString();
	                data.flagmode = subObject.get("flagmode").getAsString();
	                data.signname = subObject.get("signname").getAsString();
	                data.submittime = subObject.get("submittime").getAsString();
	                data.secretid = subObject.get("secretid").getAsInt();
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

    
}
