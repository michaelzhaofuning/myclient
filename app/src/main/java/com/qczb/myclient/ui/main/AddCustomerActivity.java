package com.qczb.myclient.ui.main;

import android.animation.LayoutTransition;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.photoselector.model.PhotoModel;
import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.BaseResult;
import com.qczb.myclient.base.MyApplication;
import com.qczb.myclient.base.MyCallBack;
import com.qczb.myclient.base.UserManager;
import com.qczb.myclient.entity.Customer;
import com.qczb.myclient.entity.Item;
import com.qczb.myclient.util.ActivityUtil;
import com.qczb.myclient.view.MyEditLinearLayout;
import com.qczb.myclient.view.PhotoPopupWindow;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
    public ArrayList<PhotoModel> photoModelsMarry = new ArrayList<>();
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
        PhotoPopupWindow.callBack(this, photoModelsMarry, requestCode, resultCode, data, null, AddCustomerFragment.mContainer);
    }

    public static class AddCustomerFragment extends ScrollViewFragment {
        static LinearLayout mContainer;
        private ArrayList<String> uris = new ArrayList<>();
        private LinearLayout weddingFeast;


        @Override
        protected void onSendForm() {
            super.onSendForm();

            final List<PhotoModel> photoModels = ((AddCustomerActivity) getActivity()).photoModelsMarry;
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
                                    map.put("marryImgs", sb.toString());
                                    submit();
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
                    map.put("marryImgs", sb.toString());
                    submit();
                }
            } else {
                submit();
            }
        }

        private void submit() {
            map.put("salesmanId", UserManager.getUID());
            map.put("salesmanName", UserManager.getUser().getName());

            if (item !=null) map.put("BId", ((Customer)item).BId);

            getHttpService().submitCustomer(map).enqueue(new MyCallBack<BaseResult>((BaseActivity) getActivity()) {
                @Override
                public void onMySuccess(Call<BaseResult> call, Response<BaseResult> response) {
                    ActivityUtil.startActivityForResult(getActivity(), SuccessActivity.class);
                    getActivity().finish();
                }

                @Override
                public void onMyFailure(Call<BaseResult> call, Response<BaseResult> response) {
                    super.onMyFailure(call, response);
                    uris.clear();
                }

                @Override
                public void onFailure(Call<BaseResult> call, Throwable t) {
                    super.onFailure(call, t);
                    uris.clear();
                }
            });
        }

        @Override
        protected void reflectToUI(LinearLayout l) {

            weddingFeast = (LinearLayout) getView().findViewById(R.id.wedding_feast);

            Customer customer = (Customer) item;
            if (((Customer)item).isMarry.equals("1"))
                weddingFeast.setVisibility(View.VISIBLE);
            if (customer.isCld.equals("1")) {
                Switch switchExhibit = (Switch) linearLayout.findViewById(R.id.switch_exhibit);
                switchExhibit.setChecked(true);
            }
            if (customer.isMarry.equals("1")) {
                Switch switchMarry = (Switch) linearLayout.findViewById(R.id.switch_wedding_feast);
                switchMarry.setChecked(true);
            }

            super.reflectToUI(l);
            super.reflectToUI(weddingFeast);

            // image
            if (!TextUtils.isEmpty(customer.getMarryImgs())) {
                String[] imgs = customer.getMarryImgs().split(",");
                final ArrayList<PhotoModel> photoModels = ((AddCustomerActivity) getActivity()).photoModelsMarry;
                for (String s : imgs) {
                    photoModels.add(new PhotoModel(s, 0));
                }
                PhotoPopupWindow.setImages((BaseActivity) getActivity(), photoModels, null, (LinearLayout) weddingFeast.findViewById(R.id.container_photos), photoModels);
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

            final MyEditLinearLayout boss_birth = (MyEditLinearLayout) linearLayout.findViewById(R.id.boss_birthday);
            boss_birth.findViewById(R.id.contentOfMyEdit).setFocusable(false);
            boss_birth.findViewById(R.id.contentOfMyEdit).setOnClickListener(new DateListener(boss_birth));

            final MyEditLinearLayout boss_birth_spouse = (MyEditLinearLayout) linearLayout.findViewById(R.id.boss_spouse_birth);
            boss_birth_spouse.findViewById(R.id.contentOfMyEdit).setFocusable(false);
            boss_birth_spouse.findViewById(R.id.contentOfMyEdit).setOnClickListener(new DateListener(boss_birth_spouse));

            final MyEditLinearLayout feast_time = (MyEditLinearLayout) linearLayout.findViewById(R.id.feast_time);
            feast_time.findViewById(R.id.contentOfMyEdit).setFocusable(false);
            feast_time.findViewById(R.id.contentOfMyEdit).setOnClickListener(new DateListener(feast_time));



        }

        /**
         * pick date
         */
        private class DateListener implements View.OnClickListener {
            MyEditLinearLayout myEditLinearLayout;
            public DateListener(MyEditLinearLayout linearLayout) {
                myEditLinearLayout = linearLayout;
            }

            @Override
            public void onClick(View v) {
                Calendar calendar = GregorianCalendar.getInstance();
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        myEditLinearLayout.setContent(year + "-" + (monthOfYear+1) + "-" + dayOfMonth );
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) + 1).show();
            }
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

        /**
         * 客户信息：
         editModel:edit/add(必填)
         BId:商家ID（编辑状态下必填）
         BName:商户名称
         salesmanId:业务员(必填)
         salesmanName:业务员姓名(必填)
         BTelephone:商户电话
         BAddress:商户地址
         BLevel:商户级别，ABCD
         isDtd:是否堆头店
         isCld:是否陈列店
         isMarry:是否婚宴
         AreaId:区域ID
         DtdImgs：堆头店照片
         CldImgs：陈列店照片
         SdhImgs：生动化照片
         BossName：老板姓名
         BossTel：老板电话
         BossBirthday：老板生日
         BossSpouseName：老板配偶姓名
         BossSpouseTel：老板配偶电话
         BossSpouseBirthday：老板配偶生日
         * @return
         */
        @Override
        protected String getTitle() {
            if (item == null) return "新增客户";
            else return "编辑客户";

        }
    }
}

