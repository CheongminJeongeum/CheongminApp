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
import java.util.List;

/**
 * Created by user on 2017. 6. 9..
 */
public class BTService extends Service {
    private final IBinder myBinder = new MyLocalBinder();

    String[] signFileNames = {"action.csv", "goreum.csv", "healthy.csv"};

    BluetoothSocket mSocket = null;
    OutputStream mOutputStream = null;
    InputStream mInputStream = null;
    Thread mWorkerThread;

    int readBufferPosition;
    byte[] readBuffer;
    String mStrDelimiter = "\n";
    char mCharDelimiter =  '\n';

    List<List<Integer>> fingerList = new ArrayList<List<Integer>>();

    List<List<Float>> zairoOneList = new ArrayList<List<Float>>(); // 임시로 모아놓는 리스트
    List<List<Float>> zairoFourList = new ArrayList<List<Float>>(); // 임시로 모아놓는 리스트

    List<List<List<Float>>> zairoReferenceData = new ArrayList<List<List<Float>>>();
    List<List<Float>> zairoInputData = new ArrayList<List<Float>>();

    int fingerData = 0;

    String[] fileNames = {"action.csv", "goreum.csv", "healthy.csv"};

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

        for(int i=0; i<signFileNames.length; i++) {
            try {
                CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open(signFileNames[i])));
                String[] lines;
                List<List<Float>> entireCsvData = new ArrayList<List<Float>>();

                if(reader.readNext() == null) continue; // 첫줄의 레이블(x1, x2, x3...제거)
                while((lines = reader.readNext()) != null) {
                    List<Float> lineData = new ArrayList<Float>();
                    for(int j=0; j<lines.length; j++) {
                        Log.d("line", lines[j]);
                        lineData.add(Float.parseFloat(lines[j]));
                    }
                    entireCsvData.add(lineData);
                }
                zairoReferenceData.add(entireCsvData);

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
        mWorkerThread = new Thread(new Runnable()
        {
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

                                    if(data.charAt(0) == 'F') {
                                        //Log.d("F", data);
                                        String splitedData = data.split(":")[1];
                                        String[] datas = splitedData.split(",");
                                        List<Integer> fingers = new ArrayList<Integer>();
                                        for(int j = 0; j<datas.length; j++) {
                                            Log.d("F", datas[j]);
                                            Integer.parseInt(datas[j]);
                                        }
                                        fingerList.add(fingers);
                                    }
                                    else if(data.startsWith("1:")) {
                                        //Log.d("1", data);
                                        String splitedData = data.split(":")[1];
                                        String[] datas = splitedData.split(",");
                                        List<Float> zairoOne = new ArrayList<Float>();
                                        for(int j = 0; j<datas.length; j++) {
                                            Log.d("1", datas[j]);
                                            Float.parseFloat(datas[j]);
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
                                            Float.parseFloat(datas[j]);
                                        }
                                        zairoFourList.add(zairoFour);
                                    }
                                    // 한 단어가 끝날 때
                                    else if(data.equals("stop")) {
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
                return "ㅗㅗ";
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
