package sm.cheongminapp.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sm.cheongminapp.R;
import sm.cheongminapp.data.ChatSignData;
import sm.cheongminapp.data.SignData;

/**
 * Created by Raye on 2017-05-28.
 */

public class SignVideoRepository {
    private static String packageName = "sm.cheongminapp";

    private Map<String, String> signFileMap = new HashMap<>();

    SignVideoRepository() {
        // TODO: DB에서 수화 매핑 정보를 오도록 수정해야함
        // 수어 <-> 경로
        /* MOV000250232 고맙다, 감사
MOV000249602 기다리다, 대기
MOV000249438 괜찮다, 무방하다
MOV000250606 늦다, 느리다 , 지각 , 더디다 , 서서히 , 지체 , 천천하다 , 찬찬하다
MOV000241289 말씀
*/
        signFileMap.put("고맙", "android.resource://" + packageName + "/" + R.raw.mov000250232);
        signFileMap.put("감사", "android.resource://" + packageName + "/" + R.raw.mov000250232);

        signFileMap.put("기다리", "android.resource://" + packageName + "/" + R.raw.mov000249602);
        signFileMap.put("대기", "android.resource://" + packageName + "/" + R.raw.mov000249602);

        signFileMap.put("괜찮",  "android.resource://" + packageName + "/" + R.raw.mov000249438);
        signFileMap.put("무방하",  "android.resource://" + packageName + "/" + R.raw.mov000249438);

        signFileMap.put("늦",  "android.resource://" + packageName + "/" + R.raw.mov000250606);
        signFileMap.put("느리",  "android.resource://" + packageName + "/" + R.raw.mov000250606);
        signFileMap.put("지각",  "android.resource://" + packageName + "/" + R.raw.mov000250606);
        signFileMap.put("더디",  "android.resource://" + packageName + "/" + R.raw.mov000250606);
        signFileMap.put("서서히",  "android.resource://" + packageName + "/" + R.raw.mov000250606);
        signFileMap.put("지체",  "android.resource://" + packageName + "/" + R.raw.mov000250606);

        signFileMap.put("말씀",  "android.resource://" + packageName + "/" + R.raw.mov000241289);
    }

    public SignData getSignData(String signText) {
        if(signFileMap.containsKey(signText))
            return new SignData(signText, signFileMap.get(signText));

        return null;
    }

    public List<SignData> getSignDataList(List<String> signTextList) {
        List<SignData> signDataList = new ArrayList<>();

        for(String signText: signTextList) {
            SignData signData = getSignData(signText);
            if(signData != null){
                signDataList.add(signData);
            }
        }

        return signDataList;
    }

    private static SignVideoRepository instance;
    public static SignVideoRepository getInstance() {
        if(instance == null)
            instance = new SignVideoRepository();

        return instance;
    }
}
