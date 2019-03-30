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

import com.app.webservice.*;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d("YJ", "onCreate func");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
  
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.login_button:
        	
            EditText usernameET =(EditText)findViewById(R.id.login_username);
            String username = usernameET.getText().toString();
            EditText passwordET =(EditText)findViewById(R.id.login_password);
            String password = passwordET.getText().toString();
            
            Toast.makeText(MainActivity.this, "btn1:"+username+password, Toast.LENGTH_SHORT).show();
            
            HashMap<String, String> properties = new HashMap<String, String>();
            properties.put("arg0", username);
            properties.put("arg1", password);
            
            WebServiceUtil.callWebService(WebServiceUtil.WEB_SERVER_URL, "UserLogin", properties, new WebServiceUtil.WebServiceCallBack() {
                @Override
                public void callBack(String result) {
                    if (result != null) {
                        Log.v("YJ",result);
//                        Gson gs = new Gson();  
//                        List<UserLoginResponse> jsonListObject = gs.fromJson(result, new TypeToken<List<UserLoginResponse>>(){}.getType());//鎶奐SON鏍煎紡鐨勫瓧绗︿覆杞负List  
//                        System.out.println("response json: "+jsonListObject.get(0).toString());
//                        
                        UserLoginResponse reponse = new UserLoginResponse(result);
                        AppCookies.setToken(reponse.getAuthtoken());
                        
                        Log.v("YJ",AppCookies.getToken());
                    }
                }
            });

            
            break;
        case R.id.loginforget:
        	
        	Toast.makeText(MainActivity.this, "btn1:", Toast.LENGTH_SHORT).show();
        	Intent intent =new Intent(MainActivity.this, ForgetActivity.class);
        	startActivity(intent);
            
        default:
            break;
        }
    }
}
