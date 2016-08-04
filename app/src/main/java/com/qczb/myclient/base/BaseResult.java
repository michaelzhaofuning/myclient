package com.qczb.myclient.base;

import com.google.gson.JsonObject;

/**
 * 2016/6/20
 *
 * @author Michael Zhao
 */
public class BaseResult extends BaseEntity {
    JsonObject result;

    public JsonObject getResult() {
        return result;
    }

    public void setResult(JsonObject result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "result=" + result +
                '}' + super.toString();
    }
}
