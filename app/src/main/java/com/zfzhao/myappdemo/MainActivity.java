package com.zfzhao.myappdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.zfzhao.myappdemo.common.utils.ImageUtil;
import com.zfzhao.myappdemo.common.utils.L;
import com.zfzhao.myappdemo.common.utils.ScreenUtils;
import com.zfzhao.myappdemo.myvolley.VolleyController;
import com.zfzhao.myappdemo.myvolley.VolleyErrorHelper;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";

    static {
        System.loadLibrary("hello_jni");
    }

    public native String getJniStr();

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        Log.d(TAG, getJniStr());

        RequestQueue requestQueue = VolleyController.getInstance(context).getRequestQueue();

        StringRequest request = new StringRequest("http://157.7.53.209:18899/getip",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {  //收到成功应答后会触发这里
                        Log.d(TAG, arg0);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) { //出现连接错误会触发这里

                    }
                }
        ) {
            @Override
            public Priority getPriority() {
                return Priority.HIGH;
            }
        };
        requestQueue.add(request);

        ImageRequest imageRequest = new ImageRequest("https://ss0.bdstatic.com/-0U0bnSm1A5BphGlnYG/tam-ogel/6af91475cae28c6736ba1c70e1a70961_259_194.jpg",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "Img size: " + getBitmapSize(bitmap) + " byte.");
                        Log.d(TAG, "Img with: " + bitmap.getWidth() + " height:" + bitmap.getHeight());
                        ImageView imgv_test = (ImageView) findViewById(R.id.imgv_test);
                        int screenWith = ScreenUtils.getScreenWidth(context);
                        imgv_test.setImageBitmap(ImageUtil.zoomBitmap(bitmap, screenWith, bitmap.getHeight() * screenWith / bitmap.getWidth()));
                    }
                }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                Log.e(TAG, "ErrorStatus: " + arg0.toString());
                String msgStr = VolleyErrorHelper.getMessage(arg0, context);
                L.e(TAG, msgStr);
            }
        });
        imageRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 3, 1.0f));
        requestQueue.add(imageRequest);

        String strUrl = "http://avatar.csdn.net/E/A/0/1_qwm8777411.jpg";
        NetworkImageView imgv_net_test = (NetworkImageView) findViewById(R.id.imgv_net_test);


        Cache cache = VolleyController.getInstance(context).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(strUrl);
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");
                Bitmap bitmap = ImageUtil.bytes2Bimap(entry.data);
                if (bitmap == null) {
                    Log.d(TAG, "get bitmap from cache.");
                    imgv_net_test.setImageBitmap(bitmap);
                    L.i(TAG, "data.length = " + data.length());
                    L.d(TAG, "with=" + bitmap.getWidth() + ", height=" + bitmap.getHeight());
                    //cache.remove(strUrl);
                    //Log.d(TAG, "del bitmap cache.");
                } else {
                    Log.e(TAG, "bitmap cache error.");
                    Log.d(TAG, "get bitmap from re-request.");
                    ImageLoader imgLoader = VolleyController.getInstance(context).getImageLoader();
                    imgv_net_test.setImageUrl(strUrl, imgLoader);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "get bitmap from re-request.");
            ImageLoader imgLoader = VolleyController.getInstance(context).getImageLoader();
            imgv_net_test.setImageUrl(strUrl, imgLoader);
        }

    }

    public int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight(); //earlier version
    }

}
