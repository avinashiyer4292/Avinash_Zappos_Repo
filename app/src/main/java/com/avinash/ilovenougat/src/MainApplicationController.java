package com.avinash.ilovenougat.src;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.avinash.ilovenougat.R;
import com.avinash.ilovenougat.src.utils.LruBitmapCache;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Dell on 9/9/2016.
 */
public class MainApplicationController extends Application {
    public static final String TAG = MainApplicationController.class
            .getSimpleName();
    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;
    private static MainApplicationController mInstance;
    /*public static MainApplicationController getInstance(){
        return mInstance;
    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Gotham Book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }


    public static synchronized MainApplicationController getInstance() {
        return mInstance;
    }
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue==null)
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        if(req!=null) {
            Log.d("APPLICATION", req.toString());
            getRequestQueue().add(req);
        }
        else
            Log.d(TAG,"Request is null");
    }

    public void cancelPendingRequests(Object tag){
        if(mRequestQueue!=null)
            mRequestQueue.cancelAll(tag);
    }
}
