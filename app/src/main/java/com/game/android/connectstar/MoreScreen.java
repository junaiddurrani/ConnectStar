package com.game.android.connectstar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.android.connectstar.utils.DataHandler;
import com.game.android.connectstar.utils.PrefClass;

import java.util.*;

public class MoreScreen extends Activity implements OnClickListener {

	private Button bhelp, bsound, btags;
	private Context context = this;
	private LinearLayout llmoretitile;
	private Random rand = new Random();
	private LinearLayout relativeLayout;

	private int[] draw = new int[] { R.drawable.pressed_roundrect_five,
			R.drawable.pressed_roundrect_six,
			R.drawable.pressed_roundrect_seven,
			R.drawable.pressed_roundrect_eight,
			R.drawable.pressed_roundrect_nine,
			R.drawable.pressed_roundrect_ten,
			R.drawable.pressed_roundrect_eleven,
			R.drawable.pressed_roundrect_twelve,
			R.drawable.pressed_roundrect_thirteen,
			R.drawable.pressed_roundrect_fourteen };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.morescreen);

		PrefClass p1 = new PrefClass(context);

		bhelp = (Button) findViewById(R.id.bhelp);
		bsound = (Button) findViewById(R.id.bsound);
		btags = (Button) findViewById(R.id.btags);

		llmoretitile = (LinearLayout) findViewById(R.id.llmoretitle);

		bhelp.setTypeface(DataHandler.getTypeface(context));
		bsound.setTypeface(DataHandler.getTypeface(context));
		btags.setTypeface(DataHandler.getTypeface(context));

		bhelp.setOnClickListener(this);
		bsound.setOnClickListener(this);
		btags.setOnClickListener(this);

		setSound();
		((TextView) findViewById(R.id.tvheading)).setTypeface(DataHandler
				.getTypeface(context));

		llmoretitile.setBackgroundResource(draw[rand.nextInt(10)]);

		relativeLayout = findViewById(R.id.relativelayout);

		Timer timer = new Timer() {
			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {

			}

			@Override
			public void schedule(MyTimer myTimer, int i, int i1) {

			}
		};
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
	private void setSound() {
		if (PrefClass.getSound())
			bsound.setText(getResources().getString(R.string.sound) + " "
					+ getResources().getString(R.string.on));
		else
			bsound.setText(getResources().getString(R.string.sound) + " "
					+ getResources().getString(R.string.off));
	}

	private void toggleSound() {
		if (PrefClass.getSound())
			PrefClass.setSound(false);
		else
			PrefClass.setSound(true);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bsound:
			toggleSound();
			setSound();
			startActivity(new Intent(this, SoundActivity.class));
			break;
		case R.id.bhelp:
			startActivity(new Intent(context, HelpActivity.class));
			break;
		case R.id.btags:
			startActivity(new Intent(context, TagsScreen.class));
			break;
		}
	}

}
