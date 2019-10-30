package soundrecorder.sockettest;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AudioSocketProxy {

    private LocalSocket mLocalSocket;
    private BufferedReader bufferedReader;
    private JsonParser jsonParser = new JsonParser();
    private static final String MIC_DETECTION_KEY = "source_localization";

    public AudioSocketProxy() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (!init()) {
                    SystemClock.sleep(5000);
                }

                while (true) {
                    String line = read();
                    if (line != null) {
                        processMessage(line);
                    }
                }
            }
        };
        new Thread(runnable).start();
    }

    private boolean init() {
        boolean ret = false;

        mLocalSocket = new LocalSocket();

        LocalSocketAddress socketAddress = new LocalSocketAddress("test", LocalSocketAddress.Namespace.ABSTRACT);

        try {
            mLocalSocket.connect(socketAddress);
            mLocalSocket.setSoTimeout(10000);
            bufferedReader = new BufferedReader(new InputStreamReader(mLocalSocket.getInputStream()));
            ret = true;
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (mLocalSocket != null) {
                    mLocalSocket.close();
                }

                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                mLocalSocket = null;
                bufferedReader = null;
            }
        }

        return ret;
    }

    private String read() {
        while (bufferedReader != null) {
            try {
                return bufferedReader.readLine();
            } catch (IOException e) {
                String msg = e.getMessage();
                if (msg != null && msg.equals("try agian")) {
                    Log.i("catch", "read: ");
                    continue;
                } else {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        SystemClock.sleep(10000);
        return null;
    }

    private void processMessage(String line) {
        if (line == null) {
            return;
        }
        JsonObject jsonObject = null;
        try {
            jsonObject = jsonParser.parse(line).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonObject == null) {
            return;
        }

        if (jsonObject.has(MIC_DETECTION_KEY)) {

            JsonArray asJsonArray = jsonObject.get(MIC_DETECTION_KEY).getAsJsonArray();
        }


    }

    private void release() {
        try {
            if (bufferedReader != null) {

                bufferedReader.close();

            }
            if (mLocalSocket != null) {
                mLocalSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
