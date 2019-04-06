package com.app.yuejuan;

import java.util.HashMap;
import java.util.List;

import com.app.utils.WebServiceUtil;
import com.app.webservice.ChangePasswordResponse;
import com.app.webservice.MarkingListResponse;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.util.Log;


public class ModifyPasswordActivity extends Activity {

	public TextView titleView;
	public EditText oldPasswordET,newPasswordET, newPasswordET2;
	public Button modifyButton;
	LinearLayout originPassBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d("YJ", "onCreate func");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        titleView = (TextView)this.findViewById(R.id.head_title);
        titleView.setText("修改密码");
        oldPasswordET = (EditText)this.findViewById(R.id.origin_password);
        originPassBox = (LinearLayout)this.findViewById(R.id.origin_password_box);
        originPassBox.setVisibility(View.GONE); //原密码暂时隐藏
        newPasswordET = (EditText)this.findViewById(R.id.new_password);
        newPasswordET2 = (EditText)this.findViewById(R.id.certain_new_password);
        modifyButton = (Button)this.findViewById(R.id.submit_modify_button);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void submitNewPassword(String password){
    	Public pub = (Public)this.getApplication();
        
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("arg0", pub.userid);
        properties.put("arg1", pub.token);
        properties.put("arg2", password);
        
        Log.v("YJ", pub.token); //
        WebServiceUtil.callWebService(WebServiceUtil.WEB_SERVER_URL, "ChangePassword", properties, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    Log.v("YJ",result);
                    
                    ChangePasswordResponse reponse = new ChangePasswordResponse(result);
                    if(Public.responseIDOK.equals(reponse.getCodeID())){
                    	Intent intent =new Intent(ModifyPasswordActivity.this, LoginActivity.class);
                    	startActivity(intent);
                    	overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                       
                    }else{
                    	Toast.makeText(ModifyPasswordActivity.this, reponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
  //方法：控件View的点击事件
    public void onClick(View v) {
    	Intent intent;
        switch (v.getId()) {
        case R.id.head_back_button:
        	
        	intent =new Intent(ModifyPasswordActivity.this, PersonalActivity.class);
        	startActivity(intent);
        	overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            break;
        case R.id.submit_modify_button:
        	String oldPass = oldPasswordET.getText().toString();
        	String newPass = newPasswordET.getText().toString();
        	String newPass2 = newPasswordET2.getText().toString();
        	
        	if(!newPass.equals(newPass2)){
        		Toast.makeText(ModifyPasswordActivity.this, "新密码和确认密码输入不一致,请重新输入", Toast.LENGTH_SHORT).show();
        		return;
        	}
        	ModifyPasswordActivity.this.submitNewPassword(newPass2);
        	
        	
        	break;
        default:
            break;
        }
    }
}
