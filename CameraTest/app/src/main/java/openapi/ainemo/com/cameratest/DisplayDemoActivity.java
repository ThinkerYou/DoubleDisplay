package openapi.ainemo.com.cameratest;

import android.app.Activity;
import android.app.Presentation;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

public class DisplayDemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_demo);
        DisplayManager displayManager = (DisplayManager) getSystemService(DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays();

        MyPresen myPresen = new MyPresen(this, displays[1]);
        myPresen.getWindow().setType(
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        myPresen.show();

    }

    class MyPresen extends Presentation {

        public MyPresen(Context outerContext, Display display) {
            super(outerContext, display);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.presentation_activity);
        }
    }
}
