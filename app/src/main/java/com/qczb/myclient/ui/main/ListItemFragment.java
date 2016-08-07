package com.qczb.myclient.ui.main;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseListFragment;
import com.qczb.myclient.base.BaseResult;
import com.qczb.myclient.entity.Item;


/**
 * 2016/8/4
 *
 * @author Michael Zhao
 */
public abstract class ListItemFragment<I> extends BaseListFragment<BaseResult, I> {
    ImageView topImageView;
    ImageView topAdd;
    TextView topTextView;
    private float myLastY = -1;
    private View scrim;
    boolean scrimShow = false;
    public static int CODE = 41;

    @Override
    protected void initActionBar() {
        View v = getActivity().getLayoutInflater().inflate(R.layout.top_layout, mAboveList);
        topImageView = (ImageView) v.findViewById(R.id.top_image);
        topAdd = (ImageView) v.findViewById(R.id.top_add);
        topTextView = (TextView) v.findViewById(R.id.top_text);
        scrim = v.findViewById(R.id.scrim);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        topAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), getTopAddActivity());
                startActivityForResult(intent, CODE);
            }
        });
        topTextView.setText(getTitle());
        topImageView.setImageDrawable(getResources().getDrawable(getTopImageID()));

    }

    protected abstract Class<?> getTopAddActivity();

    protected abstract int getTopImageID();

    protected abstract String getTitle();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onRefresh();
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
                // up
                if (dy <= 0 && mAboveList.getHeight() > getResources().getDimensionPixelSize(R.dimen.app_bar_height)) {
                    if ((mAboveList.getPaddingBottom() + (int) (dy)) > (getResources().getDimensionPixelSize(R.dimen.app_bar_height) - getResources().getDimensionPixelSize(R.dimen.top_layout_height))) {
                        mAboveList.setPadding(0, 0, 0, mAboveList.getPaddingBottom() + (int) (dy));

                    } else {
                        mAboveList.setPadding(0, 0, 0, (getResources().getDimensionPixelSize(R.dimen.app_bar_height) - getResources().getDimensionPixelSize(R.dimen.top_layout_height)));
                    }

                    // invalidate in different thread
                    // delay to wait another thread done
                    // or use the same fraction
                    if (!scrimShow && mAboveList.getHeight() < getResources().getDimensionPixelSize(R.dimen.top_layout_height) / 2)
                        scrimShow();

                    changeWithFraction();

                    myLastY = y;
                    return true;
                }

                // dy too large, FUTURE FRACTION

                // recalculate
                if (!scrimShow && mAboveList.getHeight() < getResources().getDimensionPixelSize(R.dimen.top_layout_height) / 2)
                    scrimShow();
                changeWithFraction();


                // down
                if (dy > 0 && linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0 && mAboveList.getHeight() < getResources().getDimensionPixelSize(R.dimen.top_layout_height)) {
                    if ((mAboveList.getPaddingBottom() + (int) (dy)) < 0) {
                        mAboveList.setPadding(0, 0, 0, mAboveList.getPaddingBottom() + (int) (dy));
                    } else {
                        mAboveList.setPadding(0, 0, 0, 0);
                    }

                    if (scrimShow && mAboveList.getHeight() > getResources().getDimensionPixelSize(R.dimen.top_layout_height) / 2)
                        scrimHide();

                    changeWithFraction();

                    myLastY = y;
                    return true;
                }

                myLastY = y;
                break;

            default:
                myLastY = -1;
        }
        return super.onMyTouch(view, event);
    }

    private void changeWithFraction() {
        float fraction = ((float) (mAboveList.getHeight() - getResources().getDimensionPixelSize(R.dimen.app_bar_height))) / (getResources().getDimensionPixelSize(R.dimen.top_layout_height) - getResources().getDimensionPixelSize(R.dimen.app_bar_height));
        fraction = 1 - fraction;
//        Log.e("test", fraction + "");
        topTextView.setTextSize(35 - fraction * (35 - 19));
        float centerYLarge = getResources().getDimensionPixelSize(R.dimen.top_layout_height) / 2 - getResources().getDimensionPixelSize(R.dimen.large_sp) / 2;
        float centerYSmall = (getResources().getDimensionPixelSize(R.dimen.app_bar_height) - getResources().getDimensionPixelSize(R.dimen.status_bar)) / 2  - getResources().getDimensionPixelSize(R.dimen.medium_sp) / 2 + getResources().getDimensionPixelSize(R.dimen.status_bar) / 2;
        topTextView.setTranslationY(- fraction * (centerYLarge - centerYSmall));
    }

    private void scrimHide() {
        ObjectAnimator.ofFloat(scrim, View.ALPHA, 1, 0).start();
        scrimShow = false;
    }

    private void scrimShow() {
        ObjectAnimator.ofFloat(scrim, View.ALPHA, 0, 1).start();
        scrimShow = true;
    }
}
