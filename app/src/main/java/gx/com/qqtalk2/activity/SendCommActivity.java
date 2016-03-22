package gx.com.qqtalk2.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gx.com.qqtalk2.R;
import gx.com.qqtalk2.util.Bimp;
import gx.com.qqtalk2.util.FileUtils;
import gx.com.qqtalk2.util.ImageItem;
import gx.com.qqtalk2.util.PictureBean;
import gx.com.qqtalk2.util.PictureUtil;
import gx.com.qqtalk2.util.PublicWay;
import gx.com.qqtalk2.util.Res;
import gx.com.qqtalk2.util.Talkbean;


/**
 * 首页面activity
 *
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日  下午11:48:34
 */
public class SendCommActivity extends Activity {
 
       String url="http://192.168.1.3:8080/San/talkAbout";

	String userName="一代火影";
	String currentTime;
	//评论内容
	EditText edit_content;
	//TextView 发送点击事件
	TextView textView_send;
	String content;
	//地址
	String address="先锋东外滩";

	private GridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	public static Bitmap bimap ;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Res.init(this);
		bimap = BitmapFactory.decodeResource(
				getResources(),
				R.drawable.icon_addpic_unfocused);
		PublicWay.activityList.add(this);
		parentView = getLayoutInflater().inflate(R.layout.activity_selectimg, null);
		setContentView(parentView);
		Init();
		//发送评论的事件
		Init2();

	}

	public void Init2()
	{

		edit_content= (EditText)parentView.findViewById(R.id.edit_content);

		textView_send= (TextView)parentView.findViewById(R.id.activity_selectimg_send);
		textView_send.setClickable(true);
		textView_send.setFocusable(true);
		textView_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 content = edit_content.getText().toString().trim();
				Log.d("ss","内容"+content);
				handler.sendEmptyMessage(0);


			}
		});
	}
	Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
				case 0:

					if (content.equals("")&Bimp.tempSelectBitmap.size()==0) {
						Toast.makeText(SendCommActivity.this,"内容不能为空",Toast.LENGTH_SHORT).show();

					} else {
						new UploadTask().execute(content);
//						for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
//							Log.d("ss",Bimp.tempSelectBitmap.get(i).getImagePath());
//						}
					}
					break;

				default:
					break;
			}
			return false;
		}
	});

//   class UploadTask extends AsyncTask<String,Void,String>{
//
//	@Override
//	protected String doInBackground(String... params) {
//		//当前发微博的时间
//		Date date=new Date();
//		DateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm");
//		currentTime=format.format(date);
//		HttpClient client=new DefaultHttpClient();
//		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,10000);
//		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,10000);
//		HttpPost post=new HttpPost(url);
//		MultipartEntity entity=new MultipartEntity();
//		try {
//			entity.addPart("content",new StringBody(URLEncoder.encode(params[0],"UTF-8")));
//            entity.addPart("userName",new StringBody(URLEncoder.encode(userName,"UTF-8")));
//			entity.addPart("currentTime",new StringBody(currentTime));
//
//			//当有图片时发送图片
//
//				for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
//					String path = Bimp.tempSelectBitmap.get(i).getImagePath();
//
//					Log.d("ss", "路径" + path);
//					File file = new File(path);
//					entity.addPart("file" + i, new FileBody(file));
//				}
//
//
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//
//		post.setEntity(entity);
//
//		//从客户端发送信息
//
//		try {
//			HttpResponse response=client.execute(post);
//			Log.d("ss","编号"+response.getStatusLine().getStatusCode());
//
//			if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode())
//			{
//				String result= EntityUtils.toString(response.getEntity());
//				try {
//					JSONObject jsstr=new JSONObject(result);
//					String str=jsstr.getString("type");
//					if(str.equals("comu_ok"))
//
//					{
//						Intent intent=new Intent(getApplicationContext(),SuccessActivity.class);
//						startActivity(intent);
//						return "发送成功";
//
//
//
//
//					}
//					if(str.equals("comu_error"))
//					{
//						return "发送失败";
//					}
//
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return "网络异常";
//	}
//
//	@Override
//	protected void onPostExecute(String s) {
//		super.onPostExecute(s);
//		Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
//	}
//}





	//字节上传第二版
	class UploadTask extends AsyncTask<String,Void,String>{
		String piccontent;
		List<PictureBean> picitems=new ArrayList<>();

		@Override
		protected String doInBackground(String... params) {
			//当前发微博的时间
			Date date=new Date();
			DateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm");
			currentTime=format.format(date);
			Talkbean talk=new Talkbean();
			try {
				talk.setUserName(URLEncoder.encode(userName,"UTF-8"));
				talk.setContent(URLEncoder.encode(params[0], "UTF-8"));
				talk.setAddress(URLEncoder.encode(address,"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			talk.setCurrentTime(currentTime);

			for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
				//图片
				PictureBean pic=new PictureBean();
				pic.setPictureName(Bimp.tempSelectBitmap.get(i).getImagePath().substring(1));

				piccontent = PictureUtil.bitmapToString(Bimp.tempSelectBitmap.get(i).getImagePath());
				pic.setPictureContent(piccontent);
				picitems.add(pic);

				}
			    talk.setPicitems(picitems);
			    Gson gson=new Gson();
			    String json=gson.toJson(talk);


			HttpClient client=new DefaultHttpClient();
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,10000);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,10000);
			HttpPost post=new HttpPost(url);
			try {
				StringEntity entity=new StringEntity(json.toString());
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");//设置为Json数据
				post.setEntity(entity);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}


			//从客户端发送信息

			try {
				HttpResponse response=client.execute(post);
				Log.d("ss","编号"+response.getStatusLine().getStatusCode());

				if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode())
				{
					String result= EntityUtils.toString(response.getEntity());
					try {
						JSONObject jsstr=new JSONObject(result);
						String str=jsstr.getString("type");
						if(str.equals("comu_ok"))

						{
							Intent intent=new Intent(getApplicationContext(),SuccessActivity.class);
							startActivity(intent);
							return "发送成功";




						}
						if(str.equals("comu_error"))
						{
							return "发送失败";
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			return "网络异常";
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
		}
	}
	public void Init() {
		
		pop = new PopupWindow(SendCommActivity.this);
		
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		
		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view
				.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view
				.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view
				.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(SendCommActivity.this,
						AlbumActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);	
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					Log.i("ddddddd", "----------");
					ll_popup.startAnimation(AnimationUtils.loadAnimation(SendCommActivity.this,R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(SendCommActivity.this,
							GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});

	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if(Bimp.tempSelectBitmap.size() == 9){
				return 9;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position ==Bimp.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;

				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	private static final int TAKE_PICTURE = 0x000001;


	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		Log.d("ss","执行了");
//		String imagePath = Environment.getExternalStorageDirectory() + "/Photo_LJ/" ;
//		File file = new File(imagePath);
//
//		if(!file.exists()){
//
//			file.mkdirs(); }
//
//
//		Log.d("ss", "out的路径" + file.getPath());
//		//获取拍照后未压缩的原图片，并保存在url路径
//		File f= new File(imagePath, "image.jpg");
//		Log.d("ss","路径"+f.getPath());
//		Uri uri=Uri.fromFile(f);

		// 指定调用相机拍照后照片的储存路径
		FileUtils.savePath(String.valueOf(System.currentTimeMillis()));
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(FileUtils.picPath)));
		Log.d("ss", "拍照前路径" + FileUtils.picPath);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {

//				String fileName = String.valueOf(System.currentTimeMillis());
				//Bitmap bm = (Bitmap) data.getExtras().get("data");
//				Bitmap bm=Bimp.getBitmapFromUrl(FileUtils.picPath,313.5,462.0);
//				FileUtils.saveBitmap(bm, fileName);
				ImageItem takePhoto = new ImageItem();
				takePhoto.setImagePath(FileUtils.picPath);

				Log.d("ss", "相机的" + FileUtils.picPath);
//				takePhoto.setBitmap(bm);
				Bimp.tempSelectBitmap.add(takePhoto);
			}
			break;
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			for(int i=0;i<PublicWay.activityList.size();i++){
				if (null != PublicWay.activityList.get(i)) {
					PublicWay.activityList.get(i).finish();
				}
			}
			System.exit(0);
		}
		return true;
	}

}

