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
	Animation animationL2R;
	Animation animationR2L;
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
    	animationL2R = AnimationUtils.loadAnimation(Public.context, R.anim.view_translate_left_to_right);
    	animationR2L = AnimationUtils.loadAnimation(Public.context, R.anim.view_translate_right_to_left);
    	animationL2R.setDuration(500);
    	animationR2L.setDuration(500);
//    	PagerAdapter pagerAdapter = new PagerAdapter() {  
//
//            @Override  
//            public boolean isViewFromObject(View arg0, Object arg1) {  
//  
//                return arg0 == arg1;  
//            }  
//  
//            @Override  
//            public int getCount() {  
//  
//                return viewPagerList.size();  
//            }  
//  
//            @Override  
//            public void destroyItem(ViewGroup container, int position, Object object) {  
//                container.removeView(viewPagerList.get(position));  
//            }  
//  
//            @Override  
//            public int getItemPosition(Object object) {  
//  
//                return super.getItemPosition(object);  
//            }  
//  
//            @Override  
//            public CharSequence getPageTitle(int position) {  
//  
//                return "";//titleList.get(position);  
//            }  
//  
//            @Override  
//            public Object instantiateItem(ViewGroup container, int position) {  
//                container.addView(viewPagerList.get(position));
//                
//                return viewPagerList.get(position);  
//            }  
//
//
//        };  
//        viewPager.setAdapter(pagerAdapter); 
// 
//        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
//            //此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
//            //arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
//            //当页面开始滑动的时候，三种状态的变化顺序为（1，2，0）
//            public void onPageScrollStateChanged(int arg0) {
//            
//            }
//            //此方法里有3个参数</span></span>
//            //当你滑动时一直调用这个方法直到停止滑到
//            //arg0：表示现在的页面； arg1：表示当前页面偏移百分比； arg2：表示当前页面偏移的像素；
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//            
//            }
//            //此方法里的 arg0 是表示显示的第几页，当滑到第N页，就会调用此方法，arg0=N；
//            public void onPageSelected(int arg0) {
//            	Log.v("YJ","-------------"+arg0);
//	       		switch (arg0) {
//	       		
//	       		case 0:
//	       			MarkingActivity.this.page_index = 0;
//	       			MarkingActivity.this.setTabRadioButtonSelected(0);
//	       		    break;
//	       		case 1:
//	       			MarkingActivity.this.page_index = 1;
//	       			MarkingActivity.this.setTabRadioButtonSelected(1);
//	       			break;
//	       		
//	       		default:
//	       			break;
//	       		}
//	       		MarkingActivity.this.checkMarkIsStart();
//	       	}
//       });
       //init
        //viewPager.setCurrentItem(0);
    	//this.checkMarkIsStart();
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
    		listInfo.add(item);	
    	}

    	itemClickListener = new MarkingItemListAdapter.ItemClickInterfaceListener(){
    		
			@Override
			public void Callback(final MarkingItemInfo itemInfo) {
				// TODO Auto-generated method stub
				Log.v("YJ","Click");
				Intent intent =new Intent(MarkingActivity.this, CorrectScoreEditActivity.class);
            	startActivity(intent);
			}
		};
		
    	
    	MarkingItemListAdapter bmilAdapter = new MarkingItemListAdapter(Public.context, listInfo, itemClickListener);
    	ListView markTaskListView = (ListView)viewPagerList.get(MarkingActivity.this.page_index).findViewById(R.id.marking_list_view);
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
        	
        	//setTabRadioButtonSelected(0);
        	page_index = 0;
        	//viewPager.setCurrentItem(page_index);
        	
        	viewPagerList.get(0).startAnimation(animationR2L);
        	if(viewPagerBox!=null){
        		viewPagerBox.removeView(viewPagerList.get(1));
        		viewPagerBox.addView(viewPagerList.get(0));
        	}
        	
        	this.checkMarkIsStart();
            break;
        case R.id.mk_part_button:
        	//setTabRadioButtonSelected(1);
        	page_index = 1;
        	//viewPager.setCurrentItem(page_index);
        	viewPagerList.get(1).startAnimation(animationL2R);
        	if(viewPagerBox!=null){
        		viewPagerBox.removeView(viewPagerList.get(0));
        		viewPagerBox.addView(viewPagerList.get(1));
        	}
        	
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
