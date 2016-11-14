package com.qczb.myclient.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.photoselector.model.PhotoModel;
import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.BaseEntity;
import com.qczb.myclient.base.BaseResult;
import com.qczb.myclient.base.MyApplication;
import com.qczb.myclient.base.MyCallBack;
import com.qczb.myclient.entity.Item;
import com.qczb.myclient.entity.PlanContent;
import com.qczb.myclient.util.ActivityUtil;
import com.qczb.myclient.view.MyEditLinearLayout;
import com.qczb.myclient.view.PhotoPopupWindow;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 2016/8/7
 *
 * @author Michael Zhao
 */
public class StartPlanActivity extends BaseActivity {
    ArrayList<PhotoModel> photoModels = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        getFragmentManager().beginTransaction()
                .add(R.id.container, new StartPlanFragment(), "location").commit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    666);//自定义的code
        }


    }

    @Override
    public void onPermission() {
        super.onPermission();
        ((StartPlanFragment)getFragmentManager().findFragmentByTag("location")).onPermission();

    }

    public void providers(View view) {
        ActivityUtil.startActivityForResult(this, ProviderActivity.class);
    }

    public void shot(View view) {
        new PhotoPopupWindow(this).show(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoPopupWindow.callBack(this, photoModels, requestCode, resultCode, data, null, StartPlanFragment.mContainer);
    }

    public void stocks(View view) {
        ActivityUtil.startActivityForResult(this, StockActivity.class);

    }

    public static class StartPlanFragment extends ScrollViewFragment implements TencentLocationListener{
        static LinearLayout mContainer;
        private long startTime = System.currentTimeMillis();
        private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        private TencentLocationManager locationManager;
        private String latitude="";
        private String longitude="";
        public ArrayList<PhotoModel> photoModelsVisit = new ArrayList<>();
        public ArrayList<PhotoModel> photoModelsAbsent = new ArrayList<>();










        public void onPermission() {
                        initLocation();
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

initLocation();


        }
    private void initLocation() {
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_GEO);
        locationManager = TencentLocationManager.getInstance(getActivity().getApplicationContext());
        int error = locationManager.requestLocationUpdates(request, this);
        // 返回值为0时表示位置监听器注册成功
        Log.e("location", "error: " + error);
    }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (locationManager!=null)
            locationManager.removeUpdates(this);
        }

        @Override
        public void onViewCreated(View v, Bundle savedInstanceState) {
            super.onViewCreated(v, savedInstanceState);
            mContainer = (LinearLayout) v.findViewById(R.id.container_photos);
            getHttpService().getVisit(getActivity().getIntent().getStringExtra("vid")).enqueue(new MyCallBack<BaseResult>((BaseActivity) getActivity()) {
                @Override
                public void onMySuccess(Call<BaseResult> call, Response<BaseResult> response) {
                    PlanContent planContent = JSON.parseObject(response.body().getData().get(0).getAsJsonObject().toString(), PlanContent.class);
                    item = planContent;
                    MyEditLinearLayout myEditLinearLayout= (MyEditLinearLayout) linearLayout.findViewById(R.id.visit_content);
                    myEditLinearLayout.setContent(planContent.visitContent);
                }
            });
        }

        @Override
        protected Class getItemClass() {
            return Item.class;
        }

        @Override
        protected void onSendForm() {
            super.onSendForm();





            map.put("planId", getActivity().getIntent().getStringExtra("vid"));
            map.put("longitude", longitude);
            map.put("latitude", latitude);
            map.put("state", 2+"");
            map.put("startTime", simpleDateFormat.format(new Date(startTime)));
            map.put("endTime", simpleDateFormat.format(new Date(System.currentTimeMillis())));

            if (item != null) {
                PlanContent planContent = (PlanContent) item;
                map.put("planId", planContent.planId);
            }

            getHttpService().startVisit(map).enqueue(new MyCallBack<BaseResult>((BaseActivity) getActivity()) {
                @Override
                public void onMySuccess(Call<BaseResult> call, Response<BaseResult> response) {
                    success();

                }
            });
        }


        /**
         * state:0未完成1拜访中2已完成(必填)
         jpfxContent:竞品分析内容
         lyqContent：临逾期内容
         qssbContent：缺损上报内容
         qssb_imgs：缺损上报图片
         is_medic：是否有效铺货（1是0否）
         * @return
         */
        @Override
        protected int getScrollViewContentLayoutId() {
            return R.layout.scroll_view_start_plan;
        }

        @Override
        protected int getTopImageID() {
            return R.mipmap.start_visit;
        }

        @Override
        protected String getTitle() {
            return "开始拜访";
        }

        @Override
        public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
            longitude = tencentLocation.getLongitude() + "";
            latitude = tencentLocation.getLatitude() + "";

        }

        @Override
        public void onStatusUpdate(String s, int i, String s1) {

        }
    }
}
