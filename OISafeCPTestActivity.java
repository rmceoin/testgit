package org.openintents.safecptest;

// just a comment

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OISafeCPTestActivity extends Activity {

	public static final String TAG = "OISafeCPTestActivity";

	
	public static final String AUTHORITY = "org.openintents.safe.passwd";

    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_PASSWORD = "password";
    
	private static final String[] PROJECTION =
			new String[] {
				COLUMN_NAME_ID,
				COLUMN_NAME_DESCRIPTION,
				COLUMN_NAME_PASSWORD
		};

	private Uri mUri;
	private Cursor mCursor;

    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		Button checkButton = (Button) findViewById(R.id.checkButton);

		checkButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				CheckForContentProvider();
			}
		});
		
        CheckForContentProvider();
    }
    
    private void CheckForContentProvider() {
        TextView result = (TextView) findViewById(R.id.result);
        
        Uri.Builder builder = new Uri.Builder ();
        builder.scheme ("content");
        builder.authority (AUTHORITY);
        builder.path ("/passwords");
        mUri = builder.build ();
        
        try {
	        mCursor = getContentResolver().query(
	        	    mUri,   // The content URI of the words table
	        	    PROJECTION,	// The columns to return for each row
	        	    null,                    // Selection criteria
	        	    null,                     // Selection criteria
	        	    null);                        // The sort order for the returned rows
	        if (mCursor==null) {
	        	Log.d(TAG,"mCursor=null");
	        	Log.d(TAG,"random logging");
	        	result.setText("Could not find OI Safe provider");
	        }else {
	        	result.setText("got a result");
	        	if (mCursor.moveToFirst()) {
	        	    do {
		        	    Log.d(TAG,"columns="+mCursor.getColumnCount());
		        	    String password = mCursor.getString(0);
		        	    String description = mCursor.getString(1);
			        	Log.d(TAG,"description="+description+" password="+password);
		        	    // use value
	        	    } while (mCursor.moveToNext());
	        	}
	        }
        } catch (Exception e) {
        	Log.d(TAG,"Exception: "+e.getLocalizedMessage());
        	result.setText(e.getLocalizedMessage());
        	
        }
    }
}
