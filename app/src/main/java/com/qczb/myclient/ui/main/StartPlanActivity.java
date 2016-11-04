package com.qczb.myclient.ui.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
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
                .add(R.id.container, new StartPlanFragment()).commit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
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

    public static class StartPlanFragment extends ScrollViewFragment {
        static LinearLayout mContainer;
        private long startTime = System.currentTimeMillis();
        private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

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
            map.put("longitude", "");
            map.put("latitude", "");
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
    }
}
