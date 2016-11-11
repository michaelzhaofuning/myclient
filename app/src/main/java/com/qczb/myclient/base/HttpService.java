package com.qczb.myclient.base;



import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * http request goes here...
 *
 * BASE_URL:    http://183.203.28.91:3000/yw_php_api/
 *
 * @author Michael Zhao
 *
 */
public interface HttpService {

    /////////////////////////////////////////////////////////////////////////
    ////  general
    /////////////////////////////////////////////////////////////////////////

    @Multipart
    @POST("FileUploadServlet")
    Call<BaseResult> uploadGoods(@Part MultipartBody.Part file);


    @GET("supervisor/login.htm")
    Call<UserData> login(@Query("vcPhone") String username, @Query("vcPassword") String password);

    @GET("visitPlan/findByUserid.htm")
    Call<BaseResult> getPlans(@Query("vcUserid") String uid, @Query("isPlan") Integer isPlan);

    @GET("business/findByUserid.htm")
    Call<BaseResult> getCustomers(@Query("vcUserid") String uid);

    @GET("supplier/findByBid.htm")
    Call<BaseResult> getProviders(@Query("Bid") String bid);


    @GET("bstock/findByBidAndUserId")
    Call<BaseResult> getProviders(@Query("BId") String bid, @Query("vcUserid") String uid, @Query("DId") String did);

    @GET("visitPlan/msave.htm")
    Call<BaseResult> submitPlan(@QueryMap Map<String, String> map);

    @GET("business/addBusiness.htm")
    Call<BaseResult> submitCustomer(@QueryMap Map<String, String> map);

    @GET("supplier/msave.htm")
    Call<BaseResult> submitProvider(@QueryMap Map<String, String> map);


    /**
     * editModel:edit/add（必填）
     stockId:库存ID（编辑状态下必填）
     DId：商品ID（必填）
     BId：商家ID（必填）
     vcUserid：业务员ID（必填）
     stockNums：库存数量（必填）
     * @param map
     * @return
     */
    @GET("bstock/msave")
    Call<BaseResult> submitStock(@QueryMap Map<String, String> map);


    @GET("visitPlan/saveRecord.htm")
    Call<BaseResult> startVisit(@QueryMap Map<String, String> map);

    @GET("visitPlan/findRecordByVid.htm")
    Call<BaseResult> getVisit(@Query("Vid") String vid);



    @GET("yw/yw_main/user_goods_cancel")
    Call<BaseResult> downGoods(@Query("auid") String auid, @Query("user_key") String token, @Query("user_goods_id") String goodsId);

    @GET("yw/yw_main/user_goods_on")
    Call<BaseResult> upGoods(@Query("auid") String auid, @Query("user_key") String token, @Query("user_goods_id") String goodsId);

    @GET("yw/yw_main/user_goods_detele")
    Call<BaseResult> deleteGoods(@Query("auid") String auid, @Query("user_key") String token, @Query("user_goods_id") String goodsId);



    /////////////////////////////////////////////////////////////////////////
    ////  my
    /////////////////////////////////////////////////////////////////////////



    @GET("yw/yw_user/save_user")
    Call<BaseResult> saveUserProfile(@Query("auid") String auid, @Query("user_key") String token,
                                     @Query("nickname") String nickname, @Query("gender") int gender,
                                     @Query("headurl") String headurl, @Query("phone") String phone, @Query("purpose_one") String purpose, @Query("introduction") String intro);

}
