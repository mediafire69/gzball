/**
 * 
 */
package com.android.app.imageManager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author Manu
 * 
 */
public class DescriptionAdder extends Activity {

	/**
	 * Button which permits to save your description.
	 */
	private Button validate;

	/**
	 * Editable TextBox which contains the description.
	 */
	private EditText editText;

	/**
	 * Filename of the image whose description is being edited.
	 */
	private String filename;

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

		validate = (Button) findViewById(R.id.button);
		editText = (EditText) findViewById(R.id.description);

		// Set the text showed when the EditText is empty.
		editText.setHint("Enter your description here...");

		// Have the system blur any windows behind this one.
		// TODO
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

		filename = null;

		// The activity has been paused or interrupted.
		if (savedInstanceState != null)
			filename = savedInstanceState
					.getString(NotesDbAdapter.KEY_FILENAME);

		// The image filename is provided by the main activity (ImageManager).
		if (filename == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null)
				filename = extras.getString(NotesDbAdapter.KEY_FILENAME);
		}

		// Fill description area.
		populateFields();

		// Register a callback to be invoked when the button is clicked.
		validate.setOnClickListener(new OnClickListener() {

			/**
			 * Called when the button has been clicked.
			 */
			@Override
			public void onClick(View v) {
				Intent result = new Intent();
				// result.putExtra("description",
				// editText.getText().toString());
				setResult(RESULT_OK, result);
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
		outState.putString(NotesDbAdapter.KEY_FILENAME, filename);
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
	 * Called after onPause(), when the activity comes to the foreground
	 */
	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

	/**
     * 
     */
	private void saveState() {
		String description = editText.getText().toString();

		mDbHelper.createOrUpdateNote(filename, description);
	}

	/**
     * 
     */
	private void populateFields() {
		if (filename != null) {
			Cursor note = mDbHelper.fetchNote(filename);
			if (note != null) {
				startManagingCursor(note);
				String desc = note.getString(note
						.getColumnIndexOrThrow(NotesDbAdapter.KEY_DESC));
				editText.setText(desc);
			}
		}
	}
}
