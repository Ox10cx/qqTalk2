package gx.com.qqtalk2.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class Bimp {
	public static int max = 0;
	
	public static ArrayList<ImageItem> tempSelectBitmap = new ArrayList<ImageItem>();   //选择的图片的临时列表

	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}
	//得到原图
	public static Bitmap getBitmapFromUrl(String url,double width,double height)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		Bitmap bitmap=new BitmapFactory().decodeFile(url);
		//防止OOM可以
		options.inJustDecodeBounds=false;
		int mWidth=bitmap.getWidth();
		int mHeight=bitmap.getHeight();
		Matrix matrix=new Matrix();
		float scaleWidth=1;
		float scaleHeight=1;
//		if(mWidth<=mHeight)
//		{
//			scaleWidth=(float)(width/mWidth);
//			scaleHeight=(float)(height/mHeight);
//		}else
//		{
//			scaleWidth=(float)(height/mWidth);
//			scaleHeight=(float)(width/mHeight);
//		}
		matrix.postScale(scaleWidth,scaleHeight);
		Bitmap newBitmap=Bitmap.createBitmap(bitmap,0,0,mWidth,mHeight,matrix,true);
		bitmap.recycle();
		return newBitmap;
	}
}
