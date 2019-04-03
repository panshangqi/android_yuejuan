
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
queid         题目ID
quename       题目名称
fullmark      满分
smallquenum   小题数目
scorepoints   给分点
smallqueinfo  小题信息
{
	smallqueid         小题ID
	smallquename       小题名称
	smallfullmark      小题满分
	smallscorepoints   小题给分点
}

接口用GetUsertaskqueinfo    
当smallqueinfo 为空时  用scorepoints参数   
当smallqueinfo 不为空时 用smallqueinfo 里面字典的smallscorepoints 这个参数
 */
public class GetUserTaskQueInfoResponse {

  
	protected String codeid;
	protected String message;
	public static class SmallQueInfo{
		public String smallqueid;
		public String smallquename;
		public String smallfullmark;
		public String smallscorepoints;
	}
	public static class Datas{
		public String queid;
		public String quename;
		public String fullmark;
		public String scorepoints;
		public List<SmallQueInfo> smallqueinfoList;
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
	public GetUserTaskQueInfoResponse(String result){
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
	                data.queid = subObject.get("queid").getAsString();
	                data.quename = subObject.get("quename").getAsString();
	                data.fullmark = subObject.get("fullmark").getAsString();
	                data.scorepoints = subObject.get("scorepoints").getAsString();
	                
	                data.smallqueinfoList = new ArrayList();
	                JsonArray smallQueArr = subObject.get("smallqueinfo").getAsJsonArray();
	                for(int j=0;j<smallQueArr.size();j++){
	                	SmallQueInfo sqInfo = new SmallQueInfo();
	                	JsonObject sqObject=jsonArr.get(j).getAsJsonObject();
		                sqInfo.smallqueid = sqObject.get("smallqueid").getAsString();
		                sqInfo.smallquename = sqObject.get("smallquename").getAsString();
		                sqInfo.smallfullmark = sqObject.get("smallfullmark").getAsString();
		                sqInfo.smallscorepoints = sqObject.get("smallscorepoints").getAsString();
		                
		                data.smallqueinfoList.add(sqInfo);
	                }
	                
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
