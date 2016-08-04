package com.qczb.myclient.ui.publish;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;


import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.BaseResult;
import com.qczb.myclient.ui.main.MainActivity;
import com.qczb.myclient.view.MyEditLinearLayout;
import com.qczb.myclient.view.MyLinearLayout;
import com.qczb.myclient.view.PhotoPopupWindow;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 2016/6/7
 *
 * @author Michael Zhao
 */
public class PublishGoodActivity extends BaseActivity {


    private MyEditLinearLayout content, price, title;
    private LinearLayout mContainer;
//    private ArrayList<PhotoModel> photoModels = new ArrayList<>();
    private String[] aType;
    private String typeId;
    private ArrayList<String> uris = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_good_activity);
        initToolBar("发布商品");
        content = (MyEditLinearLayout) findViewById(R.id.content);
        title = (MyEditLinearLayout) findViewById(R.id.title);
        mContainer = (LinearLayout) findViewById(R.id.container);

    }

    public void publish(View view) {

    }
/*

    public void publish(View view) {
        if (typeId == null || photoModels.size() == 0 || TextUtils.isEmpty(title.getContent()) || TextUtils.isEmpty(content.getContent())) {
            showToastMsg("请完善信息！");
            return;
        }
        for (PhotoModel model : photoModels) {
            MainActivity.uploadGoods(model.getOriginalPath(), new MyCallBack<BaseResult>(this) {
                @Override
                public void onMySuccess(Call<BaseResult> call, Response<BaseResult> response) {
                    uris.add(response.body().getResult().get("url").getAsString());
                    if (uris.size() == photoModels.size()) {
                        StringBuilder sb = new StringBuilder();

                        int i = 0;
                        for (String s : uris) {
                            sb.append(s);
                            if (i++ < uris.size() - 1)
                                sb.append(",");
                        }
                        getHttpService().submitGoods(UserManager.getUID(), UserManager.getUser().getToken(),
                                title.getContent(), typeId, content.getContent()
                                , sb.toString()).enqueue(new MyCallBack<BaseResult>(PublishGoodActivity.this) {
                            @Override
                            public void onMySuccess(Call<BaseResult> call, Response<BaseResult> response) {
                                ActivityUtil.startActivityForResult(PublishGoodActivity.this, SuccessActivity.class);
                                finish();
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

    public void addPicture(View view) {
        new PhotoPopupWindow(this).show(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoPopupWindow.callBack(this, photoModels, requestCode, resultCode, data, null, mContainer);
    }*/


}
