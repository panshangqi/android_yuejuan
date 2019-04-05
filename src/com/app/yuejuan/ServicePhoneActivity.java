package com.app.yuejuan;

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


public class ServicePhoneActivity extends Activity {

	public TextView titleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d("YJ", "onCreate func");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_phone);
        titleView = (TextView)this.findViewById(R.id.head_title);
        titleView.setText("客服电话");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
  //方法：控件View的点击事件
    public void onClick(View v) {
    	Intent intent;
        switch (v.getId()) {
        case R.id.head_back_button:
        	
        	intent =new Intent(ServicePhoneActivity.this, PersonalActivity.class);
        	startActivity(intent);
        	overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            break;

        default:
            break;
        }
    }
}
