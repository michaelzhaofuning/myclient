package com.qczb.myclient.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoPreviewActivity;
import com.qczb.myclient.R;
import com.qczb.myclient.base.BaseActivity;
import com.qczb.myclient.base.MyConstants;
import com.qczb.myclient.util.ActivityUtil;
import com.qczb.myclient.util.UiUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Photo popup window
 *
 * @author Michael Zhao
 */
public class PhotoPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    public static final int REQUEST_CODE_CAMERA = 300;
    public static final int REQUEST_CODE_PICTURE = 301;

    // if onlyOne is true -> avatar
    public static boolean onlyOne = false;
    private BaseActivity mActivity;
    protected TextView camera;
    protected TextView picture;
    protected TextView cancel;
    public static Uri lastUri;

    public PhotoPopupWindow(BaseActivity context) {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mActivity = context;
        setAnimationStyle(R.style.popup_window_anim);
        setFocusable(true);
        setOutsideTouchable(true);
        //have to set background
        setBackgroundDrawable(context.getResources().getDrawable(android.R.color.white));
        setOnDismissListener(this);

        // select picture is default
        View view = mActivity.getLayoutInflater().inflate(R.layout.popup_select_picture, null);
        setContentView(view);
        camera = (TextView) view.findViewById(R.id.camera);
        picture = (TextView) view.findViewById(R.id.picture);
        cancel = (TextView) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPopupWindow.this.dismiss();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check permission for CAMERA
                if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Check Permissions Now
                    // Callback onRequestPermissionsResult
                    ActivityCompat.requestPermissions(mActivity,
                            new String[]{Manifest.permission.CAMERA},
                            100);
                } else {
                    // permission has been granted, continue as usual
                    requestCamera(mActivity);
                }

                PhotoPopupWindow.this.dismiss();
            }
        });
    /*    picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(PhotoSelectorActivity.ONLY_ONE, onlyOne);
                ActivityUtil.startActivityForResult(mActivity, PhotoSelectorActivity.class, bundle, REQUEST_CODE_PICTURE);
                PhotoPopupWindow.this.dismiss();
            }
        });*/
    }

    public static void requestCamera(Activity activity) {
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, getTimestampUri());
        activity.startActivityForResult(intentCamera, REQUEST_CODE_CAMERA);
    }


    @Override
    public void onDismiss() {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = 1;
        mActivity.getWindow().setAttributes(params);
    }

    public void show(boolean onlyOne) {
        PhotoPopupWindow.onlyOne = onlyOne;
        showAtLocation(mActivity.findViewById(android.R.id.content), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = 0.6f;
        mActivity.getWindow().setAttributes(params);
    }

    public static Uri getTimestampUri() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmSS", Locale.CHINESE).format(new Date());
        Uri uri = Uri.fromFile(new File(MyConstants.STORAGE_IMAGE + timeStamp + ".jpg"));
        lastUri = uri;
        return uri;
    }

    public static Uri getLastUri() {
        return lastUri;
    }

    public static void callBack(BaseActivity activity, ArrayList<PhotoModel> photos, int requestCode, int resultCode, Intent data, ImageView avatar, LinearLayout container) {
        ArrayList<PhotoModel> tempPhotos = null;
        if (data != null) {
             tempPhotos = (ArrayList<PhotoModel>) data.getExtras().getSerializable("photos");
        } else {
            tempPhotos = new ArrayList<>();
        }
        if (requestCode == REQUEST_CODE_CAMERA) {
                if (!PhotoPopupWindow.onlyOne) {
                    assert tempPhotos != null;
                    tempPhotos.add(new PhotoModel(getLastUri().getPath()));
                    photos.addAll(tempPhotos);
                    setImages(activity, tempPhotos, null, container, photos);
                }
//                else
//                    Crop.of(PhotoPopupWindow.getLastUri(), PhotoPopupWindow.getTimestampUri()).asSquare().start(activity);
        }
        if (requestCode == PhotoPopupWindow.REQUEST_CODE_PICTURE && resultCode == Activity.RESULT_OK) {// selected image
                if (tempPhotos == null || tempPhotos.isEmpty()) {
                    activity.showToastMsg("您没有选择图片");
                } else {
                    if (!PhotoPopupWindow.onlyOne) {
                        photos.addAll(tempPhotos);
                        setImages(activity, tempPhotos, null, container, photos);
                    } else {
//                        Crop.of(Uri.fromFile(new File(tempPhotos.get(0).getOriginalPath())), PhotoPopupWindow.getTimestampUri()).asSquare().start(activity);
                    }
                }
            }
//        if (requestCode == Crop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
//            avatar.setImageURI(PhotoPopupWindow.getLastUri());
//        }
    }


    public static void setImages(final BaseActivity mActivity, final ArrayList<PhotoModel> tempPhotos, ImageView avatar, final LinearLayout container, final ArrayList<PhotoModel> totalPhotos) {
        final Bundle bundle = new Bundle();
        for (int i = 0; i < tempPhotos.size(); i++) {
            // if file length too large, display thumbnail (use BitmapUtil)
            File file = new File(tempPhotos.get(i).getOriginalPath());
            Uri uri = Uri.fromFile(file);
            if (onlyOne) {
                if (avatar != null) {
                    ImageLoader.getInstance().displayImage(uri.toString(), avatar);
                    avatar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bundle.putSerializable("photos", totalPhotos);
                            bundle.putInt("position", 0);
                            ActivityUtil.startActivityForResult(mActivity, PhotoPreviewActivity.class, bundle, -1);
                        }
                    });
                }
            } else {
                ImageView imageView = getImageView(mActivity);
                container.addView(imageView);
                ImageLoader.getInstance().displayImage(uri.toString(), imageView);
                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int p = container.indexOfChild(v);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("photos", totalPhotos);
                        bundle.putInt("position", p - 1);
                        ActivityUtil.startActivityForResult(mActivity, PhotoPreviewActivity.class, bundle, -1);
                    }
                };
                imageView.setOnClickListener(onClickListener);
                imageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        final View finalView = v;
                        View popup = mActivity.getLayoutInflater().inflate(R.layout.delete_popup_window, null);
                        TextView textView = (TextView) popup.findViewById(R.id.textView);
                        textView.setText("删除这张照片");
                        final PopupWindow popupWindow = new PopupWindow(popup, UiUtil.dp2px(mActivity, 300), UiUtil.dp2px(mActivity, 40), true);
                        popupWindow.setOutsideTouchable(true);
                        popupWindow.setFocusable(true);
                        popupWindow.setBackgroundDrawable(mActivity.getResources().getDrawable(android.R.color.white));
                        popupWindow.showAtLocation(mActivity.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
                        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                        params.alpha = 0.8f;
                        mActivity.getWindow().setAttributes(params);
                        popupWindow.setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                                params.alpha = 1;
                                mActivity.getWindow().setAttributes(params);
                            }
                        });
                        popup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int i = container.indexOfChild(finalView) - 1;
                                totalPhotos.remove(i);
                                container.removeView(finalView);
                                popupWindow.dismiss();
                            }
                        });
                        return true;
                    }
                });
            }
        }
    }

    public static ImageView getImageView(Activity a) {
        ImageView imageView = new ImageView(a);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                a.getResources().getDimensionPixelSize(R.dimen.image_size),
                a.getResources().getDimensionPixelSize(R.dimen.image_size));
        lp.setMargins(5, 0, 10, 0);
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    public static ImageView getImageViewLarge(Activity a) {
        ImageView imageView = new ImageView(a);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                a.getResources().getDimensionPixelSize(R.dimen.image_size_large),
                a.getResources().getDimensionPixelSize(R.dimen.image_size_large));
        lp.setMargins(5, 5, 10, 5);
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }
}
