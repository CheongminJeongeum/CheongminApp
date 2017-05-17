package sm.cheongminapp.data;

import android.graphics.drawable.Drawable;

/**
 * Created by Raye on 2017-05-16.
 */

public class Friend {
    private Drawable IconDrawable;
    private String Name;

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
}
