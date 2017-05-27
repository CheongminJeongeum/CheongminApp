package sm.cheongminapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 2017. 5. 23..
 */
public class Reservation {
    @SerializedName("request_id")
    @Expose
    public int request_id;

    @SerializedName("center_id")
    @Expose
    public int center_id;

    @SerializedName("member_id")
    @Expose
    public String member_id;

    @SerializedName("day")
    @Expose
    public String day;

    @SerializedName("start_time")
    @Expose
    public int start_time;

    @SerializedName("end_time")
    @Expose
    public int end_time;

    @SerializedName("lat")
    @Expose
    public double lat;

    @SerializedName("lng")
    @Expose
    public double lng;

    @SerializedName("reservation_info")
    @Expose
    public String reservation_info;
}
