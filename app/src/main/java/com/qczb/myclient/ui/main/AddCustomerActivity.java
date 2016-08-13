package com.qczb.myclient.ui.main;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.photoselector.model.PhotoModel;
import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.BaseResult;
import com.qczb.myclient.base.MyCallBack;
import com.qczb.myclient.util.ActivityUtil;
import com.qczb.myclient.view.PhotoPopupWindow;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 2016/8/7
 *
 * @author Michael Zhao
 */
public class AddCustomerActivity extends BaseActivity {
    public ArrayList<PhotoModel> photoModels = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        getFragmentManager().beginTransaction()
                .add(R.id.container, new AddCustomerFragment()).commit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    public void shot(View view) {
        new PhotoPopupWindow(this).show(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoPopupWindow.callBack(this, photoModels, requestCode, resultCode, data, null, AddCustomerFragment.mContainer);
    }

    public static class AddCustomerFragment extends ScrollViewFragment {
        static LinearLayout mContainer;
        private ArrayList<String> uris = new ArrayList<>();


        @Override
        protected void onSendForm() {
            final List<PhotoModel> photoModels = ((AddCustomerActivity) getActivity()).photoModels;
            for (PhotoModel model : photoModels) {
                MainActivity.uploadGoods(model.getOriginalPath(), new MyCallBack<BaseResult>((BaseActivity) getActivity()) {
                    @Override
                    public void onMySuccess(Call<BaseResult> call, Response<BaseResult> response) {
                        uris.add(response.body().getData().get(0).getAsJsonObject().get("vcImg").getAsString());
                        if (uris.size() == photoModels.size()) {
                            StringBuilder sb = new StringBuilder();

                            int i = 0;
                            for (String s : uris) {
                                sb.append(s);
                                if (i++ < uris.size() - 1)
                                    sb.append(",");
                            }
                            map.put("img", sb.toString());
                            getHttpService().submitCustomer(map).enqueue(new MyCallBack<BaseResult>((BaseActivity) getActivity()) {
                                @Override
                                public void onMySuccess(Call<BaseResult> call, Response<BaseResult> response) {
                                    ActivityUtil.startActivityForResult(getActivity(), SuccessActivity.class);
                                    getActivity().finish();
                                }
                            });
                        }
                    }

                    @Override
                    public void onMyFailure(Call<BaseResult> call, Response<BaseResult> response) {
                        super.onMyFailure(call, response);
                        uris.clear();
                    }
                });
            }
        }

        @Override
        public void onViewCreated(View v, Bundle savedInstanceState) {
            super.onViewCreated(v, savedInstanceState);
            mContainer = (LinearLayout) v.findViewById(R.id.container_photos);
            final LinearLayout weddingFeast = (LinearLayout) v.findViewById(R.id.wedding_feast);
            Switch s = (Switch) v.findViewById(R.id.switch_wedding_feast);
            weddingFeast.setLayoutTransition(new LayoutTransition());
            s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        weddingFeast.setVisibility(View.VISIBLE);
                    else
                        weddingFeast.setVisibility(View.GONE);
                }
            });
        }

        @Override
        protected int getScrollViewContentLayoutId() {
            return R.layout.scroll_view_add_customer;
        }

        @Override
        protected int getTopImageID() {
            return R.mipmap.add_customer;
        }

        @Override
        protected String getTitle() {
            return "新增客户";
        }
    }
}
