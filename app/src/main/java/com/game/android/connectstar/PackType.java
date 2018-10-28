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

import java.util.TimerTask;


public class PackType extends Activity implements OnClickListener {

	private ImageView bbasic, btablet;
	private Context context = this;
	private RelativeLayout relativeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.packtype);
		super.onCreate(savedInstanceState);

		bbasic = (ImageView) findViewById(R.id.bbasic);
		btablet = (ImageView) findViewById(R.id.btablet);
		
		//((TextView)findViewById(R.id.tvpacktype)).setTypeface(DataHandler.getTypeface(context));

		bbasic.setOnClickListener(this);
		btablet.setOnClickListener(this);
		
		//bbasic.setTypeface(DataHandler.getTypeface(context));
		//badvanced.setTypeface(DataHandler.getTypeface(context));
		//btablet.setTypeface(DataHandler.getTypeface(context));
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
		case R.id.bbasic:
			startActivity(new Intent(context, BasicPackActivity.class));
			break;
		case R.id.btablet:
			startActivity(new Intent(context, TabPackActivity.class));
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		TimeTrial.destroy();
	}

}
