package com.qczb.myclient.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseFragment;
import com.qczb.myclient.entity.Item;
import com.qczb.myclient.util.ActivityUtil;
import com.qczb.myclient.view.MyEditLinearLayout;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 2016/8/4
 *
 * @author Michael Zhao
 */
public abstract class ScrollViewFragment extends BaseFragment {
    ImageView topImageView;
    private LinearLayout linearLayout;
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

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (linearLayout.getChildAt(i) instanceof MyEditLinearLayout) {
                MyEditLinearLayout myEditLinearLayout = (MyEditLinearLayout) linearLayout.getChildAt(i);
                String formName = myEditLinearLayout.getFormName();
                for (Field field : getItemClass().getDeclaredFields()) {
                    if (field.getName().equals(formName)) {
                        try {
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
        boolean isMarryOn = false;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (linearLayout.getChildAt(i) instanceof MyEditLinearLayout) {
                MyEditLinearLayout myEditLinearLayout = (MyEditLinearLayout) linearLayout.getChildAt(i);
                if (TextUtils.isEmpty(myEditLinearLayout.getContent())) return false;
                map.put(myEditLinearLayout.getFormName(), myEditLinearLayout.getContent());
            } else if (linearLayout.getChildAt(i) instanceof Switch && linearLayout.getChildAt(i).getId() == R.id.switch_exhibit) {
                map.put("isCld", ((Switch) linearLayout.getChildAt(i)).isChecked() ? "1" : "0");
            } else if (linearLayout.getChildAt(i) instanceof Switch && linearLayout.getChildAt(i).getId() == R.id.switch_wedding_feast) {
                isMarryOn = ((Switch) linearLayout.getChildAt(i)).isChecked();
                if (!isMarryOn)
                    map.put("isMarry", "0");
            } else if (linearLayout.getChildAt(i) instanceof LinearLayout && isMarryOn) {
                map.put("isMarry", "1");
                collectInput((LinearLayout) linearLayout.getChildAt(i));
            }
        }
        return true;
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
}
