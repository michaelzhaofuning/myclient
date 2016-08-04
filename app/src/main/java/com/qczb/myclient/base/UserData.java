package com.qczb.myclient.base;

/**
 * 2016/6/21
 *
 * @author Michael Zhao
 */
public class UserData extends BaseEntity {
    /**
     * auid : 762802bf8c0040756bd2dc9fe1d8a334
     * user_key : 098e9872da3e10948afd8f0cf9b6eaf3
     * nickname : 13753174510
     * gender : 0
     * headurl : http://183.203.28.91:3000/pic/aiewu/head/1466388593804.jpg
     */

    public Result result;

    public static class Result {
        public String auid;
        public String user_key;
        public String nickname;
        public int gender;
        public String headurl;
    }
}
