package sm.cheongminapp.data;

import android.graphics.drawable.Drawable;

/**
 * Created by Raye on 2017-05-16.
 */

public class Friend {
    private String ProfileUrl;
    private String Name;

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
}
