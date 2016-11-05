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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

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

    public static class StartPlanFragment extends ScrollViewFragment implements TencentLocationListener{
        static LinearLayout mContainer;
        private long startTime = System.currentTimeMillis();
        private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        private TencentLocationManager locationManager;
        private String latitude="";
        private String longitude="";

        //声明AMapLocationClient类对象
        public AMapLocationClient mLocationClient = null;




        public void onPermission() {
                        initLocation();

//SDK在Android 6.0下需要进行运行检测的权限如下：
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_PHONE_STATE

//这里以ACCESS_COARSE_LOCATION为例



//初始化定位//声明AMapLocationClientOption对象
            /*AMapLocationClientOption mLocationOption = null;
//初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
//设置定位回调监听
            mLocationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    Log.e("test", aMapLocation.toString());
                    latitude= aMapLocation.getLatitude() + "";
                    longitude=aMapLocation.getLongitude()+"";
                }
            });
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();*/
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


            map.put("planId", UUID.randomUUID().toString());
            map.put("longitude", longitude);
            map.put("latitude", latitude);
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
