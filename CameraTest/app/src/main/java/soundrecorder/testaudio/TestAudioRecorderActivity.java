package soundrecorder.testaudio;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import openapi.ainemo.com.cameratest.R;

public class TestAudioRecorderActivity extends Activity implements View.OnClickListener {
    //音频获取源
    private int audioSource = MediaRecorder.AudioSource.MIC;
    //设置音频采样率
    private static int simpleRateInHz = 48000;
    //设置音频录制的声道
    private static int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
    //音频数据格式
    private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    //缓冲区字节大小
    private int bufferSizeInBytes = 0;
    private Button start;
    private Button stop;
    private AudioRecord mAudioRecord;
    private String audioName = "/sdcard/love.raw";
    private String newAudioName = "/sdcard/new.wav";
    private boolean isRecording = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_audio_recorder);
        init();
    }

    private void init() {
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        createAudioRecord();
    }

    private void createAudioRecord() {
        bufferSizeInBytes = AudioRecord.getMinBufferSize(simpleRateInHz, channelConfig, audioFormat);
        mAudioRecord = new AudioRecord(audioSource, simpleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                startRecorder();
                break;
            case R.id.stop:
                stopRecorder();
                break;
        }
    }

    private void stopRecorder() {
        Log.i("readSize", "stopRecorder: ");
        if (mAudioRecord != null) {
            mAudioRecord.stop();
            mAudioRecord.release();
            isRecording = false;
            mAudioRecord = null;
        }
    }

    private void startRecorder() {
        Log.i("readSize", "startRecorder: ");
        mAudioRecord.startRecording();
        isRecording = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                witeDataToFile();
            }
        }).start();

//        copyWavFile();
    }

    private void copyWavFile() {

    }

    private void witeDataToFile() {
        byte[] audioData = new byte[bufferSizeInBytes];
        int readSize = 0;
        FileOutputStream outputStream = null;
        try {
            File file = new File(audioName);
            if (file.exists()) {
                file.delete();
            }
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (isRecording) {
            readSize = mAudioRecord.read(audioData, 0, bufferSizeInBytes);
            Log.i("readSize", "witeDataToFile: " + readSize);
            if (AudioRecord.ERROR_INVALID_OPERATION != readSize) {
                try {
                    outputStream.write(audioData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
