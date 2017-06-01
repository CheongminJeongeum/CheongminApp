package sm.cheongminapp.data;

import java.io.Serializable;

/**
 * Created by user on 2017. 5. 15..
 */
public class ReservationData implements Serializable {
    public String CenterName;

    public String Reason;

    public String Date;
    public String Time; // reservation객체에서 따로 처리하여 1시 ~ 3시 형식으로 바꾸기

    public String Location;
    public String LocationDetail;

    public Double Lat;
    public Double Lng;
}