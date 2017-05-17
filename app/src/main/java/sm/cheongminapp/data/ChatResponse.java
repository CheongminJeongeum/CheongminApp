package sm.cheongminapp.data;

/**
 * Created by Raye on 2017-05-18.
 */

public class ChatResponse extends ChatObject {
    @Override
    public int getType() {
        return ChatObject.RESPONSE_OBJECT;
    }
}
