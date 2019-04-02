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
import android.graphics.drawable.Drawable;

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
import com.app.modules.MarkingItemInfo;
import com.app.modules.MarkingItemListAdapter;
import com.app.modules.ProgressItemInfo;
import com.app.webservice.*;

public class MarkingActivity extends MainBaseActivity {
	RadioButton button_all;
	RadioButton button_part;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d("YJ", "onCreate func");
        super.onCreate(savedInstanceState);

    }
    @Override
    protected int getLayoutId(){
    	return R.layout.activity_marking;
    }
    @Override
	protected void initView(){

    	button_all = (RadioButton) findViewById(R.id.mk_all_button);
    	button_part = (RadioButton) findViewById(R.id.mk_part_button);
    	int icon_size = 110;
    	Drawable drawable4=getResources().getDrawable(R.drawable.radio_button_icon_selector_4);
    	drawable4.setBounds(0,0,icon_size,4);
    	Drawable drawable5=getResources().getDrawable(R.drawable.radio_button_icon_selector_5);
    	drawable5.setBounds(0,0,icon_size,4);
    	button_all.setCompoundDrawables(null,null,null,drawable4);
    	button_part.setCompoundDrawables(null,null,null,drawable5);
    	button_all.setSelected(true);
        
        RadioButton radioBtn = getButtonById(1);
    	radioBtn.setSelected(true);
    	RadioButton radioHdBtn = getButtonHdById(1);
    	radioHdBtn.setSelected(true);
    	
    	this.getMarkTaskFromService();
	}

    public void getMarkTaskList(List<MarkingListResponse.Datas> itemsList){
    	
    	List<MarkingItemInfo> listInfo = new ArrayList(1);
    	
    	Public pub = (Public)this.getApplication();
        
    	
    	for(int i=0;i<itemsList.size();i++){
    		MarkingListResponse.Datas data = itemsList.get(i);
    		MarkingItemInfo item = new MarkingItemInfo();

        	item.taskTotalCount = data.grouptaskcount == 0 ? data.taskcount : data.grouptaskcount;
        	item.dealWithCount = data.teacount;
        	item.subjectName = pub.usersubject;
        	item.questionName = data.quename;
        	item.withoutCount = item.taskTotalCount - data.teacount;
    		listInfo.add(item);	
    	}
    	
    	MarkingItemListAdapter bmilAdapter = new MarkingItemListAdapter(Public.context, listInfo);

    	ListView markTaskListView = (ListView)findViewById(R.id.marking_list_view);
    	
    	markTaskListView.setAdapter(bmilAdapter);
    }
    public void getMarkTaskFromService(){
    	Public pub = (Public)this.getApplication();
       
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("arg0", pub.userid);
        properties.put("arg1", pub.token);
        properties.put("arg2", pub.usersubjectid);
        
        Log.v("YJ", pub.token); //
        WebServiceUtil.callWebService(WebServiceUtil.WEB_SERVER_URL, "GetWorkprogress", properties, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    Log.v("YJ",result);
                    
                    MarkingListResponse reponse = new MarkingListResponse(result);
                    if("0001".equals(reponse.getCodeID())){
                    	List<MarkingListResponse.Datas> itemsList = reponse.dataList;
                    	//TODO
                    	MarkingActivity.this.getMarkTaskList(itemsList);
                       
                    }else if("0002".equals(reponse.getCodeID())){
                    	Toast.makeText(MarkingActivity.this, "用户名信息验证失败", Toast.LENGTH_SHORT).show();
                    }else if("0003".equals(reponse.getCodeID())){
                    	Toast.makeText(MarkingActivity.this, "服务器数据异常", Toast.LENGTH_SHORT).show();
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
        case R.id.mk_all_button:
        	
        	button_all.setSelected(true);
        	button_part.setSelected(false);
            break;
        case R.id.mk_part_button:
        	button_all.setSelected(false);
        	button_part.setSelected(true);
        default:
            break;
        }
    }
}
