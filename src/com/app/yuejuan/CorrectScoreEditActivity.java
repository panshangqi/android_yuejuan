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

import com.app.modules.ProgressItemInfo;
import com.app.modules.ProgressItemListAdapter;
import com.app.modules.QuestionTaskItemInfo;
import com.app.modules.QuestionTaskItemListAdapter;
import com.app.utils.CanvasView;
import com.app.utils.WebServiceUtil;
import com.app.webservice.*;

public class CorrectScoreEditActivity extends Activity {

	LinearLayout score_panel_back_button;
	//LinearLayout canvas_view;
	private ImageView imageView;  
	 private Bitmap baseBitmap;  
	 private Canvas canvas;  
	 private Paint paint; 
	 
	public String imageUrl;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d("YJ", "onCreate func");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct_score_edit);
        score_panel_back_button = (LinearLayout)this.findViewById(R.id.score_panel_back_button);
        //canvas_view = (LinearLayout)this.findViewById(R.id.canvas_view);
        this.initView();
    }

	public void initView() {
		// this.getQueTaskListFromService();
		//View canvasView = new CanvasView(this);
		// canvas_view.addView(canvasView);
		// -------------------------------------------------
		this.imageView = (ImageView) this.findViewById(R.id.iv);
		//this.getImageBitmap("http://pics.sc.chinaz.com/files/pic/pic9/201812/zzpic15929.jpg", imageView);
		
		ImgLoadTask imgLoadTask=new ImgLoadTask(imageView);
		String url1 = "https://img.ivsky.com/img/tupian/pre/201809/30/haian-001.jpg";
		String url2 = "http://222.186.12.239:10010/csyej_20190314/001.jpg";
        imgLoadTask.execute(url2);//execute里面是图片的地址

		
//		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        imageView.measure(w, h);
//		int imgVH = this.imageView.getMeasuredHeight();
//		int imgVW = this.imageView.getMeasuredWidth();
//		Log.v("YJ", String.valueOf(imgVW) + "," + String.valueOf(imgVH));
//		// 创建一张空白图片
//		baseBitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
//		// 创建一张画布
//		canvas = new Canvas(baseBitmap);
//		// 画布背景为灰色
//		canvas.drawColor(Color.GRAY);
//		// 创建画笔
//		paint = new Paint();
//		// 画笔颜色为红色
//		paint.setColor(Color.RED);
//		// 宽度5个像素
//		paint.setStrokeWidth(5);
//		// 先将灰色背景画上
//		canvas.drawBitmap(baseBitmap, new Matrix(), paint);
//		imageView.setImageBitmap(baseBitmap);
//
//		imageView.setOnTouchListener(new OnTouchListener() {
//			int startX;
//			int startY;
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				switch (event.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//					// 获取手按下时的坐标
//					startX = (int) event.getX();
//					startY = (int) event.getY();
//					break;
//				case MotionEvent.ACTION_MOVE:
//					// 获取手移动后的坐标
//					int stopX = (int) event.getX();
//					int stopY = (int) event.getY();
//					// 在开始和结束坐标间画一条线
//					canvas.drawLine(startX, startY, stopX, stopY, paint);
//					// 实时更新开始坐标
//					startX = (int) event.getX();
//					startY = (int) event.getY();
//					imageView.setImageBitmap(baseBitmap);
//					break;
//				}
//				return true;
//			}
//		});
		// -------------------------------------------------
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
    	List<QuestionTaskItemInfo> listInfo = new ArrayList();
    	Public pub = (Public)this.getApplication();

    	for(int i=0;i<itemsList.size();i++){
    		GetUserTaskQueInfoResponse.Datas data = itemsList.get(i);
    		QuestionTaskItemInfo item = new QuestionTaskItemInfo();
        	item.queid = data.queid;
        	Log.v("YJ",item.queid);
    		listInfo.add(item);	
    	}

    	Log.v("YJ","getBackMarkList()");
    	
    	QuestionTaskItemListAdapter qtAdapter = new QuestionTaskItemListAdapter(Public.context, listInfo);

    	ListView QueTaskListView = (ListView)findViewById(R.id.question_task_list_view);
    	
    	QueTaskListView.setAdapter(qtAdapter);
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
            imageView.setImageBitmap(this.curBitmap);
            
        }catch(Exception e){
        	e.printStackTrace();
        }
        
        Log.v("YJ","show succss.");

    }
}