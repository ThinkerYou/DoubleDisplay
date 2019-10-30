package openapi.ainemo.com.cameratest.view;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.TextView;

import openapi.ainemo.com.cameratest.R;

public class SamplePresentation extends Presentation {

    private LinearLayout linearLayout;
    private TextView mTextView;
    private Context mContext;

    public SamplePresentation(Context outerContext, Display display) {
        super(outerContext, display);
        mContext = outerContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operater_child_view);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout_container);
        mTextView = (TextView) findViewById(R.id.text_view);
    }

    public void deleteTextView(TextView outTextView) {
        linearLayout.removeView(mTextView);
    }

    public void addTextView(TextView outTextView) {
        linearLayout.addView(outTextView);
    }
}
