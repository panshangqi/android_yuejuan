package com.app.modules;

import java.util.List;

//import com.app.yuejuan.MarkingItemClickInterfaceListener;
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
import android.widget.TextView;

public class MarkingItemListAdapter extends BaseAdapter{
	LayoutInflater inflater = null;
    List<MarkingItemInfo> listInfo;
    
    ItemClickInterfaceListener itemClickListener;
    public MarkingItemListAdapter(Context context,List<MarkingItemInfo> listInfo, ItemClickInterfaceListener itemClickListener){
    	
        inflater = LayoutInflater.from(context);
        this.listInfo = listInfo;
        this.itemClickListener = itemClickListener;
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
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        final MarkingItemInfo itemInfo = listInfo.get(position);
        if(convertView == null || convertView.getTag() == null){
            convertView = inflater.inflate(R.layout.list_item_marking,null);
            holder = new ViewHolder();
            holder.itemBox = convertView;
            holder.subjectNameView = (TextView)convertView.findViewById(R.id.tp_subject_name);
            holder.questionNameView = (TextView)convertView.findViewById(R.id.tp_question_name);
            holder.taskTotalView = (TextView)convertView.findViewById(R.id.tp_task_total_count);
            holder.dealWithView = (TextView)convertView.findViewById(R.id.tp_deal_with_count);
            holder.withoutView = (TextView)convertView.findViewById(R.id.tp_without_count);
            convertView.setTag(holder);
        }else{
             
            holder = (ViewHolder)convertView.getTag();
        }
        
        
        holder.subjectNameView.setText(itemInfo.subjectName);
        holder.questionNameView.setText(itemInfo.questionName);
        holder.taskTotalView.setText(String.valueOf(itemInfo.taskTotalCount));
        holder.dealWithView.setText(String.valueOf(itemInfo.dealWithCount));
        holder.withoutView.setText(String.valueOf(itemInfo.withoutCount));
        
        holder.itemBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				itemClickListener.Callback(itemInfo);
			}
		});
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
    	public View itemBox;
    	public TextView subjectNameView;
    	public TextView questionNameView;
        public TextView taskTotalView;
        public TextView dealWithView;
        public TextView dealPercentView;
        public TextView withoutView;
    }
    public static interface ItemClickInterfaceListener{
    	public void Callback(MarkingItemInfo itemInfo);
    }
}
