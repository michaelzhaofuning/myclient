package com.qczb.myclient.ui.main;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.photoselector.model.PhotoModel;
import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.BaseResult;
import com.qczb.myclient.base.MyCallBack;
import com.qczb.myclient.base.UserManager;
import com.qczb.myclient.entity.Customer;
import com.qczb.myclient.util.ActivityUtil;
import com.qczb.myclient.view.PhotoPopupWindow;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 2016/8/7
 *
 * @author Michael Zhao
 */
public class AddCustomerActivity extends BaseActivity {
    public ArrayList<PhotoModel> photoModels = new ArrayList<>();
    public Customer customer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        customer = (Customer) getIntent().getSerializableExtra("item");
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
        private LinearLayout weddingFeast;


        @Override
        protected void onSendForm() {
            final List<PhotoModel> photoModels = ((AddCustomerActivity) getActivity()).photoModels;
            if (!photoModels.isEmpty()) {
                for (PhotoModel model : photoModels) {
                    RequestParams rp = new RequestParams();
                    try {
                        rp.put("vcImg", new File(model.getOriginalPath()));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    new AsyncHttpClient().post(getActivity(), "http://test.kaopuren.cn/kj/uploadPic.htm", rp, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                        }
                    });
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
                                submit();
                            }
                        }

                        @Override
                        public void onMyFailure(Call<BaseResult> call, Response<BaseResult> response) {
                            super.onMyFailure(call, response);
                            uris.clear();
                        }
                    });
                }
            } else {
                submit();
            }
        }

        private void submit() {
            map.put("salesmanId", UserManager.getUID());
            map.put("salesmanName", UserManager.getUser().getName());

            getHttpService().submitCustomer(map).enqueue(new MyCallBack<BaseResult>((BaseActivity) getActivity()) {
                @Override
                public void onMySuccess(Call<BaseResult> call, Response<BaseResult> response) {
                    ActivityUtil.startActivityForResult(getActivity(), SuccessActivity.class);
                    getActivity().finish();
                }
            });
        }

        @Override
        protected void reflectToUI() {
            weddingFeast = (LinearLayout) getView().findViewById(R.id.wedding_feast);

            Customer customer = (Customer) item;
            if (((Customer)item).isMarry.equals("1"))
                weddingFeast.setVisibility(View.VISIBLE);
            super.reflectToUI();
            if (customer.isCld.equals("1")) {
                Switch switchExhibit = (Switch) linearLayout.findViewById(R.id.switch_exhibit);
                switchExhibit.setChecked(true);
            }
            if (customer.isMarry.equals("1")) {
                Switch switchMarry = (Switch) linearLayout.findViewById(R.id.switch_exhibit);
                switchMarry.setChecked(true);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            item = ((AddCustomerActivity) getActivity()).customer;
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onViewCreated(View v, Bundle savedInstanceState) {
            super.onViewCreated(v, savedInstanceState);
            mContainer = (LinearLayout) v.findViewById(R.id.container_photos);
            Switch s = (Switch) v.findViewById(R.id.switch_wedding_feast);
            weddingFeast = (LinearLayout) getView().findViewById(R.id.wedding_feast);

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
        protected Class getItemClass() {
            return Customer.class;
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
            if (item == null) return "新增客户";
            else return "编辑客户";

        }
    }
}
