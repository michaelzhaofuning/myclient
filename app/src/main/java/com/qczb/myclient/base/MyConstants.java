package com.qczb.myclient.base;

import android.os.Environment;

/**
 * Project constants go here ...
 *
 * @author Michael Zhao
 */
public interface MyConstants {
    boolean IS_DEBUG = false;

    String STORAGE_IMAGE = Environment.getExternalStorageDirectory().getPath() + "/myclient/images/";
}
