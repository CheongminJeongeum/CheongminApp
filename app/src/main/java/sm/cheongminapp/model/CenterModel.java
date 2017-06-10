package sm.cheongminapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raye on 2017-05-21.
 */

public class CenterModel {
    // 센터 아이디
    @SerializedName("id")
    @Expose
    public int ID;

    // 센터 이름
    @SerializedName("name")
    @Expose
    public String Name;

    // 센터 설명
    @SerializedName("info")
    @Expose
    public String Infomation;

    // 센터 전화 번호
    @SerializedName("tel")
    @Expose
    public String Tel;

    // 센터 장소
    @SerializedName("location")
    @Expose
    public LocationModel Location;
}
