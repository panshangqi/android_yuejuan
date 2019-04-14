package com.app.yuejuan;
//import org.apache.commons.codec.binary.Base64;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;

//import java.util.Date;
//import java.text.DateFormat;
//import java.rmi.Remote;
//import org.apache.axis.client.Call;
//import org.apache.axis.client.Service;
//import org.apache.axis.encoding.XMLType; 

//import javax.xml.namespace.QName;
//import java.lang.Integer;
//import javax.xml.rpc.ParameterMode;

import android.util.Base64;
import android.util.Log;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.app.modules.CorrectRecordItemInfo;
import com.app.modules.CorrectRecordItemListAdapter;
import com.app.modules.ProgressItemInfo;
import com.app.modules.ProgressItemListAdapter;
import com.app.modules.ScorePointsItemInfo;
import com.app.modules.ScorePointsItemListAdapter;
import com.app.modules.SelectQuestionItemInfo;
import com.app.modules.SelectQuestionItemListAdapter;
import com.app.modules.SelectQuestionItemListAdapter.ItemClickInterfaceListener;
import com.app.modules.TotalScoreItemInfo;
import com.app.modules.TotalScoreItemListAdapter;
import com.app.utils.CanvasView;
import com.app.utils.MyDialog;
import com.app.utils.MyScrollView;
import com.app.utils.WebServiceUtil;
import com.app.webservice.*;
class MarkingScoreData{ //正评分数需要提交的数据
	public String subjectid;     //试卷编号
	public String secretid;      //密号
	public String examid;        //考号
	public String queid;         //题目编号
	public String flag_send;     //当前评阅模式
	public String score;         //总分
	public String smallscore;    //小题分(各小题分用逗号隔开)
	public String usedtime;      //批阅所用时间
	public String signid;        //标志试卷编号
	public String comment;       //批注信息
	public String commentimage;  //批注图片
	public String invalidscore;//  修改前总分
	
	public long startTime;
	public long endTime;
	public boolean hasSubQuestion = false; // 是否有小题
	//
	public String firstmark = "";
	public String firstsmallmark = "";
	
	private String KeyValue(String key, String value, boolean dot){
		String kv = "\"" + key + "\":\"" + value + "\"";
		if(dot){
			kv += ",";
		}
		return kv;
	}
	public String toJsonString(){
		String json = "{";
		json += this.KeyValue("subjectid", this.subjectid, true);
		json += this.KeyValue("secretid", this.secretid, true);
		json += this.KeyValue("examid", this.examid, true);
		json += this.KeyValue("queid", this.queid, true);
		json += this.KeyValue("flag_send", this.flag_send, true);
		json += this.KeyValue("score", this.score, true);
		json += this.KeyValue("smallscore", this.smallscore, true);
		json += this.KeyValue("usedtime", this.usedtime, true);
		json += this.KeyValue("signid", this.signid, true);
		json += this.KeyValue("comment", this.comment, true);
		json += this.KeyValue("commentimage", this.commentimage, true);
		json += this.KeyValue("invalidscore", this.invalidscore, false);
		json += "}";
		return json;
	}
	public String toArbitrateString(){
		String json = "{";
		json += this.KeyValue("subjectid", this.subjectid, true);
		json += this.KeyValue("secretid", this.secretid, true);
		json += this.KeyValue("examid", this.examid, true);
		json += this.KeyValue("queid", this.queid, false);
		json += "}";
		return json;
	}
}
public class CorrectScoreEditActivity extends Activity {
	MyDialog myDialog;
	//画布参数
	public boolean isMarkBiaozhu = false;
	Bitmap mainBitmap;
	Canvas mainCanvas;
	Bitmap mainFaceBitmp;
	int scrollTop = 0;
	int[] canvasIds = {R.id.canvas_pen_button, R.id.canvas_delete_button, R.id.canvas_dui_button,
			R.id.canvas_bandui_button, R.id.canvas_wrong_button
	};
	int canvasBtnSelectId = -1; //当前那个操作被选中
	boolean isPenOP = false; //是否ing在进行画笔操作
	boolean isDuiOP = false;
	boolean isBanduiOP = false;
	boolean isWrongOP = false;
//	int[] canvasBtnsBg = {R.drawable.canvas_pen, R.drawable.canvas_delete, R.drawable.canvas_dui,
//			R.drawable.canvas_bandui, R.drawable.canvas_wrong
//	};
//	int[] canvasActiveBtnsBg = {R.drawable.canvas_pen_active, R.drawable.canvas_delete, R.drawable.canvas_dui,
//			R.drawable.canvas_bandui, R.drawable.canvas_wrong
//	};
	List<ImageView> canvasOpBtns;
	
	LinearLayout score_panel_back_button;
	//LinearLayout canvas_view;
	private ImageView imageView;  
	private ImageView imageViewFace;
	
	private Bitmap baseBitmap;  
	private TextView quenameView;
	private Canvas canvas;  
	private Paint paint; 
	public MyScrollView scrollView;
	public String imageUrl;
	public String selectedQueID;
	public String selectedQueName = "";
	public String selectedScretid;
	public String TYPE = "0";  //正平=0还是回评=1；
	public RadioButton recordButton, scoreButton, selectButton, zhongcaiButton;
	public TextView scoreShowTextView;
	public LinearLayout fixedScorePabel ,recordPanel, scorePanel, selectPanel;
	public ImgLoadTask imgLoadTask;
	//打分板按钮
	public String full_marks = "0"; //满分
	public String scoreShowText = "";
	public MarkingScoreData markScoreJson;
	//小题
	public Button submitTotalButton;
	public TotalScoreItemListAdapter tsAdapter;
	public ScorePointsItemListAdapter scorePointsAdapter; //默认给分列表
	public GetUserTaskQueInfoResponse.Datas subQueList; //小题数据结构
	public int selectSubQueID = 0;
	public String full_sub_marks = "0" ;//每道小题的最高分
	int[] rids = {R.id.cul_btn_0,R.id.cul_btn_1,R.id.cul_btn_2,R.id.cul_btn_3,R.id.cul_btn_4,
			R.id.cul_btn_5,R.id.cul_btn_6,R.id.cul_btn_7,R.id.cul_btn_8,R.id.cul_btn_9,
			R.id.cul_btn_10,R.id.cul_btn_11,R.id.cul_btn_12,R.id.cul_btn_13,R.id.cul_btn_14
	};
	ImageView imageGif;
	ImageView menuFlipButton;
	Animation rotate;
	LinearLayout menuBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d("YJ", "onCreate func");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct_score_edit);
        score_panel_back_button = (LinearLayout)this.findViewById(R.id.score_panel_back_button);
        scrollView = (MyScrollView)this.findViewById(R.id.canvas_scroll_bar);
        quenameView = (TextView)this.findViewById(R.id.hd_que_name);
        //canvas_view = (LinearLayout)this.findViewById(R.id.canvas_view);
        this.imageView = (ImageView) this.findViewById(R.id.iv);
        this.imageViewFace = (ImageView) this.findViewById(R.id.iv_face);
        
        
        this.recordButton = (RadioButton) this.findViewById(R.id.ct_record_button);
        this.scoreButton = (RadioButton) this.findViewById(R.id.ct_score_button);
        this.selectButton = (RadioButton) this.findViewById(R.id.ct_select_button);
        this.zhongcaiButton = (RadioButton) this.findViewById(R.id.submit_except_button);
        
        this.recordPanel = (LinearLayout) this.findViewById(R.id.ct_record_panel);
        this.scorePanel = (LinearLayout) this.findViewById(R.id.ct_score_panel);
        this.selectPanel = (LinearLayout) this.findViewById(R.id.ct_select_panel);
        this.scoreShowTextView = (TextView) this.findViewById(R.id.cul_score_text);
        
        this.submitTotalButton = (Button) this.findViewById(R.id.submit_sub_total_score_button);
        
        //默认三个面板都不显示
        this.recordPanel.setVisibility(View.INVISIBLE);
        this.scorePanel.setVisibility(View.INVISIBLE);
        this.selectPanel.setVisibility(View.INVISIBLE);
        this.submitTotalButton.setVisibility(View.INVISIBLE);
        markScoreJson = new MarkingScoreData();
        
        imageGif = (ImageView)this.findViewById(R.id.loading_image);
        
        
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);  
        LinearInterpolator lin = new LinearInterpolator();  //setInterpolator表示设置旋转速率。LinearInterpolator为匀速效果，
        rotate.setInterpolator(lin);
        
        
        canvasOpBtns = new ArrayList();
        for(int i=0;i<canvasIds.length;i++){
        	canvasOpBtns.add((ImageView)this.findViewById(canvasIds[i]));
        }
        //设置保存图片按钮不可见 debug
    	((Button)this.findViewById(R.id.save_image_button)).setVisibility(View.GONE);
    	
    	//左侧菜单
    	menuBar = (LinearLayout)this.findViewById(R.id.menu_bar);
    	menuFlipButton = (ImageView)this.findViewById(R.id.menu_flip_button);
    	menuFlipButton.setSelected(false);
    	menuFlipButton.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//AnimatorUtil.animHeightToView(this, menuBar, true, 200);
				Log.v("YJ","menuFlipButton click");
				if(menuBar.getVisibility() == 4){ //invisible
					menuBar.setVisibility(View.VISIBLE);
					menuFlipButton.setSelected(false);
				}else if(menuBar.getVisibility() == 0){
					menuBar.setVisibility(View.INVISIBLE);
					menuFlipButton.setSelected(true);
				}
			}
        });
    	
        this.scrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
			
			@Override
			public void onScroll(int scrollY) {
				// TODO Auto-generated method stub
				Log.v("YJ", "Y = " + String.valueOf(scrollY));
				scrollTop = scrollY;
			}
		});
        //get params queid
        try{
        	Intent intent = getIntent();
        	TYPE = intent.getStringExtra("type");
        	if("0".equals(TYPE)){
        		selectedQueID = intent.getStringExtra("queid");
            	selectedQueName = intent.getStringExtra("quename");
            	quenameView.setText(this.selectedQueName);
                Log.v("YJ queid ", selectedQueID);
                this.selectButton.setVisibility(View.VISIBLE);
                
              //显示给分点列表切换题目的时候需要更新
    			this.getGetUserTaskQueInfoFromService(); 
    			//加载对应的图片
    			this.getExamTaskListFromService();
    			
        	}else {
        		selectedScretid =  intent.getStringExtra("secretid");
        		selectedQueName = intent.getStringExtra("quename");
        		quenameView.setText(this.selectedQueName);
        		this.zhongcaiButton.setVisibility(View.INVISIBLE);
        		Log.v("YJ secretid ", selectedScretid);
        		this.selectButton.setVisibility(View.GONE);
        		//回评信息
        		this.getAlreadyMarkBySecretidFromService();
        	}
        	
        }catch(Exception e){
        	e.printStackTrace();
        }
        
        this.initView();
    }
    public void setCanvasButtonsVisible(int index, boolean vis){
    	for(int i=0;i<canvasIds.length;i++){
    		if(i == index){
    			if(canvasBtnSelectId == -1){ //都没有选中，第一次
    				canvasBtnSelectId = i;
    				canvasOpBtns.get(i).setSelected(true);
    				
    			}else{
    				if(i == canvasBtnSelectId){ //点击被选中的按钮，变为不选中
    					canvasOpBtns.get(i).setSelected(false);
    					canvasBtnSelectId = -1;
    				}else{
    					canvasOpBtns.get(i).setSelected(true); //否则选中
    					canvasBtnSelectId = i;
    				}	
    			}
    			
    			if(canvasBtnSelectId == 0){
        			isPenOP = true;
        		}else{
        			isPenOP = false; 
        		}
        		isDuiOP = canvasBtnSelectId == 2 ? true: false;
        		isBanduiOP = canvasBtnSelectId == 3 ? true: false;
        		isWrongOP = canvasBtnSelectId == 4 ? true: false;
        		
    		}else{
    			canvasOpBtns.get(i).setSelected(false);
    		}
    		
    		
        	//canvasOpBtns.add((ImageView)this.findViewById(canvasIds[i]));
        }
    	if(index == 1){//清空画布
			Log.v("YJ","清空画布");
			//
			//通知
    		myDialog=new MyDialog(CorrectScoreEditActivity.this,R.style.MyDialog, "YES_NO");
            myDialog.setTitle("警告！");
            myDialog.setMessage("确定要清空所有标注吗?");
            myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                @Override
                public void onYesOnclick() {
                	//...To-do
	            	 Paint p = new Paint();
	   		        //清屏
	   		        p.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
	   		        mainCanvas.drawPaint(p);
	   		        //mainCanvas.drawARGB(0,250,20,20);
	    			imageViewFace.invalidate();
	    			isMarkBiaozhu = false;
                    myDialog.dismiss();
                }
            });
            myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                @Override
                public void onNoClick() {
                    
                    myDialog.dismiss();
                }
            });
            myDialog.show();
            
	        canvasOpBtns.get(index).setSelected(false);
			
		}
    }
    public void setVisibleRecord(boolean vis){
    	if(vis){
    		this.recordPanel.setVisibility(View.VISIBLE);
    		this.recordButton.setTextColor(android.graphics.Color.RED);
    		this.setVisibleScore(false);
    		this.setVisibleSelect(false);
    	}else{
    		this.recordPanel.setVisibility(View.INVISIBLE);
    		this.recordButton.setTextColor(android.graphics.Color.WHITE);
    	}
    }
    public void setVisibleScore(boolean vis){
    	if(vis){
    		this.scorePanel.setVisibility(View.VISIBLE);
    		this.scoreButton.setTextColor(android.graphics.Color.RED);
    		this.setVisibleRecord(false);
    		this.setVisibleSelect(false);
    	}else{
    		this.scorePanel.setVisibility(View.INVISIBLE);
    		this.scoreButton.setTextColor(android.graphics.Color.WHITE);
    	}
    }
    public void setVisibleSelect(boolean vis){
    	if(vis){
    		this.selectPanel.setVisibility(View.VISIBLE);
    		this.selectButton.setTextColor(android.graphics.Color.RED);
    		this.setVisibleRecord(false);
    		this.setVisibleScore(false);
    	}else{
    		this.selectPanel.setVisibility(View.INVISIBLE);
    		this.selectButton.setTextColor(android.graphics.Color.WHITE);
    	}
    }
	public void initView() {
		//this.getQueTaskListFromService();
		//this.getExamTaskListFromService();
		//this.getAlreadyMarkListFromService();
		//this.getSelectQuestionListFromService();
		//this.getTetstPaperSignFromService(); //试卷标志信息暂时用不上

		//imgLoadTask=new ImgLoadTask(imageView);
		String url1 = "https://img.ivsky.com/img/tupian/pre/201809/30/haian-001.jpg";
		String url2 = "http://222.186.12.239:10010/csyej_20190314/001.jpg";
		String url3 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1554468075583&di=88836cfbcabd1cfebd6dd6ec46dab780&imgtype=0&src=http%3A%2F%2Fs7.sinaimg.cn%2Fmiddle%2F4b83b92dgbdf0991fe946%26690";
        //imgLoadTask.execute(url1);//execute里面是图片的地址

	}
	public String bitmapToBase64(Bitmap bitmap){
		String result = "";  
	    ByteArrayOutputStream baos = null;  
	    try {  
	        if (bitmap != null) {  
	            baos = new ByteArrayOutputStream();  
	            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  
	  
	            baos.flush();  
	            baos.close();  
	  
	            byte[] bitmapBytes = baos.toByteArray(); 
	            result += Base64.encodeToString(bitmapBytes, Base64.DEFAULT);  
	            result = result.replaceAll("[\\s*\t\n\r]", "");

	        }  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally {  
	        try {  
	            if (baos != null) {  
	                baos.flush();  
	                baos.close();  
	            }  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	    }  
	    return result; 
	}
    public void savePngImage(Bitmap mBitmap) {  
    	  try {  
		    Public pub = (Public)this.getApplication();
			File file = new File(pub.cachePath +"hah.png");
			OutputStream stream = new FileOutputStream(file);
			
			mBitmap.compress(CompressFormat.PNG, 100, stream);
			stream.close();
//			// 模拟一个广播，通知系统sdcard被挂载
//			Intent intent = new Intent();
//			intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
//			intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
//			sendBroadcast(intent);
			Log.v("YJ","保存图片成功");
		} catch (Exception e) {
			//Toast.makeText(this, "保存图片失败", 0).show();
			Log.v("YJ","保存图片失败");
			e.printStackTrace();
		}
    }  

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void renderSelectQueList(List<MarkingListResponse.Datas> itemsList){
    	List<SelectQuestionItemInfo> listInfo = new ArrayList();
    	Log.v("YJ","选择题目tab list表");
    	
    	for(int i=0;i<itemsList.size();i++){
    		MarkingListResponse.Datas data = itemsList.get(i);
    		SelectQuestionItemInfo item = new SelectQuestionItemInfo();
    		item.queid = data.queid;
    		item.quename = data.quename;
    		listInfo.add(item);
    	}

    	
    	SelectQuestionItemListAdapter qtAdapter = new SelectQuestionItemListAdapter(Public.context, listInfo, this.selectedQueID, new ItemClickInterfaceListener(){
    		public void Callback(SelectQuestionItemInfo itemInfo){
//    			CorrectScoreEditActivity.this.selectedQueID = itemInfo.queid;
//    			CorrectScoreEditActivity.this.selectedQueName = itemInfo.quename;
//    			CorrectScoreEditActivity.this.quenameView.setText(itemInfo.quename);
//    			//更新需要阅卷的信息包括图片什么的
//    			CorrectScoreEditActivity.this.setVisibleSelect(false);
//    			CorrectScoreEditActivity.this.getExamTaskListFromService();
//    			
//    			//更新 显示给分点列表
//            	CorrectScoreEditActivity.this.getGetUserTaskQueInfoFromService();
    			//TODO 局部刷新太他么麻烦了，还是重新跳转该页面吧，日
    			Intent intent =new Intent(CorrectScoreEditActivity.this, CorrectScoreEditActivity.class);
				intent.putExtra("queid", itemInfo.queid);
				intent.putExtra("quename", itemInfo.quename);
				intent.putExtra("type", "0"); //正平
            	startActivity(intent);
    		}
    	});

    	ListView theListView = (ListView)findViewById(R.id.select_question_list_view);
    	
    	theListView.setAdapter(qtAdapter);
    }
    //渲染出小题给分编辑框列表。左侧
    public void loadTotalScoreList(GetUserTaskQueInfoResponse.Datas subQueData){
    	List<GetUserTaskQueInfoResponse.SmallQueInfo> subqueArr = subQueData.smallqueinfoList;
    	
    	
    	List<TotalScoreItemInfo> listInfo = new ArrayList();
    	for(int i=0;i<subqueArr.size();i++){
    		GetUserTaskQueInfoResponse.SmallQueInfo subItem = subqueArr.get(i);
    		TotalScoreItemInfo item = new TotalScoreItemInfo();
    		item.smallqueid = subItem.smallqueid;
    		item.quenum = subItem.smallquename;
    		item.smallfullmark = subItem.smallfullmark;
        	item.quescore = "";
        	item.index = i;
        	Log.v("YJ >>>",item.quenum);
    		listInfo.add(item);	
    	}
    	
    	if("1".equals(TYPE)){
    		//回评
    		Log.v("YJ "," 回评初始化分分数");
    		if(markScoreJson.hasSubQuestion){ //有小题，初始化左边分数
        		((TextView)this.findViewById(R.id.total_score_text_view)).setText(markScoreJson.firstmark);
        		//无小题
        		Log.v("YJ >>>>", markScoreJson.firstsmallmark);
        		if(markScoreJson.firstsmallmark != null){
        			List<String> scoresList = this.getScorePoints(markScoreJson.firstsmallmark);
            		for(int i=0;i<scoresList.size();i++){
            			if(listInfo.get(i)!=null){
            				listInfo.get(i).quescore = scoresList.get(i);	
            			}
            			
            		}
        		}
        		
        	}else{
        		Log.v("YJ "," 回评，没有小题，该题分数" + markScoreJson.firstmark);
        		((TextView)this.findViewById(R.id.total_score_text_view)).setText(markScoreJson.firstmark);
        	}
    	}
    	Log.v("YJ","loadTotalScoreList()");
    	

    	tsAdapter = new TotalScoreItemListAdapter(Public.context, listInfo, subQueData.queid, new TotalScoreItemListAdapter.ItemClickInterfaceListener() {
			
			@Override
			public void Callback(TotalScoreItemInfo itemInfo) {
				// TODO Auto-generated method stub
				Log.v("YJ smallqueid", "smallqueid = "+itemInfo.smallqueid);
				CorrectScoreEditActivity.this.selectSubQueID = itemInfo.index;
				List<GetUserTaskQueInfoResponse.SmallQueInfo> subqueArr = CorrectScoreEditActivity.this.subQueList.smallqueinfoList;
				for(int i=0;i<subqueArr.size();i++){
		    		GetUserTaskQueInfoResponse.SmallQueInfo subItem = subqueArr.get(i);
		    		if(subItem.smallqueid.equals(itemInfo.smallqueid)){
		    			CorrectScoreEditActivity.this.tsAdapter.setSelectedPosition(i);
		    			//CorrectScoreEditActivity.this.tsAdapter.notifyDataSetInvalidated();
		    			//List<String> scorePointsList = CorrectScoreEditActivity.this.getScorePoints(subItem.smallscorepoints);
		    			//CorrectScoreEditActivity.this.loadGivePointsList(scorePointsList);
		    			List<ScorePointsItemInfo> listInfo = getScorePointsList(subItem.smallscorepoints);
		    			scorePointsAdapter.updateItemList(listInfo);
		    			//更新打分适配器
		    			break;
		    		}
		    	}				
			}
		});

    	ListView QueTaskListView = (ListView)findViewById(R.id.total_score_list_view_1);
    	
    	QueTaskListView.setAdapter(this.tsAdapter);
    }

    //获取小题给分点
    public String getSmallquescorepoints(int index){
    	List<GetUserTaskQueInfoResponse.SmallQueInfo> smalllist = this.subQueList.smallqueinfoList;
    	for(int i=0;i<smalllist.size();i++){
    		if(index == i){
    			return smalllist.get(i).smallscorepoints;
    		}
    	}
    	return "";
    }
    //把给分点解析成列表适配器数据
    public List<ScorePointsItemInfo> getScorePointsList(String str){
    	List<String> scorePointsList = this.getScorePoints(str);
    	List<ScorePointsItemInfo> listInfo = new ArrayList();
    	for(int i=0;i<scorePointsList.size();i++){
    		
    		ScorePointsItemInfo item = new ScorePointsItemInfo();
        	item.score = scorePointsList.get(i);
        	//Log.v("YJ",item.score);
    		listInfo.add(item);	
    	}
    	return listInfo;
    }
    public void renderGivePointsList(List<GetUserTaskQueInfoResponse.Datas> itemsList){
    	
    	Log.v("YJ","先取到所有列表，然后获取当前题目给分信息，开始渲染打分表");
    	
    	List<ScorePointsItemInfo> listInfo = new ArrayList();
    	for(int i=0;i<itemsList.size();i++){
    		GetUserTaskQueInfoResponse.Datas data = itemsList.get(i);
    		Log.v("YJ queid",data.queid);
    		if(this.selectedQueID.equals(data.queid)){
    			
    			if(data.smallqueinfoList.size() == 0) //没有小题的情况
    			{
    				this.full_marks = data.fullmark; //满分
    				Log.v("YJ","没有小题的情况");
    				this.markScoreJson.hasSubQuestion = false;
    				
    				listInfo = this.getScorePointsList(data.scorepoints);
    				((TextView)this.findViewById(R.id.total_score_text_view)).setText(markScoreJson.firstmark);
    			}else{
    				Log.v("YJ","有小题的情况");
    				this.markScoreJson.hasSubQuestion = true;
    				this.subQueList = data;   //very very important
    				if("1".equals(TYPE)){
    					//回评如果有小题显示提交按钮
        				submitTotalButton.setVisibility(View.VISIBLE);
    				}
    				
    				//默认给出第一个给分点
    				if(data.smallqueinfoList.size()>0){
    					
    					this.selectSubQueID = 0;
    					listInfo = this.getScorePointsList(this.getSmallquescorepoints(0));
    					
    				}
    				this.loadTotalScoreList(this.subQueList); //左侧总分列表
    			}
    			break;
    		}
    	}
    	
    	//this.loadGivePointsList(scorePointsList);
    	Log.v("YJ","getBackMarkList()");
    	
    	scorePointsAdapter = new ScorePointsItemListAdapter(Public.context, listInfo, new ScorePointsItemListAdapter.ItemClickInterfaceListener() {
			
			@Override
			public void Callback(ScorePointsItemInfo itemInfo) {
				// TODO Auto-generated method stub
				Log.v("YJ score", itemInfo.score);
				if("0".equals(TYPE)){
					//提交正评分数
					if(markScoreJson.hasSubQuestion){
						Log.v("YJ","提交正评分数，有小题");
						//把分数 存在左侧框框 并计算 总分；
						tsAdapter.setScoreByPosition(selectSubQueID,itemInfo.score);
						selectSubQueID ++;
						
						tsAdapter.setSelectedPosition(selectSubQueID);
						String total_score = tsAdapter.getTotalScore();
						((TextView)CorrectScoreEditActivity.this.findViewById(R.id.total_score_text_view)).setText(total_score);
						if(selectSubQueID >= tsAdapter.getCount()){
							Log.v("YJ","submit haha");
						}else{
							//------------------------更新打分适配器
							List<ScorePointsItemInfo> listInfo = getScorePointsList(getSmallquescorepoints(selectSubQueID));
			    			scorePointsAdapter.updateItemList(listInfo);
			    			//------------------------
						}
						if(tsAdapter.getAll()){
							submitTotalButton.setVisibility(View.VISIBLE);
						}else{
							submitTotalButton.setVisibility(View.GONE);
						}
						
					}else{
						saveMarkingScore(itemInfo.score, "");
					}
				}else{
					//提交回评分数
					if(CorrectScoreEditActivity.this.markScoreJson.hasSubQuestion){
						Log.v("YJ","提交回评分数，有小题");
						//把分数 存在左侧框框 并计算 总分；
						tsAdapter.setScoreByPosition(selectSubQueID,itemInfo.score);
						selectSubQueID ++;
						tsAdapter.setSelectedPosition(selectSubQueID);
						String total_score = tsAdapter.getTotalScore();
						((TextView)CorrectScoreEditActivity.this.findViewById(R.id.total_score_text_view)).setText(total_score);
						if(selectSubQueID >= tsAdapter.getCount()){
							Log.v("YJ","submit haha");
						}else{
							//------------------------更新打分适配器
							List<ScorePointsItemInfo> listInfo = getScorePointsList(getSmallquescorepoints(selectSubQueID));
			    			scorePointsAdapter.updateItemList(listInfo);
			    			//------------------------
						}
						if(tsAdapter.getAll()){
							submitTotalButton.setVisibility(View.VISIBLE);
						}else{
							submitTotalButton.setVisibility(View.GONE);
						}
						
					}else{
						CorrectScoreEditActivity.this.saveAlreadyMarkScore(itemInfo.score, "");
					}
				}
				
				
			}
		});

    	ListView QueTaskListView = (ListView)findViewById(R.id.score_points_list_view);
    	
    	QueTaskListView.setAdapter(scorePointsAdapter);
    	
    }
    public void renderCorrectScoreItemList(List<AlreadyMarkListResponse.Datas> itemsList){
    	List<CorrectRecordItemInfo> listInfo = new ArrayList();
    	Log.v("YJ","开始阅卷记录list表");
    	
    	for(int i=0;i<itemsList.size();i++){
    		AlreadyMarkListResponse.Datas data = itemsList.get(i);
    		CorrectRecordItemInfo item = new CorrectRecordItemInfo();
    		item.order = String.valueOf(i+1);
    		item.score = data.firstmark;
    		item.secretid = data.secretid;
    		item.quename = data.quename;
    		listInfo.add(item);
    	}

    	Log.v("YJ","getBackMarkList()");
    	
    	CorrectRecordItemListAdapter qtAdapter = new CorrectRecordItemListAdapter(Public.context, listInfo, new CorrectRecordItemListAdapter.ItemClickInterfaceListener(){
    		public void Callback(CorrectRecordItemInfo itemInfo){
    			Intent intent =new Intent(CorrectScoreEditActivity.this, CorrectScoreEditActivity.class);
				intent.putExtra("secretid", itemInfo.secretid);
				intent.putExtra("quename", itemInfo.quename);
				intent.putExtra("type", "1"); //回评
            	startActivity(intent);
    		}
    	});

    	ListView theListView = (ListView)findViewById(R.id.correct_record_list_view);
    	
    	theListView.setAdapter(qtAdapter);
    }
    //获取题目id，name,满分，给分点scorepoints 小题信息 //所有可批阅的题目信息
    public void getGetUserTaskQueInfoFromService(){
    	Public pub = (Public)this.getApplication();
       
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("arg0", pub.userid);
        properties.put("arg1", pub.token);
        properties.put("arg2", pub.usersubjectid);
        
        WebServiceUtil.callWebService(WebServiceUtil.getURL(), "GetUsertaskqueinfo", properties, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                	Log.v("YJ","getGetUserTaskQueInfoFromService");
                	Log.v("YJ",result);
                    
                    GetUserTaskQueInfoResponse reponse = new GetUserTaskQueInfoResponse(result);
                    if("0001".equals(reponse.getCodeID())){
                    	
                    	List<GetUserTaskQueInfoResponse.Datas> itemsList = reponse.dataList;
                    	CorrectScoreEditActivity.this.renderGivePointsList(itemsList);
                       
                    }else if("0002".equals(reponse.getCodeID())){
                    	Intent intent =new Intent(CorrectScoreEditActivity.this, LoginActivity.class);
                    	startActivity(intent);
                    }else if("0003".equals(reponse.getCodeID())){
                    	Toast.makeText(CorrectScoreEditActivity.this, "服务器数据异常", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    //选题数据获取 选题标签
    public void getSelectQuestionListFromService(){
    	Public pub = (Public)this.getApplication();
        
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("arg0", pub.userid);
        properties.put("arg1", pub.token);
        properties.put("arg2", pub.usersubjectid);
        
        Log.v("YJ", pub.token); //
        WebServiceUtil.callWebService(WebServiceUtil.getURL(), "GetWorkprogress", properties, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    Log.v("YJ",result);
                    
                    MarkingListResponse reponse = new MarkingListResponse(result);
                    if("0001".equals(reponse.getCodeID())){
                    	List<MarkingListResponse.Datas> itemsList = reponse.dataList;
                    	//TODO
                    	CorrectScoreEditActivity.this.renderSelectQueList(itemsList);
                       
                    }else if("0002".equals(reponse.getCodeID())){
                    	//Toast.makeText(AlreadyMarkActivity.this, reponse.getMessage(), Toast.LENGTH_SHORT).show();
                    	Intent intent =new Intent(CorrectScoreEditActivity.this, LoginActivity.class);
                    	startActivity(intent);
                    }else{
                    	Toast.makeText(CorrectScoreEditActivity.this, reponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    //保存回评分数
    
     public void saveAlreadyMarkScore(String score, String smallscore){
    	((TextView)this.findViewById(R.id.total_score_text_view)).setText(score);
    	String scoreJson = "";
    	this.markScoreJson.endTime = System.currentTimeMillis();
    	this.markScoreJson.usedtime = String.valueOf(this.markScoreJson.endTime - this.markScoreJson.startTime);
		this.markScoreJson.score = score;
		this.markScoreJson.smallscore = smallscore;
		if(isMarkBiaozhu == true){
			this.markScoreJson.commentimage = this.bitmapToBase64(this.mainFaceBitmp);	
		}else{
			this.markScoreJson.commentimage = "";
		}
		//this.markScoreJson.commentimage = this.bitmapToBase64(this.mainFaceBitmp);
		scoreJson = this.markScoreJson.toJsonString();
		Log.v("YJ save result", scoreJson);
		
    	Public pub = (Public)this.getApplication();
        
        
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("arg0", pub.userid);
        properties.put("arg1", pub.token);
        properties.put("arg2", scoreJson);
        
        Log.v("YJ", pub.token); //
        WebServiceUtil.callWebService(WebServiceUtil.getURL(), "SaveAlreadmarknewscore", properties, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                	Log.v("YJ","保存回评分数");
                    Log.v("YJ",result);
                    
                    SaveMarkingScoreResponse reponse = new SaveMarkingScoreResponse(result);
                    if("0001".equals(reponse.getCodeID())){
                    	//List<MarkingListResponse.Datas> itemsList = reponse.dataList;
                    	//TODO
                    	//CorrectScoreEditActivity.this.renderSelectQueList(itemsList);
                    	
                		//加载对应的图片
                    	//CorrectScoreEditActivity.this.getExamTaskListFromService();
                    	//Toast.makeText(CorrectScoreEditActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    	//通知
                		myDialog=new MyDialog(CorrectScoreEditActivity.this,R.style.MyDialog, "YES");
                        myDialog.setTitle("温馨提示！");
                        myDialog.setMessage("分数修改成功，请回评下一题或者继续批阅！");
                        myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                            @Override
                            public void onYesOnclick() {
                            	
                                myDialog.dismiss();
                                Intent intent =new Intent(CorrectScoreEditActivity.this, AlreadyMarkActivity.class);
                				startActivity(intent);
                            }
                        });
                        
                        myDialog.show();
                        
                    }else if("0002".equals(reponse.getCodeID())){
                    	//Toast.makeText(AlreadyMarkActivity.this, reponse.getMessage(), Toast.LENGTH_SHORT).show();
                    	Intent intent =new Intent(CorrectScoreEditActivity.this, LoginActivity.class);
                    	startActivity(intent);
                    }else{
                    	Toast.makeText(CorrectScoreEditActivity.this, reponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    } 
     
    //保存正评分数
    /*
     * 
	     注：提交的score参数：需采用json字符串格式，需要包括的数据字段如下
	String subjectid     试卷编号
	String secretid      密号
	String examid        考号
	String queid         题目编号
	String flag_send     当前评阅模式
	String score         总分
	String smallscore    小题分(各小题分用逗号隔开)
	String usedtime      批阅所用时间
	String signid        标志试卷编号
	String comment       批注信息
	String commentimage  批注图片
     */
    public void saveMarkingScore(String score, String smallscore){
    	((TextView)this.findViewById(R.id.total_score_text_view)).setText(score);
    	String scoreJson = "";
    	this.markScoreJson.endTime = System.currentTimeMillis();
    	this.markScoreJson.usedtime = String.valueOf(this.markScoreJson.endTime - this.markScoreJson.startTime);
		this.markScoreJson.score = score;
		this.markScoreJson.smallscore = smallscore;
		
		//File f=new File(R.drawable.testbase64);
		if(false){
			InputStream inputStream = getResources().openRawResource(R.raw.testbase64);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			String line,imagebase64="";
			try {
				while ((line = reader.readLine()) != null) {
					imagebase64 += line;
				}
				inputStream.close();
				inputStreamReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			

			this.markScoreJson.commentimage = imagebase64;
		}else{
			if(isMarkBiaozhu == true){
				this.markScoreJson.commentimage = this.bitmapToBase64(this.mainFaceBitmp);	
			}else{
				this.markScoreJson.commentimage = "";
			}
			
		}
		
		scoreJson = this.markScoreJson.toJsonString();
		Log.v("YJ save result", scoreJson);
		
    	Public pub = (Public)this.getApplication();
        
        
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("arg0", pub.userid);
        properties.put("arg1", pub.token);
        properties.put("arg2", scoreJson);
        
        Log.v("YJ", pub.token); //
        WebServiceUtil.callWebService(WebServiceUtil.getURL(), "SaveNormalScore", properties, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    Log.v("YJ",result);
                    
                    SaveMarkingScoreResponse reponse = new SaveMarkingScoreResponse(result);
                    if("0001".equals(reponse.getCodeID())){
                    	//List<MarkingListResponse.Datas> itemsList = reponse.dataList;
                    	//TODO
                    	//CorrectScoreEditActivity.this.renderSelectQueList(itemsList);
                    	if(markScoreJson.hasSubQuestion){
                    		//如果有小题
                    		tsAdapter.ClearData();
                    		tsAdapter.setSelectedPosition(0);
                    		selectSubQueID = 0;
                    		submitTotalButton.setVisibility(View.GONE);
                    		
                    		Log.v("YJ","有小题，清理并继续加载下一个任务");
                    	}else{
                    		Log.v("YJ","没有小题，继续加载下一个任务");
                    	}
                		//加载对应的图片
                    	CorrectScoreEditActivity.this.getExamTaskListFromService();

                    }else if("0002".equals(reponse.getCodeID())){
                    	//Toast.makeText(AlreadyMarkActivity.this, reponse.getMessage(), Toast.LENGTH_SHORT).show();
                    	Intent intent =new Intent(CorrectScoreEditActivity.this, LoginActivity.class);
                    	startActivity(intent);
                    }else{
                    	Toast.makeText(CorrectScoreEditActivity.this, "0x22"+reponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    //获取标志试卷类别信息
    public void getTetstPaperSignFromService(){

        HashMap<String, String> properties = new HashMap<String, String>();
        WebServiceUtil.callWebService(WebServiceUtil.getURL(), "GetTestpaperSign", properties, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                	Log.v("YJ","获取标志试卷类别信息getTetstPaperSignFromService");
                    Log.v("YJ",result);
                    
                    GetTestPaperSignResponse reponse = new GetTestPaperSignResponse(result);
                    if("0001".equals(reponse.getCodeID())){
                    	List<GetTestPaperSignResponse.Datas> itemsList = reponse.dataList;
                    	//TODO
                    	//CorrectScoreEditActivity.this.renderSelectQueList(itemsList);
                       
                    }else{
                    	Toast.makeText(CorrectScoreEditActivity.this, reponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
   //获取已评接口，用于阅卷记录显示
    public void getAlreadyMarkListFromService(){
    	Public pub = (Public)this.getApplication();
    	String userid = pub.getUserID();
    	String token = pub.getToken();
    	String subjectid = pub.usersubjectid;
       
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("arg0", userid);
        properties.put("arg1", token);
        properties.put("arg2", subjectid);
        //properties.put("arg3", "0");
        //properties.put("arg4", "");
        
        WebServiceUtil.callWebService(WebServiceUtil.getURL(), "GetAlreadymarklist", properties, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    Log.v("YJX",result);
                    AlreadyMarkListResponse reponse = new AlreadyMarkListResponse(result);
                    if("0001".equals(reponse.getCodeID())){
                    	List<AlreadyMarkListResponse.Datas> itemsList = reponse.dataList;
                    	CorrectScoreEditActivity.this.renderCorrectScoreItemList(reponse.dataList);
                    }else if("0002".equals(reponse.getCodeID())){
                    	//Toast.makeText(AlreadyMarkActivity.this, reponse.getMessage(), Toast.LENGTH_SHORT).show();
                    	Intent intent =new Intent(CorrectScoreEditActivity.this, LoginActivity.class);
                    	startActivity(intent);
                    }else{
                    	Toast.makeText(CorrectScoreEditActivity.this, reponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    //获取指定密号试卷的已评数据 功能：根据用户名、令牌、科目ID、试卷密号 获取信息
    public void getAlreadyMarkBySecretidFromService(){
    	Public pub = (Public)this.getApplication();
    	String userid = pub.getUserID();
    	String token = pub.getToken();
    	String subjectid = pub.usersubjectid;
    	
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("arg0", userid);
        properties.put("arg1", token);
        properties.put("arg2", subjectid);
        properties.put("arg3", this.selectedScretid);
        //properties.put("arg4", "");
        imageGif.startAnimation(rotate);
        imageGif.setVisibility(View.VISIBLE);
        WebServiceUtil.callWebService(WebServiceUtil.getURL(), "GetAlreadmarkinfo", properties, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                	Log.v("YJ","GetAlreadmarkinfo获取回评信息");
                    Log.v("YJ",result);
                    GetAlreadyMarkInfoResponse reponse = new GetAlreadyMarkInfoResponse(result);
                    if("0001".equals(reponse.getCodeID())){
                    	List<GetAlreadyMarkInfoResponse.Datas> itemsList = reponse.dataList;
                    	//CorrectScoreEditActivity.this.renderCorrectScoreItemList(reponse.dataList);
                    	if(itemsList.size()>0){
                    		GetAlreadyMarkInfoResponse.Datas data = itemsList.get(0);
                    		//设置当前批阅模式
                    		CorrectScoreEditActivity.this.markScoreJson.subjectid = Public.usersubjectid;
                    		CorrectScoreEditActivity.this.markScoreJson.flag_send = data.flag_send;
                    		CorrectScoreEditActivity.this.markScoreJson.examid = data.examid;
                    		CorrectScoreEditActivity.this.markScoreJson.secretid = data.secretid;
                    		CorrectScoreEditActivity.this.markScoreJson.queid =  data.queid;
                    		CorrectScoreEditActivity.this.markScoreJson.startTime = System.currentTimeMillis();
                    		CorrectScoreEditActivity.this.markScoreJson.signid = "0";
                    		CorrectScoreEditActivity.this.markScoreJson.comment = "";
                    		CorrectScoreEditActivity.this.markScoreJson.commentimage = data.commentimage; //显示已有标注过则为空
                    		CorrectScoreEditActivity.this.markScoreJson.invalidscore = data.firstmark;
                    		CorrectScoreEditActivity.this.markScoreJson.firstsmallmark = data.firstsmallmark;
                    		CorrectScoreEditActivity.this.markScoreJson.firstmark = data.firstmark;
                    		String url = Public.imageUrl(data.imgurl);
                    		ImgLoadTask imgLoadTask=new ImgLoadTask(imageView);
                    		imgLoadTask.execute(url);//execute里面是图片的地址
                    		CorrectScoreEditActivity.this.selectedQueID = data.queid;
                    		CorrectScoreEditActivity.this.getGetUserTaskQueInfoFromService(); //选题目
                    		//总分设为0
                    		((TextView)CorrectScoreEditActivity.this.findViewById(R.id.total_score_text_view)).setText("0");
                    	}
                    	
                    }else if("0002".equals(reponse.getCodeID())){
                    	//Toast.makeText(AlreadyMarkActivity.this, reponse.getMessage(), Toast.LENGTH_SHORT).show();
                    	Intent intent =new Intent(CorrectScoreEditActivity.this, LoginActivity.class);
                    	startActivity(intent);
                    }else{
                    	Toast.makeText(CorrectScoreEditActivity.this, reponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    //获取选定题目的学生试卷信息，功能：根据用户名、令牌、科目ID、题目ID 获取信息
    //每次选取阅卷任务的第一张
    public void getExamTaskListFromService(){
    	Public pub = (Public)this.getApplication();
    	String userid = pub.getUserID();
    	String token = pub.getToken();
    	String subjectid = pub.usersubjectid;
    	String queid = this.selectedQueID;
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("arg0", userid);
        properties.put("arg1", token);
        properties.put("arg2", subjectid);
        properties.put("arg3", queid);
        //properties.put("arg4", "");
        imageGif.startAnimation(rotate);
        imageGif.setVisibility(View.VISIBLE);
        WebServiceUtil.callWebService(WebServiceUtil.getURL(), "GetExamtaskinfo", properties, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                	Log.v("YJ","GetExamTaskInfo");
                    Log.v("YJ",result);
                    GetExamTaskInfoResponse reponse = new GetExamTaskInfoResponse(result);
                    if("0001".equals(reponse.getCodeID())){
                    	List<GetExamTaskInfoResponse.Datas> itemsList = reponse.dataList;
                    	//CorrectScoreEditActivity.this.renderCorrectScoreItemList(reponse.dataList);
                    	if(itemsList.size()>0){
                    		GetExamTaskInfoResponse.Datas data = itemsList.get(0);
                    		//设置当前批阅模式
                    		CorrectScoreEditActivity.this.markScoreJson.subjectid = Public.usersubjectid;
                    		CorrectScoreEditActivity.this.markScoreJson.flag_send = data.flag_send;
                    		CorrectScoreEditActivity.this.markScoreJson.examid = data.examid;
                    		CorrectScoreEditActivity.this.markScoreJson.secretid = data.secretid;
                    		CorrectScoreEditActivity.this.markScoreJson.queid =  CorrectScoreEditActivity.this.selectedQueID;
                    		CorrectScoreEditActivity.this.markScoreJson.startTime = System.currentTimeMillis();
                    		CorrectScoreEditActivity.this.markScoreJson.signid = "0";
                    		CorrectScoreEditActivity.this.markScoreJson.comment = "";
                    		CorrectScoreEditActivity.this.markScoreJson.commentimage = ""; //没有标注过则为空
                    		String url = Public.imageUrl(data.imgurl);
                    		ImgLoadTask imgLoadTask=new ImgLoadTask(imageView);
                    		imgLoadTask.execute(url);//execute里面是图片的地址
                    		//总分设为0
                    		((TextView)CorrectScoreEditActivity.this.findViewById(R.id.total_score_text_view)).setText("0");
                    	}else{
                    		//通知
                    		myDialog=new MyDialog(CorrectScoreEditActivity.this,R.style.MyDialog, "YES");
                            myDialog.setTitle("警告！");
                            myDialog.setMessage("没有试卷信息，请确定返回！");
                            myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                                @Override
                                public void onYesOnclick() {
                                	Intent intent;
                                	if(Public.isMarkingActivity == 1){
                                		intent =new Intent(CorrectScoreEditActivity.this, MarkingActivity.class);
                                    	startActivity(intent);
                                    	//overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                                	}else{
                                		intent =new Intent(CorrectScoreEditActivity.this, AlreadyMarkActivity.class);
                        				startActivity(intent);
                        				//overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                                	}
                                    myDialog.dismiss();
                                }
                            });
                            myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                                @Override
                                public void onNoClick() {
                                    
                                    myDialog.dismiss();
                                }
                            });
                            myDialog.show();
                              

                    	}
                    	
                    }else if("0002".equals(reponse.getCodeID())){
                    	//Toast.makeText(AlreadyMarkActivity.this, reponse.getMessage(), Toast.LENGTH_SHORT).show();
                    	Intent intent =new Intent(CorrectScoreEditActivity.this, LoginActivity.class);
                    	startActivity(intent);
                    }else{
                    	Toast.makeText(CorrectScoreEditActivity.this, reponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public void SubmitArbitrate(){
    	Public pub = (Public)this.getApplication();
    	String userid = pub.getUserID();
    	String token = pub.getToken();
    	String score = markScoreJson.toArbitrateString();
    	Log.v("YJ socre", score);
    	
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("arg0", userid);
        properties.put("arg1", token);
        properties.put("arg2", score);
        WebServiceUtil.callWebService(WebServiceUtil.getURL(), "Savearbitrate", properties, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                	Log.v("YJ","提交仲裁");
                    Log.v("YJ",result);
                    GetAlreadyMarkInfoResponse reponse = new GetAlreadyMarkInfoResponse(result);
                    if("0001".equals(reponse.getCodeID())){
                    	Toast.makeText(CorrectScoreEditActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    	
                    }else if("0002".equals(reponse.getCodeID())){
                    	//Toast.makeText(AlreadyMarkActivity.this, reponse.getMessage(), Toast.LENGTH_SHORT).show();
                    	Intent intent =new Intent(CorrectScoreEditActivity.this, LoginActivity.class);
                    	startActivity(intent);
                    }else{
                    	Toast.makeText(CorrectScoreEditActivity.this, reponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public List<String> getScorePoints(String str){
    	
    	List<String> lists = new ArrayList();
    	String score = "";
    	for(int i=0;i<str.length();i++){
    		char ch = str.charAt(i);
    		if(ch != ','){
    			score += ch;
    		}else{
    			if(score.length() > 0){
    				lists.add(score);
    				score = "";
    			}
    		}
    	}
    	if(score.length() > 0){
			lists.add(score);
			score = "";
		}
    	return lists;
    }
    public String checkScoreLegal(String str){
    	if(str.length() > 6){
    		return str.substring(0,6);
    	}
    	String result = "";
    	if(str.equals("")){
    		return "0";
    	}
    	if(str.equals(".")){
    		return "0.";
    	}
    	int pos = 0;
    	
    	for(int i=0;i<str.length();i++){
    		if(str.charAt(i) == '.'){
    			pos++;
    		}
    		if(pos > 1){
    			return result;
    		}
    		result += str.charAt(i);
    	}
    	if(pos == 0){ //如果没有小数点，排除多余的0
    		String temp = "";
    		int ps = 0;
    		for(int i=0;i<result.length();i++){
        		if(str.charAt(i) == '0'){
        			ps++;
        		}
        	}
    		if(ps == result.length()){ //全部是0
    			return "0";
    		}else{
    			return String.valueOf(Integer.parseInt(result));
    		}

    	}
    	
    	return result;
    }
    public void onClick(View v) {
		int eventID = v.getId();
		//Toast.makeText(CorrectScoreEditActivity.this, "分数最高为", 0).show();
		
		for(int i=0;i<rids.length;i++){
			if(rids[i] == eventID){
				try{
					if(i>=0&&i<=9){
						this.scoreShowText += String.valueOf(i);
					}else if(i==10){
						this.scoreShowText += ".";
					}else if(i==11){
						if(this.scoreShowText.length() > 0){
							this.scoreShowText = this.scoreShowText.substring(0, this.scoreShowText.length()-1);	
						}
					}else if(i==12){ //满分
						if(this.markScoreJson.hasSubQuestion){
							if(this.tsAdapter != null){
								this.scoreShowText = tsAdapter.getFullMark();
							}else{
								this.scoreShowText = "0";
							}
							
						}else{
							this.scoreShowText = this.full_marks;	
						}
						
					}else if(i==13){ //零分
						this.scoreShowText = "0";
					}else if(i==14){ //提交分数
						
						
						//提交正评分数
						if("0".equals(TYPE)){
							if(this.markScoreJson.hasSubQuestion){
								//判断分数合法性
								float scoreF = Float.parseFloat(this.scoreShowText);
								float scoreFullF = Float.parseFloat(tsAdapter.getFullMark());
								if(scoreF > scoreFullF){
									Toast.makeText(CorrectScoreEditActivity.this, "分数最高为" + scoreFullF, 0).show();
									this.scoreShowText = "0";
									
								}else{
									//把分数 存在左侧框框 并计算 总分；
									tsAdapter.setScoreByPosition(selectSubQueID,this.scoreShowText);
									selectSubQueID ++;
									tsAdapter.setSelectedPosition(selectSubQueID);
									String total_score = tsAdapter.getTotalScore();
									((TextView)CorrectScoreEditActivity.this.findViewById(R.id.total_score_text_view)).setText(total_score);
									if(selectSubQueID >= tsAdapter.getCount()){
										Log.v("YJ","submit haha");
									}
									if(tsAdapter.getAll()){
										submitTotalButton.setVisibility(View.VISIBLE);
									}else{
										submitTotalButton.setVisibility(View.GONE);
									}
									this.scoreShowText = "0";
								}
								
								
								
							}else{
								//判断分数合法性
								float scoreF = Float.parseFloat(this.scoreShowText);
								float scoreFullF = Float.parseFloat(this.full_marks);
								if(scoreF > scoreFullF){
									Toast.makeText(CorrectScoreEditActivity.this, "分数最高为" + scoreFullF, 0).show();
									this.scoreShowText = "0";
									
								}else{
									this.saveMarkingScore(this.scoreShowText, "");
									this.scoreShowText = "0";
								}
							}
						}else{
							//提交回评分数
							//提交回评分数
							if(this.markScoreJson.hasSubQuestion){
								//判断分数合法性
								float scoreF = Float.parseFloat(this.scoreShowText);
								float scoreFullF = Float.parseFloat(tsAdapter.getFullMark());
								if(scoreF > scoreFullF){
									Toast.makeText(CorrectScoreEditActivity.this, "分数最高为" + scoreFullF, 0).show();
									this.scoreShowText = "0";
									
								}else{
									//把分数 存在左侧框框 并计算 总分；
									tsAdapter.setScoreByPosition(selectSubQueID,this.scoreShowText);
									selectSubQueID ++;
									tsAdapter.setSelectedPosition(selectSubQueID);
									String total_score = tsAdapter.getTotalScore();
									((TextView)CorrectScoreEditActivity.this.findViewById(R.id.total_score_text_view)).setText(total_score);
									if(selectSubQueID >= tsAdapter.getCount()){
										Log.v("YJ","submit haha");
									}
									if(tsAdapter.getAll()){
										submitTotalButton.setVisibility(View.VISIBLE);
									}else{
										submitTotalButton.setVisibility(View.GONE);
									}
									this.scoreShowText = "0";
								}
								
							}else{
								//判断分数合法性
								float scoreF = Float.parseFloat(this.scoreShowText);
								float scoreFullF = Float.parseFloat(this.full_marks);
								if(scoreF > scoreFullF){
									Toast.makeText(CorrectScoreEditActivity.this, "分数最高为" + scoreFullF, 0).show();
									this.scoreShowText = "0";
									
								}else{
									this.saveAlreadyMarkScore(this.scoreShowText, "");
								}
							}
						}
						
						
					}
					this.scoreShowText = this.checkScoreLegal(this.scoreShowText );
					this.scoreShowTextView.setText(this.scoreShowText);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				return;
			}
		}
		for(int i=0;i<canvasIds.length;i++){
			if(canvasIds[i] == eventID){
				
				this.setCanvasButtonsVisible(i, true);
				
				return;
			}
		}
        switch (v.getId()) {
        case R.id.submit_sub_total_score_button:
        	//提交正评分数
			if("0".equals(TYPE)){
				String totalScore = this.tsAdapter.getTotalScore();
				String smallscores = this.tsAdapter.getSubScoreList();
				this.saveMarkingScore(totalScore, smallscores);
			}else{
				//提交回评分数
				String totalScore = this.tsAdapter.getTotalScore();
				String smallscores = this.tsAdapter.getSubScoreList();
				this.saveAlreadyMarkScore(totalScore, smallscores);
				
			}
        	Log.v("YJ","submit_sub_total_score_button onclick");
        	
        	break;
        case R.id.save_image_button:
        	
        	//Toast.makeText(CorrectScoreEditActivity.this, "保存图片", 0).show();
        	Log.v("YJ","save image");
        	//String base64Str = this.bitmapToBase64(mainFaceBitmp);
        	savePngImage(mainFaceBitmp);
        	//Log.v("YJ",base64Str);
        	break;
        case R.id.score_panel_back_button:
        	
        	Intent intent;
        	if(Public.isMarkingActivity == 1){
        		intent =new Intent(CorrectScoreEditActivity.this, MarkingActivity.class);
            	startActivity(intent);
            	//overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        	}else{
        		intent =new Intent(CorrectScoreEditActivity.this, AlreadyMarkActivity.class);
				startActivity(intent);
				//overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        	}
            break;
        case R.id.submit_except_button: //提交仲裁
        	
        	myDialog=new MyDialog(CorrectScoreEditActivity.this,R.style.MyDialog, "YES_NO");
            myDialog.setTitle("警告！");
            myDialog.setMessage("确定要提交仲裁吗?");
            myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                @Override
                public void onYesOnclick() {
                	SubmitArbitrate();
                	//加载对应的图片
                	getExamTaskListFromService();
                	myDialog.dismiss();
                }
            });
            myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                @Override
                public void onNoClick() {
                    
                    myDialog.dismiss();
                }
            });
            myDialog.show();
        	break;
        case R.id.ct_record_button:
        	if(this.recordPanel.getVisibility() == 4){ //invisible
        		this.setVisibleRecord(true);
        	}else if(this.recordPanel.getVisibility() == 0){
        		this.setVisibleRecord(false);
        	}
        	
    		this.getAlreadyMarkListFromService();
        	break;
        case R.id.ct_score_button:
        	if(this.scorePanel.getVisibility() == 4){ //invisible
        		this.setVisibleScore(true);
        	}else if(this.scorePanel.getVisibility() == 0){
        		this.setVisibleScore(false);
        	}
        	
        	break;
        case R.id.ct_select_button:
        	if(this.selectPanel.getVisibility() == 4){ //invisible
        		this.setVisibleSelect(true);
        	}else if(this.selectPanel.getVisibility() == 0){
        		this.setVisibleSelect(false);
        	}
        	
    		this.getSelectQuestionListFromService();
        	break;
        case R.id.canvas_area:
        	
        	//画布区域
        	//隐藏阅卷记录和选题
        	if(this.recordPanel.getVisibility() == 0){ //visible
        		this.setVisibleRecord(false);
        	}
        	if(this.selectPanel.getVisibility() == 0){ //visible
        		this.setVisibleSelect(false);
        	}
        	Log.v("YJ canvas area","click");
        	break;
        	
        default:
            break;
        }
    }
    //异步绘图
    public class ImgLoadTask extends AsyncTask<String,Integer,Bitmap>{

        private ImageView imageView;
        private Bitmap curBitmap;
        //为什么要加一个构造方法--有传值的需求
        public  ImgLoadTask(ImageView imageView){
            this.imageView=imageView;
        }
        public void getCanvasBitmap(){
        	
        }
        @Override
        protected Bitmap doInBackground(String... strings) {

            //加载网络图片，最后获取到一个Bitmap对象，返回Bitmap对象
        	Log.v("YJ","start load image");
            Bitmap bm=null;
            try {
            	//创建URL对象
                URL url=new URL(strings[0]);
                //通过URL对象得到HttpURLConnection
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();//这边需要强制转换）
                //得到输入流
                connection.setDoInput(true);
                connection.connect();
                if(connection.getResponseCode() == 200){
                	InputStream inputStream=connection.getInputStream();
                    //把输入流转换成Bitmap类型对象
                    bm= BitmapFactory.decodeStream(inputStream);
                    Log.v("YJ","end load image");
                    inputStream.close();
                }else{
                	Log.v("YJ response code","url get fail code" +String.valueOf(connection.getResponseCode()));
                }
                
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bm;
        }
        
        /** 
         * 得到bitmap的大小 
         */  
        public Bitmap base64ToBitmap(String base64Data) {
            byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            // 获得图片的宽高   
            try{
            	int width = bitmap.getWidth();   
                int height = bitmap.getHeight();  
                int imgViewW = imageView.getWidth();
                int imgViewH = 300;//imageView.getHeight();
                float scaleW = imgViewW*1.0f / width;
                //float scaleH = imgViewH / height;
                imgViewH = (int)(scaleW * height);
                Log.v("YJ scale",String.valueOf(scaleW) + "," + String.valueOf(scaleW));
                Log.v("YJ",String.valueOf(width) + "," + String.valueOf(height));
                Log.v("YJ",String.valueOf(imgViewW) + "," + String.valueOf(imgViewH));

                // 取得想要缩放的matrix参数   
                Matrix matrix = new Matrix();   
                matrix.postScale(scaleW, scaleW);
                mainBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);  
                
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(imgViewW,imgViewH);
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);//使图片充满控件大小,very imporment
                
                imageViewFace.setLayoutParams(params);
                imageViewFace.setScaleType(ImageView.ScaleType.FIT_XY);
//                mainFaceBitmp=BitmapFactory
//             		   .decodeResource(getResources(),R.drawable.canvas_pen)
//             		   .copy(Bitmap.Config.ARGB_8888,true);
                //mainFaceBitmp = Bitmap.createBitmap(imgViewW, imgViewH, Config.ARGB_8888);
                //mainFaceBitmp.setHasAlpha(true);
                
                //如果回评有标注，则把已有标注图层画上去
                if(markScoreJson.commentimage.length() > 0){
                	Log.v("YJ","有标注,转化画布");
                	Bitmap tempBitmap = base64ToBitmap(markScoreJson.commentimage);
                	
                	Matrix _matrix = new Matrix();   
                	float _scaleW = imgViewW*1.0f / tempBitmap.getWidth();
                	Log.v("YJ _scale",String.valueOf(_scaleW) + "," + String.valueOf(_scaleW));
                	
                	if(Math.abs(_scaleW - 1.0f) < 0.01f){
                		_scaleW = 0.99f;
                	}
                	_matrix.postScale(_scaleW, _scaleW);
            		mainFaceBitmp = Bitmap.createBitmap(tempBitmap, 0, 0, tempBitmap.getWidth(), tempBitmap.getHeight(), _matrix, true);
                	
                	
            		isMarkBiaozhu = true;
                	Log.v("YJ manFaceWidth w h = ", String.valueOf(mainFaceBitmp.getWidth()) + "," + String.valueOf(mainFaceBitmp.getHeight()));
                }else{
                	//没有标注,重新创建画布
                	Log.v("YJ","没有标注,重新创建画布");
                	isMarkBiaozhu = false;
                	mainFaceBitmp = Bitmap.createBitmap(imgViewW, imgViewH, Config.ARGB_8888);
                }
                
        		// 创建一张画布
        		Canvas canvas = new Canvas(mainFaceBitmp);
        		mainCanvas = canvas;
        		canvas.drawARGB(0,250,20,20);
        		// 创建画笔
        		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//消除锯齿
        		// 画笔颜色为红色
        		paint.setColor(Color.RED);
        		// 宽度5个像素
        		paint.setStrokeWidth(1);
        		
        		// 先将灰色背景画上
        		canvas.drawBitmap(mainFaceBitmp, new Matrix(), paint);
        		
        		//背景图片绘制到ImageView
        		imageView.setImageBitmap(mainBitmap);
        		//imageView.setBackgroundColor(Color.TRANSPARENT);
        		
        		imageViewFace.setImageBitmap(mainFaceBitmp);
        		imageViewFace.setBackgroundColor(Color.TRANSPARENT);
        		// 设置view监听1
        		imageGif.clearAnimation();
        		imageGif.setVisibility(View.GONE);
        		scrollView.setOnTouchListener(new CanvasTouchListener(canvas, paint, imageViewFace));
            }catch(Exception e){
            	e.printStackTrace();
            }
            
            Log.v("YJ","show succss.");

        }
    }
    //绘图事件监听
    class CanvasTouchListener implements OnTouchListener {
    	float downx, downy, x, y;
    	ImageView image;
    	Canvas canvas;
    	Paint paint;
    	public CanvasTouchListener(Canvas canvas, Paint paint, ImageView image){
    		this.canvas = canvas;
    		this.image = image;
    		this.paint = paint;
    	}
    	@Override
    	public boolean onTouch(View v, MotionEvent event) {
    		int action = event.getAction();
//    		if(!isPenOP){ //
//    			return true;
//    		}
    		if(isDuiOP){
    			
    			switch (action) {
        		// 按下
        		case MotionEvent.ACTION_DOWN:
        			Log.v("YJ scrollTop", String.valueOf(scrollTop));
        			downx = event.getX();
        			downy = event.getY() +scrollTop;
        			isMarkBiaozhu = true;
        			//return true;
        			// 创建画笔
            		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//消除锯齿
            		// 画笔颜色为红色
            		paint.setColor(Color.RED);
            		// 宽度5个像素
            		paint.setStrokeWidth(3);
            		this.canvas.drawLine(downx-20, downy, downx, downy + 20, paint);
            		this.canvas.drawLine(downx -1, downy + 19, downx + 40, downy - 20, paint);
        			// 刷新image
        			this.image.invalidate();
        			break;
        		default:
        			break;
        		}
    		}
    		if(isBanduiOP){
    			
    			switch (action) {
        		// 按下
        		case MotionEvent.ACTION_DOWN:
        			downx = event.getX();
        			downy = event.getY() +scrollTop;
        			//return true;
        			isMarkBiaozhu = true;
        			// 创建画笔
            		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//消除锯齿
            		// 画笔颜色为红色
            		paint.setColor(Color.RED);
            		// 宽度5个像素
            		paint.setStrokeWidth(3);
            		this.canvas.drawLine(downx-20, downy, downx, downy + 20, paint);
            		this.canvas.drawLine(downx-1, downy + 19, downx + 40, downy - 20, paint);
            		this.canvas.drawLine(downx, downy -12, downx + 35, downy + 20, paint);
        			// 刷新image
        			this.image.invalidate();
        			break;
        		default:
        			break;
        		}
    		}
    		if(isWrongOP){
    			switch (action) {
        		// 按下
        		case MotionEvent.ACTION_DOWN:
        			downx = event.getX();
        			downy = event.getY() +scrollTop;
        			isMarkBiaozhu = true;
        			//return true;
        			// 创建画笔
            		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//消除锯齿
            		// 画笔颜色为红色
            		paint.setColor(Color.RED);
            		// 宽度5个像素
            		paint.setStrokeWidth(3);
            		this.canvas.drawLine(downx-20, downy-20, downx+20, downy + 20, paint);
            		this.canvas.drawLine(downx-20, downy + 20, downx + 20, downy - 20, paint);
        			// 刷新image
        			this.image.invalidate();
        			break;
        		default:
        			break;
        		}
    		}
    	
    		if(isPenOP){
    			switch (action) {
        		// 按下
        		case MotionEvent.ACTION_DOWN:
        			downx = event.getX();
        			downy = event.getY() +scrollTop;
        			//return true;
        			break;
        		// 移动
        		case MotionEvent.ACTION_MOVE:
        			// 路径画板
        			isMarkBiaozhu = true;
        			x = event.getX();
        			y = event.getY()+scrollTop;
        			// 画线
        			Log.v("YJ move", String.valueOf(x) + "," + String.valueOf(y));
        			this.canvas.drawLine(downx, downy, x, y, this.paint);
        			// 刷新image
        			this.image.invalidate();
        			downx = x;
        			downy = y;
        			Log.v("YJ","11");
        			//return false;
        			break;
        		case MotionEvent.ACTION_UP:
        			break;

        		default:
        			break;
        		}
    		}
    		setVisibleRecord(false);
    		setVisibleSelect(false);
    		// true：告诉系统，这个触摸事件由我来处理
    		// false：告诉系统，这个触摸事件我不处理，这时系统会把触摸事件传递给imageview的父节点
    		return isPenOP ? true : false; //false 禁止滚动， true滚动
    	}

    }
}
