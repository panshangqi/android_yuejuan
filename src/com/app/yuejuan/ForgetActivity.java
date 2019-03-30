package com.app.yuejuan;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;
import android.util.Log;


public class ForgetActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d("YJ", "onCreate func");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
       
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
  //方法：控件View的点击事件
//    public void onClick(View v) {
//        switch (v.getId()) {
//        case R.id.login_button:
//        	
////            EditText usernameET =(EditText)findViewById(R.id.login_username);
////            String username = usernameET.getText().toString();
////            EditText passwordET =(EditText)findViewById(R.id.login_password);
////            String password = passwordET.getText().toString();
////            
////            Toast.makeText(ForgetActivity.this, "btn1:"+username+password, Toast.LENGTH_SHORT).show();
//            break;
//              
//            
//        default:
//            break;
//        }
    //}
}
