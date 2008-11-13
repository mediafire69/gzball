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

	// 
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;

	// Variables for menu access.
	private static final int ADD_COMMENT_ID = Menu.FIRST;
	private static final int DEL_COMMENT_ID = Menu.FIRST + 1;

	/**
     * 
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

		// Reference the Gallery view
		gallery = (Gallery) findViewById(R.id.gallery);

		// Reference the Image view
		imageView = (ImageView) findViewById(R.id.image);

		// Reference the Text view
		textView = (TextView) findViewById(R.id.title);

		// Set the adapter to our custom adapter (below)
		gallery.setAdapter(new ImageAdapter(this, "/sdcard/apppli", ".jpg"));

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

		// Create the interface between the database (used for saving
		// descriptions of the images) and your application.
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
	 * Update the textView at the bottom of the application. Called manually,
	 * when the image is changed and when the description for the current image
	 * is changed.
	 */
	private void updateDescription() {
		String title = "Filename : " + filename() + "\nDescription : "
				+ description();
		textView.setText(title);
	}

	/**
	 * 
	 * @return the filename of the selected image in the gallery.
	 */
	private String filename() {
		return ((ImageAdapter) gallery.getAdapter())
				.getFileNameAtPosition(gallery.getSelectedItemPosition());
	}

	/**
	 * 
	 * @return
	 */
	private String description() {
		String description;
		Cursor row = mDbHelper.fetchNote(filename());
		if (row != null) {
			startManagingCursor(row);
			description = row.getString(row
					.getColumnIndexOrThrow(NotesDbAdapter.KEY_DESC));
		} else {
			description = "";
		}
		return description;
	}

	/**
	 * 
	 */
	private void createNote() {
		Intent i = new Intent(this, DescriptionAdder.class);

		i.putExtra(NotesDbAdapter.KEY_FILENAME, filename());
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	/**
	 * 
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		updateDescription();
	}

}