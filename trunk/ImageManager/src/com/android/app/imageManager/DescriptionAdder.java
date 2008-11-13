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
	 * 
	 */
	private Button validate;
	
	/**
	 * 
	 */
	private EditText editText;
	
	/**
	 * 
	 */
	private String filename;

	/**
	 * 
	 */
	private NotesDbAdapter mDbHelper;
	
	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Be sure to call the super class.
        super.onCreate(savedInstanceState);
        
        // See assets/res/any/layout/translucent_background.xml for this
        // view layout definition, which is being set here as
        // the content of our screen.
        setContentView(R.layout.description_edit);
        
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        
        validate = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.description);
        
        
        // Have the system blur any windows behind this one.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        
        filename = null;
        if (savedInstanceState != null)
        	filename =  savedInstanceState.getString(NotesDbAdapter.KEY_FILENAME);

        if (filename == null) {
			Bundle extras = getIntent().getExtras();            
			if (extras != null)
				filename = extras.getString(NotesDbAdapter.KEY_FILENAME);
		}
        
        populateFields();
        
        validate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent result = new Intent();
				//result.putExtra("description", editText.getText().toString());
				setResult(RESULT_OK,result);
				finish();
			}
        });
    }
	
	/**
	 * 
	 */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(NotesDbAdapter.KEY_FILENAME, filename);
    }
    
    /**
     * 
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }
    
    /**
     * 
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
            	String desc = note.getString(
            			note.getColumnIndexOrThrow(NotesDbAdapter.KEY_DESC));
            	editText.setText(desc);
            }
        }
    }
}
