package com.app.modules;

import java.util.List;

import com.app.yuejuan.R;
import com.app.yuejuan.R.drawable;
import com.app.yuejuan.R.id;
import com.app.yuejuan.R.layout;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.View.OnFocusChangeListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

public class ProgressItemListAdapter extends BaseAdapter{
	LayoutInflater inflater = null;
    List<ProgressItemInfo> listInfo;
    
    public ProgressItemListAdapter(Context context,List<ProgressItemInfo> listInfo){
    	
        inflater = LayoutInflater.from(context);
        this.listInfo = listInfo;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listInfo.size();
    }
    @Override
    public Object getItem(int index) {
        // TODO Auto-generated method stub
        return listInfo.get(index);
    }
    @Override
    public long getItemId(int index) {
        // TODO Auto-generated method stub
        return index;
    }
    public class MyRunnable implements Runnable{
    	public ViewHolder holder;
    	public ProgressItemInfo itemInfo;
    	public void setViewHolder(ViewHolder holder, ProgressItemInfo itemInfo){
    		this.holder = holder;
    		this.itemInfo = itemInfo;
    	}
    	public void run(){
    		int barWidth = holder.progress_barView.getWidth();
    		int blockWidth = (int)(barWidth * itemInfo.reat * 0.01);
            LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) holder.progress_blockView.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20  
            linearParams.width = blockWidth; 
            holder.progress_blockView.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
    		
    	}
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        final ProgressItemInfo itemInfo = listInfo.get(position);
        if(convertView == null || convertView.getTag() == null){
            convertView = inflater.inflate(R.layout.list_item_task_progress,null);
            holder = new ViewHolder();
            holder.subjectNameView = (TextView)convertView.findViewById(R.id.tp_subject_name);
            holder.questionNameView = (TextView)convertView.findViewById(R.id.tp_question_name);
            holder.taskTotalView = (TextView)convertView.findViewById(R.id.tp_task_total_count);
            holder.dealWithView = (TextView)convertView.findViewById(R.id.tp_deal_with_count);
            holder.dealPercentView = (TextView)convertView.findViewById(R.id.tp_deal_with_percent);
            holder.progress_barView = (LinearLayout)convertView.findViewById(R.id.progress_bar_h);
            holder.progress_blockView = (LinearLayout)convertView.findViewById(R.id.progress_block_h);
            convertView.setTag(holder);
        }else{
             
            holder = (ViewHolder)convertView.getTag();
        }
        Log.v("YJ",String.valueOf(itemInfo.taskTotalCount));
        holder.subjectNameView.setText(itemInfo.subjectName);
        holder.questionNameView.setText(itemInfo.questionName);
        holder.taskTotalView.setText(String.valueOf(itemInfo.taskTotalCount));
        holder.dealWithView.setText(String.valueOf(itemInfo.dealWithCount));

        holder.dealPercentView.setText(String.valueOf(itemInfo.reat) +"%");
        
        //int barWidth = holder.progress_barView.getWidth();
        //measure方法的参数值都设为0即可  
        MyRunnable runable = new MyRunnable();
        runable.setViewHolder(holder, itemInfo);
        
        	
        
        

        holder.progress_barView.post(runable);
        //获取组件宽度  
        //holder.progress_barView
        
        
//        holder.numView.setOnTouchListener(new OnTouchListener(){
//
//			@Override
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				// TODO Auto-generated method stub
//				
//				return false;
//			}
//        	
//        });
//        holder.scoreView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				//wifiListener.Callback(itemInfo);
//			}
//		});
//        holder.timeView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				//wifiListener.CallbackInfo(itemInfo);
//			}
//		});
        return convertView;
    }
    public class ViewHolder{
    	public TextView subjectNameView;
    	public TextView questionNameView;
        public TextView taskTotalView;
        public TextView dealWithView;
        public TextView dealPercentView;
        public LinearLayout progress_barView;
        public LinearLayout progress_blockView;
    }
}
