package com.android.app.imageManager;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

/**
 * That is the activity used to add or modify a description for an image.
 * 
 */
public class DescriptionAdder extends Activity {

	/*
	 * Cette activité est quasi nulle. Il ne reste plus que les accès à la BDD
	 * qui stocke les descriptions (pas interessant à coder :)
	 */

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

		// TODO

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
