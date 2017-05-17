package sm.cheongminapp.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import sm.cheongminapp.model.Result;

/**
 * Created by Raye on 2017-05-02.
 */

public interface IApiService {
    @FormUrlEncoded
    @POST("users/login")
    Call<Result> Login(
            @Field("id") String id,
            @Field("passwd") String password);

    @FormUrlEncoded
    @POST("requests/center/{id}")
    Call<Result> RequestReservation(
            @Path("id") int centerID,
            @Field("day") String day,
            @Field("start_time") int startTime,
            @Field("end_time") int endTime,
            @Field("reservation_info") String info);
}