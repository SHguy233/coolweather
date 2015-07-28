package com.coolweather.app.fragment;

import com.example.coolweather.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CityFragment extends Fragment {

	private TextView  textView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.city_fragment_content, container, false);
		textView = (TextView) view.findViewById(R.id.my_city_name);
		String cityName = getArguments().getString("city_name");
		textView.setText(cityName);
		return view;
	}
}
