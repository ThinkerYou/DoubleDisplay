package openapi.ainemo.com.cameratest;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OperaterViewActivity extends Activity {

    private LinearLayout linearLayout;
    private TextView textView;
    private boolean isAdd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operater_view);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout_container);
        initAddView();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAdd) {
                    linearLayout.removeView(textView);
                } else {
                    initAddView();
                }

                isAdd = !isAdd;
            }
        });
    }

    private void initAddView() {
        textView = new TextView(OperaterViewActivity.this);
        textView.setText("被添加");
        textView.setTextSize(30);
        linearLayout.addView(textView);
    }
}
