package com.qczb.myclient.base;

import com.google.gson.JsonObject;

/**
 * 2016/5/4
 *
 * @author Michael Zhao
 */
public class BaseEntity {
    int ret;
    String msg;
    long timestamp;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "ret=" + ret +
                ", msg='" + msg + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
