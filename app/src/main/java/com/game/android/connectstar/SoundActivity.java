package com.game.android.connectstar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.game.android.connectstar.utils.DataHandler;
import com.game.android.connectstar.utils.PrefClass;

import java.util.TimerTask;

public class SoundActivity extends Activity implements OnClickListener {

	Activity activity;

	private LinearLayout ll1, ll2, ll3, ll4;
	private Button bsound, bvolumeplus, bvolumeminus, bvolumereset;
	private TextView textViewvalue;

	private AudioManager audioManager;
	private int maxVolume = 0, minVolume = 0, currentVolume = 0;
	private LinearLayout relativeLayout;

	public SoundActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = SoundActivity.this;

		setContentView(R.layout.activity_sound);

		findViews();

	}

	private void findViews() {

		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		if (currentVolume > 10) {
			currentVolume = 10;
		}

		ll1 = (LinearLayout) findViewById(R.id.ll1);
		ll2 = (LinearLayout) findViewById(R.id.ll2);
		ll3 = (LinearLayout) findViewById(R.id.ll3);
		ll4 = (LinearLayout) findViewById(R.id.ll4);

		bsound = (Button) findViewById(R.id.bsound);
		bvolumeplus = (Button) findViewById(R.id.bvolumeplus);
		bvolumeminus = (Button) findViewById(R.id.bvolumeminus);
		bvolumereset = (Button) findViewById(R.id.bvolumereset);

		textViewvalue = (TextView) findViewById(R.id.textViewvalue);

		bsound.setTypeface(DataHandler.getTypeface(activity), Typeface.BOLD);
		bvolumeplus.setTypeface(DataHandler.getTypeface(activity),
				Typeface.BOLD);
		bvolumeminus.setTypeface(DataHandler.getTypeface(activity),
				Typeface.BOLD);
		bvolumereset.setTypeface(DataHandler.getTypeface(activity),
				Typeface.BOLD);
		textViewvalue.setTypeface(DataHandler.getTypeface(activity),
				Typeface.BOLD);

		textViewvalue.setText(String.valueOf(currentVolume));

		ll1.setOnClickListener(this);
		ll2.setOnClickListener(this);
		ll3.setOnClickListener(this);
		ll4.setOnClickListener(this);
		bvolumereset.setOnClickListener(this);

		relativeLayout = findViewById(R.id.relativelayout);
		java.util.Timer timer = new java.util.Timer();
		MyTimer myTimer = new MyTimer();
		timer.schedule(myTimer, 1000, 10000);

	}

	class MyTimer extends TimerTask {

		@Override
		public void run() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ColorDrawable[] color = {new ColorDrawable(Color.CYAN), new ColorDrawable(Color.GREEN), new ColorDrawable(Color.WHITE),
							new ColorDrawable(Color.BLUE), new ColorDrawable(Color.CYAN), new ColorDrawable(Color.YELLOW), new ColorDrawable(Color.CYAN)};
					TransitionDrawable transitionDrawable = new TransitionDrawable(color);
					transitionDrawable.startTransition(8000);
					relativeLayout.setBackground(transitionDrawable);
				}
			});
		}
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ll1:

			break;
		case R.id.ll2:

			if (currentVolume >= maxVolume) {
				currentVolume = maxVolume;
			} else {
				currentVolume += 1;
			}

			if (currentVolume > 10) {
				currentVolume = 10;
			}

			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
					currentVolume, AudioManager.FLAG_PLAY_SOUND);

			textViewvalue.setText(String.valueOf(currentVolume));

			break;
		case R.id.ll3:
			if (currentVolume <= 0) {
				currentVolume = 0;
			} else {
				currentVolume -= 1;
			}

			if (currentVolume > 10) {
				currentVolume = 10;
			}

			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
					currentVolume, AudioManager.FLAG_PLAY_SOUND);
			textViewvalue.setText(String.valueOf(currentVolume));
			break;
		case R.id.bvolumereset:
			showEraseDataDialog();
			break;
		default:
			break;
		}
	}

	private void showEraseDataDialog() {

		Typeface font = DataHandler.getTypeface(activity);

		final Dialog completeDialog = new Dialog(this,
				R.style.creativeDialogTheme);
		completeDialog.setContentView(R.layout.dialog_reset_data);
		completeDialog.getWindow().setLayout(-1, -2);

		completeDialog.setCanceledOnTouchOutside(false);
		completeDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);

		TextView textViewAlert = (TextView) completeDialog
				.findViewById(R.id.text);
		Button buttonYes = (Button) completeDialog
				.findViewById(R.id.buttonConfirm);
		Button buttonNo = (Button) completeDialog
				.findViewById(R.id.buttonCnacel);

		textViewAlert.setTypeface(font);
		buttonYes.setTypeface(font, Typeface.BOLD);
		buttonNo.setTypeface(font, Typeface.BOLD);

		buttonNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				completeDialog.dismiss();

			}
		});

		buttonYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PrefClass.clearData();
				PreferenceManager.getDefaultSharedPreferences(activity).edit()
						.clear().commit();
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0,
						AudioManager.FLAG_PLAY_SOUND);
				currentVolume = 0;
				textViewvalue.setText(String.valueOf(currentVolume));
				Toast.makeText(activity, "Reset data", Toast.LENGTH_LONG)
						.show();
				completeDialog.dismiss();

			}
		});

		completeDialog.show();

	}
}
