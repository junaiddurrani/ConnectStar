package com.game.android.connectstar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.game.android.connectstar.utils.DataHandler;
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

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;

@SuppressWarnings("deprecation")
public class GameActivity extends Activity implements OnTouchListener,
		OnClickListener {

	static final int DIALOG_WIN_ID = 1;
	public static final int SOUND_MOVED = 1;
	public static final int SOUND_WIN = 2;
	public static GameActivity current;
	public TextView tvtime;;
	public static int screenHeight;
	public static int screenWidth;
	private TextView tvhint, tvhintlabel, tvpack, tvlevelLabel, tvlevel,
			tvbest, tvbestLabel, tvmoves, tvmovesLabel;
	int currentStars;
	int hFactHeight;
	int lFactHeight;
	Point blockPos = new Point();
	Point touchPos = new Point();
	boolean isHintAvailable;
	String board;
	DotsView dtView = null;
	GameState gameState;
	private AudioManager mAudioManager;
	private SoundPool mSoundPool;
	private HashMap<Integer, Integer> mSoundPoolMap;
	public String packName;
	public int puzzleID;
	int stepMoved;
	boolean tipMode;
	private Context context = this;
	private Dialog completeDialog = null;
	private File picFile;
	private Typeface font;
	private ImageView breplay, bhint, bmenu, bback, bnext;
	private AbsoluteLayout absLayout;

	private int path_width;
	private int path_height;
	private String getPack;
	private int getLevel;
	private int adsVar = 0;

	// variables for new dots
	private int theme = 1;
	private int levelClreared = 0;
	private static final int REQUEST_WRITE_STORAGE = 11;
	private boolean howToDialog;

	public void onCreate(Bundle bund) {
		super.onCreate(bund);
		@SuppressWarnings("unused")
		PrefClass prefclass = new PrefClass(context);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.gamescreen_new);
		current = this;

		getPack = DataHandler.getLast_pack();
		getLevel = DataHandler.getCuurrentLevel();

		font = DataHandler.getTypeface(context);

		theme = PrefClass.getTheme();

		Log.d("msg", "Theme : " + theme);

		tvhint = (TextView) findViewById(R.id.tvhint);
		// tvhintlabel = (TextView) findViewById(R.id.tvhintlabel);

		tvhint.setTypeface(font);
		// tvhintlabel.setTypeface(font);

		bmenu = (ImageView) findViewById(R.id.bmenu);
		bmenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		bback = (ImageView) findViewById(R.id.bprevious);
		bback.setOnClickListener(this);
		bhint = (ImageView) findViewById(R.id.bhint);
		bhint.setOnClickListener(this);
		bnext = (ImageView) findViewById(R.id.bnext);
		bnext.setOnClickListener(this);
		breplay = (ImageView) findViewById(R.id.breplay);
		breplay.setOnClickListener(this);

		// tvbestLabel = (TextView) findViewById(R.id.tvbestLabel);
		// tvbestLabel.setTypeface(font);

		tvlevel = (TextView) findViewById(R.id.tvlevel);
		tvlevel.setTypeface(font);

		// tvhintlabel = (TextView) findViewById(R.id.tvhintlabel);
		// tvhintlabel.setTypeface(font);

		tvmoves = (TextView) findViewById(R.id.tvmoves);
		tvmoves.setTypeface(font);
		tvpack = (TextView) findViewById(R.id.tvpack);
		tvpack.setTypeface(font);
		// tvlevelLabel = (TextView) findViewById(R.id.tvlevelLabel);
		// tvlevelLabel.setTypeface(font);
		tvbest = (TextView) findViewById(R.id.tvbest);
		tvbest.setTypeface(font);
		tvtime = (TextView) findViewById(R.id.tvtime);
		tvtime.setTypeface(font);
		// tvmovesLabel = (TextView) findViewById(R.id.tvmovesLabel);
		// tvmovesLabel.setTypeface(font);

		screenWidth = DataHandler.getDevice_width();
		screenHeight = DataHandler.getDevice_height();
		float aspectRatio = 1.0F * (float) screenWidth / (float) screenHeight;
		if ((screenWidth != 480 || screenHeight != 800)
				&& (double) aspectRatio != 0.6D) {
			if (screenWidth == 320 && screenHeight == 480) {
				float width = (float) screenWidth / 320.0F;
				float height = (float) screenHeight / 480.0F;
				LevelBlock.Orig_x = (int) (8f * width);
				LevelBlock.Orig_y = (int) (60f * height);
				LevelBlock.CELL_WIDTH = (int) (50.0F * width);
				LevelBlock.CELL_HEIGHT = (int) (50.0F * height);
			} else if (screenWidth == 480 && screenHeight == 854) {
				float width = (float) screenWidth / 320.0F;
				LevelBlock.Orig_x = (int) (10.0F * width);
				LevelBlock.Orig_y = (int) (68.0F * width);
				LevelBlock.CELL_WIDTH = (int) (50.0F * width);
				LevelBlock.CELL_HEIGHT = (int) (50.0F * width);
			} else {
				float width = (float) screenWidth / 320.0F;
				float height = (float) screenHeight / 533.5F;
				LevelBlock.Orig_x = (int) (10.0F * width);
				LevelBlock.Orig_y = (int) (68.0F * height);
				LevelBlock.CELL_WIDTH = (int) (50.0F * width);
				LevelBlock.CELL_HEIGHT = (int) (50.0F * height);
			}
		} else {
			float width = (float) screenWidth / 320.0F;
			LevelBlock.Orig_x = (int) (8f * width);
			LevelBlock.Orig_y = (int) (60f * width);
			LevelBlock.CELL_WIDTH = (int) (50.0F * width);
			LevelBlock.CELL_HEIGHT = (int) (50.0F * width);
		}

		if (TimeTrial.isTimeTrial())
			TimeTrial.update(context, TimeTrial.getDuration());
		initSounds();
		currentStars = 0;
		absLayout = (AbsoluteLayout) findViewById(R.id.mainGameLayout);
		path_width = -4 + screenWidth;
		path_height = screenHeight - (-4 + path_width + LevelBlock.Orig_y);

		prepareBoard(getPack, getLevel);

		updateUI();

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

	}

	private void prepareBoard(String pack, int level) {
		DataHandler.setCurrentPack(pack);
		DataHandler.setCurrentLevel(level);
		DotsView flView = new DotsView(this, path_width, path_width, false,
				DataHandler.checkAndReturnColor(pack, context), theme);
		dtView = flView;
		dtView.gamelayer = this;
		LayoutParams params = new LayoutParams(path_width, path_width, 2,
				path_height);
		dtView.setLayoutParams(params);
		absLayout.addView(dtView);
		dtView.setupDotsView();

		if (pack == null) {
			pack = "5x5";
			level = 1;
		}

		loadPuzzle(pack, level);
	}

	public boolean showHowToDialog() {
		if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
				"isFirstRun", true)
				&& !howToDialog) {
			howToDialog = true;
			final Dialog dialog = new Dialog(this, R.style.creativeDialogTheme);
			dialog.setContentView(R.layout.how_to_dialog);
			dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			dialog.setCanceledOnTouchOutside(false);
			dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);

			TextView tvtitle = (TextView) dialog.findViewById(R.id.tvtitle);
			tvtitle.setTypeface(font, Typeface.BOLD);
			TextView tvCurrentStep = (TextView) dialog.findViewById(R.id.text);
			tvCurrentStep.setTypeface(font);
			dialog.findViewById(R.id.layout_root).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.cancel();
						}
					});
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					PreferenceManager.getDefaultSharedPreferences(context)
							.edit().putBoolean("isFirstRun", false).apply();
					TimeTrial.start();
				}
			});
			TimeTrial.pause();
			dialog.show();
			return true;
		}
		return false;
	}

	private void setupDialog(Dialog dialog) {
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
		TextView tvtitle = (TextView) dialog.findViewById(R.id.tvtitle);
		tvtitle.setTypeface(font, Typeface.BOLD);
		// tvtitle.setTextColor(PrefClass.getColor());
		TextView tvCurrentStep = (TextView) dialog.findViewById(R.id.text);
		tvCurrentStep.setTypeface(font);
		String movesLabel = getResources().getString(R.string.movesLabel);
		String bestLabel = getResources().getString(R.string.bestLabel);

		TextView tvBestStep = (TextView) dialog.findViewById(R.id.text2);
		tvBestStep.setTypeface(font);

		if (TimeTrial.isTimeTrial()) {
			dialog.setCancelable(false);
			tvCurrentStep.setText(getString(R.string.label_level_solved,
					TimeTrial.getLevelsSolved()));
			int bestSolved = GameSaver.sharedInstance(getApplicationContext())
					.getBestLevelsSolved(TimeTrial.getDuration());
			tvBestStep.setText(bestLabel + bestSolved);

			ImageView bdialogMenu = (ImageView) dialog
					.findViewById(R.id.bdialogMenu);
			bdialogMenu.setOnClickListener(this);
		} else {
			tvCurrentStep.setText(movesLabel + stepMoved);
			int bestMoves = GameSaver.sharedInstance(getApplicationContext())
					.getBestMove4PackedBoard(dtView.problemPart);
			tvBestStep.setText(bestLabel + bestMoves);
			setRateStar(starRatingFromSteps(stepMoved, dtView.getDotCount()),
					dialog);

			Button bdialogMenu = (Button) dialog.findViewById(R.id.bdialogMenu);
			bdialogMenu.setOnClickListener(this);

			ImageView bdialogNext = (ImageView) dialog
					.findViewById(R.id.bdialogNext);
			bdialogNext.setOnClickListener(this);
		}

		// Button bdialogReplay = (Button)
		// dialog.findViewById(R.id.bdialogReplay);
		// bdialogReplay.setOnClickListener(this);
		//
		// Button bdialogNext = (Button) dialog.findViewById(R.id.bdialogNext);
		// bdialogNext.setOnClickListener(this);
		//
		// Button bshare = (Button) dialog.findViewById(R.id.bshare);
		// bshare.setOnClickListener(this);

		ImageView bdialogReplay = (ImageView) dialog
				.findViewById(R.id.bdialogReplay);
		bdialogReplay.setOnClickListener(this);

		ImageView bshare = (ImageView) dialog.findViewById(R.id.bshare);
		bshare.setOnClickListener(this);
	}

	public static int starRatingFromSteps(int steps, int bestSteps) {
		return steps <= bestSteps ? 3
				: ((double) steps <= 1.3D * (double) ((float) bestSteps) ? 2
						: 1);
	}

	public void clearBoard() {
		stepMoved = 0;
		gameState = GameState.STATE_PLAYING;
	}

	@SuppressLint({"UseSparseArrays", "WrongConstant"})
	public void initSounds() {
		mSoundPool = new SoundPool(20, 3, 0);
		mSoundPoolMap = new HashMap<Integer, Integer>();
		mSoundPoolMap.put(Integer.valueOf(1),
				Integer.valueOf(mSoundPool.load(this, R.raw.dots1, 1)));
		mSoundPoolMap.put(Integer.valueOf(2),
				Integer.valueOf(mSoundPool.load(this, R.raw.dots2, 1)));
		mSoundPoolMap.put(Integer.valueOf(3),
				Integer.valueOf(mSoundPool.load(this, R.raw.dots3, 1)));
		mSoundPoolMap.put(Integer.valueOf(4),
				Integer.valueOf(mSoundPool.load(this, R.raw.dots4, 1)));
		mSoundPoolMap.put(Integer.valueOf(5),
				Integer.valueOf(mSoundPool.load(this, R.raw.dots5, 1)));
		mSoundPoolMap.put(Integer.valueOf(6),
				Integer.valueOf(mSoundPool.load(this, R.raw.dots6, 1)));
		mSoundPoolMap.put(Integer.valueOf(7),
				Integer.valueOf(mSoundPool.load(this, R.raw.dots7, 1)));
		mSoundPoolMap.put(Integer.valueOf(8),
				Integer.valueOf(mSoundPool.load(this, R.raw.dots8, 1)));
		mSoundPoolMap.put(Integer.valueOf(9),
				Integer.valueOf(mSoundPool.load(this, R.raw.dots9, 1)));
		mSoundPoolMap.put(Integer.valueOf(10),
				Integer.valueOf(mSoundPool.load(this, R.raw.dots10, 1)));
		mSoundPoolMap.put(Integer.valueOf(11),
				Integer.valueOf(mSoundPool.load(this, R.raw.dots11, 1)));
		mSoundPoolMap.put(Integer.valueOf(12),
				Integer.valueOf(mSoundPool.load(this, R.raw.dots12, 1)));
		mSoundPoolMap.put(Integer.valueOf(13),
				Integer.valueOf(mSoundPool.load(this, R.raw.dots13, 1)));
		mSoundPoolMap.put(Integer.valueOf(14),
				Integer.valueOf(mSoundPool.load(this, R.raw.dots14, 1)));
		mSoundPoolMap.put(Integer.valueOf(15),
				Integer.valueOf(mSoundPool.load(this, R.raw.dots15, 1)));
		mSoundPoolMap.put(Integer.valueOf(16),
				Integer.valueOf(mSoundPool.load(this, R.raw.dots16, 1)));
		mSoundPoolMap.put(Integer.valueOf(17),
				Integer.valueOf(mSoundPool.load(this, R.raw.dots17, 1)));
		mSoundPoolMap.put(Integer.valueOf(18),
				Integer.valueOf(mSoundPool.load(this, R.raw.complete, 1)));
		mAudioManager = (AudioManager) getSystemService("audio");
	}

	public void levelCleared() {
		Log.d("win", "completed");
		GameSaver gameSaver = GameSaver.sharedInstance(this
				.getApplicationContext());
		if (TimeTrial.isTimeTrial()) {
			TimeTrial.incLevelsSolved();
			int bestSolved = gameSaver.getBestLevelsSolved(TimeTrial
					.getDuration());
			if (bestSolved == 0 || TimeTrial.getLevelsSolved() > bestSolved) {
				gameSaver.setBestLevelsSolved(TimeTrial.getDuration(),
						TimeTrial.getLevelsSolved());
			}
		} else {
			int bestMoves = gameSaver
					.getBestMove4PackedBoard(dtView.problemPart);
			if (bestMoves == 0 || stepMoved < bestMoves) {
				gameSaver
						.setBestMove4PackedBoard(dtView.problemPart, stepMoved);
			}
		}

		playwinsound();
		updatePanelStatus();
		if (!TimeTrial.isTimeTrial()) {
			showCompletedDialog();
		} else {
			levelClreared = 1;
			playLevelDirection(1);
		}
	}

	public void showCompletedDialog() {
		if (completeDialog != null) {
			setupDialog(completeDialog);
		}

		adsVar = PrefClass.getAdsVar();
		int values = getResources().getInteger(R.integer.interstitial_interval);
		if (adsVar == values - 1 && PrefClass.isPremium() == false)

			PrefClass.setAdsVar(++adsVar);
		showDialog(1);
	}

	public void loadPuzzle(String pack, int level) {
		clearBoard();
		board = LevelManager.sharedInstance(getApplication().getResources())
				.newBoardForPack(pack, level, true, this);
		if (board != null) {
			DataHandler.setCurrentPack(pack);
			stepMoved = 0;
			// moveList.clear();
			dtView.parseData(board);
			dtView.invalidate();
			packName = pack;
			puzzleID = level;
			updatePanelStatus();
			updateUI();
			gameState = GameState.STATE_PLAYING;
			GameSaver gameSaver = GameSaver.sharedInstance(this
					.getApplicationContext());
			gameSaver.lastPackSelected = pack;
			gameSaver.lastLevelSlected = level;
			gameSaver.saveState();
			if (levelClreared == 1) {
				// recreate();
			}
		}
		// tvlevel.setTextColor(DataHandler.checkAndReturnColor(packName,
		// context));
		// tvlevelLabel.setTextColor(DataHandler.checkAndReturnColor(packName,
		// context));
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bhint:
			hintButtonPressed();
			break;
		case R.id.breplay:
			TimeTrial.reset(context);
			TimeTrial.start();
			dtView.parseData(board);
			dtView.invalidate();
			stepMoved = 0;
			updatePanelStatus();
			updateUI();
			break;
		case R.id.bprevious:
			levelClreared = 1;
			playLevelDirection(-1);
			break;
		case R.id.bnext:
			levelClreared = 1;
			playLevelDirection(1);
			break;
		case R.id.bdialogMenu:
			dismissDialog(1);
			finish();
			break;
		case R.id.bdialogNext:
			levelClreared = 1;
			dismissDialog(1);
			playLevelDirection(1);
			break;
		case R.id.bdialogReplay:
			TimeTrial.reset(context);
			TimeTrial.start();
			dismissDialog(1);
			restartLevel();
			break;
		case R.id.bshare:
			if (Build.VERSION.SDK_INT >= 23) {
				if (hasPermission()) {
					shareit();
				} else {
					requestPermissions(
							new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
							REQUEST_WRITE_STORAGE);
				}
			} else {
				shareit();
			}
			break;
		}
	}

	protected Dialog onCreateDialog(int dialogNum) {
		switch (dialogNum) {
		case 1:
			if (completeDialog != null) {
				setupDialog(completeDialog);
				return completeDialog;
			} else {
				completeDialog = new Dialog(this, R.style.creativeDialogTheme);
				if (TimeTrial.isTimeTrial())
					completeDialog
							.setContentView(R.layout.time_trial_complete_dialog);
				else
					completeDialog.setContentView(R.layout.completedialog_new);
				completeDialog.getWindow().setLayout(-1, -2);
				setupDialog(completeDialog);
			}
		default:
			return completeDialog;
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			shareit();
		} else {
			Toast.makeText(context,
					getResources().getString(R.string.perm_denied),
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateUI();
	}

	public boolean onTouch(View view, MotionEvent event) {
		return true;
	}

	public void playLevelDirection(int value) {
		ArrayList<String> list = new ArrayList<String>();
		list.add("5x5");
		list.add("6x6");
		list.add("7x7");
		list.add("8x8");
		list.add("9x9");
		list.add("10x10");
		list.add("a5x5");
		list.add("a6x6");
		list.add("a7x7");
		list.add("a8x8");
		list.add("a9x9");
		list.add("a10x10");
		list.add("11x11");
		list.add("12x12");
		list.add("13x13");
		list.add("14x14");

		LevelManager levelManager = LevelManager
				.sharedInstance(getApplication().getResources());

		for (int i = 0; i < list.size(); ++i) {
			String pack = (String) list.get(i);
			if (packName.compareToIgnoreCase(pack) == 0) {
				if (value == 1) {
					if (puzzleID < levelManager.getNumberOfPacks(packName)) {
						++puzzleID;
					} else {
						puzzleID = 1;
						packName = (String) list.get((i + 1) % list.size());
					}
				} else if (value == -1) {
					if (puzzleID > 1) {
						puzzleID += -1;
					} else {
						packName = (String) list.get((i - 1 + list.size())
								% list.size());
						puzzleID = levelManager.getNumberOfPacks(packName);
					}
				}
				break;
			}
		}

		list.clear();
		prepareBoard(packName, puzzleID);
	}

	public void playsound(int color) {
		if (color == getResources().getColor(R.color.dots1)) {
			playsoundbyid(1);
		} else if (color == getResources().getColor(R.color.dots2)) {
			playsoundbyid(2);
		} else if (color == getResources().getColor(R.color.dots3)) {
			playsoundbyid(3);
		} else if (color == getResources().getColor(R.color.dots4)) {
			playsoundbyid(4);
		} else if (color == getResources().getColor(R.color.dots5)) {
			playsoundbyid(5);
		} else if (color == getResources().getColor(R.color.dots6)) {
			playsoundbyid(6);
		} else if (color == getResources().getColor(R.color.dots7)) {
			playsoundbyid(7);
		} else if (color == getResources().getColor(R.color.dots8)) {
			playsoundbyid(8);
		} else if (color == getResources().getColor(R.color.dots9)) {
			playsoundbyid(9);
		} else if (color == getResources().getColor(R.color.dots10)) {
			playsoundbyid(10);
		} else if (color == getResources().getColor(R.color.dots11)) {
			playsoundbyid(11);
		} else if (color == getResources().getColor(R.color.dots12)) {
			playsoundbyid(12);
		} else if (color == getResources().getColor(R.color.dots13)) {
			playsoundbyid(13);
		} else if (color == getResources().getColor(R.color.dots14)) {
			playsoundbyid(14);
		} else if (color == getResources().getColor(R.color.dots15)) {
			playsoundbyid(15);
		} else if (color == getResources().getColor(R.color.dots16)) {
			playsoundbyid(16);
		} else if (color == getResources().getColor(R.color.dots17)) {
			playsoundbyid(17);
		}
	}

	public void playsoundbyid(int value) {
		if (PrefClass.getSound()) {
			float soundVar = (float) mAudioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			// / (float) mAudioManager
			// .getStreamMaxVolume(AudioManager.STREAM_RING);
			int soundMap = ((Integer) mSoundPoolMap.get(Integer.valueOf(value)))
					.intValue();
			mSoundPool.play(soundMap, soundVar, soundVar, 1, 0, 1.0F);
		}
	}

	public void playwinsound() {
		playsoundbyid(18);
	}

	public void restartLevel() {
		loadPuzzle(packName, puzzleID);
	}

	// function to set star rating in dialog
	public void setRateStar(int value, Dialog dialog) {
		ImageView[] imgViewArr = new ImageView[] {
				(ImageView) dialog.findViewById(R.id.ratingview1),
				(ImageView) dialog.findViewById(R.id.ratingview2),
				(ImageView) dialog.findViewById(R.id.ratingview3) };

		for (int i = 0; i < value; ++i) {
			imgViewArr[i].setImageResource(R.drawable.icon_selected_star);
		}

		for (int i = value; i < 3; ++i) {
			imgViewArr[i].setImageResource(R.drawable.icon_default_star);
		}

	}

	// function to show hint
	public void hintButtonPressed() {
		if (dtView != null) {
			if ((PrefClass.getHint() == 0)) {
				// Show Activity to get hints
				// startActivity(new Intent(context, GetHints.class));
				Toast.makeText(current, "No more hints", Toast.LENGTH_SHORT).show();
			} else {
				dtView.showHint();
				consumeHint();
				updateUI();
			}

		}

	}

	public void updatePanelStatus() {
		TextView tvmoves = (TextView) findViewById(R.id.tvmoves);
		if (TimeTrial.isTimeTrial()) {
			tvmoves.setText(getString(R.string.label_level_solved,
					TimeTrial.getLevelsSolved()));
			int bestSolved = GameSaver.sharedInstance(getApplicationContext())
					.getBestLevelsSolved(TimeTrial.getDuration());
			if (bestSolved == 0) {
				tvbest.setText("Best: ----");
			} else {
				tvbest.setText("Best: " + String.valueOf(bestSolved));
			}
		} else {
			tvmoves.setText("Moves: " + String.valueOf(stepMoved));
			int moves = GameSaver.sharedInstance(getApplicationContext())
					.getBestMove4PackedBoard(dtView.problemPart);
			if (moves == 0) {
				tvbest.setText("Best: ----");
			} else {
				tvbest.setText("Best: " + String.valueOf(moves));
			}

			int star = 0;
			if (moves > 0) {
				DotsView mDotsView = dtView;
				star = 0;
				if (mDotsView != null) {
					star = starRatingFromSteps(moves, dtView.getDotCount());
				}
			}
			if (currentStars != star) {
				currentStars = star;
			}
		}

		((TextView) findViewById(R.id.tvlevel)).setText("Level "
				+ String.valueOf(puzzleID));
		((TextView) findViewById(R.id.tvpack)).setText(packName);
	}

	private void shareit() {

		View view = findViewById(R.id.mainGameLayout);// your layout id
		view.getRootView();
		int fileIndex = PrefClass.getFileIndex();
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File picDir = new File(Environment.getExternalStorageDirectory()
					+ "/" + getResources().getString(R.string.folder_name));
			if (!picDir.exists()) {
				picDir.mkdir();
			}
			view.setDrawingCacheEnabled(true);
			view.buildDrawingCache(true);
			Bitmap bitmap = view.getDrawingCache();
			String fileName = "Screen" + fileIndex + ".jpg";
			picFile = new File(picDir + "/" + fileName);
			try {
				picFile.createNewFile();
				FileOutputStream picOut = new FileOutputStream(picFile);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
						(bitmap.getHeight()));
				boolean saved = bitmap.compress(CompressFormat.JPEG, 100,
						picOut);
				if (saved) {
					fileIndex++;
					PrefClass.setFileIndex(fileIndex);
				} else {
					Toast.makeText(context, "Error Try Again",
							Toast.LENGTH_SHORT).show();
					// Error
				}
				picOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			view.destroyDrawingCache();
		} else {
			Toast.makeText(context, "Media not mounted", Toast.LENGTH_SHORT)
					.show();
		}
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("image/jpeg");
		sharingIntent.putExtra(Intent.EXTRA_STREAM,
				Uri.parse(picFile.getAbsolutePath()));
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}

	private void consumeHint() {
		int hint = PrefClass.getHint();
		PrefClass.saveHint(--hint);
	}

	@SuppressLint("NewApi")
	private boolean hasPermission() {
		boolean hasPerm = false;
		if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
			hasPerm = true;
		}
		return hasPerm;
	}

	public void updateUI() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				tvhint.setText(String.valueOf(PrefClass.getHint()));
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();

		TimeTrial.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		TimeTrial.pause();
	}

	// tap joy
	private VunglePub vunglePub = VunglePub.getInstance();
	private Random rand = new Random();
	private String appId = "";
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
		tap_appId = getResources().getString(R.string.tap_appId);
		tap_appKey = getResources().getString(R.string.tap_appKey);
		tap_hintId = getResources().getString(R.string.tap_hintId);

		flags = new Hashtable<String, String>();
		flags.put(TapjoyConnectFlag.ENABLE_LOGGING, "true");

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
