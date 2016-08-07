package com.qczb.myclient.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.entity.Item;
import com.qczb.myclient.ui.main.PlanDetailActivity;
import com.qczb.myclient.ui.main.StartPlanActivity;
import com.qczb.myclient.util.ActivityUtil;

import java.util.List;

/**
 * 2016/5/14
 *
 * @author Michael Zhao
 */
public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> implements View.OnClickListener {
    private List<Item> mList;
    private BaseActivity mActivity;
    private String mWhich;
    public ListItemAdapter(BaseActivity activity, List<Item> list, String which) {
        mList = list;
        mActivity = activity;
        mWhich = which;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(position);
        if (mWhich.equals("plan")) {
            holder.title.setText(mList.get(position).visitPlanTitle);
            holder.date.setText(mList.get(position).visitPlanTime);
            holder.location.setText(mList.get(position).visitPlanContent);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        int p = (int) v.getTag();

        ActivityUtil.startActivityForResult(mActivity, StartPlanActivity.class);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, location, date, status;
        ImageView avatar;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            location = (TextView) itemView.findViewById(R.id.location);
            date = (TextView) itemView.findViewById(R.id.date);
            status = (TextView) itemView.findViewById(R.id.status);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}