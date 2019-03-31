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

import com.app.modules.BackMarkItemInfo;
import com.app.modules.BackMarkItemListAdapter;
import com.app.webservice.*;

public class MarkingActivity extends Activity {

	public TextView markFaceBtn;
	public TextView markBackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d("YJ", "onCreate func");
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.activity_marking);
        markFaceBtn = (TextView)findViewById(R.id.radio_mark_button);  
        markBackBtn = (TextView)findViewById(R.id.radio_mark_back_button);
        
        this.getBackMarkList();
        
    }

    public void getBackMarkList(){
    	BackMarkItemInfo item = new BackMarkItemInfo();
    	item.que_num = 1;
    	item.que_score = 50;
    	item.que_time = "xx: 00: 00";
    	
    	List<BackMarkItemInfo> listInfo = new ArrayList(1);
    	
    	listInfo.add(item);
    	listInfo.add(item);
    	listInfo.add(item);
    	listInfo.add(item);
    	listInfo.add(item);
    	Log.v("YJ","getBackMarkList()");
    	Toast.makeText(MarkingActivity.this, "onCreate", Toast.LENGTH_SHORT).show();
    	BackMarkItemListAdapter bmilAdapter = new BackMarkItemListAdapter(Public.context, listInfo);

    	ListView AlreadyMackListView = (ListView)findViewById(R.id.already_mark_list_view);
    	
    	AlreadyMackListView.setAdapter(bmilAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
  
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.radio_mark_button:
        	
        	markFaceBtn.setTextColor(Color.parseColor("#FFFFFF"));
        	markFaceBtn.setBackgroundColor(Color.parseColor("#ff9647"));
        	markBackBtn.setTextColor(Color.parseColor("#333333"));
        	markBackBtn.setBackgroundColor(Color.parseColor("#ffffff"));
        	
        	Toast.makeText(MarkingActivity.this, "btn1:", Toast.LENGTH_SHORT).show();

            break;
        case R.id.radio_mark_back_button:
        	
        	markFaceBtn.setTextColor(Color.parseColor("#333333"));
        	markFaceBtn.setBackgroundColor(Color.parseColor("#ffffff"));
        	markBackBtn.setTextColor(Color.parseColor("#ffffff"));
        	markBackBtn.setBackgroundColor(Color.parseColor("#ff9647"));
        	Toast.makeText(MarkingActivity.this, "btn1:", Toast.LENGTH_SHORT).show();
        	
            
        default:
            break;
        }
    }
}
