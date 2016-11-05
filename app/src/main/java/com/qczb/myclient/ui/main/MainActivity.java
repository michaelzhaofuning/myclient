package com.qczb.myclient.ui.main;

import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.BaseResult;
import com.qczb.myclient.base.MyApplication;
import com.qczb.myclient.base.MyCallBack;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;


// key: myclient  password: myclient
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private long firstTimeOfExit = 0;
    private LinearLayout bar0, bar1, bar2;
    private TextView textViewBar0, textViewBar1, textViewBar2;
    private ImageView imageViewBar0, imageViewBar1, imageViewBar2;
    private Fragment homeFragment, myFragment, publishFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        initView();

        homeFragment = new PlanFragment();
        publishFragment = new ExecuteFragment();
        myFragment = new CustomerFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.container, homeFragment, "home")
                .add(R.id.container, publishFragment, "my")
                .add(R.id.container, myFragment, "publish")
                .commit();

        selectedBar(0);
    }


    private void initView() {
        bar0 = (LinearLayout) findViewById(R.id.bar0);
        bar1 = (LinearLayout) findViewById(R.id.bar1);
        bar2 = (LinearLayout) findViewById(R.id.bar2);

        bar0.setOnClickListener(this);
        bar1.setOnClickListener(this);
        bar2.setOnClickListener(this);

        textViewBar0 = (TextView) findViewById(R.id.textView_bar0);
        textViewBar1 = (TextView) findViewById(R.id.textView_bar1);
        textViewBar2 = (TextView) findViewById(R.id.textView_bar2);

        imageViewBar0 = (ImageView) findViewById(R.id.imageView_bar0);
        imageViewBar1 = (ImageView) findViewById(R.id.imageView_bar1);
        imageViewBar2 = (ImageView) findViewById(R.id.imageView_bar2);


    }

    private void selectedBar(int i) {
        resetBar();
        switch (i) {
            case 0:
                textViewBar0.setTextColor(getResources().getColor(R.color.colorPrimary));
                imageViewBar0.setImageResource(R.mipmap.bar01);
                getFragmentManager().beginTransaction().show(homeFragment).hide(myFragment).hide(publishFragment).commit();
//                getFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                break;
            case 1:
                textViewBar1.setTextColor(getResources().getColor(R.color.colorPrimary));
                imageViewBar1.setImageResource(R.mipmap.bar11);
                getFragmentManager().beginTransaction().show(publishFragment).hide(myFragment).hide(homeFragment).commit();
//                getFragmentManager().beginTransaction().replace(R.id.container, publishFragment).commit();


                break;
            case 2:
                imageViewBar2.setImageResource(R.mipmap.bar21);
                textViewBar2.setTextColor(getResources().getColor(R.color.colorPrimary));                getFragmentManager().beginTransaction().show(homeFragment).hide(myFragment).hide(publishFragment).commit();
                getFragmentManager().beginTransaction().show(myFragment).hide(homeFragment).hide(publishFragment).commit();
//                getFragmentManager().beginTransaction().replace(R.id.container, myFragment).commit();

                break;
        }
    }

    private void resetBar() {
        imageViewBar0.setImageResource(R.mipmap.bar00);
        imageViewBar1.setImageResource(R.mipmap.bar10);
        imageViewBar2.setImageResource(R.mipmap.bar20);

        textViewBar0.setTextColor(getResources().getColor(R.color.default_text_color));
        textViewBar1.setTextColor(getResources().getColor(R.color.default_text_color));
        textViewBar2.setTextColor(getResources().getColor(R.color.default_text_color));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bar0:
                selectedBar(0);
                break;
            case R.id.bar1:
                selectedBar(1);
                break;
            case R.id.bar2:
                selectedBar(2);
                break;
        }
    }

    @Override
    public boolean onKeyUp ( int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - firstTimeOfExit > 2000) {
                showToastMsg("再按一次退出");
                firstTimeOfExit = currentTime;
            } else {
                MyApplication.getMyApplication().exit();
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void publish(View view) {

    }

    public static void uploadGoods(String path, MyCallBack<BaseResult> callback) {
        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", "test.jpg", requestBody);
        Call<BaseResult> call = MyApplication.getMyApplication().getHttpService().uploadGoods(part);
        call.enqueue(callback);
    }
}
