package com.qczb.myclient.ui.main;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.qczb.myclient.R;
import com.qczb.myclient.adapter.ListItemAdapter;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.BaseListFragment;
import com.qczb.myclient.base.BaseResult;
import com.qczb.myclient.entity.Item;

import retrofit2.Call;
import retrofit2.Response;


/**
 * 2016/8/4
 *
 * @author Michael Zhao
 */
public class ListItemFragment extends BaseListFragment<BaseResult, Item> {
    ImageView topImageView;
    TextView topTextView;
    private float myLastY = -1;

    @Override
    public Call<BaseResult> getRetrofitCall() {
        return null;
    }

    @Override
    protected Class<Item> getItemClass() {
        return Item.class;
    }

    @Override
    protected JsonArray getJsonArray(Response<BaseResult> response) {
        return null;
    }

    @Override
    protected void initActionBar() {
        View v = getActivity().getLayoutInflater().inflate(R.layout.top_layout, mAboveList);
        topImageView = (ImageView) v.findViewById(R.id.top_image);
        topTextView = (TextView) v.findViewById(R.id.top_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @Override
    protected String getCacheKey() {
        return null;
    }

    @Override
    public Item newEntity() {
        return new Item();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new ListItemAdapter((BaseActivity) getActivity(), mList);
    }

    @Override
    protected boolean onMyTouch(View view, MotionEvent event) {
        float y = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // initial
                if (myLastY == -1)
                    myLastY = y;

                float dy = y - myLastY;
                Log.e("test", "dy: " + dy + "  paddingBottom: " + mAboveList.getPaddingBottom());
                if (dy <= 0 && mAboveList.getHeight() > getResources().getDimensionPixelSize(R.dimen.app_bar_height)) {
                    if ((mAboveList.getPaddingBottom() + (int) (dy / 2)) > (getResources().getDimensionPixelSize(R.dimen.app_bar_height) - getResources().getDimensionPixelSize(R.dimen.top_layout_height)))
                        mAboveList.setPadding(0, 0, 0, mAboveList.getPaddingBottom() + (int) (dy / 2));
                    else
                        mAboveList.setPadding(0, 0, 0, (getResources().getDimensionPixelSize(R.dimen.app_bar_height) - getResources().getDimensionPixelSize(R.dimen.top_layout_height)));
                    return true;
                }

                if (dy > 0 && mAboveList.getHeight() < getResources().getDimensionPixelSize(R.dimen.top_layout_height)) {
                    if ((mAboveList.getPaddingBottom() + (int) (dy / 2)) < 0)
                        mAboveList.setPadding(0, 0, 0, mAboveList.getPaddingBottom() + (int) (dy / 2));
                    else
                        mAboveList.setPadding(0, 0, 0, 0);
                    return true;
                }
                myLastY = y;
                break;
            default:
                myLastY = -1;

        }
        return super.onMyTouch(view, event);
    }
}
