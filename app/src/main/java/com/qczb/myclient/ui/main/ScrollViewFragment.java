package com.qczb.myclient.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.photoselector.model.PhotoModel;
import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.BaseFragment;
import com.qczb.myclient.base.MyApplication;
import com.qczb.myclient.entity.Item;
import com.qczb.myclient.util.ActivityUtil;
import com.qczb.myclient.view.MyEditLinearLayout;
import com.qczb.myclient.view.PhotoPopupWindow;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * 2016/8/4
 *
 * @author Michael Zhao
 */
public abstract class ScrollViewFragment extends BaseFragment {
    ImageView topImageView;
    protected LinearLayout linearLayout;
    Map<String, String> map = new HashMap<>();
    protected Object item;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.coordinator_fragment, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        topImageView = (ImageView) v.findViewById(R.id.top_image);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getTitle());
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.navigation_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        topImageView.setImageDrawable(getResources().getDrawable(getTopImageID()));
        NestedScrollView nestedScrollView = (NestedScrollView) v.findViewById(R.id.scroll_view);
        linearLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(getScrollViewContentLayoutId(), null);
        nestedScrollView.addView(linearLayout);
        if (linearLayout.findViewById(R.id.commit) != null)
            linearLayout.findViewById(R.id.commit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!collectInput(linearLayout)) showToastMsg("请完善信息");
                    else onSendForm();
                }
            });

        if (item == null) return;

        reflectToUI(linearLayout);

    }

    protected void reflectToUI(LinearLayout linearLayout) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (linearLayout.getChildAt(i) instanceof MyEditLinearLayout) {
                MyEditLinearLayout myEditLinearLayout = (MyEditLinearLayout) linearLayout.getChildAt(i);
                String formName = myEditLinearLayout.getFormName();
                for (Field field : getItemClass().getDeclaredFields()) {
                    if (field.getName().equals(formName)) {
                        try {
                            if (field.get(item) != null)
                                myEditLinearLayout.setContent(field.get(item).toString());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    protected abstract Class getItemClass();

    protected boolean collectInput(LinearLayout linearLayout) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (linearLayout.getChildAt(i) instanceof MyEditLinearLayout) {
                MyEditLinearLayout myEditLinearLayout = (MyEditLinearLayout) linearLayout.getChildAt(i);
                if (TextUtils.isEmpty(myEditLinearLayout.getContent())) return false;
                map.put(myEditLinearLayout.getFormName(), myEditLinearLayout.getContent());
            }
        }
        return true;
    }

    protected void sendImgs(final List<PhotoModel> photoModels, final List<String> uris, final String form, final OnSentImgsListener onSentImgsListener) {
        if (!photoModels.isEmpty()) {
            for (PhotoModel model : photoModels) {
                RequestParams rp = new RequestParams();
                try {
                    if (model.getOriginalPath() != null)
                        rp.put("vcImg", new File(model.getOriginalPath()));
                    else if (model.getUri() != null) uris.add(model.getUri());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if (model.getOriginalPath() != null)
                    new AsyncHttpClient().post(getActivity(), MyApplication.BASE_URL + "FileUploadServlet", rp, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            Log.e("file", response.toString());
                            //{"fileURL":"http:\/\/192.168.1.101:8080\/kxw\/uploads\/armedhead.jpg","status":"1","message":"操作成功"}
                            if (response.optString("status").equals("1")) {
                                uris.add(response.optString("fileURL"));
                                if (uris.size() == photoModels.size()) {
                                    StringBuilder sb = new StringBuilder();

                                    int i = 0;
                                    for (String s : uris) {
                                        sb.append(s);
                                        if (i++ < uris.size() - 1)
                                            sb.append(",");
                                    }
                                    map.put(form, sb.toString());
                                    onSentImgsListener.onSentImgs();
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            uris.clear();
                        }
                    });

            }

            if (uris.size() == photoModels.size()) {
                StringBuilder sb = new StringBuilder();

                int i = 0;
                for (String s : uris) {
                    sb.append(s);
                    if (i++ < uris.size() - 1)
                        sb.append(",");
                }
                map.put(form, sb.toString());
                onSentImgsListener.onSentImgs();

            }
        } else {
            onSentImgsListener.onSentImgs();

        }
    }

    protected void receiveImgs(final String imgs, LinearLayout container, ArrayList<PhotoModel> photoModels) {
        // image
        if (!TextUtils.isEmpty(imgs)) {
            String[] imgsArray = imgs.split(",");
            for (String s : imgsArray) {
                photoModels.add(new PhotoModel(s, 0));
            }
            PhotoPopupWindow.setImages((BaseActivity) getActivity(), photoModels, null, container, photoModels);
        }
    }

    protected void success() {
        Bundle b = new Bundle();
        b.putString("title", getTitle() + "成功");
        ActivityUtil.startActivityForResult(getActivity(), SuccessActivity.class, b, 100);
        getActivity().finish();
    }

    protected void onSendForm() {
        if (item == null) {
            map.put("editModel", "add");
        } else {
            map.put("editModel", "edit");
        }
    }

    protected abstract int getScrollViewContentLayoutId();

    protected abstract int getTopImageID();

    protected abstract String getTitle();

    public static interface OnSentImgsListener {
        void onSentImgs();
    }
}
