package sm.cheongminapp.data;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by Raye on 2017-05-16.
 */

public class Friend implements Serializable {
    private String UserID;
    private String ProfileUrl;
    private String Name;

    public Friend(String userID, String name, String profileUrl) {
        setUserID(userID);
        setName(name);
        setProfileUrl(profileUrl);
    }

    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    public String getProfileUrl() {
        return ProfileUrl;
    }
    public void setProfileUrl(String profileUrl) {
        ProfileUrl = profileUrl;
    }

    public String getUserID() {
        return UserID;
    }
    public void setUserID(String userID) {
        UserID = userID;
    }
}
