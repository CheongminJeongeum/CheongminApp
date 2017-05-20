package sm.cheongminapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raye on 2017-05-21.
 */

public class Location {
    @SerializedName("lat")
    public float lat;

    @SerializedName("lng")
    public float lng;
}
