package com.app.yuejuan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
	TextView ipET;
	EditText passwordET;
	EditText usernameET;
	Button loginButton;
	boolean isLogining = false;
	boolean isLoginSuccess = false;
	// 声明一个Handler对象
    private static Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d("YJ", "onCreate func");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ipET = (TextView)this.findViewById(R.id.login_ip);
        ipET.setText(Public.imageHost);
        usernameET =(EditText)findViewById(R.id.login_username);
        passwordET =(EditText)findViewById(R.id.login_password);
        Public pub = (Public)this.getApplication();
        loginButton = (Button)this.findViewById(R.id.login_button);
        usernameET.setText(pub.getUserID());
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public class myThread implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int k = 8;
            while(k>=0){
            	k--;
            	if(k<=0){
            		
                    
                    isLogining = false;
                    
                    loginButton.post(new Runnable() {  
                        
                        @Override  
                        public void run() {  
                        	
                        	loginButton.setText("登 录");
                            loginButton.setBackgroundResource(R.drawable.buttonsharp);
                            if(!isLoginSuccess){
                            	Toast.makeText(LoginActivity.this, "访问错误", Toast.LENGTH_SHORT).show();	
                            }
                            
                        }  
                    });  
            		break;
            	}
            	Log.v("YJ Wait", String.valueOf(k));
            	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            }
		}
    	
    }
    //登陆
	public void toLoginClick(){
		
		
		if(isLogining){
			Log.v("YJ ", "正在登登录，稍后点击");	
			return;
		}
		isLoginSuccess = false;
		isLogining = true;
		loginButton.setText("正在登录...");
        loginButton.setBackgroundResource(R.drawable.buttonsharp_gray);
		Log.v("YJ ", "点击登录");
		Thread thread = new Thread(new myThread());
		thread.start();
		
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        String ip = ipET.getText().toString();
        
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("arg0", username);
        properties.put("arg1", password);
        Public pub = (Public)this.getApplication();
        pub.setUserID(username);
        Public.imageHost = ip;
        
        WebServiceUtil.callWebService(WebServiceUtil.getURL(), "UserLogin", properties, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
            	loginButton.setText("登 录");
                loginButton.setBackgroundResource(R.drawable.buttonsharp);
                isLogining = false;
                if (result != null) {
                    Log.v("YJ",result);
                    isLoginSuccess = true;
                    UserLoginResponse reponse = new UserLoginResponse(result);
                    if("0001".equals(reponse.getCodeID())){
                    	//AppCookies.setToken(reponse.getAuthtoken());
                        Public pub = (Public)LoginActivity.this.getApplication();
                        pub.setToken(reponse.getAuthtoken());
                        //pub.setUserName(pub.getUserID());
                        //pub.setHostIP(pub.imageHost);
                        LoginActivity.this.getUserInfo();
                       
                    }else{
                    	Toast.makeText(LoginActivity.this, reponse.getMessage(), Toast.LENGTH_SHORT).show();
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
        
        WebServiceUtil.callWebService(WebServiceUtil.getURL(), "GetUserinfo", properties, new WebServiceUtil.WebServiceCallBack() {
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
        	
        	//Toast.makeText(LoginActivity.this, "btn1:", Toast.LENGTH_SHORT).show();
        	Intent intent =new Intent(LoginActivity.this, ForgetPasswordActivity.class);
        	startActivity(intent);
            
        default:
            break;
        }
    }
}
