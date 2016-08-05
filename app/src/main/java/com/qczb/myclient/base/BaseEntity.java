package com.qczb.myclient.base;

import com.google.gson.JsonObject;

/**
 * 2016/5/4
 *
 * @author Michael Zhao
 */
public class BaseEntity {
    int status;
    String desc;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "status=" + status +
                ", desc='" + desc + '\'' +
                '}';
    }
}
