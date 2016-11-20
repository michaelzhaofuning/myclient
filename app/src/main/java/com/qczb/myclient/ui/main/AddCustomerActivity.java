package com.qczb.myclient.ui.main;

import android.animation.LayoutTransition;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.photoselector.model.PhotoModel;
import com.qczb.myclient.R;
import com.qczb.myclient.adapter.AreaAdapter;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.BaseResult;
import com.qczb.myclient.base.MyCallBack;
import com.qczb.myclient.base.UserManager;
import com.qczb.myclient.entity.Customer;
import com.qczb.myclient.util.ActivityUtil;
import com.qczb.myclient.view.MyEditLinearLayout;
import com.qczb.myclient.view.PhotoPopupWindow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 2016/8/7
 *
 * @author Michael Zhao
 */
public class AddCustomerActivity extends BaseActivity {
    public ArrayList<PhotoModel> photoModelsMarry = new ArrayList<>();
    public ArrayList<PhotoModel> photoModelsStack = new ArrayList<>();
    public ArrayList<PhotoModel> photoModelsExhibit = new ArrayList<>();
    public ArrayList<PhotoModel> photoModelsVivid = new ArrayList<>();
    public Customer customer;
    private boolean marry, stack, exhibit, vivid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        customer = (Customer) getIntent().getSerializableExtra("item");
        getFragmentManager().beginTransaction()
                .add(R.id.container, new AddCustomerFragment(), "customer").commit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    public void shot(View view) {
        new PhotoPopupWindow(this).show(false);
        marry = true;
        stack = false;
        exhibit = false;
        vivid = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AddCustomerFragment fragment = (AddCustomerFragment) getFragmentManager().findFragmentByTag("customer");
        if (marry)
            PhotoPopupWindow.callBack(this, photoModelsMarry, requestCode, resultCode, data, null, (LinearLayout) fragment.linearLayout.findViewById(R.id.container_photos_marry));
        if (exhibit)
            PhotoPopupWindow.callBack(this, photoModelsExhibit, requestCode, resultCode, data, null, (LinearLayout) fragment.linearLayout.findViewById(R.id.container_photos_exhibit));
        if (vivid)
            PhotoPopupWindow.callBack(this, photoModelsVivid, requestCode, resultCode, data, null, (LinearLayout) fragment.linearLayout.findViewById(R.id.container_photos_vivid));
        if (stack)
            PhotoPopupWindow.callBack(this, photoModelsStack, requestCode, resultCode, data, null, (LinearLayout) fragment.linearLayout.findViewById(R.id.container_photos_stack));
    }

    public void shotVivid(View view) {
        new PhotoPopupWindow(this).show(false);
        marry = false;
        stack = false;
        exhibit = false;
        vivid = true;
    }

    public void shotStack(View view) {
        new PhotoPopupWindow(this).show(false);
        marry = false;
        stack = true;
        exhibit = false;
        vivid = false;
    }

    public void shotExhibit(View view) {
        new PhotoPopupWindow(this).show(false);
        marry = false;
        stack = false;
        exhibit = true;
        vivid = false;
    }

    public static class AddCustomerFragment extends ScrollViewFragment {
        private ArrayList<String> urisMarry = new ArrayList<>();
        private ArrayList<String> urisExibit = new ArrayList<>();
        private ArrayList<String> urisStack = new ArrayList<>();
        private ArrayList<String> urisVivid = new ArrayList<>();
        private LinearLayout weddingFeast;
        private boolean marry, exhibit, stack, vivid;


        @Override
        protected void onSendForm() {
            super.onSendForm();

            sendImgs(((AddCustomerActivity) getActivity()).photoModelsMarry, urisMarry, "marryImgs", new OnSentImgsListener() {
                @Override
                public void onSentImgs() {
                    marry = true;
                    if (exhibit && stack && vivid)
                        submit();
                }
            });

            sendImgs(((AddCustomerActivity) getActivity()).photoModelsExhibit, urisExibit, "CldImgs", new OnSentImgsListener() {
                @Override
                public void onSentImgs() {
                    exhibit = true;
                    if (marry && stack && vivid)
                        submit();                }
            });

            sendImgs(((AddCustomerActivity) getActivity()).photoModelsStack, urisStack, "DtdImgs", new OnSentImgsListener() {
                @Override
                public void onSentImgs() {
                    stack = true;
                    if (exhibit && marry && vivid)
                        submit();
                }
            });

            sendImgs(((AddCustomerActivity) getActivity()).photoModelsVivid, urisVivid, "SdhImgs", new OnSentImgsListener() {
                @Override
                public void onSentImgs() {
                    vivid = true;
                    if (exhibit && stack && marry)
                        submit();
                }
            });


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
                    urisExibit.clear();
                    urisStack.clear();
                    urisMarry.clear();
                }

                @Override
                public void onFailure(Call<BaseResult> call, Throwable t) {
                    super.onFailure(call, t);
                    urisExibit.clear();
                    urisStack.clear();
                    urisMarry.clear();
                }
            });
        }

        @Override
        protected boolean collectInput(LinearLayout linearLayout) {
            boolean isMarryOn = false;
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                if (linearLayout.getChildAt(i) instanceof Switch && linearLayout.getChildAt(i).getId() == R.id.switch_exhibit) {
                    map.put("isCld", ((Switch) linearLayout.getChildAt(i)).isChecked() ? "1" : "0");
                } else if (linearLayout.getChildAt(i) instanceof Switch && linearLayout.getChildAt(i).getId() == R.id.switch_wedding_feast) {
                    isMarryOn = ((Switch) linearLayout.getChildAt(i)).isChecked();
                    if (!isMarryOn)
                        map.put("isMarry", "0");
                } else if (linearLayout.getChildAt(i) instanceof LinearLayout && isMarryOn) {
                    map.put("isMarry", "1");
                    super.collectInput((LinearLayout) linearLayout.getChildAt(i));
                }
            }
            return super.collectInput(linearLayout);
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
            if (customer.isDtd.equals("1")) {
                Switch switchStack = (Switch) linearLayout.findViewById(R.id.switch_stack);
                switchStack.setChecked(true);
            }

            super.reflectToUI(l);
            super.reflectToUI(weddingFeast);

            receiveImgs(customer.getMarryImgs(), (LinearLayout) weddingFeast.findViewById(R.id.container_photos_marry), ((AddCustomerActivity) getActivity()).photoModelsMarry);
            receiveImgs(customer.getDtdImgs(), (LinearLayout) linearLayout.findViewById(R.id.container_photos_stack), ((AddCustomerActivity) getActivity()).photoModelsStack);
            receiveImgs(customer.getSdhImgs(), (LinearLayout) linearLayout.findViewById(R.id.container_photos_vivid), ((AddCustomerActivity) getActivity()).photoModelsVivid);
            receiveImgs(customer.getCldImgs(), (LinearLayout) linearLayout.findViewById(R.id.container_photos_exhibit), ((AddCustomerActivity) getActivity()).photoModelsExhibit);
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


            final MyEditLinearLayout addr = (MyEditLinearLayout) linearLayout.findViewById(R.id.addr);
            addr.findViewById(R.id.contentOfMyEdit).setFocusable(false);
            addr.findViewById(R.id.contentOfMyEdit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ChooseAreaActivity.class);
                    startActivityForResult(intent, 130);
                }
            });



        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK && requestCode == 130) { // choose area
                AreaAdapter.MyArea myArea = (AreaAdapter.MyArea) data.getSerializableExtra("area");
                final MyEditLinearLayout addr = (MyEditLinearLayout) linearLayout.findViewById(R.id.addr);
                addr.setContent(data.getStringExtra("areaName"));
                map.put("AreaId", myArea.id);
            }
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

