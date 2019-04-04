
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
secretid        密号
examid          考号
flag_send       当前批阅模式
imgurl          图片路径（考虑图片路径中会有中文出现，现imgurl进行了加密数据变换）
 */
public class GetExamTaskInfoResponse {

  
	protected String codeid;
	protected String message;
	public class Datas{
		public String secretid;
		public String examid;
		public String flag_send;
		public String imgurl;
	}
	public List<Datas> dataList;
	

	public GetExamTaskInfoResponse(String result){
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
	                data.secretid = subObject.get("secretid").getAsString();
	                data.examid = subObject.get("examid").getAsString();
	                data.flag_send = subObject.get("flag_send").getAsString();
	                data.imgurl = subObject.get("imgurl").getAsString();
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
