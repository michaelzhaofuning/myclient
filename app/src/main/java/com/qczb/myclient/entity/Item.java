package com.qczb.myclient.entity;

import com.qczb.myclient.base.BaseEntity;

import java.util.List;

/**
 * 2016/8/4
 *
 * @author Michael Zhao
 */
public class Item extends BaseEntity {

    /**
     * data : [{"BName":"222","visitPlanTime":"2016-08-04 21:44:52","state":"0","salesmanName":"112","salesmanId":"34242","visitPlanTitle":"11","VId":"40289f815601acc0015601ad1cd80001","visitPlanContent":"222朋","addTime":"2016-08-04 21:44:52"},{"BName":"天关","visitPlanTime":"2016-08-04 21:42:36","state":"0","salesmanName":"张三","salesmanId":"333","visitPlanTitle":"计划1","VId":"40289f815601b1e4015601b512470002","visitPlanContent":"地地","addTime":"2016-08-04 21:42:36"},{"BName":"1","visitPlanTime":"2016-07-05 00:00:00","state":"0","salesmanName":"1","salesmanId":"","visitPlanTitle":"1","VId":"40289f815620094f0156200cc7ac0002","visitPlanContent":"1","addTime":"2016-07-25 11:15:23"}]
     * opFlag : success
     */

    public String opFlag;
    /**
     * BName : 222
     * visitPlanTime : 2016-08-04 21:44:52
     * state : 0
     * salesmanName : 112
     * salesmanId : 34242
     * visitPlanTitle : 11
     * VId : 40289f815601acc0015601ad1cd80001
     * visitPlanContent : 222朋
     * addTime : 2016-08-04 21:44:52
     */

    public List<Data> data;

    public static class Data {
        public String BName;
        public String visitPlanTime;
        public String state;
        public String salesmanName;
        public String salesmanId;
        public String visitPlanTitle;
        public String VId;
        public String visitPlanContent;
        public String addTime;
    }
}
