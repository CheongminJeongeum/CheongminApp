package sm.cheongminapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raye on 2017-05-21.
 */

public class Profile {
    @SerializedName("id")
    @Expose
    public String ID;

    @SerializedName("name")
    @Expose
    public String Name;

    @SerializedName("phone")
    @Expose
    public String Phone;

    @SerializedName("email")
    @Expose
    public String Email;

    @SerializedName("state")
    @Expose
    public int StateCode;

    @SerializedName("stateMsg")
    @Expose
    public String StateMsg;

    @SerializedName("is_translator")
    @Expose
    public boolean IsTranslator;

    @SerializedName("option")
    @Expose
    public int option;
}
