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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.TimerTask;


public class BasicPackActivity extends Activity implements OnClickListener {

	private ImageView bpack5, bpack6, bpack7, bpack8, bpack9, bpack10;
	private Intent intent;
	private TextView tvselectPack;
	private Context context = this;
	private String packName = "5x5";
	private RelativeLayout relativeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.packselectscreen);

		//tvselectPack = (TextView) findViewById(R.id.tvSelectPack);
		//tvselectPack.setTypeface(DataHandler.getTypeface(context));

		bpack5 = (ImageView) findViewById(R.id.bpack5);
		bpack6 = (ImageView) findViewById(R.id.bpack6);
		bpack7 = (ImageView) findViewById(R.id.bpack7);
		bpack8 = (ImageView) findViewById(R.id.bpack8);
		bpack9 = (ImageView) findViewById(R.id.bpack9);
		bpack10 = (ImageView) findViewById(R.id.bpack10);

	/*	bpack5.setTypeface(DataHandler.getTypeface(context));
		bpack6.setTypeface(DataHandler.getTypeface(context));
		bpack7.setTypeface(DataHandler.getTypeface(context));
		bpack8.setTypeface(DataHandler.getTypeface(context));
		bpack9.setTypeface(DataHandler.getTypeface(context));
		bpack10.setTypeface(DataHandler.getTypeface(context));*/

		bpack5.setOnClickListener(this);
		bpack6.setOnClickListener(this);
		bpack7.setOnClickListener(this);
		bpack8.setOnClickListener(this);
		bpack9.setOnClickListener(this);
		bpack10.setOnClickListener(this);

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
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bpack5:
			packName = "5x5";
			break;
		case R.id.bpack6:
			packName = "6x6";
			break;
		case R.id.bpack7:
			packName = "7x7";
			break;
		case R.id.bpack8:
			packName = "8x8";
			break;
		case R.id.bpack9:
			packName = "9x9";
			break;
		case R.id.bpack10:
			packName = "10x10";
			break;
		}
		intent = new Intent(this, LevelsActivity.class);
		intent.putExtra("packname", packName);
		intent.putExtra("packDisplayName", packName);
		startActivity(intent);
	}

}
