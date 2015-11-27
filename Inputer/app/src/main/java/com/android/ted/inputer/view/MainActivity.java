package com.android.ted.inputer.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.ted.inputer.R;
import com.android.ted.inputer.util.VersionUtil;


public class MainActivity extends Activity {
	public static int OVERLAY_PERMISSION_REQ_CODE = 0x0012;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button startFloatWindow = (Button) findViewById(R.id.start_float_window);
		startFloatWindow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
				startService(intent);
				finish();
			}
		});
	}

    @Override
    protected void onResume() {
        super.onResume();
        canDrawOverlays();
    }

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            canDrawOverlays();
		}
	}

	public void canDrawOverlays() {
        if(VersionUtil.isM()){
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
        }
	}

}
