package com.app.yuejuan;

import java.util.HashMap;

import com.app.webservice.AppCookies;
import com.app.webservice.UserLoginResponse;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.util.Log;


public class PersonalActivity extends MainBaseActivity {

	public TextView usernameView;
	public TextView useridView;
	public TextView userpowerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d("YJ", "onCreate func");
        super.onCreate(savedInstanceState);
    }
    @Override
    protected int getLayoutId(){
    	return R.layout.activity_personal;
    }
    @Override
	protected void initView(){
    	usernameView = (TextView)findViewById(R.id.my_username);
        useridView = (TextView)findViewById(R.id.my_user_id);
        userpowerView = (TextView)findViewById(R.id.my_authority);
        Public pub = (Public)PersonalActivity.this.getApplication();

    	usernameView.setText(pub.username);
    	useridView.setText("账号：" + pub.userid);
    	userpowerView.setText("权限：" + pub.userpower);
    	
    	RadioButton radioBtn = getButtonById(3);
    	radioBtn.setSelected(true);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//  //方法：控件View的点击事件
    @Override
	public void widgetClick(View v){
        switch (v.getId()) {
        case R.id.logout_button:
        	Public pub = (Public)this.getApplication();
            String userid = pub.getUserID();
            String token = pub.getToken();
            Log.v("YJ","user_id: " + userid + ",token:" +token);
        	//String token1 = AppCookies.getToken();
            HashMap<String, String> properties = new HashMap<String, String>();
            properties.put("arg0", userid);
            properties.put("arg1", token);
            Intent intent =new Intent(PersonalActivity.this, LoginActivity.class);
        	startActivity(intent);
        	overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        	Log.v("YJ","Login out");
            WebServiceUtil.callWebService(WebServiceUtil.WEB_SERVER_URL, "UserLogout", properties, new WebServiceUtil.WebServiceCallBack() {
                @Override
                public void callBack(String result) {
                    if (result != null) {
                        Log.v("YJ",result);
                        UserLoginResponse reponse = new UserLoginResponse(result);
                        //AppCookies.setToken(reponse.getAuthtoken());
                        if("0001".equals(reponse.getCodeID())){
                        	
                        }else{
                        	Toast.makeText(PersonalActivity.this, "服务器数据异常", Toast.LENGTH_SHORT).show();
                            
                        }
                    }
                }
            });
            break;
              
        case R.id.my_change_password_button:
        	Intent intent1 =new Intent(PersonalActivity.this, ForgetPasswordActivity.class);
        	startActivity(intent1);
        	overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        	break;
        default:
            break;
        }
    }
}
