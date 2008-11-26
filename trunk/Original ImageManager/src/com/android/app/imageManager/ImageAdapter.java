package com.android.app.imageManager;

import java.io.File;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * Image manager permit to convert a usual picture (JPG, PNG,...) in a bitmap
 * which is the only format drawable by Android from.
 */
public class ImageAdapter extends BaseAdapter {

	// The background theme color
	private int mGalleryItemBackground;

	// Filenames table
	private String[] mImgsTokens;

	// Bitmap table associate to the filenames table
	private Bitmap[] mPhotos;

	// Android context
	private Context mContext;

	// the path where the ImageAdapter search files
	private String mPath;

	/**
	 * Constructor
	 */
	public ImageAdapter(Context c, String path, String extension) {
		mContext = c;
		mPath = path;
		File dir = new File(mPath);

		// Tables creation
		File[] files = dir.listFiles(new ExtensionFilter(extension));
		if (files != null) {
			mImgsTokens = new String[files.length];
			mPhotos = new Bitmap[mImgsTokens.length];

			// Get the Default Background of the Gallery (the image is
			// surrounded by borders)
			TypedArray a = mContext
					.obtainStyledAttributes(android.R.styleable.Theme);
			mGalleryItemBackground = a.getResourceId(
					android.R.styleable.Theme_galleryItemBackground, 0);

			// Bitmaps Decoding
			for (int i = 0; i < files.length; i++) {
				try {
					mImgsTokens[i] = files[i].getName();
					mPhotos[i] = BitmapFactory.decodeFile(mPath + mImgsTokens[i]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			a.recycle();
		}
	}

	/**
	 * get Counter of image
	 */
	public int getCount() {
		return mPhotos != null ? mPhotos.length : 0;
	}

	/**
	 * Implemented function from BaseAdapter Getter of item
	 */
	public Object getItem(int position) {
		return mPhotos[position];
	}

	/**
	 * Implemented function from BaseAdapter Getter of itemId
	 */
	public long getItemId(int position) {
		return -1;
	}

	/**
	 * Get a View that displays the data at the specified position in the data
	 * set of our adapter. Here, we create a view manually but it's possible to
	 * get it from an XML layout file too.
	 * 
	 * @param position
	 *            The position in the data set we want to modify.
	 * @param convertView
	 *            The old view to reuse, if needed.
	 * @param parent
	 *            The parent linked to this adapter.
	 * 
	 * @return Returns the bitmap corresponding to the filename at the specified
	 *         position.
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView i = new ImageView(mContext);
	
		i.setImageBitmap(mPhotos[position]);
		i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		// set the size of the image showed in the parent.
		i.setLayoutParams(new Gallery.LayoutParams(136, 88));
	
		// The preferred Gallery item background
		i.setBackgroundResource(mGalleryItemBackground);
		return i;
	}

	/**
	 * getter of Filename form position
	 */
	public String getFileNameAtPosition(int position) {
		return mImgsTokens[position];
	}

}
