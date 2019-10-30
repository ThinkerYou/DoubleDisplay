package soundrecorder.sockettest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.LocalServerSocket;
import android.net.LocalSocket;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import openapi.ainemo.com.cameratest.R;

public class SocketTestAct extends Activity {

    //    private SocketTest socketTest;
    public static final String OPENAPI_CALL_STARTAUDIORECORD = "nemo.intent.action.openAPI.startAudioRecord";
    public static final String OPENAPI_CALL_STOPAUDIORECORD = "nemo.intent.action.openAPI.stopaudiorecord";
    private Button sendBtn;
    private ThreadPoolExecutor mExecutorService;
    private LinkedBlockingDeque<Runnable> workQueue;
    private LocalServerSocket serverSocket;
    private LocalSocket socket;
    private OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_test);
//        socketTest = new SocketTest();

        sendBtn = (Button) findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (init()) {
                    MyRunable myRunable = new MyRunable();
                    if (workQueue != null) {
                        workQueue.add(myRunable);
                    }
                    createSocket();
                }
            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(OPENAPI_CALL_STARTAUDIORECORD);
        intentFilter.addAction(OPENAPI_CALL_STOPAUDIORECORD);
        registerReceiver(new AudioDataBroadCast(), intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        socketTest.close();
    }

    private boolean init() {
        boolean isInit = false;
        try {
            workQueue = new LinkedBlockingDeque<>();
            mExecutorService = new ThreadPoolExecutor(1, 2, 200, TimeUnit.SECONDS, workQueue);
            serverSocket = new LocalServerSocket("test");
            isInit = true;
        } catch (IOException e) {
            e.printStackTrace();
            isInit = false;
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return isInit;

    }


    class AudioDataBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("AudioDataBroadCast", "onReceive: " + intent.getAction());
            if (intent.getAction().equals(OPENAPI_CALL_STARTAUDIORECORD)) {
                Log.i("startAudioRecord 00", "onReceive: ");
                if (init()) {
                    MyRunable myRunable = new MyRunable();
                    if (workQueue != null) {
                        workQueue.add(myRunable);
                    }
                    createSocket();
                }

            } else if (intent.getAction().equals(OPENAPI_CALL_STOPAUDIORECORD)) {
                Log.i("stopaudiorecord", "onReceive: ");
                closeSocket();

            }
        }
    }

    private void closeSocket() {
        Log.i("closeSocket", "closeSocket: ");

        try {
            outputStream.close();
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createSocket() {

        try {
            while (true) {

                if (mExecutorService != null) {
                    mExecutorService.execute(workQueue.take());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MyRunable implements Runnable {

        MyRunable() {
        }

        @Override
        public void run() {
            try {
                socket = serverSocket.accept();
                outputStream = socket.getOutputStream();
                outputStream.write("hello".getBytes());
                outputStream.flush();
                Log.i("bytes", "sendAudioDataToClient: ");
//
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
