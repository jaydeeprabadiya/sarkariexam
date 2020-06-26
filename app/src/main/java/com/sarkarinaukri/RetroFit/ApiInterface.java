package com.sarkarinaukri.RetroFit;

import com.sarkarinaukri.model.CommentDetails;
import com.sarkarinaukri.model.Commentcount;
import com.sarkarinaukri.model.Corona;
import com.sarkarinaukri.model.Likeunlike;
import com.sarkarinaukri.model.Newlogin;
import com.sarkarinaukri.model.Newvideo;
import com.sarkarinaukri.model.QuestionList;
import com.sarkarinaukri.model.ResultEntity;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Anurag Katiyar on 07/07/2018.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    Call<ResultEntity> login(@Field("email") String email,
                             @Field("password") String password,
                             @Field("deviceId") String deviceId,
                             @Field("deviceType") String deviceType);

    @FormUrlEncoded
    @POST("signup")
    Call<ResultEntity> signup(@Field("email") String email,
                              @Field("password") String password,
                              @Field("fullName") String fullName,
                              @Field("mobile") String mobile,
                              @Field("deviceId") String deviceId,
                              @Field("deviceType") String deviceType,
                              @Field("referalCode") String referalCode);

    @FormUrlEncoded
    @POST("social/login")
    Call<ResultEntity> socialLogin(@Field("socialId") String socialId,
                                   @Field("fullName") String fullName,
                                   @Field("signupMedium") String signupMedium,
                                   @Field("deviceType") String deviceType,
                                   @Field("deviceId") String deviceId,
                                   @Field("photo") String photo,
                                   @Field("email") String email);

    @FormUrlEncoded
    @POST("forgot")
    Call<ResultEntity> forgotPassword(@Field("email") String email);

    @GET("filter")
    Call<ResultEntity> getFilter(@Header("token") String token);

    @Multipart
    @POST("user/updateprofile")
    Call<ResultEntity> updateProfile(@Part MultipartBody.Part photo,
                                     @Header("token") String token,
                                     @Part("fullName") RequestBody fullName,
                                     @Part("mobile") RequestBody mobile);

    @GET("logout")
    Call<ResultEntity> logout(@Header("token") String token);


    @FormUrlEncoded
    @POST("user/changepassword")
    Call<ResultEntity> changePassword(@Field("oldPassword") String oldPassword,
                                      @Field("newPassword") String newPassword,
                                      @Field("confirmPassword") String confirmPassword,
                                      @Header("token") String token);

    @FormUrlEncoded
    @POST("question/all")
    Call<ResultEntity> getQuestion(@Field("type") String type,
                                   @Field("page") String page,
                                   @Field("questionId") String questionId,
                                   @Field("userId") String userId,
                                   @Header("token") String token);

    @FormUrlEncoded
    @POST("question/delete")
    Call<ResultEntity> questionDelete(@Field("questionId") String questionId,
                                      @Header("token") String token);

    @FormUrlEncoded
    @POST("question/like")
    Call<ResultEntity> questionLike(@Field("questionId") String questionId,
                                    @Header("token") String token);

    @FormUrlEncoded
    @POST("answer/getmyanswer")
    Call<ResultEntity> getAnswer(@Field("type") String type,
                                 @Field("page") String page,
                                 @Field("questionId") String questionId,
                                 @Field("userId") String userId,
                                 @Header("token") String token);

    @FormUrlEncoded
    @POST("question/getanswer")
    Call<ResultEntity> getAnswerOfQuestion(@Field("questionId") String questionId,
                                           @Header("token") String token);

    @FormUrlEncoded
    @POST("question/postanswer")
    Call<ResultEntity> postAnswer(@Field("questionId") String questionId,
                                  @Field("answer") String answer,
                                  @Header("token") String token);

    @FormUrlEncoded
    @POST("question/postoption")
    Call<ResultEntity> postMCQOption(@Field("questionId") String questionId,
                                     @Field("answer") String answer,
                                     @Header("token") String token);

    @FormUrlEncoded
    @POST("answer/likedislike")
    Call<ResultEntity> likeDislikeAnswer(@Field("answerId") String answerId,
                                         @Field("answer") String answer,
                                         @Field("questionId") String questionId,
                                         @Field("userId") String userId,
                                         @Field("type") String type,
                                         @Header("token") String token);

    @FormUrlEncoded
    @POST("question/share")
    Call<ResultEntity> shareQuestion(@Field("questionId") String questionId,
                                     @Header("token") String token);

    @FormUrlEncoded
    @POST("question/follow")
    Call<ResultEntity> questionFollowUnfollow(@Field("questionId") String questionId,
                                              @Header("token") String token);

    @FormUrlEncoded
    @POST("follow/create")
    Call<ResultEntity> userFollow(@Field("userId") String userId,
                                  @Header("token") String token);


    @Multipart
    @POST("question/add")
    Call<ResultEntity> uploadQuestion(@Part MultipartBody.Part questionImage,
                                      @Header("token") String token,
                                      @Part("question") RequestBody question,
                                      @Part("options") RequestBody options,
                                      @Part("rightOption") RequestBody rightOption,
                                      @Part("categoryId") RequestBody categoryId);


    @FormUrlEncoded
    @POST("notification/list")
    Call<ResultEntity> notificationList(@Field("page") String page,
                                        @Header("token") String token);


    @GET("home.php")
    Call<ResultEntity> getJobsForHome(@Query("home") String home);

    @GET("app_list.php")
    Call<ResultEntity> getJobs(@Query("catId") String catId);


    @FormUrlEncoded
    @POST("get_data.php?")
    Call<QuestionList> getQuestions(@Field("id") String userid);


    @FormUrlEncoded
    @POST("get_queans.php")
    Call<QuestionList> getNewQuestions(@Field("id") String userid);


    @FormUrlEncoded
    @POST("get_video.php")
    Call<QuestionList> getVideo(@Field("id") String userid);


    @FormUrlEncoded
    @POST("like.php?")
    Call<Likeunlike> Likeunlike(@Field("user_id") String userid,
                                @Field("id") String id,
                                @Field("type") String type);

    @FormUrlEncoded
    @POST("get_comment.php?")
    Call<CommentDetails> getComment(
            @Field("id") String id,
            @Field("type") String type);

    @FormUrlEncoded
    @POST("comment.php?")
    Call<CommentDetails>sendComment(
            @Field("user_id") String userid,
            @Field("id") String id,
            @Field("type") String type,
            @Field("comment") String comment);


    @FormUrlEncoded
    @POST("share.php?")
    Call<Likeunlike> watsapplike(@Field("user_id") String userid,
                                @Field("id") String id,
                                @Field("type") String type);

    @FormUrlEncoded
    @POST("add_wishlist.php?")
    Call<Likeunlike> Whishlist(@Field("user_id") String userid,
                                 @Field("id") String id,
                                 @Field("type") String type);

    @FormUrlEncoded
    @POST("user_wishlist.php?")
    Call<QuestionList> getWhishlist(@Field("user_id") String userid);



    @FormUrlEncoded
    @POST("insert_wishlist.php?")
    Call<Likeunlike> Likesave(@Field("user_id") String userid,
                                 @Field("id") String id,
                                 @Field("type") String type);

    @FormUrlEncoded
    @POST("get_comment_count.php?")
    Call<Commentcount> getCommentcount(@Field("post_id") String postid,
                                @Field("type") String type);


    @FormUrlEncoded
    @POST("login.php?")
    Call<Newlogin> Newlogin(@Field("email") String email,
                            @Field("password") String password);

    @FormUrlEncoded
    @POST("ragistration.php?")
    Call<Newlogin> NewRegistration(@Field("name") String name,
                                   @Field("email") String email,
                                   @Field("mobile") String mobile,
                                   @Field("password") String password);


    @FormUrlEncoded
    @POST("devicecount.php?")
    Call<Likeunlike> viewcount(@Field("device_tokan") String devicetoken,
                               @Field("post_id") String id);


    @FormUrlEncoded
    @POST("sociallogin.php?")
    Call<Newlogin> socialsLogin(@Field("token_id") String socialId,
                                    @Field("email") String email,
                                    @Field("type") String signupMedium,
                                    @Field("fullName") String fullName);


    @FormUrlEncoded
    @POST("delete_id_wishlist.php?")
    Call<Likeunlike> Deleteidwish(@Field("user_id") String userid,
                              @Field("id") String id,
                              @Field("type") String type);


    @FormUrlEncoded
    @POST("get_data_paggination.php?")
    Call<QuestionList> getdatapagination(@Field("id") String userid,
                                         @Field("page_no") String pageno);


    @FormUrlEncoded
    @POST("get_queans_paggination.php?")
    Call<QuestionList> getquestionpagination(@Field("id") String userid,
                                         @Field("page_no") String pageno);


    @FormUrlEncoded
    @POST("get_video_paggination.php?")
    Call<QuestionList> getvideopagination(@Field("id") String userid,
                                          @Field("page_no") String pageno);

    @FormUrlEncoded
    @POST("transactionHistory")
    Call<Newvideo> Newgetvideopagination(@Field("page_link") String pageno,
                                         @Field("user_id") String userid);

    @FormUrlEncoded
    @POST("get_karona.php?")
    Call<Corona> getcorona(@Field("id") String userid);





}
