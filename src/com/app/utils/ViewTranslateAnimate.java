package com.app.utils;

import java.util.List;

import com.app.yuejuan.Public;
import com.app.yuejuan.R;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class ViewTranslateAnimate {
	public List<View> viewList;
	public LinearLayout parentView;
	private int position = 0; //default pos = 0
	private boolean hasChildView = false;
	private Animation animationInLeft;
	private Animation animationOutRight;
	private Animation animationInRight;
	private Animation animationOutLeft;
	public ViewTranslateAnimate(LinearLayout parentView, List<View> viewList, int default_pos){
		this.parentView = parentView;
		this.viewList = viewList;
		animationInLeft = AnimationUtils.loadAnimation(Public.context, R.anim.in_from_left);
		animationOutRight = AnimationUtils.loadAnimation(Public.context, R.anim.out_to_right);
		animationInRight = AnimationUtils.loadAnimation(Public.context, R.anim.in_from_right);
		animationOutLeft = AnimationUtils.loadAnimation(Public.context, R.anim.out_to_left);
		this.position = default_pos;
		this.parentView.addView(viewList.get(default_pos));
	}
	

	public int getPos(){
		return this.position;
	}
	public View getViewByID(int Rid){
		return viewList.get(this.position).findViewById(Rid);
	}
	public void setDruation(int time){ //ms
		animationInLeft.setDuration(time);
		animationOutRight.setDuration(time);
		animationInRight.setDuration(time);
		animationOutLeft.setDuration(time);
	}

	public void setCurrentItem(int pos){
		if(pos == this.position)
			return;
		if(this.parentView != null){
			if(this.position == 0){
				viewList.get(this.position).startAnimation(animationOutRight);
				viewList.get(pos).startAnimation(animationInLeft);
			}else{
				viewList.get(this.position).startAnimation(animationOutLeft);
				viewList.get(pos).startAnimation(animationInRight);
			}
			this.parentView.removeView(viewList.get(this.position));
			this.parentView.addView(viewList.get(pos));
			this.position = pos;
		}
	}
}
