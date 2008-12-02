package com.android.app.imageManager;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This is a simple Image viewer for Android.
 */
public class ImageManager extends Activity {

	/*
	 * Nous vous avons laissé quelques fonctions de base afin que vous puissiez
	 * rentrer chez vous avant minuit... Tous les emplacements TODO sont à
	 * compléter. Il reste aussi à ajouter des fonctions pour la gestion du menu
	 * et du lancement de l'activité permettant l'ajout de commentaires.
	 */

	// Some request codes useful when starting a sub-activity. They are
	// important in the function onActivityResult which can control which
	// activity has done his job (when many sub-activities).
	// In our case, ACTIVITY_EDIT is not used.
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
	private Gallery mGallery;

	/**
	 * ImageView which allows to show an image.
	 */
	private ImageView mImageView;

	/**
	 * TextView permits to show some text in your application.
	 */
	private TextView mTextView;

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
		// TODO find the reference of the elements used in the UI.
		// you can use the function findViewById(int).
		// ex: TextView tv = (TextView) findViewById(R.id.text_id)
		// Note: the variables already exist and the id are defined in the xml
		// file.

		// Set the adapter to our custom adapter (ImageAdapter)
		// It enables the gallery to access the images on the sdcard.
		// TODO when mGalery is referenced, you can uncomment this code.
		// mGallery.setAdapter(new ImageAdapter(this, "/sdcard/appli/",
		// ".jpg"));

		// Set a item Selected listener, and show in the imageView the selected
		// image. This happen when the selected image of the gallery has
		// changed.
		// TODO add a listener on item selection of the gallery

		// Create the NotesDbAdapter
		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();
	}

	/**
	 * Access method for the filename of the selected image in the gallery.
	 * 
	 * @return the filename of the selected image in the gallery.
	 */
	private String filename() {
		return ((ImageAdapter) mGallery.getAdapter())
		.getFileNameAtPosition(mGallery.getSelectedItemPosition());
	}

	/**
	 * Access method for the description of the selected image in the gallery
	 * 
	 * @return the description linked to the current image.
	 */
	private String description() {
		String description;
		// Get the row whose column "filename" is the name of the current image.
		Cursor row = mDbHelper.fetchDescription(filename());
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

}