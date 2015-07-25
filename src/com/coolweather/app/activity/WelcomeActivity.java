package com.coolweather.app.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.example.coolweather.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class WelcomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		final Intent intent = new Intent(this, ChooseAreaActivity.class);
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				startActivity(intent);
			}
		};
		timer.schedule(task, 1000 * 3);
	}
}
