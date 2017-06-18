package sm.cheongminapp.service;

import android.app.Service;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2017. 6. 9..
 */
public class BTService extends Service {
    private final IBinder myBinder = new MyLocalBinder();

    String[][] signFileNames = {{"action.csv", "goreum.csv", "healthy.csv"}};
    String[] fingerFileNames = {"상황_병원.csv", "상황_회사.csv"};
    String[][] vocaList = {{"가래", "간호사", "건강", "고름", "곪다", "기침", "몸살", "문병", "발목",
            "복통", "빈혈", "설사", "수술", "심장마비", "아프다", "암", "약국", "열", "의사", "임신", "입원",
            "저혈압", "주사", "퇴원", "폐렴"}, {"경력", "근무", "대리", "대리(직급)", "부장", "사원", "사장", "서류",
            "야근", "월급", "출근", "출근", "출장", "취업", "퇴근", "회사원", "회의", "휴가"}};

    BluetoothSocket mSocket = null;
    OutputStream mOutputStream = null;
    InputStream mInputStream = null;
    Thread mWorkerThread;

    int readBufferPosition;
    byte[] readBuffer;
    String mStrDelimiter = "\n";
    char mCharDelimiter =  '\n';

    List<List<Float>> zairoOneList = new ArrayList<List<Float>>(); // 임시로 모아놓는 리스트
    List<List<Float>> zairoFourList = new ArrayList<List<Float>>(); // 임시로 모아놓는 리스트

    // 레퍼런스 자이로 데이터
    List<List<List<List<Float>>>> zairoReferenceData = new ArrayList<List<List<List<Float>>>>();
    // 인풋 자이로 데이터
    List<List<Float>> zairoInputData = new ArrayList<List<Float>>();
    // 레퍼런스 손가락 데이터
    List<List<List<List<Integer>>>> fingerReferenceData = new ArrayList<List<List<List<Integer>>>>();
    // 인풋 손가락 데이터
    List<List<Integer>> fingerInputData = new ArrayList<List<Integer>>();

    int fingerData = 0;

    @Override
    public IBinder onBind(Intent arg0) {
        return myBinder;
    }

    public class MyLocalBinder extends Binder {
        public BTService getService() {
            return BTService.this;
        }
    }

    public void setBluetoothSocket(BluetoothSocket mSocket) {
        this.mSocket = mSocket;

        try {
            mInputStream = mSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //loadReferenceZairoData();
        loadReferenceFingersData();
    }

    public void loadReferenceZairoData() {
        for(int outer = 0; outer<signFileNames.length; outer++) {
            List<List<List<Float>>> zairoData = new ArrayList<List<List<Float>>>();
            for (int i = 0; i < signFileNames[outer].length; i++) {
                try {
                    CSVReader reader = new CSVReader(
                            new InputStreamReader(getAssets().open(signFileNames[outer][i])));
                    String[] lines;
                    List<List<Float>> entireCsvData = new ArrayList<List<Float>>();

                    if (reader.readNext() == null) continue; // 첫줄의 레이블(x1, x2, x3...제거)
                    while ((lines = reader.readNext()) != null) {
                        List<Float> lineData = new ArrayList<Float>();
                        for (int j = 0; j < lines.length; j++) {
                            Log.d("line", lines[j]);
                            lineData.add(Float.parseFloat(lines[j]));
                        }
                        entireCsvData.add(lineData);
                    }
                    zairoData.add(entireCsvData);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            zairoReferenceData.add(zairoData);
        }
    }

    public void loadReferenceFingersData() {
        // 최대 3번의 손가락 형태 변화가 있을 수 있으므로
        for(int outer=0; outer<fingerFileNames.length; outer++) {
            fingerReferenceData.add(new ArrayList<List<List<Integer>>>());
            for (int i = 0; i < 3; i++) {
                fingerReferenceData.get(outer).add(new ArrayList<List<Integer>>());
            }

            try {
                CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open(fingerFileNames[outer])));
                String[] lines;

                while ((lines = reader.readNext()) != null) {
                    for (int j = 1; j < lines.length; j++) {
                        Log.d("line", lines[j]);
                        List<Integer> lineData = new ArrayList<Integer>();
                        for (int k = 0; k < lines[j].length(); k++) {
                            if (lines[j].charAt(k) == '-') {
                                lineData.add(-1);
                            } else {
                                lineData.add(lines[j].charAt(k) - '0');
                            }
                        }
                        fingerReferenceData.get(outer).get(j).add(lineData);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 데이터 수신(쓰레드 사용 수신된 메시지를 계속 검사함)
    public void beginListenForData() {
        final Handler handler = new Handler();

        readBufferPosition = 0;                 // 버퍼 내 수신 문자 저장 위치.
        readBuffer = new byte[4096];            // 수신 버퍼.

        // 문자열 수신 쓰레드.
        mWorkerThread = new Thread(new Runnable() {
            public String[] prevFingerShape = {"-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1"};

            Map<String, Integer> predictedVoca = new HashMap<String, Integer>();

            @Override
            public void run() {
                // interrupt() 메소드를 이용 스레드를 종료시키는 예제이다.
                // interrupt() 메소드는 하던 일을 멈추는 메소드이다.
                // isInterrupted() 메소드를 사용하여 멈추었을 경우 반복문을 나가서 스레드가 종료하게 된다.
                while(!Thread.currentThread().isInterrupted()) {
                    try {
                        // InputStream.available() : 다른 스레드에서 blocking 하기 전까지 읽은 수 있는 문자열 개수를 반환함.
                        int byteAvailable = mInputStream.available();   // 수신 데이터 확인
                        /*
                        int bytes = mInputStream.read(readBuffer, curLength, readBuffer.length - curLength);
                        if (bytes > 0) {
                            // still reading
                            curLength += bytes;
                            Log.d("byteNum", Integer.toString(bytes));
                            Log.d("payload", new String(readBuffer, "US-ASCII"));
                        }
                        */
                        if(byteAvailable > 0) {                        // 데이터가 수신된 경우.
                            byte[] packetBytes = new byte[byteAvailable];
                            //Log.d("byteAv", Integer.toString(byteAvailable));
                            // read(buf[]) : 입력스트림에서 buf[] 크기만큼 읽어서 저장 없을 경우에 -1 리턴.


                            mInputStream.read(packetBytes);
                            for(int i=0; i<byteAvailable; i++) {
                                byte b = packetBytes[i];
                                if(b == mCharDelimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    //  System.arraycopy(복사할 배열, 복사시작점, 복사된 배열, 붙이기 시작점, 복사할 개수)
                                    //  readBuffer 배열을 처음 부터 끝까지 encodedBytes 배열로 복사.
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);

                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    boolean isChanged = false;
                                    if(data.charAt(0) == 'F') {
                                        //Log.d("F", data);
                                        String splitedData = data.split(":")[1];
                                        String[] datas = splitedData.split(",");
                                        List<Integer> fingers = new ArrayList<Integer>();
                                        for(int j = 0; j<datas.length; j++) {
                                            if(!prevFingerShape[j].equals(datas[j])) {
                                                prevFingerShape[j] = datas[j];
                                                isChanged = true;
                                            }
                                            fingers.add(Integer.parseInt(datas[j]));
                                        }
                                        if(isChanged) {
                                            for(int j=0; j<fingers.size(); j++) {
                                                Log.d("F", Integer.toString(fingers.get(j)));
                                            }
                                            fingerInputData.add(fingers);
                                        }
                                    }
                                    else if(data.startsWith("1:")) {
                                        //Log.d("1", data);
                                        String splitedData = data.split(":")[1];
                                        String[] datas = splitedData.split(",");
                                        List<Float> zairoOne = new ArrayList<Float>();
                                        for(int j = 0; j<datas.length; j++) {
                                            Log.d("1", datas[j]);
                                            zairoOne.add(Float.parseFloat(datas[j]));
                                        }
                                        zairoOneList.add(zairoOne);
                                    }
                                    else if(data.startsWith("4:")) {
                                        //Log.d("4", data);
                                        String splitedData = data.split(":")[1];
                                        String[] datas = splitedData.split(",");
                                        List<Float> zairoFour = new ArrayList<Float>();
                                        for(int j = 0; j<datas.length; j++) {
                                            Log.d("4", datas[j]);
                                            zairoFour.add(Float.parseFloat(datas[j]));
                                        }
                                        zairoFourList.add(zairoFour);
                                    }
                                    // 한 단어가 끝날 때
                                    else if(data.equals("stop")) {
                                        /*
                                            1번 자이로와 4번 자이로의 데이터를 합침.
                                         */
                                        for(int j=0; j<fingerInputData.size(); j++) {
                                            for(int k=0; k<fingerInputData.get(i).size(); k++) {
                                                Log.d("inputfinger", Integer.toString(
                                                        fingerInputData.get(i).get(j).intValue()));
                                            }
                                        }
                                        int length = zairoOneList.size() > zairoFourList.size() ?
                                                zairoFourList.size() : zairoOneList.size();
                                        for(int j=0; j<length; j++) {
                                            List<Float> zairoData = new ArrayList<Float>();
                                            for(int k=0; k<zairoOneList.get(j).size(); k++) {
                                                zairoData.add(zairoOneList.get(j).get(k));
                                            }
                                            for(int k=0; k<zairoFourList.get(j).size(); k++) {
                                                zairoData.add(zairoFourList.get(j).get(k));
                                            }
                                            zairoInputData.add(zairoData);
                                        }

                                        String voca = prediction();
                                        // 챗액티비티에 브로드캐스트 전송
                                        // 전역변수들 모두 초기화
                                    }
                                }
                                else if(b == '�') {
                                    continue;
                                }
                                else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }

                    } catch (Exception e) {    // 데이터 수신 중 오류 발생.

                    }
                }
            }
            // 예측한 단어 반환
            public String prediction() {
                // 손가락에서 추리고 남은 후보군들
                HashMap<String, Integer> noVoca = new HashMap<String, Integer>();

                for(int i=0; i<fingerReferenceData.size(); i++) {
                    // 어떤 상황이냐에 따라 패스할지 말지 결정
                    for(int j=0; j<fingerReferenceData.get(i).size(); j++) {
                        for(int k=0; k<fingerReferenceData.get(i).get(j).size(); k++) {
                            // 손가락 데이터 8개 순회
                            List<Integer> fingerInfoList = fingerReferenceData.get(i).get(j).get(k);
                            for(int l=0; l<fingerInfoList.size(); l++) {
                                if(fingerInfoList.get(l) == -1) continue;
                                else if(fingerInfoList.get(l).intValue()
                                        != fingerInputData.get(j).get(l).intValue()) {
                                    // 안되는 단어들 처리
                                    noVoca.put(i+"-"+k, 1);
                                    break;
                                }
                            }

                        }
                    }
                }

                for(int i=0; i<vocaList.length; i++) {
                    // 여기서 상황 한정 결과 추리기
                    for(int j=0; j<vocaList[i].length; j++) {
                        if(noVoca.get(i+"-"+j) == 1) {
                            Log.d("vocalist", vocaList[i][j]);
                            predictedVoca.put(vocaList[i][j], 1);
                        }
                    }
                }

                // 자이로 데이터로 예측
                return matching();
            }

            public String matching() {
                float minDistance = 999999999;
                int minI = 0, minJ = 0;

                for(int i=0; i<signFileNames.length; i++) {
                    for(int j=0; j<signFileNames[i].length; j++) {
                        if(predictedVoca.get(signFileNames[i][j]) == 1) {
                            float res = calc(i, j);
                            if(minDistance > res) {
                                minDistance = res;
                                minI = i;
                                minJ = j;
                            }
                        }
                    }
                }

                return vocaList[minI][minJ];
            }

            public float calc(int y, int x) {
                List<List<Float>> referData = zairoReferenceData.get(y).get(x);
                float[][] matrix = new float[referData.size()][zairoInputData.size()];

                for(int i=0; i<referData.size(); i++) {
                    for(int j=0; j<zairoInputData.size(); j++) {
                        float result = cosineSim(referData.get(i), zairoInputData.get(j));
                        if(i == 0 && j == 0) matrix[i][j] = result;
                        else if(i == 0) matrix[i][j] = matrix[i][j-1] + result;
                        else if(j == 0) matrix[i][j] = matrix[i-1][j] + result;
                        else matrix[i][j] = Math.min(matrix[i-1][j-1], Math.min(matrix[i][j-1],
                                    matrix[i-1][j])) + result;
                    }
                }

                return matrix[referData.size()-1][zairoInputData.size()-1];
            }

            private float cosineSim(List<Float> referData, List<Float> inputData) {
                float mo = 0;
                float ja = 0;
                float referValue = 0, inputValue = 0;

                for(int i=0; i<referData.size(); i++) {
                    ja += referData.get(i)*inputData.get(i);
                }
                for(int i=0; i<referData.size(); i++) {
                    referValue += referData.get(i)*referData.get(i);
                    inputValue += inputData.get(i)*inputData.get(i);
                }
                mo = (float) Math.sqrt(referValue) * (float) Math.sqrt(inputValue);

                return Math.abs(1 - (ja / mo));
            }
        });
        mWorkerThread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mWorkerThread.interrupt();
        try {
            mInputStream.close();
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
