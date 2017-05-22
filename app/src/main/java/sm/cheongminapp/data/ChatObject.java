package sm.cheongminapp.data;

import android.support.annotation.NonNull;

/**
 * Created by Raye on 2017-05-18.
 */

public abstract class ChatObject {

    public static final int INPUT_OBJECT = 0;
    public static final int RESPONSE_OBJECT = 1;
    public static final int SIGN_IMAGE_OBJECT = 2;

    private String text;

    @NonNull
    public String getText() {
        return text;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    public abstract int getType();
}
