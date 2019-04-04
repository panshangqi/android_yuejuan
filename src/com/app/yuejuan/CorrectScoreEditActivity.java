package com.app.yuejuan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.view.View;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

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

import com.app.modules.ProgressItemInfo;
import com.app.modules.ProgressItemListAdapter;
import com.app.modules.QuestionTaskItemInfo;
import com.app.modules.QuestionTaskItemListAdapter;
import com.app.utils.WebServiceUtil;
import com.app.webservice.*;

public class CorrectScoreEditActivity extends Activity {

	LinearLayout score_panel_back_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d("YJ", "onCreate func");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct_score_edit);
        score_panel_back_button = (LinearLayout)this.findViewById(R.id.score_panel_back_button);
        this.initView();
    }
    public void initView(){
    	this.getQueTaskListFromService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void renderQuetionList(List<GetUserTaskQueInfoResponse.Datas> itemsList){
    	List<QuestionTaskItemInfo> listInfo = new ArrayList();
    	Public pub = (Public)this.getApplication();

    	for(int i=0;i<itemsList.size();i++){
    		GetUserTaskQueInfoResponse.Datas data = itemsList.get(i);
    		QuestionTaskItemInfo item = new QuestionTaskItemInfo();
        	item.queid = data.queid;
        	Log.v("YJ",item.queid);
    		listInfo.add(item);	
    	}

    	Log.v("YJ","getBackMarkList()");
    	
    	QuestionTaskItemListAdapter qtAdapter = new QuestionTaskItemListAdapter(Public.context, listInfo);

    	ListView QueTaskListView = (ListView)findViewById(R.id.question_task_list_view);
    	
    	QueTaskListView.setAdapter(qtAdapter);
    }
    public void getQueTaskListFromService(){
    	Public pub = (Public)this.getApplication();
       
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("arg0", pub.userid);
        properties.put("arg1", pub.token);
        properties.put("arg2", pub.usersubjectid);
        

        WebServiceUtil.callWebService(WebServiceUtil.WEB_SERVER_URL, "GetUsertaskqueinfo", properties, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    Log.v("YJ",result);
                    
                    GetUserTaskQueInfoResponse reponse = new GetUserTaskQueInfoResponse(result);
                    if("0001".equals(reponse.getCodeID())){
                    	List<GetUserTaskQueInfoResponse.Datas> itemsList = reponse.dataList;
                    	CorrectScoreEditActivity.this.renderQuetionList(itemsList);
                       
                    }else if("0002".equals(reponse.getCodeID())){
                    	Intent intent =new Intent(CorrectScoreEditActivity.this, LoginActivity.class);
                    	startActivity(intent);
                    }else if("0003".equals(reponse.getCodeID())){
                    	Toast.makeText(CorrectScoreEditActivity.this, "服务器数据异常", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.score_panel_back_button:
        	
        	Intent intent;
        	if(Public.isMarkingActivity == 1){
        		intent =new Intent(CorrectScoreEditActivity.this, MarkingActivity.class);
            	startActivity(intent);
            	//overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        	}else{
        		intent =new Intent(CorrectScoreEditActivity.this, AlreadyMarkActivity.class);
				startActivity(intent);
				//overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        	}
            break;
//        case R.id.loginforget:
//        	
//        	Toast.makeText(CorrectScoreEditActivity.this, "btn1:", Toast.LENGTH_SHORT).show();
//        	Intent intent =new Intent(CorrectScoreEditActivity.this, ForgetPasswordActivity.class);
//        	startActivity(intent);
            
        default:
            break;
        }
    }
}
