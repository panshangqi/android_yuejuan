
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
queid         ��ĿID
quename       ��Ŀ����
fullmark      ����
smallquenum   С����Ŀ
scorepoints   ���ֵ�
smallqueinfo  С����Ϣ
{
	smallqueid         С��ID
	smallquename       С������
	smallfullmark      С������
	smallscorepoints   С����ֵ�
}

�ӿ���GetUsertaskqueinfo    
��smallqueinfo Ϊ��ʱ  ��scorepoints����   
��smallqueinfo ��Ϊ��ʱ ��smallqueinfo �����ֵ��smallscorepoints �������
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
			JsonParser parser=new JsonParser();  //����JSON������
			JsonObject object=(JsonObject) parser.parse(result);  //����JsonObject����
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