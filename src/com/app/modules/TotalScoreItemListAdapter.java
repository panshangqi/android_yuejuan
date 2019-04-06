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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TotalScoreItemListAdapter extends BaseAdapter{
	LayoutInflater inflater = null;
    List<TotalScoreItemInfo> listInfo;
    String queid;
    ItemClickInterfaceListener itemClickListener;
    private int selectedPosition = 0;// 选中的位置 
    public TotalScoreItemListAdapter(Context context,List<TotalScoreItemInfo> listInfo, String queid, ItemClickInterfaceListener itemClickListener){
    	
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
    public String getTotalScore(){
    	float total = 0;
    	for(int i=0;i<this.listInfo.size();i++){
    		if("".equals(this.listInfo.get(i).quescore)){
    			
    		}else{
    			total += Float.parseFloat(this.listInfo.get(i).quescore);
    		}
    	}
    	return String.valueOf(total);
    }
    public boolean getAll(){
    	int result = 0;
    	for(int i=0;i<this.listInfo.size();i++){
    		if("".equals(this.listInfo.get(i).quescore)){
    			
    		}else{
    			result ++;
    		}
    	}
    	if(result < this.listInfo.size())
    		return false;
    	return true;
    }
    //小题分数逗号隔开
    public String getSubScoreList(){
    	String res = "";
		for(int i=0;i<this.listInfo.size();i++){
    		if(i==0){
    			res += this.listInfo.get(i).quescore;
    		}else{
    			res += ","+this.listInfo.get(i).quescore;
    		}
    	}
		return res;
    }
    public void ClearData(){
    	for(int i=0;i<this.listInfo.size();i++){
    		this.listInfo.get(i).quescore = "";
    	}
    	this.notifyDataSetInvalidated();
    }
    public void setSelectedPosition(int position) {  
    	if(position >= this.listInfo.size()){
    		return;
    	}
        selectedPosition = position;  
        this.notifyDataSetInvalidated();
    } 
    public void setScoreByPosition(int position, String score){
    	if(position >= this.listInfo.size()){
    		return;
    	}
    	this.listInfo.get(position).quescore = score;
    	this.notifyDataSetInvalidated();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        final TotalScoreItemInfo itemInfo = listInfo.get(position);
        if(convertView == null || convertView.getTag() == null){
            convertView = inflater.inflate(R.layout.list_item_total_score_box,null);
            holder = new ViewHolder();
            holder.boxView = (LinearLayout)convertView.findViewById(R.id.tsb_box);
            holder.quenumView = (TextView)convertView.findViewById(R.id.tsb_num);
            holder.quescoreView = (TextView)convertView.findViewById(R.id.tsb_score);
            
            
            convertView.setTag(holder);
        }else{
             
            holder = (ViewHolder)convertView.getTag();
        }

        holder.quenumView.setText(itemInfo.quenum);
        holder.quescoreView.setText(itemInfo.quescore);
        if(selectedPosition != position){
        	holder.boxView.setBackgroundColor(android.graphics.Color.WHITE);
        }else{
        	holder.boxView.setBackgroundColor(0xffdddddd);
        }
        holder.boxView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//v.setBackgroundColor(0xffaaaaaa);
				itemClickListener.Callback(itemInfo);
			}
		});
        return convertView;
    }
    public class ViewHolder{
    	public LinearLayout boxView;
        public TextView quenumView;
        public TextView quescoreView;
    }
    public static interface ItemClickInterfaceListener{
    	public void Callback(TotalScoreItemInfo itemInfo);
    }
}
