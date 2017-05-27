package sm.cheongminapp.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import sm.cheongminapp.data.Reservation;
import sm.cheongminapp.model.Center;
import sm.cheongminapp.model.data.EmptyData;
import sm.cheongminapp.model.data.LoginResult;
import sm.cheongminapp.model.Profile;
import sm.cheongminapp.model.Result;

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
    Call<Profile> GetMyProfile();

    @GET("users/{id}")
    Call<Profile> GetProfile(@Path("id") String id);

    @GET("centers/")
    Call<List<Center>> GetCenters();

    @FormUrlEncoded
    @POST("/centers/{id}/requests")
    Call<Result> RequestReservation(
            @Path("id") int centerID,
            @Field("day") String day,
            @Field("start_time") int startTime,
            @Field("end_time") int endTime,
            @Field("info") String info,
            @Field("lat") double lat,
            @Field("lng") double lng);

    @GET("/reservations/member/{member_id}")
    Call<List<Reservation>> getMyReservations(@Path("member_id") String member_id);

    @FormUrlEncoded
    @PUT("regid")
    Call<Result> RegId(
            @Field("id") String id,
            @Field("regId") String regId);
}