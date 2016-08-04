package com.qczb.myclient.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Process;

import com.facebook.drawee.backends.pipeline.Fresco;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Extends application to add some methods
 *
 * @author Michael Zhao
 */
public class MyApplication extends Application {
    private static MyApplication app;
    public static final String BASE_URL = "http://183.203.28.91:3000/yw_php_api/";
    private HttpService httpService;

    public static MyApplication getMyApplication() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Fresco.initialize(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        httpService = retrofit.create(HttpService.class);
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
