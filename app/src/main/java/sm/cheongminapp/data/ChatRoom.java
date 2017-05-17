package sm.cheongminapp.data;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by Raye on 2017-05-18.
 */

public class ChatRoom implements Serializable {
    private Drawable iconDrawable;
    private String name;
    private String lastChat;

    public String getLastChat() {
        return lastChat;
    }

    public void setLastChat(String lastChat) {
        this.lastChat = lastChat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }
}
