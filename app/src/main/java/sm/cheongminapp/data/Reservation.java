package sm.cheongminapp.data;

/**
 * Created by user on 2017. 5. 15..
 */
public class Reservation {
    public int request_id;
    public int center_id;
    public String member_id;
    public String day;
    public int start_time;
    public int end_time;
    public double lat, lng;
    public String reservation_info;

    public Reservation(int center_id, String member_id, String day, int start_time,
                       int end_time, double lat, double lng, String reservation_info) {
        this.center_id = center_id;
        this.member_id = member_id;
        this.day = day;
        this.start_time = start_time;
        this.end_time = end_time;
        this.lat = lat;
        this.lng = lng;
        this.reservation_info = reservation_info;
    }
}
