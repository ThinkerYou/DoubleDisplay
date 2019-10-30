package soundrecorder.IPCTest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import openapi.ainemo.com.cameratest.R;

public class MessengerActivity extends Activity {

    private Messenger serviceMessenger;
    private Messenger messenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        ServiceConnection conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                serviceMessenger = new Messenger(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        messenger = new Messenger(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        String h = msg.getData().getString("h");
                        Log.i("hansage", "handleMessage: "+h);
//                        Toast.makeText(MessengerActivity.this, h, Toast.LENGTH_SHORT).show();
                        break;
                }
                super.handleMessage(msg);
            }
        });
        bindService(new Intent(this, MessengerService.class), conn, BIND_AUTO_CREATE);


        findViewById(R.id.btu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Message message = new Message();
                    message.what = 0;
                    Bundle data = message.getData();
                    data.putString("hell", "hello");
                    message.replyTo = messenger;
                    serviceMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
