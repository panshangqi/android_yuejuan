package com.app.yuejuan;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

//import java.util.Date;
//import java.text.DateFormat;
//import java.rmi.Remote;
//import org.apache.axis.client.Call;
//import org.apache.axis.client.Service;
//import org.apache.axis.encoding.XMLType; 

//import javax.xml.namespace.QName;
//import java.lang.Integer;
//import javax.xml.rpc.ParameterMode;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import com.app.utils.CanvasView;
import com.app.utils.WebServiceUtil;
import com.app.webservice.*;
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
		switch (action) {
		// 按下
		case MotionEvent.ACTION_DOWN:
			downx = event.getX();
			downy = event.getY();
			return true;
		// 移动
		case MotionEvent.ACTION_MOVE:
			// 路径画板
			x = event.getX();
			y = event.getY();
			// 画线
			Log.v("YJ move", String.valueOf(x) + "," + String.valueOf(y));
			this.canvas.drawLine(downx, downy, x, y, this.paint);
			// 刷新image
			this.image.invalidate();
			downx = x;
			downy = y;
			Log.v("YJ","11");
			return true;
			
		case MotionEvent.ACTION_UP:
			break;

		default:
			break;
		}
		// true：告诉系统，这个触摸事件由我来处理
		// false：告诉系统，这个触摸事件我不处理，这时系统会把触摸事件传递给imageview的父节点
		return true;
	}

}
public class CorrectScoreEditActivity extends Activity {

	LinearLayout score_panel_back_button;
	//LinearLayout canvas_view;
	private ImageView imageView;  
	private Bitmap baseBitmap;  
	private TextView quenameView;
	private Canvas canvas;  
	private Paint paint; 
	ScrollView scrollView;
	public String imageUrl;
	public String selectedQueID;
	public String selectedQueName = "";
	TextView recordButton, scoreButton, selectButton;
	LinearLayout recordPanel, scorePanel, selectPanel;
	ImgLoadTask imgLoadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d("YJ", "onCreate func");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct_score_edit);
        score_panel_back_button = (LinearLayout)this.findViewById(R.id.score_panel_back_button);
        scrollView = (ScrollView)this.findViewById(R.id.canvas_scroll_bar);
        quenameView = (TextView)this.findViewById(R.id.hd_que_name);
        //canvas_view = (LinearLayout)this.findViewById(R.id.canvas_view);
        this.imageView = (ImageView) this.findViewById(R.id.iv);
        this.recordButton = (TextView) this.findViewById(R.id.ct_record_button);
        this.scoreButton = (TextView) this.findViewById(R.id.ct_score_button);
        this.selectButton = (TextView) this.findViewById(R.id.ct_select_button);
        
        this.recordPanel = (LinearLayout) this.findViewById(R.id.ct_record_panel);
        this.scorePanel = (LinearLayout) this.findViewById(R.id.ct_score_panel);
        //默认三个面板都不显示
        this.recordPanel.setVisibility(View.INVISIBLE);
        this.scorePanel.setVisibility(View.INVISIBLE);
        
        //get params queid
        try{
        	Intent intent = getIntent();
        	selectedQueID = intent.getStringExtra("queid");
        	selectedQueName = intent.getStringExtra("quename");
            Log.v("YJ queid ", selectedQueID);
            quenameView.setText(this.selectedQueName);
        }catch(Exception e){
        	e.printStackTrace();
        }
        
        this.initView();
    }

	public void initView() {
		this.getQueTaskListFromService();
		this.getExamTaskListFromService();
		this.getAlreadyMarkListFromService();
		//View canvasView = new CanvasView(this);
		// canvas_view.addView(canvasView);
		// -------------------------------------------------
		
		//this.getImageBitmap("http://pics.sc.chinaz.com/files/pic/pic9/201812/zzpic15929.jpg", imageView);
		scrollView.setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				Log.v("YJ","00");
				return true;
			}
		});
		imgLoadTask=new ImgLoadTask(imageView);
		String url1 = "https://img.ivsky.com/img/tupian/pre/201809/30/haian-001.jpg";
		String url2 = "http://222.186.12.239:10010/csyej_20190314/001.jpg";
		String url3 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1554468075583&di=88836cfbcabd1cfebd6dd6ec46dab780&imgtype=0&src=http%3A%2F%2Fs7.sinaimg.cn%2Fmiddle%2F4b83b92dgbdf0991fe946%26690";
        //imgLoadTask.execute(url1);//execute里面是图片的地址

	}
	
    public void save(View view) {  
    	  try {  
			File file = new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis() + ".jpg");
			OutputStream stream = new FileOutputStream(file);
			baseBitmap.compress(CompressFormat.JPEG, 100, stream);
			stream.close();
			// 模拟一个广播，通知系统sdcard被挂载
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
			intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
			sendBroadcast(intent);
			Toast.makeText(this, "保存图片成功", 0).show();
		} catch (Exception e) {
			Toast.makeText(this, "保存图片失败", 0).show();
			e.printStackTrace();
		}
    }  

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void renderQuetionList(List<GetUserTaskQueInfoResponse.Datas> itemsList){
    	List<ScorePointsItemInfo> listInfo = new ArrayList();
    	Log.v("YJ","开始渲染打分表");
    	List<String> scorePointsList = new ArrayList();
    	for(int i=0;i<itemsList.size();i++){
    		GetUserTaskQueInfoResponse.Datas data = itemsList.get(i);
    		Log.v("YJ queid",data.queid);
    		if(this.selectedQueID.equals(data.queid)){
    			if(data.smallqueinfoList.size() == 0) //没有小题的情况
    			{
    				Log.v("YJ","没有小题的情况");
    				scorePointsList = this.getScorePoints(data.scorepoints);
    			}else{
    				Log.v("YJ","有小题的情况");
    			}
    			break;
    		}
    	}
    	
    	for(int i=0;i<scorePointsList.size();i++){
    		
    		ScorePointsItemInfo item = new ScorePointsItemInfo();
        	item.queid = scorePointsList.get(i);
        	Log.v("YJ",item.queid);
    		listInfo.add(item);	
    	}

    	Log.v("YJ","getBackMarkList()");
    	
    	ScorePointsItemListAdapter qtAdapter = new ScorePointsItemListAdapter(Public.context, listInfo);

    	ListView QueTaskListView = (ListView)findViewById(R.id.score_points_list_view);
    	
    	QueTaskListView.setAdapter(qtAdapter);
    }
    public void renderCorrectScoreItemList(List<AlreadyMarkListResponse.Datas> itemsList){
    	List<CorrectRecordItemInfo> listInfo = new ArrayList();
    	Log.v("YJ","开始阅卷记录list表");
    	
    	for(int i=0;i<itemsList.size();i++){
    		AlreadyMarkListResponse.Datas data = itemsList.get(i);
    		CorrectRecordItemInfo item = new CorrectRecordItemInfo();
    		item.order = String.valueOf(i+1);
    		item.score = data.firstmark;
    		listInfo.add(item);
    	}

    	Log.v("YJ","getBackMarkList()");
    	
    	CorrectRecordItemListAdapter qtAdapter = new CorrectRecordItemListAdapter(Public.context, listInfo);

    	ListView theListView = (ListView)findViewById(R.id.correct_record_list_view);
    	
    	theListView.setAdapter(qtAdapter);
    }
    public void getQueTaskListFromService(){
    	Public pub = (Public)this.getApplication();
       
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("arg0", pub.userid);
        properties.put("arg1", pub.token);
        properties.put("arg2", pub.usersubjectid);
        
        WebServiceUtil.callWebService(WebServiceUtil.WEB_SERVER_URL, "GetUsertaskqueinfo", properties, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    Log.v("YJ",result);
                    
                    GetUserTaskQueInfoResponse reponse = new GetUserTaskQueInfoResponse(result);
                    if("0001".equals(reponse.getCodeID())){
                    	List<GetUserTaskQueInfoResponse.Datas> itemsList = reponse.dataList;
                    	CorrectScoreEditActivity.this.renderQuetionList(itemsList);
                       
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
        
        WebServiceUtil.callWebService(WebServiceUtil.WEB_SERVER_URL, "GetAlreadymarklist", properties, new WebServiceUtil.WebServiceCallBack() {
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
                    }else if("0002".equals(reponse.getCodeID())){
                    	Toast.makeText(CorrectScoreEditActivity.this, reponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    //获取选定题目的学生试卷信息，功能：根据用户名、令牌、科目ID、题目ID 获取信息
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
        
        WebServiceUtil.callWebService(WebServiceUtil.WEB_SERVER_URL, "GetExamtaskinfo", properties, new WebServiceUtil.WebServiceCallBack() {
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
                    		String url = Public.imageUrl(data.imgurl);
                    		CorrectScoreEditActivity.this.imgLoadTask.execute(url);//execute里面是图片的地址
                    	}
                    	
                    }else if("0002".equals(reponse.getCodeID())){
                    	//Toast.makeText(AlreadyMarkActivity.this, reponse.getMessage(), Toast.LENGTH_SHORT).show();
                    	Intent intent =new Intent(CorrectScoreEditActivity.this, LoginActivity.class);
                    	startActivity(intent);
                    }else if("0002".equals(reponse.getCodeID())){
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
    public void onClick(View v) {
        switch (v.getId()) {
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
//        case R.id.loginforget:
//        	
//        	Toast.makeText(CorrectScoreEditActivity.this, "btn1:", Toast.LENGTH_SHORT).show();
//        	Intent intent =new Intent(CorrectScoreEditActivity.this, ForgetPasswordActivity.class);
//        	startActivity(intent);
            
        default:
            break;
        }
    }
}
class ImgLoadTask extends AsyncTask<String,Integer,Bitmap>{

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
            this.curBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);  
            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imgViewW,imgViewH);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);//使图片充满控件大小,very imporment
            
    		
    		// 创建一张画布
    		Canvas canvas = new Canvas(this.curBitmap);

    		// 创建画笔
    		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//消除锯齿
    		// 画笔颜色为红色
    		paint.setColor(Color.RED);
    		// 宽度5个像素
    		paint.setStrokeWidth(1);
    		
    		// 先将灰色背景画上
    		canvas.drawBitmap(this.curBitmap, new Matrix(), paint);
    		
    		imageView.setImageBitmap(this.curBitmap);
    		// 设置view监听
    		imageView.setOnTouchListener(new CanvasTouchListener(canvas, paint, imageView));
        }catch(Exception e){
        	e.printStackTrace();
        }
        
        Log.v("YJ","show succss.");

    }
}