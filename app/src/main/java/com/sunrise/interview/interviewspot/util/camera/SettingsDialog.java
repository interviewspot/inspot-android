package com.sunrise.interview.interviewspot.util.camera;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.sunrise.interview.interviewspot.R;


public class SettingsDialog extends Activity {
	//private TextView prTextEncodingFormat;
	//private TextView prTextContainerFormat;
	private Spinner prSpinnerEncodingFormat;
	private Spinner prSpinnerConainterFormat;
	private Spinner prSpinnerResolution;
	private Button prBtnOk;
	private Button prBtnCancel;

	//for folderSpinner: 1. original folder, 2. browse for a folder
	public static final int cpuH263 = 0;
	public static final int cpuMP4_SP = 1;
	public static final int cpuH264 = 2;
	
	public static final int cpu3GP = 0;
	public static final int cpuMP4 = 1;
	
	public static final int cpuRes176 = 0;
	public static final int cpuRes320 = 1;
	public static final int cpuRes720 = 2;
	
	//public static final String cpuEncodingFormatOptionString = "cpuEncodingFormatOption";
	//public static final String cpuContainerFormatOptionString = "cpuContainerFormatOption";
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.dialog_settings);	
		prSpinnerEncodingFormat = (Spinner) findViewById(R.id.dialog_settings_encoding_format_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.dialog_settings_encoding_format_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prSpinnerEncodingFormat.setAdapter(adapter);	
		prSpinnerEncodingFormat.setSelection(CConstant.puEncodingFormat);
		prSpinnerEncodingFormat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int rowSelected = (int)arg3;
				CConstant.puEncodingFormat = rowSelected;
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				//do nothing
			}
		});
		
		prSpinnerConainterFormat = (Spinner) findViewById(R.id.dialog_settings_container_format_spinner);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this, R.array.dialog_settings_container_format_array, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prSpinnerConainterFormat.setAdapter(adapter2);	
		prSpinnerConainterFormat.setSelection(CConstant.puContainerFormat);
		prSpinnerConainterFormat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int rowSelected = (int)arg3;
				CConstant.puContainerFormat = rowSelected;
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				//do nothing
			}
		});
		prSpinnerResolution = (Spinner) findViewById(R.id.dialog_settings_resolution_spinner);
		ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
                this, R.array.dialog_settings_resolution_array, android.R.layout.simple_spinner_item);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prSpinnerResolution.setAdapter(adapter3);	
		prSpinnerResolution.setSelection(CConstant.puResolutionChoice);
		prSpinnerResolution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int rowSelected = (int)arg3;
				CConstant.puResolutionChoice = rowSelected;
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				//do nothing
			}
		});
		prBtnOk = (Button) findViewById(R.id.dialog_settings_btn1);
		prBtnOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//export picture to specified location with specified name
				setResult(RESULT_OK);
				finish();
			}
		});
		prBtnCancel = (Button) findViewById(R.id.dialog_settings_btn2);
		prBtnCancel.setOnClickListener(new View.OnClickListener() {	
			public void onClick(View v) {
				//cancel export, just finish the activity
				SettingsDialog.this.finish();
			}
		});
		getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}
}

