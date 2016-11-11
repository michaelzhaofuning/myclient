package com.qczb.myclient.ui.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.qczb.myclient.R;
import com.qczb.myclient.adapter.ListItemAdapter;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.BaseResult;
import com.qczb.myclient.base.MyCallBack;
import com.qczb.myclient.base.UserManager;
import com.qczb.myclient.entity.Customer;
import com.qczb.myclient.entity.Goods;
import com.qczb.myclient.entity.Item;
import com.qczb.myclient.entity.Provider;
import com.qczb.myclient.entity.Stock;
import com.qczb.myclient.view.MyEditLinearLayout;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 2016/11/11
 *
 * @author Michael Zhao
 */

public class AddStockActivity extends BaseActivity {
    public Stock item;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        item = (Stock) getIntent().getSerializableExtra("item");

        getFragmentManager().beginTransaction()
                .add(R.id.container, new AddStockActivity.AddStockFragment()).commit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }




    public static class AddStockFragment extends ScrollViewFragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            item = ((AddStockActivity) getActivity()).item;

            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onViewCreated(View v, Bundle savedInstanceState) {
            super.onViewCreated(v, savedInstanceState);


            final MyEditLinearLayout editLinearLayoutbid = (MyEditLinearLayout) linearLayout.findViewById(R.id.DId);
            editLinearLayoutbid.findViewById(R.id.contentOfMyEdit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                }
            });
            editLinearLayoutbid.findViewById(R.id.contentOfMyEdit).setFocusable(false);

            final MyEditLinearLayout editLinearLayoutbusiness = (MyEditLinearLayout) linearLayout.findViewById(R.id.goods);
            editLinearLayoutbusiness.findViewById(R.id.contentOfMyEdit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getHttpService().getGoods().enqueue(new MyCallBack<BaseResult>((BaseActivity) getActivity()) {
                        @Override
                        public void onMySuccess(Call<BaseResult> call, Response<BaseResult> response) {
                            final List<Goods> goodses =  JSON.parseArray(response.body().getData().toString(), Goods.class);
                            final CharSequence[] a = new String[goodses.size()];

                            int i=0;
                            for (Goods c : goodses) {
                                a[i++] = c.getDName();
                            }

                            new AlertDialog.Builder(getActivity()).setSingleChoiceItems(a, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    editLinearLayoutbusiness.setContent(a[which]);
                                    editLinearLayoutbid.setContent(goodses.get(which).getDId());
                                }
                            }).setTitle("请选择商品").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                        }
                    });

                }
            });
            editLinearLayoutbusiness.findViewById(R.id.contentOfMyEdit).setFocusable(false);


        }

        @Override
        protected void onSendForm() {
            super.onSendForm();

            if (item !=null) map.put("stockId", ((Stock)item).getStockId());
            map.put("BId", ListItemAdapter.bid);
            map.put("vcUserid", UserManager.getUID());
            getHttpService().submitStock(map).enqueue(new MyCallBack<BaseResult>((BaseActivity) getActivity()) {
                @Override
                public void onMySuccess(Call<BaseResult> call, Response<BaseResult> response) {
                    success();
                }
            });
        }

        @Override
        protected int getScrollViewContentLayoutId() {
            return R.layout.scroll_view_add_stock;
        }

        @Override
        protected int getTopImageID() {
            return R.mipmap.add_provider;
        }

        @Override
        protected String getTitle() {
            if (item == null) return "新增库存";
            else  return "编辑库存";
        }

        /**
         * editModel:edit/add（必填）
         stockId:库存ID（编辑状态下必填）
         DId：商品ID（必填）
         BId：商家ID（必填）
         vcUserid：业务员ID（必填）
         stockNums：库存数量（必填）
         * @return
         */
        @Override
        protected Class getItemClass() {
            return Stock.class;
        }
    }
}
