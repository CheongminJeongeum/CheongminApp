package sm.cheongminapp.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by user on 2017. 5. 23..
 */
public class Reservation implements Serializable {

    @SerializedName("center_name")
    @Expose
    public String CenterName;

    @SerializedName("date")
    @Expose
    public String Date;

    @SerializedName("start_time")
    @Expose
    public int StartTime;

    @SerializedName("end_time")
    @Expose
    public int EndTime;

    @SerializedName("reservation_info")
    @Expose
    public String Reason;

    @SerializedName("reservation_result")
    @Expose
    public int Result;

    @SerializedName("location_detail")
    @Expose
    public String Location;

    @SerializedName("lat")
    @Expose
    public double Lat;

    @SerializedName("lng")
    @Expose
    public double Lng;


    public String getTimeRangeText() {
        StringBuilder builder = new StringBuilder();
        builder.append(StartTime);
        builder.append(" ~ ");
        builder.append(EndTime);
        builder.append("시");

        return builder.toString();
    }
}