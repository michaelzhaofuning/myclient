package com.qczb.myclient.ui.main;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;


import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.MyConstants;
import com.qczb.myclient.base.UserManager;
import com.qczb.myclient.util.ActivityUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Activity will be showed when app is launching
 *
 * @author Michael Zhao
 */
public class FlashActivity extends Activity {
    private final long DELAY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!UserManager.isLogin()) {
                    ActivityUtil.startActivityForResult(FlashActivity.this, LoginActivity.class);
                    finish();
                } else {
                    ActivityUtil.startActivityForResult(FlashActivity.this, MainActivity.class);
                    finish();
                }
            }
        }, DELAY);
    }
}
