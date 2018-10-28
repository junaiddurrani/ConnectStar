package com.game.android.connectstar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class GetHints extends Activity implements OnClickListener {
	private Button bpay, bshare, bvideo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hints_choice);

		bpay =  findViewById(R.id.bpayhints);
		bshare =  findViewById(R.id.bshareHints);
		bvideo = findViewById(R.id.bvideoHints);

		bshare.setOnClickListener(this);
		bvideo.setOnClickListener(this);
		bpay.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bshareHints:
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.developer.durrani.multilator")));
			//shareIt();
			break;
		case R.id.bpayhints:
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.durrani.farah.amazingcalculator")));
			//TapjoyConnect.getTapjoyConnectInstance().showOffersWithCurrencyID(tap_hintId, true);
			break;
		case R.id.bvideoHints:
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.studio.rfj.unitconverter")));
			//vunglePub.playAd(overrideConfig);
			break;
		}
	}
}

