package com.qczb.myclient.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qczb.myclient.R;


/**
 * 2016/6/11
 *
 * @author Michael Zhao
 */
public class MyEditLinearLayout extends LinearLayout {
    private TextView tvContent;
    private TextView tvTitle;
    private ImageView decoration;
    private String formName;

    public MyEditLinearLayout(Context context) {
        super(context);
    }

    public MyEditLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View v = LayoutInflater.from(context).inflate(R.layout.my_edit_linearlayout, this, true);
        tvTitle = (TextView) v.findViewById(R.id.titleOfMyEdit);
        tvContent = (TextView) v.findViewById(R.id.contentOfMyEdit);
        decoration = (ImageView)v.findViewById(R.id.decoration);
        View divider = v.findViewById(R.id.divider);
        View ivArrow = v.findViewById(R.id.iv_arrow);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyEditLinearLayout);
        String title = a.getString(R.styleable.MyEditLinearLayout_titleOfMyEditLinearLayout);
        int textColorOfTitle = a.getColor(R.styleable.MyLinearLayout_textColorOfTitle, getResources().getColor(android.R.color.darker_gray));
        String content = a.getString(R.styleable.MyEditLinearLayout_contentOfMyEditLinearLayout);
        boolean dividerShow = a.getBoolean(R.styleable.MyEditLinearLayout_dividerShowEdit, true);
        Drawable drawable = a.getDrawable(R.styleable.MyEditLinearLayout_decoratedDrawable);
        formName = a.getString(R.styleable.MyEditLinearLayout_formNameOfMyEditLinearLayout);

        tvTitle.setText(title);
        tvTitle.setTextColor(textColorOfTitle);
        tvContent.setText(content);
        decoration.setImageDrawable(drawable);
        decoration.setColorFilter(getResources().getColor(R.color.colorPrimary));
        if (!dividerShow) divider.setVisibility(GONE);

        a.recycle();
    }

    public MyEditLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setContent(CharSequence content) {
        tvContent.setText(content);
    }

    public String getContent() {
        return tvContent.getText().toString();
    }

    public String getFormName() {
        return formName;
    }
}
