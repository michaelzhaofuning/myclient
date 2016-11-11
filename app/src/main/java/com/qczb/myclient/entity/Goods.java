package com.qczb.myclient.entity;

import java.io.Serializable;

/**
 * 2016/11/11
 *
 * @author Michael Zhao
 */

public class Goods implements Serializable {
    /**
     * DIcon : /uploads/2016-10-22/201610222300952107.jpg
     * DName : 可口可乐300ml
     * DId : 40289f81563be8f701563bea53380002
     * DNowPrice : 90
     */

    public String DIcon;
    public String DName;
    public String DId;
    public int DNowPrice;

    public String getDIcon() {
        return DIcon;
    }

    public void setDIcon(String DIcon) {
        this.DIcon = DIcon;
    }

    public String getDName() {
        return DName;
    }

    public void setDName(String DName) {
        this.DName = DName;
    }

    public String getDId() {
        return DId;
    }

    public void setDId(String DId) {
        this.DId = DId;
    }

    public int getDNowPrice() {
        return DNowPrice;
    }

    public void setDNowPrice(int DNowPrice) {
        this.DNowPrice = DNowPrice;
    }
}
