package sm.cheongminapp.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raye on 2017-05-22.
 */

public class ChatSignData extends ChatObject {
    public List<SignData> SignDataList = new ArrayList<>();

    @Override
    public int getType() {
        return SIGN_IMAGE_OBJECT;
    }
}
