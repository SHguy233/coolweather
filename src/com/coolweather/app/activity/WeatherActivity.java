package com.coolweather.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.app.service.AutoUpdateService;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;
import com.example.coolweather.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class WeatherActivity extends BaseActivity implements OnClickListener{
	
	private LinearLayout weatherInfoLayout;
	
	private TextView cityName;
	
	private TextView publishTime;
	
	private TextView weatherDesc;
	
	private TextView temp1;
	
	private TextView temp2;
	
	private TextView currentDate;
	
	private Button switchCity;
	
	private Button refreshWeather;
	
	private ImageView weatherPic;
	
	private long exitTime = 0;
	
	private SlidingMenu slidingMenu;
	
	private Button exit;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityName = (TextView) findViewById(R.id.city_name);
		publishTime =(TextView) findViewById(R.id.publish_text);
		weatherDesc = (TextView) findViewById(R.id.weather_desc);
		temp1 = (TextView) findViewById(R.id.temp1);
		temp2 = (TextView) findViewById(R.id.temp2);
		currentDate = (TextView) findViewById(R.id.current_date);
		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		String countyCode = getIntent().getStringExtra("county_code");
		weatherPic = (ImageView) findViewById(R.id.weather_pic);
		if (!TextUtils.isEmpty(countyCode)) {
			publishTime.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityName.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		} else {
			showWeather();
		}
		String weather = showWeather();
		if ("多云".equals(weather)) {
			weatherPic.setImageResource(R.drawable.duoyun);
		} else if ("小雨".equals(weather)) {
			weatherPic.setImageResource(R.drawable.xiaoyu);
		} else if ("阴转阵雨".equals(weather)) {
			weatherPic.setImageResource(R.drawable.yinzhuanduoyun);
		} else {
			weatherPic.setImageResource(R.drawable.qing);
		}
		//滑动菜单
		slidingMenu = new SlidingMenu(this);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setMenu(R.layout.sliding_menu);
		exit = (Button) findViewById(R.id.exit);
		exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ActivityCollector.finishAll();
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishTime.setText("同步中...");
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			String weatherCode = prefs.getString("weather_code", "");
			if (!TextUtils.isEmpty(weatherCode)) {
				queryWeatherInfo(weatherCode);
			}
		default:
			break;
		}
	}
	
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
		queryFromServer(address, "countyCode");
	}
	
	private void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
		queryFromServer(address, "weatherCode");
	}
	
	private void queryFromServer(final String address,final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(final String response) {
				if ("countyCode".equals(type)) {
					if (!TextUtils.isEmpty(response)) {
						String[] array = response.split("\\|");
						if (array != null && array.length == 2) {
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				} else if ("weatherCode".equals(type)) {
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						public void run() {
							showWeather();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					public void run() {
						publishTime.setText("同步失败");
					}
				});
			}
		});
	}
	
	private String showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityName.setText(prefs.getString("city_name", ""));
		temp1.setText(prefs.getString("temp1", ""));
		temp2.setText(prefs.getString("temp2", ""));
		weatherDesc.setText(prefs.getString("weather_desc", ""));
		publishTime.setText("今天" + prefs.getString("publish_time", "") + "发布");
		currentDate.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityName.setVisibility(View.VISIBLE);
		if (prefs.getBoolean("back_auto_update", true)) {
			Intent intent = new Intent(this, AutoUpdateService.class);
			startService(intent);
		}
		String weather = prefs.getString("weather_desc", "");
		return weather;
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
			} else {
				ActivityCollector.finishAll();
			}	
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

