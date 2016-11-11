package com.qczb.myclient.entity;

import java.io.Serializable;

/**
 * 2016/11/11
 *
 * @author Michael Zhao
 */

public class Stock implements Serializable {
    /**
     * BId : 402800815843ee39015843ef76470001
     * stockNums : 30
     * BName : a
     * DName : 可口可乐300ml
     * stockId : 4028378152b132490152b132b09f0002
     * DId : 40289f81563be8f701563bea53380002
     * addTime : 2016-11-09 23:25:01
     */

    private String BId;
    private int stockNums;
    private String BName;
    private String DName;
    private String stockId;
    private String DId;
    private String addTime;

    public String getBId() {
        return BId;
    }

    public void setBId(String BId) {
        this.BId = BId;
    }

    public int getStockNums() {
        return stockNums;
    }

    public void setStockNums(int stockNums) {
        this.stockNums = stockNums;
    }

    public String getBName() {
        return BName;
    }

    public void setBName(String BName) {
        this.BName = BName;
    }

    public String getDName() {
        return DName;
    }

    public void setDName(String DName) {
        this.DName = DName;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getDId() {
        return DId;
    }

    public void setDId(String DId) {
        this.DId = DId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
