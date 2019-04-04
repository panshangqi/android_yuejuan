package com.app.yuejuan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.app.utils.WebServiceUtil;
import com.app.webservice.*;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d("YJ", "onCreate func");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    //登陆
	public void toLoginClick(){
		EditText usernameET =(EditText)findViewById(R.id.login_username);
        String username = usernameET.getText().toString();
        EditText passwordET =(EditText)findViewById(R.id.login_password);
        String password = passwordET.getText().toString();
       
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("arg0", username);
        properties.put("arg1", password);
        Public pub = (Public)this.getApplication();
        pub.setUserID(username);
        WebServiceUtil.callWebService(WebServiceUtil.WEB_SERVER_URL, "UserLogin", properties, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    Log.v("YJ",result);
                    
                    UserLoginResponse reponse = new UserLoginResponse(result);
                    if("0001".equals(reponse.getCodeID())){
                    	//AppCookies.setToken(reponse.getAuthtoken());
                        Public pub = (Public)LoginActivity.this.getApplication();
                        pub.setToken(reponse.getAuthtoken());
                        LoginActivity.this.getUserInfo();
                       
                    }else if("0002".equals(reponse.getCodeID())){
                    	Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                    }else if("0003".equals(reponse.getCodeID())){
                    	Toast.makeText(LoginActivity.this, "本系统暂不支持管理员登录", Toast.LENGTH_SHORT).show();
                    }else if("0004".equals(reponse.getCodeID())){
                    	Toast.makeText(LoginActivity.this, "服务器数据异常", Toast.LENGTH_SHORT).show();
                    }else if("0005".equals(reponse.getCodeID())){
                    	Toast.makeText(LoginActivity.this, "数据异常验证失败", Toast.LENGTH_SHORT).show();
                    }else if("0006".equals(reponse.getCodeID())){
                    	Toast.makeText(LoginActivity.this, "数据异常登录失败", Toast.LENGTH_SHORT).show();
                    }
                    
                }
            }
        });
	}
    public void getUserInfo(){
 
    	Public pub = (Public)this.getApplication();
    	String userid = pub.getUserID();
    	String token = pub.getToken();
       
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("arg0", userid);
        properties.put("arg1", token);
        
        WebServiceUtil.callWebService(WebServiceUtil.WEB_SERVER_URL, "GetUserinfo", properties, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    Log.v("YJ",result);
                    
                    GetUserInfoResponse reponse = new GetUserInfoResponse(result);
                    if("0001".equals(reponse.getCodeID())){
                    	Public pub = (Public)LoginActivity.this.getApplication();
                       
                    	pub.userid = reponse.userid;
                    	pub.username = reponse.username;
                    	pub.usernowproject = reponse.usernowproject;
                    	pub.usersubjectid = reponse.usersubjectid;
                    	pub.usersubject = reponse.usersubject;
                    	pub.userpower = reponse.userpower;
                        
                        Intent intent =new Intent(LoginActivity.this, MarkingActivity.class);
                    	startActivity(intent);
                    	//设置切换动画，从右边进入，左边退出
        				overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        				
                    }else if("0002".equals(reponse.getCodeID())){
                    	Toast.makeText(LoginActivity.this, "用户信息验证失败", Toast.LENGTH_SHORT).show();
                    }else if("0003".equals(reponse.getCodeID())){
                    	Toast.makeText(LoginActivity.this, "服务器数据异常", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.login_button:
        	
            this.toLoginClick();
            break;
        case R.id.loginforget:
        	
        	Toast.makeText(LoginActivity.this, "btn1:", Toast.LENGTH_SHORT).show();
        	Intent intent =new Intent(LoginActivity.this, ForgetPasswordActivity.class);
        	startActivity(intent);
            
        default:
            break;
        }
    }
}
