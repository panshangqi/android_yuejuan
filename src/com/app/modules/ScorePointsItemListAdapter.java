package com.app.modules;

import java.util.List;

import com.app.modules.SelectQuestionItemListAdapter.ItemClickInterfaceListener;
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

public class ScorePointsItemListAdapter extends BaseAdapter{
	LayoutInflater inflater = null;
    List<ScorePointsItemInfo> listInfo;
    ItemClickInterfaceListener itemClickListener;
    public ScorePointsItemListAdapter(Context context,List<ScorePointsItemInfo> listInfo,ItemClickInterfaceListener itemClickListener){
    	
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
        final ScorePointsItemInfo itemInfo = listInfo.get(position);
        if(convertView == null || convertView.getTag() == null){
            convertView = inflater.inflate(R.layout.list_item_score_potions,null);
            holder = new ViewHolder();
            holder.numView = (TextView)convertView.findViewById(R.id.qt_question_num);
            
            convertView.setTag(holder);
        }else{
             
            holder = (ViewHolder)convertView.getTag();
        }

        holder.numView.setText(itemInfo.score);

        holder.numView.setOnClickListener(new OnClickListener(){



			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				itemClickListener.Callback(itemInfo);
	
			}
        	
        });

        return convertView;
    }
    public class ViewHolder{
    	
        public TextView numView;
    }
    public static interface ItemClickInterfaceListener{
    	public void Callback(ScorePointsItemInfo itemInfo);
    }
}
