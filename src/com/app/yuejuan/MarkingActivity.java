package com.app.yuejuan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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
import com.app.utils.ViewTranslateAnimate;
import com.app.utils.WebServiceUtil;
import com.app.webservice.*;

public class MarkingActivity extends MainBaseActivity {
	RadioButton button_all;
	RadioButton button_part;
	List<View> viewPagerList;
	RadioGroup radioTabGroup;
	MarkingItemListAdapter.ItemClickInterfaceListener itemClickListener;
	int page_index = 0;
	ViewPager viewPager;
	LinearLayout viewPagerBox;
	Animation animationInLeft;
	Animation animationOutRight;
	ViewTranslateAnimate animateUtil;
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
    	radioTabGroup = (RadioGroup)findViewById(R.id.radioTabGroup);
    	button_all = (RadioButton) findViewById(R.id.mk_all_button);
    	button_part = (RadioButton) findViewById(R.id.mk_part_button);
    	int icon_size = 110;
    	Drawable drawable4=getResources().getDrawable(R.drawable.radio_button_icon_selector_4);
    	drawable4.setBounds(0,0,icon_size,4);
    	Drawable drawable5=getResources().getDrawable(R.drawable.radio_button_icon_selector_5);
    	drawable5.setBounds(0,0,icon_size,4);
    	button_all.setCompoundDrawables(null,null,null,drawable4);
    	button_part.setCompoundDrawables(null,null,null,drawable5);
    	setTabRadioButtonSelected(0);
        
        RadioButton radioBtn = getButtonById(1);
    	radioBtn.setSelected(true);
    	RadioButton radioHdBtn = getButtonHdById(1);
    	radioHdBtn.setSelected(true);
    	
    	//viewPager = (ViewPager)findViewById(R.id.viewpager);
    	viewPagerBox = (LinearLayout)findViewById(R.id.viewpager);
    	viewPagerList = new ArrayList();
    	View view1 = Public.inflater.inflate(R.layout.layout_mark_all, null);
    	View view2 = Public.inflater.inflate(R.layout.layout_mark_part, null);
    	viewPagerList.add(view1);
    	viewPagerList.add(view2);
    	animateUtil = new ViewTranslateAnimate(viewPagerBox, viewPagerList, 0);
    	animateUtil.setDruation(300);

    	this.checkMarkIsStart();
	}
    public void checkMarkIsStart(){
    	Public pub = (Public)this.getApplication();
        
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("arg0", pub.usersubjectid);

        WebServiceUtil.callWebService(WebServiceUtil.getURL(), "GetSubjectstatus", properties, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    Log.v("YJ",result);
                    
                    GetSubjectStatusResponse reponse = new GetSubjectStatusResponse(result);
                    if("0001".equals(reponse.getCodeID())){
                    	//Toast.makeText(MarkingActivity.this, "本科目阅卷进程已启动", Toast.LENGTH_SHORT).show();
                    	MarkingActivity.this.getMarkTaskFromService();
                    	
                    }else if("0002".equals(reponse.getCodeID())){
                    	Toast.makeText(MarkingActivity.this, "本科目阅卷进程未启动", Toast.LENGTH_SHORT).show();
                    }else if("0003".equals(reponse.getCodeID())){
                    	Toast.makeText(MarkingActivity.this, "服务器数据异常", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
        	item.queid = data.queid;
    		listInfo.add(item);	
    	}

    	itemClickListener = new MarkingItemListAdapter.ItemClickInterfaceListener(){
    		
			@Override
			public void Callback(final MarkingItemInfo itemInfo) {
				// TODO Auto-generated method stub
				Log.v("YJ","Click");
				Intent intent =new Intent(MarkingActivity.this, CorrectScoreEditActivity.class);
				intent.putExtra("queid", itemInfo.queid);
				intent.putExtra("quename", itemInfo.questionName);
				intent.putExtra("type", "0"); //正平
            	startActivity(intent);
			}
		};
		
    	
    	MarkingItemListAdapter bmilAdapter = new MarkingItemListAdapter(Public.context, listInfo, itemClickListener);
    	ListView markTaskListView = (ListView)animateUtil.getViewByID(R.id.marking_list_view);
    	markTaskListView.setAdapter(bmilAdapter);
    }
    public void getMarkTaskFromService(){
    	Public pub = (Public)this.getApplication();
       
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("arg0", pub.userid);
        properties.put("arg1", pub.token);
        properties.put("arg2", pub.usersubjectid);
        
        Log.v("YJ", pub.token); //
        WebServiceUtil.callWebService(WebServiceUtil.getURL(), "GetWorkprogress", properties, new WebServiceUtil.WebServiceCallBack() {
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
                    	Intent intent =new Intent(MarkingActivity.this, LoginActivity.class);
                    	startActivity(intent);
                    	//Toast.makeText(MarkingActivity.this, "用户名信息验证失败", Toast.LENGTH_SHORT).show();
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
        	
    		setTabRadioButtonSelected(0);
        	animateUtil.setCurrentItem(0);
        	this.checkMarkIsStart();
            break;
        case R.id.mk_part_button:
        	setTabRadioButtonSelected(1);
        	animateUtil.setCurrentItem(1);
        	this.checkMarkIsStart();
        default:
            break;
        }
    }
    public void setTabRadioButtonSelected(int _id){
    	int count = radioTabGroup.getChildCount();
   
    	for(int i=0;i<count;i++){
    		RadioButton rb = (RadioButton)radioTabGroup.getChildAt(i);
    
    		if(i == _id){
        		rb.setSelected(true);
    		}else{
    			rb.setSelected(false);
    		}
    	}

    }
}
