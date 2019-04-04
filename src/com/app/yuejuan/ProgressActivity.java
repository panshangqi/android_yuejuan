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
import com.app.modules.ProgressItemInfo;
import com.app.modules.ProgressItemListAdapter;
import com.app.utils.WebServiceUtil;
import com.app.webservice.*;

public class ProgressActivity extends MainBaseActivity {

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d("YJ", "onCreate func");
        super.onCreate(savedInstanceState);

    }
    @Override
    protected int getLayoutId(){
    	return R.layout.activity_progress;
    }
    @Override
	protected void initView(){


        
        RadioButton radioBtn = getButtonById(2);
    	radioBtn.setSelected(true);
    	
    	
    	this.getTaskProgressFromService();
    	
	}

    public void getTaskProgressList(List<TaskProgressListResponse.Datas> itemsList){
    	

    	List<ProgressItemInfo> listInfo = new ArrayList();
    	Public pub = (Public)this.getApplication();
        
    	
    	for(int i=0;i<itemsList.size();i++){
    		TaskProgressListResponse.Datas data = itemsList.get(i);
    		ProgressItemInfo item = new ProgressItemInfo();
        	item.taskTotalCount = data.grouptaskcount == 0 ? data.taskcount : data.grouptaskcount;
        	item.dealWithCount = data.teacount;
        	item.subjectName = pub.usersubject;
        	item.questionName = data.quename;
        	item.reat = data.reat;
    		listInfo.add(item);	
    	}

    	Log.v("YJ","getBackMarkList()");
    	
    	ProgressItemListAdapter bmilAdapter = new ProgressItemListAdapter(Public.context, listInfo);

    	ListView AlreadyMackListView = (ListView)findViewById(R.id.progress_mark_list_view);
    	
    	AlreadyMackListView.setAdapter(bmilAdapter);
    }
    public void getTaskProgressFromService(){
    	Public pub = (Public)this.getApplication();
       
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("arg0", pub.userid);
        properties.put("arg1", pub.token);
        properties.put("arg2", pub.usersubjectid);
        

        WebServiceUtil.callWebService(WebServiceUtil.WEB_SERVER_URL, "GetWorkprogress", properties, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    Log.v("YJ",result);
                    
                    TaskProgressListResponse reponse = new TaskProgressListResponse(result);
                    if("0001".equals(reponse.getCodeID())){
                    	List<TaskProgressListResponse.Datas> itemsList = reponse.dataList;
                    	ProgressActivity.this.getTaskProgressList(itemsList);
                       
                    }else if("0002".equals(reponse.getCodeID())){
                    	Toast.makeText(ProgressActivity.this, "用户名信息验证失败", Toast.LENGTH_SHORT).show();
                    }else if("0003".equals(reponse.getCodeID())){
                    	Toast.makeText(ProgressActivity.this, "服务器数据异常", Toast.LENGTH_SHORT).show();
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
        
        default:
            break;
        }
    }
}
