package com.insa.sffs.android.gzball;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class GZBall1 extends Activity {

	private GZBallView mSnakeView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mSnakeView = (GZBallView) findViewById(R.id.view);
		// mSnakeView.setTextView((TextView) findViewById(R.id.text));

		/*if (savedInstanceState == null) {
			// We were just launched -- set up a new game
			mSnakeView.setMode(SnakeView.READY);
		} else {
			// We are being restored
			Bundle map = savedInstanceState.getBundle(ICICLE_KEY);
			if (map != null) {
				mSnakeView.restoreState(map);
			} else {
				mSnakeView.setMode(SnakeView.PAUSE);
			}
		}*/
	}
}