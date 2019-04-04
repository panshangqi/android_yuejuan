package com.app.yuejuan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;

//import java.util.Date;
//import java.text.DateFormat;
//import java.rmi.Remote;
//import org.apache.axis.client.Call;
//import org.apache.axis.client.Service;
//import org.apache.axis.encoding.XMLType; 

//import javax.xml.namespace.QName;
//import java.lang.Integer;
//import javax.xml.rpc.ParameterMode;
import android.util.Log;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.app.modules.AlreadyMarkItemInfo;
import com.app.modules.AlreadyMarkItemListAdapter;
import com.app.utils.WebServiceUtil;
import com.app.webservice.*;

public class AlreadyMarkActivity extends MainBaseActivity {

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d("YJ", "onCreate func");
        super.onCreate(savedInstanceState);

    }
    @Override
    protected int getLayoutId(){
    	return R.layout.activity_already_mark;
    }
    @Override
	protected void initView(){
    	RadioButton radioBtn = getButtonById(1);
    	radioBtn.setSelected(true);
    	RadioButton radioHdBtn = getButtonHdById(2);
    	radioHdBtn.setSelected(true);
        this.checkMarkIsStart();
	}
    public void checkMarkIsStart(){
    	Public pub = (Public)this.getApplication();
        
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("arg0", pub.usersubjectid);

        WebServiceUtil.callWebService(WebServiceUtil.WEB_SERVER_URL, "GetSubjectstatus", properties, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    Log.v("YJ",result);
                    GetSubjectStatusResponse reponse = new GetSubjectStatusResponse(result);
                    if("0001".equals(reponse.getCodeID())){
                    	//Toast.makeText(MarkingActivity.this, "本科目阅卷进程已启动", Toast.LENGTH_SHORT).show();
                    	AlreadyMarkActivity.this.getAlreadyMarkList();
                    }else if("0002".equals(reponse.getCodeID())){
                    	Toast.makeText(AlreadyMarkActivity.this, "本科目阅卷进程未启动", Toast.LENGTH_SHORT).show();
                    }else if("0003".equals(reponse.getCodeID())){
                    	Toast.makeText(AlreadyMarkActivity.this, "服务器数据异常", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public void renderItemList(List<AlreadyMarkListResponse.Datas> itemList){

    	List<AlreadyMarkItemInfo> listInfo = new ArrayList(1);
    	
    	for(int i=0;i<itemList.size();i++){
    		AlreadyMarkListResponse.Datas data = itemList.get(i);
    		AlreadyMarkItemInfo item = new AlreadyMarkItemInfo();
    		item.que_num = data.quename;
        	item.que_score = data.firstmark;
        	item.que_time = data.submittime;
        	listInfo.add(item);
    	}
    	Log.v("YJ","renderItemList()");
    	
    	AlreadyMarkItemListAdapter bmilAdapter = new AlreadyMarkItemListAdapter(Public.context, listInfo);
    	ListView AlreadyMackListView = (ListView)findViewById(R.id.already_mark_list_view);
    	AlreadyMackListView.setAdapter(bmilAdapter);
    }
    public void getAlreadyMarkList(){
    	Public pub = (Public)this.getApplication();
    	String userid = pub.getUserID();
    	String token = pub.getToken();
    	String subjectid = pub.usersubjectid;
       
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("arg0", userid);
        properties.put("arg1", token);
        properties.put("arg2", subjectid);
        //properties.put("arg3", "0");
        //properties.put("arg4", "");
        
        WebServiceUtil.callWebService(WebServiceUtil.WEB_SERVER_URL, "GetAlreadymarklist", properties, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    Log.v("YJX",result);
                    AlreadyMarkListResponse reponse = new AlreadyMarkListResponse(result);
                    if("0001".equals(reponse.getCodeID())){
                    	AlreadyMarkActivity.this.renderItemList(reponse.dataList);
                    }else if("0002".equals(reponse.getCodeID())){
                    	//Toast.makeText(AlreadyMarkActivity.this, reponse.getMessage(), Toast.LENGTH_SHORT).show();
                    	Intent intent =new Intent(AlreadyMarkActivity.this, LoginActivity.class);
                    	startActivity(intent);
                    }else if("0002".equals(reponse.getCodeID())){
                    	Toast.makeText(AlreadyMarkActivity.this, reponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
  
    @Override
	public void widgetClick(View v){
        switch (v.getId()) {
//        case R.id.radio_mark_button:
//        	
//        	markFaceBtn.setTextColor(Color.parseColor("#FFFFFF"));
//        	markFaceBtn.setBackgroundColor(Color.parseColor("#ff9647"));
//        	markBackBtn.setTextColor(Color.parseColor("#333333"));
//        	markBackBtn.setBackgroundColor(Color.parseColor("#ffffff"));
//        	
//        	Toast.makeText(AlreadyMarkActivity.this, "btn1:", Toast.LENGTH_SHORT).show();
//
//            break;
//        case R.id.radio_mark_back_button:
//        	
//        	markFaceBtn.setTextColor(Color.parseColor("#333333"));
//        	markFaceBtn.setBackgroundColor(Color.parseColor("#ffffff"));
//        	markBackBtn.setTextColor(Color.parseColor("#ffffff"));
//        	markBackBtn.setBackgroundColor(Color.parseColor("#ff9647"));
//        	Toast.makeText(AlreadyMarkActivity.this, "btn1:", Toast.LENGTH_SHORT).show();
        	
            
        default:
            break;
        }
    }
}
