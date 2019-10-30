package soundrecorder.IPCTest;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

public class MessengerService extends Service {

    private final Messenger messenger;

    @SuppressLint("HandlerLeak")
    public MessengerService() {
        messenger = new Messenger(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Bundle data = msg.getData();
                        String hell = data.getString("hell");
                        Toast.makeText(MessengerService.this, hell, Toast.LENGTH_SHORT).show();
                        Messenger replyTo = msg.replyTo;
                        Message message = new Message();
                        message.what = 1;
                        Bundle data1 = message.getData();
                        data1.putString("h", "helle client");
                        try {
                            replyTo.send(message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                super.handleMessage(msg);
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

}
