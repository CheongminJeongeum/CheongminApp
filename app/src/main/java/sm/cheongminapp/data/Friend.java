package sm.cheongminapp.data;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Raye on 2017-05-16.
 */

public class Friend implements Serializable {

    @Expose
    @SerializedName("id")
    public String ID;

    @Expose
    @SerializedName("username")
    public String Name;

    @Expose
    @SerializedName("state_message")
    public String StateMessage;

    public String ProfileUrl;

    public Friend() {
    }
}
