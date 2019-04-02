package com.app.yuejuan;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public abstract class MainBaseActivity extends Activity implements View.OnClickListener{
	private RadioButton button_1;
    private RadioButton button_2;
    private RadioButton button_3;
    private List<RadioButton>radioList;
    public Intent intent;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(this.getLayoutId());
        radioList = new ArrayList();
        //找到四个按钮
        button_1 = (RadioButton) findViewById(R.id.button_1);
        button_2 = (RadioButton) findViewById(R.id.button_2);
        button_3 = (RadioButton) findViewById(R.id.button_3);
        
        radioList.add(button_1);
        radioList.add(button_2);
        radioList.add(button_3);
        
        //设置icon大小
        int icon_size = 45;
        Drawable drawable1=getResources().getDrawable(R.drawable.radio_button_icon_selector_1);
        drawable1.setBounds(0,0,icon_size,icon_size);
        button_1.setCompoundDrawables(null,drawable1,null,null);
        
        Drawable drawable2=getResources().getDrawable(R.drawable.radio_button_icon_selector_2);
        drawable2.setBounds(0,0,icon_size,icon_size);
        button_2.setCompoundDrawables(null,drawable2,null,null);
        
        Drawable drawable3=getResources().getDrawable(R.drawable.radio_button_icon_selector_3);
        drawable3.setBounds(0,0,icon_size,icon_size);
        button_3.setCompoundDrawables(null,drawable3,null,null);
        
        Log.v("YJ","Main Base Activity onCreate");
        initView();
        
    }
	protected abstract int getLayoutId();
	protected abstract void initView();
	public abstract void widgetClick(View v);
	public RadioButton getButtonById(int id){
		//id  1,2,3,4
		if(id >0 && id <= radioList.size()){
			int _id = id - 1;
			return radioList.get(_id);
		}
		return null;
	}
	@Override
    public void onClick(View v) {
		
		switch (v.getId()) {
		
	        case R.id.button_1:
	        	
	        	intent =new Intent(MainBaseActivity.this, MarkingActivity.class);
            	startActivity(intent);
	            break;
	        case R.id.button_2:
	        	intent =new Intent(MainBaseActivity.this, ProgressActivity.class);
            	startActivity(intent);
	            break;
	        case R.id.button_3:
	        	
	        	intent =new Intent(MainBaseActivity.this, PersonalActivity.class);
            	startActivity(intent);
	            break;
	
	        default:
	            break;
	    }
		widgetClick(v);
	}
}
