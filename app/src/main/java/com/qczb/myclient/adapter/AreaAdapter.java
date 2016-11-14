package com.qczb.myclient.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.entity.Area;
import com.qczb.myclient.entity.Provider;
import com.qczb.myclient.ui.main.AddProviderActivity;
import com.qczb.myclient.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 2016/11/14
 *
 * @author Michael Zhao
 */
public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder> implements View.OnClickListener{
    private List<Area> mList;
    private BaseActivity mActivity;
    private MyArea myArea;
    public AreaAdapter(BaseActivity activity, List<Area> list) {
        mList = list;
        mActivity = activity;

        for (Area area : mList) {
            if (area.getPId().equals("0")) {
                myArea = new MyArea();
                myArea.id = area.getId();
                myArea.name = area.getName();
            }
        }
        processData(myArea);
    }

    private void processData(MyArea parent) {
        for (Area area : mList) {
            if (area.getPId().equals(parent.id)) {
                MyArea myArea = new MyArea();
                myArea.id = area.getId();
                myArea.name = area.getName();
                parent.children.add(myArea);
                processData(myArea);
            }
        }
    }

    @Override
    public AreaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AreaAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.area_item, parent, false));
    }

    @Override
    public void onBindViewHolder(AreaAdapter.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(position);



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        int p = (int) v.getTag();

        Bundle b = new Bundle();
        b.putSerializable("item", mList.get(p));
        ActivityUtil.startActivityForResult(mActivity, AddProviderActivity.class, b, 100);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, location, date, status, company, companyTel, companyAddress;
        ImageView avatar;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            location = (TextView) itemView.findViewById(R.id.location);
            date = (TextView) itemView.findViewById(R.id.date);
            status = (TextView) itemView.findViewById(R.id.status);
            title = (TextView) itemView.findViewById(R.id.title);
            companyAddress = (TextView) itemView.findViewById(R.id.company_address);
            company = (TextView) itemView.findViewById(R.id.company);
            companyTel = (TextView) itemView.findViewById(R.id.company_tel);
        }
    }

    public static class MyArea {
        public List<MyArea> children = new ArrayList<>();
        public String name;
        public String id;
    }
}
