package com.qczb.myclient.base;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 2016/6/20
 *
 * @author Michael Zhao
 */
public class BaseResult extends BaseEntity {
    JsonArray data;

    public JsonArray getData() {
        return data;
    }

    public void setData(JsonArray data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "result=" + data +
                '}' + super.toString();
    }
}
