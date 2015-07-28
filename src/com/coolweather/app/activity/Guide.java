package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.coolweather.app.adapter.ViewPagerAdapter;
import com.example.coolweather.R;

public class Guide extends Activity implements OnPageChangeListener{
	
	private ViewPager viewPager;
	
	private List<View> views;
	
	private ViewPagerAdapter adapter;
	
	private ImageView[] dots;
	
	private int[] ids = {R.id.point1, R.id.point2, R.id.point3};
	
	private Button enterButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		initViews();
		initDots();
		enterButton = (Button) views.get(2).findViewById(R.id.enter_button);
		enterButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Guide.this, ChooseAreaActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		
	}
	
	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(this);
		views = new ArrayList<View>();
		views.add(inflater.inflate(R.layout.welcome_1, null));
		views.add(inflater.inflate(R.layout.welcome_2, null));
		views.add(inflater.inflate(R.layout.welcome_3, null));
		adapter = new ViewPagerAdapter(this, views);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);
		
	}
	
	private void initDots() {
		dots = new ImageView[views.size()];
		for (int i = 0; i < views.size(); i++) {
			dots[i] = (ImageView) findViewById(ids[i]);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		for (int i = 0; i < ids.length; i++) {
			if (arg0 == i) {
				dots[i].setImageResource(R.drawable.selected_point);
			} else {
				dots[i].setImageResource(R.drawable.unselected_point);
			}
		}
	}
}
