package sm.cheongminapp.data;

/**
 * Created by user on 2017. 5. 14..
 */
public class Center {
    public int center_id;
    public String name;
    public double lat;
    public double lng;
    public String information;
    public String tel;

    public Center(String name, double lat, double lng, String information, String tel) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.information = information;
        this.tel = tel;
    }
}
