package sm.cheongminapp;

import android.graphics.drawable.Drawable;

/**
 * Created by Raye on 2017-05-16.
 */

public class FriendListItem {
    private Drawable IconDrawable;
    private String Name;
    private String Description;

    public Drawable getIconDrawable() {
        return IconDrawable;
    }
    public void setIconDrawable(Drawable iconDrawable) {
        IconDrawable = iconDrawable;
    }

    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }
}
