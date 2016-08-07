package com.qczb.myclient.base;



import okhttp3.FormBody;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

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
    @POST("yw/yw_user/upload_head")
    Call<BaseResult> uploadImage(@Part MultipartBody.Part file);


    @GET("supervisor/login.htm")
    Call<UserData> login(@Query("vcPhone") String username, @Query("vcPassword") String password);

    @GET("visitPlan/findByUserid.htm")
    Call<BaseResult> getPlans(@Query("vcUserid") String uid, @Query("state") Integer state, @Query("stateOpt") String stateOpt, @Query("planTime") String time, @Query("planTimeOpt") String planTimeOpt);



    /////////////////////////////////////////////////////////////////////////
    ////  home
    /////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////
    ////  publish
    /////////////////////////////////////////////////////////////////////////

    @GET("yw/yw_main/goods_submit")
    Call<BaseResult> submitGoods(@Query("auid") String auid, @Query("user_key") String token,
                                 @Query("goodsname") String name, @Query("goodssortb") String price,
                                 @Query("goodscontent") String content, @Query("submit_pic_urls") String pics);



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

    @Multipart
    @POST("yw/yw_main/upload_goods")
    Call<BaseResult> uploadGoods(@Part MultipartBody.Part part);
}
