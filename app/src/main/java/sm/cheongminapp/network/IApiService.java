package sm.cheongminapp.network;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import sm.cheongminapp.data.ChatRoom;
import sm.cheongminapp.data.Friend;
import sm.cheongminapp.data.Reservation;
import sm.cheongminapp.model.CenterModel;
import sm.cheongminapp.model.ProfileModel;
import sm.cheongminapp.model.ResultModel;
import sm.cheongminapp.model.data.EmptyData;
import sm.cheongminapp.model.data.LoginResult;

/**
 * Created by Raye on 2017-05-02.
 */

public interface IApiService {
    @FormUrlEncoded
    @POST("users/login")
    Call<ResultModel<LoginResult>> Login(
            @Field("id") String id,
            @Field("passwd") String password);

    @GET("users/")
    Call<ResultModel<ProfileModel>> getMyProfile();

    @GET("users/{id}")
    Call<ResultModel<ProfileModel>> getProfile(
            @Path("id") String id);

    @Multipart
    @POST("profiles/images/{user_id}")
    Call<ResultModel<EmptyData>> uploadProfileImage(
            @Path("user_id") String userID,
            @Part() MultipartBody.Part file);

    @GET("friends")
    Call<ResultModel<List<Friend>>> getFriends();

    @GET("centers")
    Call<ResultModel<List<CenterModel>>> getCenters();

    @FormUrlEncoded
    @POST("centers/{center_id}/requests")
    Call<ResultModel<EmptyData>> requestReservation(
            @Path("center_id") int centerID,
            @Field("id") String userID,
            @Field("day") String day,
            @Field("start_time") int startTime,
            @Field("end_time") int endTime,
            @Field("info") String info,
            @Field("detail_location") String location,
            @Field("lat") double lat,
            @Field("lng") double lng);

    @GET("reservations/member/{member_id}")
    Call<ResultModel<List<Reservation>>> getMyReservations(
            @Path("member_id") String memberId);

    @GET("requests/member/{member_id}")
    Call<ResultModel<List<Reservation>>> getMyRequests(
            @Path("member_id") String memberId);

    @FormUrlEncoded
    @PUT("regid")
    Call<ResultModel<EmptyData>> RegId(
            @Field("id") String id,
            @Field("regId") String regId);


    @FormUrlEncoded
    @POST("chat/room")
    Call<ResultModel<ChatRoom>> createChatRoom(
            @Field("member1") String memberID,
            @Field("member2") String targetID);

    @GET("chat/room")
    Call<ResultModel<List<ChatRoom>>> getChatRooms(
            @Query("id") String memberID);

    @FormUrlEncoded
    @POST("chat/korean")
    Call<ResultModel<EmptyData>> sendMessageOnKorean(
            @Field("id") String id,
            @Field("room") int room,
            @Field("msg") String msg);

    @FormUrlEncoded
    @POST("chat/sign")
    Call<ResultModel<EmptyData>> sendMessageOnSign(
            @Field("id") String id,
            @Field("room") int room,
            @Field("msg") String msg);
}