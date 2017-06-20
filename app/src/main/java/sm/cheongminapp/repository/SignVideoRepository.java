package sm.cheongminapp.repository;

import android.content.Context;
import android.os.Debug;
import android.util.Log;

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
    private final static String resourcePrefix = "android.resource://";

    private Map<String, String> signFileMap = new HashMap<>();

    SignVideoRepository(Context context) {

        String filePath = resourcePrefix + context.getPackageName() + "/";

        Log.d("PackageName", context.getPackageName());

        /*
        MOV000250232 고맙다, 감사
        MOV000249602 기다리다, 대기
        MOV000249438 괜찮다, 무방하다
        MOV000250606 늦다, 느리다 , 지각 , 더디다 , 서서히 , 지체 , 천천하다 , 찬찬하다
        MOV000241289 말씀
        */
        signFileMap.put("고맙", filePath + R.raw.mov000250232);
        signFileMap.put("감사하", filePath + R.raw.mov000250232);

        signFileMap.put("기다리", filePath + R.raw.mov000249602);
        signFileMap.put("대기", filePath + R.raw.mov000249602);

        signFileMap.put("괜찮", filePath + R.raw.mov000249438);
        signFileMap.put("무방하", filePath + R.raw.mov000249438);

        signFileMap.put("늦", filePath + R.raw.mov000250606);
        signFileMap.put("느리", filePath + R.raw.mov000250606);
        signFileMap.put("지각", filePath + R.raw.mov000250606);
        signFileMap.put("더디", filePath + R.raw.mov000250606);
        signFileMap.put("서서히", filePath + R.raw.mov000250606);
        signFileMap.put("지체",  filePath + R.raw.mov000250606);

        signFileMap.put("말씀",  filePath + R.raw.mov000241289);

        // 6/20
        signFileMap.put("안녕하세요", filePath + R.raw.mov000244910);

        signFileMap.put("만남", filePath + R.raw.mov000252208);
        signFileMap.put("만나다", filePath + R.raw.mov000252208);

        signFileMap.put("부끄럽다", filePath + R.raw.mov000254200);

        signFileMap.put("배", filePath + R.raw.mov000254721); // 신체

        signFileMap.put("연락", filePath + R.raw.mov000255737);
        signFileMap.put("연결", filePath + R.raw.mov000255737);

        signFileMap.put("아프다", filePath + R.raw.mov000256144);

        signFileMap.put("질문", filePath + R.raw.mov000258834);
        signFileMap.put("묻다", filePath + R.raw.mov000258834);
        signFileMap.put("문의", filePath + R.raw.mov000258834);
        signFileMap.put("물어보다", filePath + R.raw.mov000258834);

        signFileMap.put("물어보다", filePath + R.raw.mov000259394);
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
    public static SignVideoRepository getInstance(Context context) {
        if(instance == null)
            instance = new SignVideoRepository(context);

        return instance;
    }
}
