package com.app.yuejuan;

import java.util.HashMap;

import com.app.webservice.AppCookies;
import com.app.webservice.UserLoginResponse;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.util.Log;


public class MyActivity extends Activity {

	public TextView usernameView;
	public TextView useridView;
	public TextView userpowerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d("YJ", "onCreate func");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        usernameView = (TextView)findViewById(R.id.my_username);
        useridView = (TextView)findViewById(R.id.my_user_id);
        userpowerView = (TextView)findViewById(R.id.my_authority);
        Public pub = (Public)MyActivity.this.getApplication();

    	usernameView.setText(pub.username);
    	useridView.setText("账号：" + pub.userid);
    	userpowerView.setText("权限：" + pub.userpower);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
  //方法：控件View的点击事件
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.logout_button:
        	Public pub = (Public)this.getApplication();
            String userid = pub.getUserID();
            String token = pub.getToken();
            Log.v("YJ","user_id: " + userid + ",token:" +token);
        	//String token = AppCookies.getToken();
            HashMap<String, String> properties = new HashMap<String, String>();
            properties.put("arg0", userid);
            properties.put("arg1", token);
            
            WebServiceUtil.callWebService(WebServiceUtil.WEB_SERVER_URL, "UserLogout", properties, new WebServiceUtil.WebServiceCallBack() {
                @Override
                public void callBack(String result) {
                    if (result != null) {
                        Log.v("YJ",result);
                        UserLoginResponse reponse = new UserLoginResponse(result);
                        //AppCookies.setToken(reponse.getAuthtoken());
                        if("0001".equals(reponse.getCodeID())){
                        	Intent intent =new Intent(MyActivity.this, MainActivity.class);
                        	startActivity(intent);
                        	Log.v("YJ","Login out");
                        }else{
                        	Toast.makeText(MyActivity.this, "服务器数据异常", Toast.LENGTH_SHORT).show();
                            
                        }
                    }
                }
            });
            break;
              
        case R.id.my_change_password_button:
        	Intent intent =new Intent(MyActivity.this, ForgetActivity.class);
        	startActivity(intent);
        	break;
        default:
            break;
        }
    }
}
