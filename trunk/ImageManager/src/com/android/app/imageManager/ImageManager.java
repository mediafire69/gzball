package com.android.app.imageManager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * 
 * @author mgarnier
 * 
 */
public class ImageManager extends Activity {

	// Some request codes useful when starting a sub-activity. They are
	// important in the function onActivityResult which can control which
	// activity has done his job (when many sub-activities)
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;

	// Variables for menu access.
	private static final int ADD_COMMENT_ID = Menu.FIRST;
	private static final int DEL_COMMENT_ID = Menu.FIRST + 1;

	/**
	 * Interface between the database and your application. The database is used
	 * for saving descriptions of the images.
	 */
	private NotesDbAdapter mDbHelper;

	/**
	 * Gallery which can show images in a sort of slide show.
	 */
	private Gallery gallery;

	/**
	 * ImageView which allows to show an image.
	 */
	private ImageView imageView;

	/**
	 * TextView permits to show some text in your application.
	 */
	private TextView textView;

	/**
	 * This is the entry point of the application. onCreate is called when the
	 * activity is first created.
	 * 
	 * @param savedInstanceState
	 *            The previous state on the application if it has been saved and
	 *            the application interrupted (For example, when an other
	 *            application is launched, this one could have been paused).
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set the activity content from a layout resource.
		setContentView(R.layout.main);

		// Reference all the widget in the UI
		gallery = (Gallery) findViewById(R.id.gallery);
		imageView = (ImageView) findViewById(R.id.image);
		textView = (TextView) findViewById(R.id.title);

		// Set the adapter to our custom adapter (ImageAdapter)
		gallery.setAdapter(new ImageAdapter(this, "/sdcard/appli/", ".jpg"));

		// Set a item Selected listener, and show in the imageView the selected
		// image. This happen when the selected image of the gallery has
		// changed.
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			/**
			 * Callback method to be invoked when an item in this view has been
			 * selected.
			 */
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Bitmap image = (Bitmap) gallery.getSelectedItem();
				imageView.setImageBitmap(image);

				updateDescription();
			}

			/**
			 * Callback method to be invoked when the selection disappears from
			 * this view.
			 */
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				imageView.setImageBitmap(null);
				textView.setText(null);
			}
		});

		// Create the NotesDbAdapter
		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();

		// Registers a context menu to be shown for the given view (multiple
		// views can show the context menu).
		registerForContextMenu(gallery);
	}

	/**
	 * Initialize the contents of the Activity's standard options menu.
	 * 
	 * @param menu
	 *            You should place your menu items in to menu.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, ADD_COMMENT_ID, 0, R.string.menu_add_comment);
		menu.add(0, DEL_COMMENT_ID, 0, R.string.menu_delete);
		return true;
	}

	/**
	 * Called when a panel's menu item has been selected by the user.
	 * 
	 * @param featureId
	 *            The panel that the menu is in.
	 * @param item
	 *            The menu item that was selected.
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case ADD_COMMENT_ID:
			createNote();
			return true;
		case DEL_COMMENT_ID:
			mDbHelper.deleteNote(filename());
			updateDescription();
			return true;
		}
		// never reached in the case of the ImageManager Application
		return super.onMenuItemSelected(featureId, item);
	}

	/**
	 * Update the textView located at the bottom of the application. Called
	 * manually, when the image is changed and when the description for the
	 * current image is changed.
	 */
	private void updateDescription() {
		String title = "Filename : " + filename() + "\nDescription : "
				+ description();
		textView.setText(title);
	}

	/**
	 * Access method for the filename of the selected image in the gallery.
	 * 
	 * @return the filename of the selected image in the gallery.
	 */
	private String filename() {
		return ((ImageAdapter) gallery.getAdapter())
				.getFileNameAtPosition(gallery.getSelectedItemPosition());
	}

	/**
	 * Access method for the description of the selected image in the gallery
	 * 
	 * @return the description linked to the current image.
	 */
	private String description() {
		String description;
		// Get the row whose column "filename" is the name of the current image.
		Cursor row = mDbHelper.fetchNote(filename());
		if (row != null) {
			// Description is present.
			startManagingCursor(row);
			description = row.getString(row
					.getColumnIndexOrThrow(NotesDbAdapter.KEY_DESC));
		} else {
			// No description in the database for the current image.
			description = "No description...";
		}
		return description;
	}

	/**
	 * Allow to create or modify a description
	 */
	private void createNote() {
		Intent i = new Intent(this, DescriptionAdder.class);

		i.putExtra(NotesDbAdapter.KEY_FILENAME, filename());
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	/**
	 * Called when an activity you launched exits, giving you the requestCode
	 * you started it with, the resultCode it returned, and any additional data
	 * from it.
	 * 
	 * @param requestCode
	 *            The integer request code originally supplied to
	 *            startActivityForResult().
	 * @param resultCode
	 *            The integer result code returned by the child activity through
	 *            its setResult().
	 * @param data
	 *            An Intent, which can return result data to the caller (various
	 *            data can be attached to Intent "extras").
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK)
			updateDescription();
	}

}