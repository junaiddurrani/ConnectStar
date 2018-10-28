package com.game.android.connectstar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TimeTrial extends Activity implements View.OnClickListener {

	private static boolean isTimeTrial = false;
	private static long duration = TimeUnit.SECONDS.toMillis(31);
	private static int levelsSolved = 0;
	private static Timer timer;
	private Context context = this;
	private RelativeLayout relativeLayout;

	static {
		isTimeTrial = false;
		duration = TimeUnit.SECONDS.toMillis(31);
		levelsSolved = 0;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_trial);
		/*
		 * TextView tvSelectTime = (TextView) findViewById(R.id.tvSelectTime);
		 * tvSelectTime.setTypeface(DataHandler.getTypeface(context));
		 */
		ImageView time1 = (ImageView) findViewById(R.id.time1);
		ImageView time2 = (ImageView) findViewById(R.id.time2);
		ImageView time3 = (ImageView) findViewById(R.id.time3);
		ImageView time4 = (ImageView) findViewById(R.id.time4);
		ImageView time5 = (ImageView) findViewById(R.id.time5);
		ImageView time6 = (ImageView) findViewById(R.id.time6);

		/*
		 * time1.setTypeface(DataHandler.getTypeface(context));
		 * time2.setTypeface(DataHandler.getTypeface(context));
		 * time3.setTypeface(DataHandler.getTypeface(context));
		 * time4.setTypeface(DataHandler.getTypeface(context));
		 * time5.setTypeface(DataHandler.getTypeface(context));
		 * time6.setTypeface(DataHandler.getTypeface(context));
		 */

		time1.setOnClickListener(this);
		time2.setOnClickListener(this);
		time3.setOnClickListener(this);
		time4.setOnClickListener(this);
		time5.setOnClickListener(this);
		time6.setOnClickListener(this);


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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.time1:
			duration = TimeUnit.SECONDS.toMillis(31);
			break;
		case R.id.time2:
			duration = TimeUnit.SECONDS.toMillis(61);
			break;
		case R.id.time3:
			duration = TimeUnit.SECONDS.toMillis(121);
			break;
		case R.id.time4:
			duration = TimeUnit.SECONDS.toMillis(181);
			break;
		case R.id.time5:
			duration = TimeUnit.SECONDS.toMillis(241);
			break;
		case R.id.time6:
			duration = TimeUnit.SECONDS.toMillis(301);
			break;
		}
		isTimeTrial = true;
		startActivity(new Intent(context, PackType.class));
	}

	public static void update(Context context, long durationInMillis) {
		int[] duration = Timer.formatDuration(durationInMillis);
		GameActivity.current.tvtime.setText(context.getString(
				duration[1] < 10 ? R.string.label_time1 : R.string.label_time2,
				duration[0], duration[1]));
	}

	public static void init(final Context context) {
		if (isTimeTrial) {
			levelsSolved = 0;
			timer = new Timer(duration, TimeUnit.SECONDS.toMillis(1)) {
				@Override
				public void onTick(long millisUntilFinished) {
					TimeTrial.update(context, millisUntilFinished);
				}

				@Override
				public void onFinish() {
					TimeTrial.update(context, 0);
					GameActivity.current.playwinsound();
					GameActivity.current.showCompletedDialog();
				}

				@Override
				public void schedule(MoreScreen.MyTimer myTimer, int i, int i1) {

				}
			};
		}
	}

	public static void start() {
		if (isTimeTrial) {
			timer.start();
		}
	}

	public static void pause() {
		if (isTimeTrial) {
			timer.pause();
		}
	}

	public static void reset(Context context) {
		if (isTimeTrial) {
			timer.cancel();
			timer = null;
			init(context);
		}
	}

	public static void destroy() {
		if (isTimeTrial) {
			isTimeTrial = false;
			timer.cancel();
			timer = null;
		}
	}

	public static boolean isTimeTrial() {
		return isTimeTrial;
	}

	public static long getDuration() {
		return duration;
	}

	public static int getLevelsSolved() {
		return levelsSolved;
	}

	public static void incLevelsSolved() {
		TimeTrial.levelsSolved++;
	}
}
