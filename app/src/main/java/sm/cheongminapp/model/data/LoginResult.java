package sm.cheongminapp.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
{
  "data": {
    "center_id": 1,
    "id": "admin1",
    "is_translator": "true"
  },
  "result": "true"
}
 */
public class LoginResult {
    @SerializedName("center_id")
    @Expose
    public int CenterID;

    @SerializedName("id")
    @Expose
    public String ID;

    @SerializedName("is_translator")
    @Expose
    public boolean IsTranslator;
}
