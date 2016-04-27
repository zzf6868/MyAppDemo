package com.zfzhao.myappdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zfzhao.myappdemo.common.utils.ImageUtil;
import com.zfzhao.myappdemo.common.utils.ScreenUtils;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";

    public native String getJniStr();

    static {
        System.loadLibrary("hello_jni");
    }

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        Log.d(TAG, getJniStr());

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
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
        );
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
                        imgv_test.setImageBitmap(ImageUtil.zoomBitmap(bitmap, screenWith, bitmap.getHeight()*screenWith/bitmap.getWidth()));
                    }
                }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                Log.e(TAG, "ErrorStatus: " + arg0.toString());
            }
        });

        requestQueue.add(imageRequest);
    }

    public int getBitmapSize(Bitmap bitmap){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight(); //earlier version
    }


}
