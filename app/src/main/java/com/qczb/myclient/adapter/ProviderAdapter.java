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
import com.qczb.myclient.entity.Provider;
import com.qczb.myclient.ui.main.AddProviderActivity;
import com.qczb.myclient.util.ActivityUtil;

import java.util.List;

/**
 * 2016/11/2
 *
 * @author Michael Zhao
 */
public class ProviderAdapter extends RecyclerView.Adapter<ProviderAdapter.ViewHolder> implements View.OnClickListener{
    private List<Provider> mList;
    private BaseActivity mActivity;
    public ProviderAdapter(BaseActivity activity, List<Provider> list) {
        mList = list;
        mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.provider_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(position);
        holder.title.setText(mList.get(position).SName);
        holder.date.setText(mList.get(position).addTime);
        holder.location.setText(mList.get(position).SAddr);
        holder.status.setText(mList.get(position).SPhone);
        holder.company.setText(mList.get(position).SCompany);
        holder.companyAddress.setText(mList.get(position).SCompanyAddr);
        holder.companyTel.setText(mList.get(position).SCompanyTel);


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
}
