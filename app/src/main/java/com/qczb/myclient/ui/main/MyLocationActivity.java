package com.qczb.myclient.ui.main;

import android.os.Bundle;
import android.util.Log;

import com.qczb.myclient.R;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 2016/2/5
 *
 * @author Michael Zhao
 */
public class MyLocationActivity extends MapActivity implements TencentLocationListener {
    private TencentLocationManager locationManager;
    private StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        initLocation();
    }

    private void initLocation() {
        TencentLocationRequest request = TencentLocationRequest.create();
        locationManager = TencentLocationManager.getInstance(getApplicationContext());
        int error = locationManager.requestLocationUpdates(request, this);
        // 返回值为0时表示位置监听器注册成功
        Log.e("location", "error: " + error);
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        if (TencentLocation.ERROR_OK == i) {
            // 定位成功
            String string = "定位成功 !\n" + "纬度： " + tencentLocation.getLatitude() + "  经度： "
                    + tencentLocation.getLongitude() + "\n" + tencentLocation.getAddress() + "\n\n";
            sb.append(string);
            Log.e("location", string);

//            send location in MainActivity
//            sendLocation(tencentLocation.getLatitude(), tencentLocation.getLongitude());

        } else {
            // 定位失败
            Log.e("location", "定位失败 code: " + i + " " + s);
        }

        /*TencentMap tencentMap = ((MapView) findViewById(R.id.mapview)).getMap();
        tencentMap.setCenter(new LatLng(tencentLocation.getLatitude(), tencentLocation.getLongitude()));
        Marker marker = tencentMap.addMarker(new MarkerOptions()
                .position(new LatLng(tencentLocation.getLatitude(), tencentLocation.getLongitude()))
                .title(tencentLocation.getAddress())
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .defaultMarker())
                .draggable(false));
        marker.showInfoWindow();// 设置默认显示一个infowinfow*/

    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
    }

    /*public void sendLocation(double latitude, double longitude) {
        RequestParams rp = new RequestParams();
        rp.put("userid", UserManager.getUID());
        rp.put("latitude", latitude);
        rp.put("longitude", longitude);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        rp.put("dateTime", simpleDateFormat.format(new Date(System.currentTimeMillis())));
        MyHttpClient.postRaw(this, "/syyt/location/pushLocation.htm", rp, new MyHttpClient.MyHttpHandler() {
            @Override
            protected void onSuccess(JSONObject response) {

            }
        });
    }*/
}
