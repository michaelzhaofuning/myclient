package com.qczb.myclient.entity;

import com.qczb.myclient.base.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 2016/8/4
 *
 * @author Michael Zhao
 */
public class Item implements Serializable {
    /**
     * {"desc":"成功","status":"200","data":[{"BName":"1111","visitPlanTime":"2016-08-05 00:00:00","state":"0","salesmanName":"1111","salesmanId":"34242","visitPlanTitle":"1111","VId":"8a22c4eb565afa0c01565b4788190003","visitPlanContent":"11111","addTime":"2016-08-05 23:17:09"}]}
     */
    public String BName;
    public String visitPlanTime;
    public String state;
    public String salesmanName;
    public String salesmanId;
    public String visitPlanTitle;
    public String VId;
    public String visitPlanContent;
    public String addTime;
    public String Bid;
}
