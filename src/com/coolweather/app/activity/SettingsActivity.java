package com.coolweather.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.coolweather.R;

public class SettingsActivity extends Activity {
	
	private Switch BackAutoUpdate;
	
	private Button confirm;
	
	private LinearLayout frequencyLayout;
	
	private LinearLayout frequencyTimeLayout;
	
	private EditText updateFrequency;
	
	private Button back;
	
	private Spinner spinner;
	
	private String[] frequencyList = new String[] {"�Զ���", "4Сʱ", "8Сʱ", "12Сʱ"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings_area);
		BackAutoUpdate = (Switch) findViewById(R.id.back_auto_update);
		frequencyLayout = (LinearLayout) findViewById(R.id.update_frequency_layout);
		frequencyTimeLayout = (LinearLayout) findViewById(R.id.update_time_layout);
		//�������̨�Զ�����ʱ�ɼ�
		frequencyLayout.setVisibility(View.INVISIBLE);
		frequencyTimeLayout.setVisibility(View.INVISIBLE);
		BackAutoUpdate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			
				if (isChecked) {
					isBackAutoUpdate(true);
				} else {
					isBackAutoUpdate(false);
				}
				
			}
		});
		//�����������ָı����Ƶ��
		confirm = (Button) findViewById(R.id.confirm);
		updateFrequency = (EditText) findViewById(R.id.update_frequency);
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String frequencyTime = updateFrequency.getText().toString();
				changeFrequency(frequencyTime);
				Toast.makeText(SettingsActivity.this, "���", Toast.LENGTH_SHORT).show();
			}
		});
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this, ChooseAreaActivity.class);
				startActivity(intent);
			}
		});
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, frequencyList);
		spinner = (Spinner) findViewById(R.id.spinner);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setPrompt("��ѡ��Ƶ��");
		spinner.setSelection(1, true);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == 0) {
					showSetTime(true);
				} else {
					showSetTime(false);
					switch (position) {
					case 1:
						changeFrequency("4");
						break;
					case 2:
						changeFrequency("8");
						break;
					case 3:
						changeFrequency("12");
						break;
					default:
						break;
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
	}
	
	public void isBackAutoUpdate(Boolean isAuto) {
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		if (isAuto) {
			editor.putBoolean("back_auto_update", true);
			frequencyLayout.setVisibility(View.VISIBLE);
		} else {
			editor.putBoolean("back_auto_update", false);
			frequencyLayout.setVisibility(View.INVISIBLE);
		}
		editor.commit();
	}
	
	public void changeFrequency(String frequencyTime) {
		int time = Integer.parseInt(frequencyTime);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		editor.putInt("update_frequency_time", time);
		editor.commit();
	}
	
	public void showSetTime(Boolean diy) {
		if (diy) {
			frequencyTimeLayout.setVisibility(View.VISIBLE);
		} else {
			frequencyTimeLayout.setVisibility(View.INVISIBLE);
		}
	}
}
