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

public class SelectQuestionItemListAdapter extends BaseAdapter{
	LayoutInflater inflater = null;
    List<SelectQuestionItemInfo> listInfo;
    String queid;
    ItemClickInterfaceListener itemClickListener;
    public SelectQuestionItemListAdapter(Context context,List<SelectQuestionItemInfo> listInfo, String queid, ItemClickInterfaceListener itemClickListener){
    	
        inflater = LayoutInflater.from(context);
        this.listInfo = listInfo;
        this.queid = queid;
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
        final SelectQuestionItemInfo itemInfo = listInfo.get(position);
        if(convertView == null || convertView.getTag() == null){
            convertView = inflater.inflate(R.layout.list_item_select_question,null);
            holder = new ViewHolder();
            holder.quenameView = (TextView)convertView.findViewById(R.id.qt_question_name);
            
            
            convertView.setTag(holder);
        }else{
             
            holder = (ViewHolder)convertView.getTag();
        }

        holder.quenameView.setText(itemInfo.quename);
        if(this.queid.equals(itemInfo.queid)){
        	holder.quenameView.setTextColor(android.graphics.Color.RED);
        }else{
        	holder.quenameView.setTextColor(0xff555555);
        }
        holder.quenameView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				itemClickListener.Callback(itemInfo);
			}
		});
        return convertView;
    }
    public class ViewHolder{
    	
        public TextView quenameView;
    }
    public static interface ItemClickInterfaceListener{
    	public void Callback(SelectQuestionItemInfo itemInfo);
    }
}
