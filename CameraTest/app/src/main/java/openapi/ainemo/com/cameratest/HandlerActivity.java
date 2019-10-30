package openapi.ainemo.com.cameratest;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HandlerActivity extends Activity implements View.OnClickListener {

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        button1 = (Button) findViewById(R.id.btn1);
        button2 = (Button) findViewById(R.id.btn2);
        button3 = (Button) findViewById(R.id.btn3);
        button4 = (Button) findViewById(R.id.btn4);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                MyHandler myHandler = new MyHandler(getApplicationContext());
                Message message = new Message();
                message.what = 1;
                message.obj = "button1";
                myHandler.sendMessage(message);
                break;
            case R.id.btn2:
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        MyHandler myHandler = new MyHandler(getApplicationContext(),Looper.getMainLooper());
                        Message message = new Message();
                        message.what = 2;
                        message.obj = "button2";
                        myHandler.sendMessage(message);
                    }
                }.start();
                break;
            case R.id.btn3:
                final MyHandler myHandler3 = new MyHandler(getApplicationContext());
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Message message = new Message();
                        message.what = 3;
                        message.obj = "button3";
                        myHandler3.sendMessage(message);
                    }
                }.start();
                break;
            case R.id.btn4:
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        button4.setText("4444");
                    }
                }.start();
                break;
        }
    }
}
