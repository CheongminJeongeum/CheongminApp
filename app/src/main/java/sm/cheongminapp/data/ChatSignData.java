package sm.cheongminapp.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raye on 2017-05-22.
 */

public class ChatSignData extends ChatObject {
    private List<SignData> signDataList = new ArrayList<>();
    private int playIndex;

    @Override
    public int getType() {
        return SIGN_IMAGE_OBJECT;
    }

    public List<SignData> getSignDataList() {
        return signDataList;
    }
    public void setSignDataList(List<SignData> signDataList) {
        this.signDataList = signDataList;
    }

    public int getPlayIndex() {
        return playIndex;
    }
    public void setPlayIndex(int playIndex) {
        this.playIndex = playIndex;
    }
}
