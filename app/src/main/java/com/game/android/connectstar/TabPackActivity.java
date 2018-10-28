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


public class TabPackActivity extends Activity implements OnClickListener {

	private ImageView bpack11, bpack12, bpack13, bpack14;
	private Intent intent;
	private TextView tvselectPack;
	private Context context = this;
	private String packName = "11x11";
	private RelativeLayout relativeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabpackscreen);

		/*tvselectPack = (TextView) findViewById(R.id.tvSelectPack);
		tvselectPack.setTypeface(DataHandler.getTypeface(context));*/

		bpack11 = (ImageView) findViewById(R.id.bpack11);
		bpack12 = (ImageView) findViewById(R.id.bpack12);
		bpack13 = (ImageView) findViewById(R.id.bpack13);
		bpack14 = (ImageView) findViewById(R.id.bpack14);

		/*bpack11.setTypeface(DataHandler.getTypeface(context));
		bpack12.setTypeface(DataHandler.getTypeface(context));
		bpack13.setTypeface(DataHandler.getTypeface(context));
		bpack14.setTypeface(DataHandler.getTypeface(context));*/
		
		bpack11.setOnClickListener(this);
		bpack12.setOnClickListener(this);
		bpack13.setOnClickListener(this);
		bpack14.setOnClickListener(this);

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
		case R.id.bpack11:
			packName = "11x11";
			break;
		case R.id.bpack12:
			packName = "12x12";
			break;
		case R.id.bpack13:
			packName = "13x13";
			break;
		case R.id.bpack14:
			packName = "14x14";
			break;
		}
		intent = new Intent(this, LevelsActivity.class);
		intent.putExtra("packname", packName);
		intent.putExtra("packDisplayName", packName);
		startActivity(intent);
	}

}
