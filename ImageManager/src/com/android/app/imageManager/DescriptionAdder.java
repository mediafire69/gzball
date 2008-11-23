package com.android.app.imageManager;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * That is the activity used to add or modify a description for an image.
 * 
 * @author mgarnier
 */
public class DescriptionAdder extends Activity {

	/**
	 * Button which permits to save your description.
	 */
	private Button mValidate;

	/**
	 * Editable TextBox which contains the description.
	 */
	private EditText mEditText;

	/**
	 * Filename of the image whose description is being edited.
	 */
	private String mFilename;

	/**
	 * Interface between the database and your application. The database is used
	 * for saving descriptions of the images.
	 */
	private NotesDbAdapter mDbHelper;

	/**
	 * Called when the activity is starting.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.description_edit);

		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();

		mValidate = (Button) findViewById(R.id.button);
		mEditText = (EditText) findViewById(R.id.description);

		// Set the text showed when the EditText is empty.
		mEditText.setHint("Enter your description here...");

		// Set max possible lines to 1
		mEditText.setMaxLines(1);

		mFilename = null;

		// The activity has been paused or interrupted.
		if (savedInstanceState != null)
			mFilename = savedInstanceState
					.getString(NotesDbAdapter.KEY_FILENAME);

		// The image filename is provided by the main activity (ImageManager).
		if (mFilename == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null)
				mFilename = extras.getString(NotesDbAdapter.KEY_FILENAME);
		}

		// Fill description area.
		populateFields();

		// Register a callback to be invoked when the button is clicked.
		mValidate.setOnClickListener(new OnClickListener() {

			/**
			 * Called when the button has been clicked.
			 */
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});
	}

	/**
	 * Called to save the state from an activity before being killed so that the
	 * state can be restored in onCreate(Bundle).
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(NotesDbAdapter.KEY_FILENAME, mFilename);
	}

	/**
	 * Called when an activity is going into the background, but has not (yet)
	 * been killed.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	/**
	 * Called after onPause(), when the activity comes to the foreground.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

	/**
	 * Save the current description to the database.
	 */
	private void saveState() {
		String _description = mEditText.getText().toString();
		mDbHelper.createOrUpdateNote(mFilename, _description);
	}

	/**
	 * Fill the description area if present in the database.
	 */
	private void populateFields() {
		if (mFilename != null) {
			Cursor _description = mDbHelper.fetchDescription(mFilename);
			if (_description != null) {
				startManagingCursor(_description);
				String _desc = _description.getString(_description
						.getColumnIndexOrThrow(NotesDbAdapter.KEY_DESC));
				mEditText.setText(_desc);
			}
		}
	}
}
