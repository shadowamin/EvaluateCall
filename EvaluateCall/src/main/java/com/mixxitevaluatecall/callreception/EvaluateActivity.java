package com.mixxitevaluatecall.callreception;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import com.mixxitevaluatecall.R;
import com.mixxitevaluatecall.callreception.SimpleGestureFilter.SimpleGestureListener;

public class EvaluateActivity extends Activity implements SimpleGestureListener{
	LinearLayout	buttonSend;
	private RatingBar ratingBar;
	private EditText comment;
	private int statu = 0;
	private boolean progress = true;
	private ProgressBar progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();
	private ImageView imageSwipUp;
	private SimpleGestureFilter detector;
	private boolean swipeToClose = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate);
		buttonSend=(LinearLayout) findViewById(R.id.buttonSend);
		ratingBar = (RatingBar) findViewById(R.id.ratingEvaluate);
		comment = (EditText) findViewById(R.id.editComment);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		imageSwipUp = (ImageView) findViewById(R.id.imageSwipUp);
		
		OnTouchListener listener = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				progress = false;
				progressBar.setVisibility(View.INVISIBLE);
				return false;
			}
		};
		imageSwipUp.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_UP)
					swipeToClose = false;
				if (action == MotionEvent.ACTION_DOWN)
					swipeToClose = true;
				return false;
			}
		});
		ratingBar.setOnTouchListener(listener);
		comment.setOnTouchListener(listener);
		detector = new SimpleGestureFilter(this, this);
		
		new Thread(new Runnable() {
			public void run() {
				while (progressBarStatus < 100 && progress) {

					progressBarStatus = getStatu();
					progressBarHandler.post(new Runnable() {
						public void run() {
							progressBar.setProgress(progressBarStatus);
						}
					});
				}
				if (progressBarStatus >= 100 && progress) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// finish();
				}
			}
		}).start();
	}
	public int getStatu() {
		statu += 10;

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return statu;

	}
	@Override
	public void onSwipe(int direction) {
		if (direction == SimpleGestureFilter.SWIPE_UP && swipeToClose)
			{finish();
			overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			}
	}
	@Override
	public void onDoubleTap() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		// Call onTouchEvent of SimpleGestureFilter class
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}
}
