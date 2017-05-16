package sm.cheongminapp.data;

/**
 * Created by user on 2017. 5. 15..
 */
public class Request {
    int request_id;
    int center_id;
    String member_id;
    String day;
    int start_time;
    int end_time;
    String reservation_info;

    public Request(int center_id, String member_id, String day, int start_time,
                   int end_time, String reservation_info) {
        this.center_id = center_id;
        this.member_id = member_id;
        this.day = day;
        this.start_time = start_time;
        this.end_time = end_time;
        this.reservation_info = reservation_info;
    }
}
