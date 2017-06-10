package sm.cheongminapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raye on 2017-05-02.
 */

public class ResultModel<T> {
    @SerializedName("result")
    @Expose
    public boolean IsSuccessful;

    @SerializedName("data")
    @Expose
    public T Data;
}

