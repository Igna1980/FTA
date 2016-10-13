package com.bamobile.fdtks.activities;

import com.bamobile.fdtks.R;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class LegalNoticesActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_legal_notices);
		((TextView)findViewById(R.id.textViewId)).setText(
				GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this));
	}

}
