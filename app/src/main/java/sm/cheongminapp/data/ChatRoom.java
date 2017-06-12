package sm.cheongminapp.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Raye on 2017-05-18.
 */

public class ChatRoom implements Serializable {

    @Expose
    @SerializedName("room_id")
    public int room_id;

    @Expose
    @SerializedName("room_name")
    public String room_name;
}
