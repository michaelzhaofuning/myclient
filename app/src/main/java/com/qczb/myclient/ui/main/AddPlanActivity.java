package com.qczb.myclient.ui.main;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.qczb.myclient.R;
import com.qczb.myclient.adapter.ListItemAdapter;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.BaseResult;
import com.qczb.myclient.base.MyCallBack;
import com.qczb.myclient.base.UserManager;
import com.qczb.myclient.entity.Item;
import com.qczb.myclient.view.MyEditLinearLayout;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.GregorianCalendar;

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


        }

        @Override
        protected void onSendForm() {
            super.onSendForm();

            map.put("salesmanId", UserManager.getUID());
            map.put("salesmanName", UserManager.getUser().getName());
            map.put("state", "0");
            getHttpService().submitPlan(map).enqueue(new MyCallBack<BaseResult>((BaseActivity) getActivity()) {
                @Override
                public void onMySuccess(Call<BaseResult> call, Response<BaseResult> response) {
                    success();
                }
            });
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
