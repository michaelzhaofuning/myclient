package com.qczb.myclient.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Process;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Extends application to add some methods
 *
 * @author Michael Zhao
 */
public class MyApplication extends Application {
    private static MyApplication app;
    public static final String BASE_URL = "http://test.kaopuren.cn/";
//    public static final String BASE_URL = "http://192.168.1.101:8080/kxw/";
    private HttpService httpService;

    public static MyApplication getMyApplication() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Fresco.initialize(this);
        initImageLoader();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        httpService = retrofit.create(HttpService.class);
    }

    private void initImageLoader() {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .threadPoolSize(20)
                .build();
        ImageLoader.getInstance().init(configuration);
        File file = new File(MyConstants.STORAGE_IMAGE);
        if (!file.exists()) {
            if (file.mkdirs()) Log.i("image loader", MyConstants.STORAGE_IMAGE + " created !");
        } else {
            Log.i("image loader", "images dir have been created!");
        }
    }

    public SharedPreferences getSharedPreference() {
        return MyApplication.getMyApplication().getSharedPreferences("app", Context.MODE_PRIVATE);
    }

    public HttpService getHttpService() {
        return httpService;
    }

    public void exit() {
        // close database
        CacheManager.getInstance().closeDB();
        System.exit(0);
        Process.killProcess(Process.myPid());
    }
}
