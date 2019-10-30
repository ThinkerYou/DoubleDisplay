package openapi.ainemo.com.cameratest;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class MyHandler extends Handler {
    private Context mtx;

    public MyHandler(Context context) {
        mtx = context;
    }

    public MyHandler(Context context, Looper mLooper) {
        super(mLooper);
        mtx = context;

    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        switch (msg.what) {
            case 1:
            case 2:
            case 3:
                Toast.makeText(mtx, ((String) msg.obj), Toast.LENGTH_SHORT).show();

        }
    }
}
