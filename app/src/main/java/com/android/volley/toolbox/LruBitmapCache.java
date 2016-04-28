package com.android.volley.toolbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.LruCache;

public class LruBitmapCache extends LruCache<String,Bitmap> implements ImageLoader.ImageCache {
    public static int getDefaultLruBitmapCacheSize(){
        final int maxMemory=(int)(Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize=maxMemory/8;
        return cacheSize;
    }

    public LruBitmapCache(){
        this(getDefaultLruBitmapCacheSize());
    }

    public LruBitmapCache(int sizeInKiloBytes){
        super(sizeInKiloBytes);
    }

    @Override
    public int sizeOf(String key, Bitmap value){
        return value.getRowBytes()*value.getHeight()/1024;
    }

    @Override
    public Bitmap getBitmap(String url){
        return get(url);
    }

    @Override
    public void putBitmap(String url,Bitmap bitmap){
        put(url,bitmap);
    }
}