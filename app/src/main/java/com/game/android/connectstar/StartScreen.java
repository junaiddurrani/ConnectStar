package com.game.android.connectstar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.game.android.connectstar.utils.DataHandler;
import com.game.android.connectstar.utils.ParticleAcessor;
import com.game.android.connectstar.utils.PrefClass;
import com.tapjoy.TapjoyConnect;
import com.tapjoy.TapjoyConnectFlag;
import com.tapjoy.TapjoyConnectNotifier;
import com.tapjoy.TapjoyEarnedPointsNotifier;
import com.tapjoy.TapjoyNotifier;
import com.tapjoy.TapjoySpendPointsNotifier;
import com.vungle.publisher.AdConfig;
import com.vungle.publisher.EventListener;
import com.vungle.publisher.VunglePub;

import java.util.*;
import java.util.Timer;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class StartScreen extends Activity implements OnClickListener {

	private TextView bmore, btimeTrial, bHelp, bstore;
	private ImageView bselectPack;
	private Intent intent;
	private Context context = this;
	private TweenManager tm;
	private boolean isAnimationRunning = false;

	private LinearLayout ll1, ll2, ll3, ll4, ll5;
	RelativeLayout llmain;

	private VunglePub vunglePub = VunglePub.getInstance();
	private Random rand = new Random();
	private String appId = "";
	private RelativeLayout relativeLayout;

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
		setContentView(R.layout.startscreen_new);
		getScreenDimen();

        appId = getResources().getString(R.string.appId);
        vunglePub.init(this, appId);

		PrefClass prefclass = new PrefClass(context);

		AppRater.app_launched(context);

		Tween.registerAccessor(LinearLayout.class, new ParticleAcessor());
		tm = new TweenManager();

		bstore = findViewById(R.id.bstore);
		bmore = findViewById(R.id.bmore);
		bselectPack = findViewById(R.id.bstart);
		btimeTrial = findViewById(R.id.btimeTrial);
		bHelp = findViewById(R.id.bhelp);

		ll1 = (LinearLayout) findViewById(R.id.ll1);
		ll2 = (LinearLayout) findViewById(R.id.ll2);
		ll3 = (LinearLayout) findViewById(R.id.ll3);
		ll4 = (LinearLayout) findViewById(R.id.ll4);
		// ll5 = (LinearLayout) findViewById(R.id.ll5);
		llmain = findViewById(R.id.llmainheading);

		// bstart.setTypeface(DataHandler.getTypeface(context), Typeface.BOLD);
		// bmore.setTypeface(DataHandler.getTypeface(context), Typeface.BOLD);
		/*
		 * bselectPack .setTypeface(DataHandler.getTypeface(context),
		 * Typeface.BOLD);
		 */
		// btimeTrial.setTypeface(DataHandler.getTypeface(context),
		// Typeface.BOLD);
		// bHelp.setTypeface(DataHandler.getTypeface(context), Typeface.BOLD);

		// llmain.setBackgroundResource(draw[rand.nextInt(10)]);

		// bstart.setOnClickListener(this);
		bmore.setOnClickListener(this);
		bselectPack.setOnClickListener(this);
		btimeTrial.setOnClickListener(this);
		bHelp.setOnClickListener(this);
		bstore.setOnClickListener(this);

		/*
		 * ((TextView) findViewById(R.id.tvheading)).setTypeface(DataHandler
		 * .getTypeface(context));
		 */

		@SuppressWarnings("unused")
		PrefClass p1 = new PrefClass(context);
		DataHandler
				.checkAndReturnColor(GameSaver.sharedInstance(context
						.getApplicationContext()).lastPackSelected, context);

		startTween();
		threadForTween();

		if (DataHandler.tapjoy_ads) {
			tap_appId = getResources().getString(R.string.tap_appId);
			tap_appKey = getResources().getString(R.string.tap_appKey);
			tap_hintId = getResources().getString(R.string.tap_hintId);

			flags = new Hashtable<String, String>();
			flags.put(TapjoyConnectFlag.ENABLE_LOGGING, "true");

			initialiseTapjoy();
		}

		if (DataHandler.video_ads) {
			initialiseVungle();
		}

		relativeLayout = findViewById(R.id.relativelayout);

        Timer timer = new Timer();
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

	private void threadForTween() {

		new Thread(new Runnable() {
			private long lastMillis = -1;

			@Override
			public void run() {
				while (isAnimationRunning) {
					if (lastMillis > 0) {
						long currentMillis = System.currentTimeMillis();
						final float delta = (currentMillis - lastMillis) / 1000f;

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								tm.update(delta);
							}
						});
						lastMillis = currentMillis;
					} else {
						lastMillis = System.currentTimeMillis();
					}
					try {
						Thread.sleep(1000 / 60);
					} catch (InterruptedException ex) {
					}
				}
			}
		}).start();
	}

    private void startTween() {
		Timeline.createSequence()
				.beginSequence()
				.push(Tween.set(ll2, ParticleAcessor.POSITION_X).target(
						-1 * DataHandler.getDevice_width()))
				.push(Tween.set(ll1, ParticleAcessor.POSITION_X).target(
						-1 * DataHandler.getDevice_width()))
				.push(Tween.set(ll3, ParticleAcessor.POSITION_X).target(
						-1 * DataHandler.getDevice_width()))
				.push(Tween.set(ll4, ParticleAcessor.POSITION_X).target(
						-1 * DataHandler.getDevice_width()))
				.push(Tween.set(ll5, ParticleAcessor.POSITION_X).target(
						-1 * DataHandler.getDevice_width()))
				.pushPause(0.5f)
				.push(Tween.to(ll2, ParticleAcessor.POSITION_X, 0.5f).target(0))
				.push(Tween.to(ll1, ParticleAcessor.POSITION_X, 0.5f).target(0))
				.push(Tween.to(ll3, ParticleAcessor.POSITION_X, 0.5f).target(0))
				.push(Tween.to(ll4, ParticleAcessor.POSITION_X, 0.5f).target(0))
				.push(Tween.to(ll5, ParticleAcessor.POSITION_X, 0.5f).target(0))
				.end().start(tm);
	}

	private void getScreenDimen() {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		DataHandler.setDevice_width(dm.widthPixels);
		DataHandler.setDevice_height(dm.heightPixels);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * case R.id.bstart: intent = new Intent(context, GameActivity.class);
		 * // intent.putExtra("pack", //
		 * GameSaver.sharedInstance(context.getApplicationContext
		 * ()).lastPackSelected); // intent.putExtra("pid", //
		 * GameSaver.sharedInstance
		 * (context.getApplicationContext()).lastLevelSlected);
		 * DataHandler.setCurrentPack(GameSaver.sharedInstance(context
		 * .getApplicationContext()).lastPackSelected);
		 * DataHandler.setCurrentLevel(GameSaver.sharedInstance(context
		 * .getApplicationContext()).lastLevelSlected); startActivity(intent);
		 * break;
		 */
		case R.id.bmore:
			startActivity(new Intent(context, SoundActivity.class));
			break;
		case R.id.bstart:
			startActivity(new Intent(context, PackType.class));
			break;
		case R.id.btimeTrial:
			startActivity(new Intent(context, TimeTrial.class));
			break;
		case R.id.bstore:
			startActivity(new Intent(context, GetHints.class));
			break;
		case R.id.bhelp:
			startActivity(new Intent(context, HelpActivity.class));
			break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		vunglePub.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		vunglePub.onResume();
	}

	// tap joy
	private Hashtable<String, String> flags;
	private AdConfig overrideConfig;
	private int totalTapHints;
	private boolean earnedHints = false;
	private int sharedHints = 5;
	private boolean isShared = false;
	private int earnedTapHints = 0;
	private String tap_appId = "";
	private String tap_appKey = "";
	private String tap_hintId = "";
	private String TAG = "StartScreen";

	private void initialiseTapjoy() {

		TapjoyConnect.requestTapjoyConnect(this, tap_appId, tap_appKey, flags,
				new TapjoyConnectNotifier() {
					@Override
					public void connectSuccess() {
						onConnectSuccess();
					}

					@Override
					public void connectFail() {
						onConnectFail();
					}
				});

		TapjoyConnect.getTapjoyConnectInstance().getTapPoints(
				new TapjoyNotifier() {

					@Override
					public void getUpdatePointsFailed(String error) {
						Log.i(TAG, error);
					}

					@Override
					public void getUpdatePoints(String currencyName,
							int pointTotal) {
						Log.i(TAG, "currencyName: " + currencyName);
						Log.i(TAG, "pointTotal: " + pointTotal);
						if (earnedHints) {
							earnedTapHints = pointTotal;
							spendTapPoints();
							earnedHints = false;
						} else {
						}
					}
				});

	}

	private void initialiseVungle() {
		vunglePub = VunglePub.getInstance();
		vunglePub.setEventListeners(vungleEventListener);
		overrideConfig = new AdConfig();
		overrideConfig.setIncentivized(true);
	}

	private void onConnectSuccess() {
		TapjoyConnect.getTapjoyConnectInstance().setEarnedPointsNotifier(
				new TapjoyEarnedPointsNotifier() {
					@Override
					public void earnedTapPoints(int amount) {
						earnedHints = true;
						totalTapHints = amount;
						Log.i(TAG, "you have earned " + totalTapHints);
					}
				});
	}

	private void onConnectFail() {
		Log.e(TAG, "Tapjoy connect call failed.");

	}

	private void spendTapPoints() {
		TapjoyConnect.getTapjoyConnectInstance().spendTapPoints(earnedTapHints,
				new TapjoySpendPointsNotifier() {
					@Override
					public void getSpendPointsResponseFailed(String error) {
						Log.i(TAG, error);
					}

					@Override
					public void getSpendPointsResponse(String currencyName,
							int pointTotal) {
						Log.i(TAG, "spent tap points : " + earnedTapHints);
						addHint(earnedTapHints);
					}
				});

	}

	private void addHint(int value) {
		int hint = PrefClass.getHint();
		hint = hint + value;
		PrefClass p1 = new PrefClass(context);
		PrefClass.saveHint(hint);
	}

	private final EventListener vungleEventListener = new EventListener() {

		@Override
		public void onVideoView(boolean arg0, int arg1, int arg2) {
			// if (arg0) {
			// addHint(getResources().getInteger(R.integer.videoHints));
			// runOnUiThread(new Runnable() {
			//
			// @Override
			// public void run() {
			// Toast.makeText(context, "5 Hints added",
			// Toast.LENGTH_SHORT).show();
			// }
			// });
			// } else {
			// runOnUiThread(new Runnable() {
			//
			// @Override
			// public void run() {
			// Toast.makeText(context, "Watch Complete video to add hints",
			// Toast.LENGTH_SHORT).show();
			// }
			// });
			// }
		}

		@Override
		public void onAdUnavailable(final String arg0) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, arg0, Toast.LENGTH_LONG).show();
				}
			});

		}

		@Override
		public void onAdStart() {

		}

		@Override
		public void onAdEnd(boolean wasSuccessfulView,
				boolean wasCallToActionClicked) {
			if (wasSuccessfulView) {
				addHint(getResources().getInteger(R.integer.videoHints));
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(context, "5 Hints added",
								Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(context,
								"Watch Complete video to add hints",
								Toast.LENGTH_SHORT).show();
					}
				});

			}
			initialiseVungle();
		}

		@Override
		public void onAdPlayableChanged(boolean arg0) {
			Toast.makeText(context,
					"You cannot play any ad now. Try after sometime",
					Toast.LENGTH_LONG).show();
		}
	};

}
