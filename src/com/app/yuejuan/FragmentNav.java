package com.app.yuejuan;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class FragmentNav extends Fragment{
	private View mView;
	private int layoutID;
	public FragmentNav(int layoutID){
		this.layoutID = layoutID;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if(mView == null) {
        	//R.layout.fragment_bottom_nav_1_layout
            mView = inflater.inflate(this.layoutID, container, false);
        }
 
        return mView;
    }
//    public void onClick(View v) {
//        switch (v.getId()) {
//        case R.id.radio_mark_button:
//        	
//        	
//            break;
//        case R.id.radio_mark_back_button:
//        	
//        	
//        	
//            
//        default:
//            break;
//        }
//    }
}