package sm.cheongminapp.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import sm.cheongminapp.data.ChatRoom;
import sm.cheongminapp.data.Friend;
import sm.cheongminapp.data.Reservation;
import sm.cheongminapp.model.Center;
import sm.cheongminapp.model.Profile;
import sm.cheongminapp.model.Result;
import sm.cheongminapp.model.data.EmptyData;
import sm.cheongminapp.model.data.LoginResult;

/**
 * Created by Raye on 2017-05-02.
 */

public interface IApiService {
    @FormUrlEncoded
    @POST("users/login")
    Call<Result<LoginResult>> Login(
            @Field("id") String id,
            @Field("passwd") String password);

    @GET("users/")
    Call<Result<Profile>> getMyProfile();

    @GET("users/{id}")
    Call<Result<Profile>> getProfile(
            @Path("id") String id);

    @GET("friends")
    Call<Result<List<Friend>>> getFriends();

    @GET("centers")
    Call<Result<List<Center>>> getCenters();

    @FormUrlEncoded
    @POST("centers/{id}/requests")
    Call<Result<EmptyData>> requestReservation(
            @Path("id") int centerID,
            @Field("id") String userID,
            @Field("day") String day,
            @Field("start_time") int startTime,
            @Field("end_time") int endTime,
            @Field("info") String info,
            @Field("lat") double lat,
            @Field("lng") double lng);

    @GET("reservations/member/{member_id}")
    Call<Result<List<Reservation>>> getMyReservations(
            @Path("member_id") String member_id);

    @FormUrlEncoded
    @PUT("regid")
    Call<Result> RegId(
            @Field("id") String id,
            @Field("regId") String regId);


    @FormUrlEncoded
    @POST("chat/room")
    Call<Result> createChatRoom(
            @Field("member1") String memberID,
            @Field("member2") String targetID);

    @GET("chat/room")
    Call<Result<List<ChatRoom>>> getChatRooms(
            @Query("id") String memberID);

    @FormUrlEncoded
    @POST("chat/korean")
    Call<Result> sendMessageOnKorean(
            @Field("id") String id,
            @Field("room") int room,
            @Field("msg") String msg);

    @FormUrlEncoded
    @POST("chat/sign")
    Call<Result> sendMessageOnSign(
            @Field("id") String id,
            @Field("room") int room,
            @Field("msg") String msg);
}