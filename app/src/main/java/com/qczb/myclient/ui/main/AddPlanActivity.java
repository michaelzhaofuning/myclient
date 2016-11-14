package com.qczb.myclient.ui.main;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Switch;

import com.alibaba.fastjson.JSON;
import com.qczb.myclient.R;
import com.qczb.myclient.adapter.AreaAdapter;
import com.qczb.myclient.adapter.ListItemAdapter;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.BaseResult;
import com.qczb.myclient.base.MyCallBack;
import com.qczb.myclient.base.UserManager;
import com.qczb.myclient.entity.Customer;
import com.qczb.myclient.entity.Item;
import com.qczb.myclient.view.MyEditLinearLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 2016/8/7
 *
 * @author Michael Zhao
 */
public class AddPlanActivity extends BaseActivity {
    public Item item;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = (Item) getIntent().getSerializableExtra("item");

        setContentView(R.layout.base_activity);
        getFragmentManager().beginTransaction()
                .add(R.id.container, new AddPlanFragment()).commit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    public static class AddPlanFragment extends ScrollViewFragment {


        private List<Customer> customers;
        private List<Customer> customersDelive = new ArrayList<>();
        private CharSequence[] a;
        private boolean[] b;
        private int j;
        private MyEditLinearLayout editLinearLayoutbid;
        private MyEditLinearLayout editLinearLayoutbusiness;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            item = ((AddPlanActivity) getActivity()).item;

            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onViewCreated(View v, Bundle savedInstanceState) {
            super.onViewCreated(v, savedInstanceState);
            LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.scroll_view_content);

            final MyEditLinearLayout editLinearLayout = (MyEditLinearLayout) linearLayout.findViewById(R.id.pick_date);
            editLinearLayout.findViewById(R.id.contentOfMyEdit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = GregorianCalendar.getInstance();
                    new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            editLinearLayout.setContent(year + "-" + (monthOfYear+1) + "-" + dayOfMonth );
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) + 1).show();
                }
            });
            editLinearLayout.findViewById(R.id.contentOfMyEdit).setFocusable(false);

            editLinearLayoutbid = (MyEditLinearLayout) linearLayout.findViewById(R.id.bid);
            editLinearLayoutbid.findViewById(R.id.contentOfMyEdit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            editLinearLayoutbid.findViewById(R.id.contentOfMyEdit).setFocusable(false);

            editLinearLayoutbusiness = (MyEditLinearLayout) linearLayout.findViewById(R.id.business);
            editLinearLayoutbusiness.findViewById(R.id.contentOfMyEdit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ChooseAreaActivity.class);
                    startActivityForResult(intent, 130);
                }
            });
            editLinearLayoutbusiness.findViewById(R.id.contentOfMyEdit).setFocusable(false);


        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK && requestCode == 130) { // choose area
                AreaAdapter.MyArea myArea = (AreaAdapter.MyArea) data.getSerializableExtra("area");
//                final MyEditLinearLayout addr = (MyEditLinearLayout) linearLayout.findViewById(R.id.addr);
//                addr.setContent(data.getStringExtra("areaName"));
//                map.put("AreaId", myArea.id);
                getHttpService().getCustomers(UserManager.getUID(), myArea.id).enqueue(new MyCallBack<BaseResult>((BaseActivity) getActivity()) {
                    @Override
                    public void onMySuccess(Call<BaseResult> call, Response<BaseResult> response) {
                        customersDelive.clear();
                        customers = JSON.parseArray(response.body().getData().toString(), Customer.class);

                        if (customers == null || customers.isEmpty()) {
                            showToastMsg("该区域暂无商家！");
                            return;
                        }

                        a = new String[customers.size()];
                        b = new boolean[customers.size()];

                        int i=0;
                        for (Customer c : customers) {
                            a[i++] = c.BName;
                        }

                        new AlertDialog.Builder(getActivity()).setMultiChoiceItems(a, b, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                            }
                        }).setTitle("请选择商家").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StringBuilder sb = new StringBuilder();
                                for (int j = 0; j<customers.size(); j++) {
                                    if (b[j]) {
                                        customersDelive.add(customers.get(j));
                                        sb.append(customers.get(j).BName);
                                        sb.append(",");
                                    }

                                }
                                editLinearLayoutbid.setContent(customers.get(0).BId);

                                editLinearLayoutbusiness.setContent(sb.toString());

                                dialog.dismiss();
                            }
                        }).show();
                    }
                });

            }
        }

        @Override
        protected void onSendForm() {
            super.onSendForm();
            if (item !=null) map.put("Vid", ((Item)item).VId);

            map.put("salesmanId", UserManager.getUID());
            map.put("salesmanName", UserManager.getUser().getName());
            map.put("state", "0");

            if (item == null) {
                for (Customer c : customersDelive) {
                    map.put("BName", c.BName);
                    map.put("Bid", c.BId);
                    getHttpService().submitPlan(map).enqueue(new MyCallBack<BaseResult>((BaseActivity) getActivity()) {
                        @Override
                        public void onMySuccess(Call<BaseResult> call, Response<BaseResult> response) {
                            if (j++ == 0)
                                success();
                        }
                    });
                }
            } else {
                getHttpService().submitPlan(map).enqueue(new MyCallBack<BaseResult>((BaseActivity) getActivity()) {
                    @Override
                    public void onMySuccess(Call<BaseResult> call, Response<BaseResult> response) {
                        if (j++ == 0)
                            success();
                    }
                });
            }
        }

        @Override
        protected int getScrollViewContentLayoutId() {
            return R.layout.scroll_view_add_plan;
        }

        @Override
        protected int getTopImageID() {
            return R.mipmap.add_plan;
        }

        @Override
        protected String getTitle() {
            if (item == null)
                return "新增拜访";
            else
                return "编辑拜访";
        }

        @Override
        protected Class getItemClass() {
            return Item.class;
        }
    }
}
