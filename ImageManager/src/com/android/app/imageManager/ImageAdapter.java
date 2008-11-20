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
 * that is the only format drawable by Android from
 */
public class ImageAdapter extends BaseAdapter {

	// The background theme color
	private int mGalleryItemBackground;

	// Filenames table
	private String[] imgsTokens;

	// Bitmap table associate to the filenames table
	private Bitmap[] photos;

	// Android context
	private Context mContext;

	// the path where the ImageAdapter search files
	private String path;

	/**
	 * Constructor
	 */
	public ImageAdapter(Context c, String path, String extension) {
		mContext = c;
		this.path = path;
		File dir = new File(path);

		// Tables creation
		File[] files = dir.listFiles(new ExtensionFilter(extension));
		if (files != null) {
			imgsTokens = new String[files.length];
			photos = new Bitmap[imgsTokens.length];

			// Background color getting
			TypedArray a = mContext
					.obtainStyledAttributes(android.R.styleable.Theme);
			mGalleryItemBackground = a.getResourceId(
					android.R.styleable.Theme_galleryItemBackground, 0);

			// Bitmaps Decoding
			for (int i = 0; i < files.length; i++) {
				try {
					imgsTokens[i] = files[i].getName();
					photos[i] = BitmapFactory.decodeFile(path + imgsTokens[i]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			a.recycle();
		}
	}

	/**
	 * getter of Filename form position
	 */
	public String getFileNameAtPosition(int position) {
		return imgsTokens[position];
	}

	/**
	 * get Counter of image
	 */
	public int getCount() {
		if (photos != null)
			return photos.length;
		else
			return 0;
	}

	/**
	 * Implemented fonction from BaseAdapter Getter of item
	 */
	public Object getItem(int position) {
		return photos[position];
	}

	/**
	 * Implemented fonction from BaseAdapter Getter of itemId
	 */
	public long getItemId(int position) {
		return position;
	}

	// TODO a comprendre !!
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView i = new ImageView(mContext);

		i.setImageBitmap(photos[position]);
		i.setScaleType(ImageView.ScaleType.FIT_XY);
		i.setLayoutParams(new Gallery.LayoutParams(136, 88));

		// The preferred Gallery item background
		i.setBackgroundResource(mGalleryItemBackground);
		return i;
	}

}
