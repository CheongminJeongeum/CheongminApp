package sm.cheongminapp.repository;

import java.util.HashMap;
import java.util.Map;

import sm.cheongminapp.R;

/**
 * Created by Raye on 2017-05-28.
 */

public class SignVideoRepository {
    private static String packageName = "sm.cheongminapp";

    private Map<String, String> signFileMap = new HashMap<>();

    SignVideoRepository() {
        // TODO: DB에서 수화 매핑 정보를 오도록 수정해야함
        // 수어 <-> 경로
        signFileMap.put("괜찮다",  "android.resource://" + packageName + "/" + R.raw.mov000249438);
        signFileMap.put("고맙다", "android.resource://" + packageName + "/" + R.raw.mov000250232);
    }

    public String getSignVideo(String text) {
        if(signFileMap.containsKey(text))
            return signFileMap.get(text);

        return "";
    }

    private static SignVideoRepository instance;
    public static SignVideoRepository getInstance() {
        if(instance == null)
            instance = new SignVideoRepository();

        return instance;
    }
}
