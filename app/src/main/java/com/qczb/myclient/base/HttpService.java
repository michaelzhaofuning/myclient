package com.qczb.myclient.base;



import okhttp3.MultipartBody;
import retrofit2.Call;
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


    @GET("yw/yw_user/no_login")
    Call<UserData> login(@Query("username") String username, @Query("password") String password);

    @GET("yw/yw_user/no_exist")
    Call<BaseResult> isUsernameExists(@Query("username") String username);


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

    @GET("yw/yw_main/user_goods")
    Call<BaseResult> myGoods(@Query("auid") String auid, @Query("user_key") String token);

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
