package com.commonsense.hkgalden.ui;

import com.commonsense.hkgaldenPaid.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingFragment extends PreferenceFragment
{
	public static final String preference = "Brightness";

	
    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
      //  sp.registerOnSharedPreferenceChangeListener(this);//监听数据变化，调节屏幕亮度   
    }
}