package sm.cheongminapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raye on 2017-05-21.
 */

public class LocationModel {
    @SerializedName("lat")
    public float Lat;

    @SerializedName("lng")
    public float Lng;
}
