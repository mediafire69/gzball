/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.android.app.imageManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This is a modified version of the original file created by Google used in a
 * tutorial. Its purpose is to communicate with a database stored in Android.
 * The SGBD used is SQLite.
 */
public class NotesDbAdapter {

	// our columns
	public static final String KEY_FILENAME = "filename";
	public static final String KEY_DESC = "description";

	private static final String TAG = "NotesDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	/**
	 * Database creation sql statement
	 */
	private static final String DATABASE_CREATE = "create table descriptions ("
			+ "filename text not null primary key, description text);";

	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_TABLE = "descriptions";
	private static final int DATABASE_VERSION = 1;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS descriptions");
			onCreate(db);
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public NotesDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Open the descriptions database. If it cannot be opened, try to create a
	 * new instance of the database. If it cannot be created, throw an exception
	 * to signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public NotesDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Close the connection.
	 */
	public void close() {
		mDbHelper.close();
	}

	/**
	 * Create or update a description using the filename and description
	 * provided.
	 * 
	 * @param filename
	 *            the filename of the image
	 * @param description
	 *            the description
	 */
	public void createOrUpdateNote(String filename, String description) {
		Cursor cursor = fetchDescription(filename);

		ContentValues values = new ContentValues();

		if (cursor == null) {
			// no description available for the current filename
			// we add a new line
			values.put(KEY_FILENAME, filename);
			values.put(KEY_DESC, description);
			mDb.insert(DATABASE_TABLE, null, values);
		} else {
			// we have to update the description previously saved
			values.put(KEY_DESC, description);
			mDb.update(DATABASE_TABLE, values, KEY_FILENAME + "='" + filename
					+ "'", null);
		}
	}

	/**
	 * Delete the description with the given filename
	 * 
	 * @param filename
	 *            filename of description to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteNote(String filename) {
		return mDb.delete(DATABASE_TABLE, KEY_FILENAME + "='" + filename + "'",
				null) > 0;
	}

	/**
	 * Return a Cursor positioned at the description that matches the given
	 * filename.
	 * 
	 * @return Cursor positioned to matching description, if found or null if
	 *         not.
	 */
	public Cursor fetchDescription(String filename) throws SQLException {

		Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {
				KEY_FILENAME, KEY_DESC }, KEY_FILENAME + "='" + filename + "'",
				null, null, null, null, null);
		if (mCursor != null && mCursor.getCount() > 0) {
			mCursor.moveToFirst();
		} else {
			mCursor = null;
		}
		return mCursor;
	}
}
