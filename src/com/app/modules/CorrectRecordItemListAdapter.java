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
import android.widget.TextView;

public class CorrectRecordItemListAdapter extends BaseAdapter{
	LayoutInflater inflater = null;
    List<CorrectRecordItemInfo> listInfo;
    
    public CorrectRecordItemListAdapter(Context context,List<CorrectRecordItemInfo> listInfo){
    	
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
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        final CorrectRecordItemInfo itemInfo = listInfo.get(position);
        if(convertView == null || convertView.getTag() == null){
            convertView = inflater.inflate(R.layout.list_item_correct_record,null);
            holder = new ViewHolder();
            holder.orderView = (TextView)convertView.findViewById(R.id.ct_order);
            holder.scoreView = (TextView)convertView.findViewById(R.id.ct_score);
            
            convertView.setTag(holder);
        }else{
             
            holder = (ViewHolder)convertView.getTag();
        }

        holder.orderView.setText(itemInfo.order);
        holder.scoreView.setText(itemInfo.score);

        

        return convertView;
    }
    public class ViewHolder{
    	
        public TextView orderView;
        public TextView scoreView;
    }
}
