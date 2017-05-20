package sm.cheongminapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raye on 2017-05-21.
 */

public class Center {
    @SerializedName("id")
    @Expose
    public int ID;

    @SerializedName("name")
    @Expose
    public String Name;

    @SerializedName("info")
    @Expose
    public String Infomation;

    @SerializedName("tel")
    @Expose
    public String Tel;

    @SerializedName("location")
    @Expose
    public Location Location;
}
