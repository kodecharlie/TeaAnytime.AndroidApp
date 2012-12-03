package com.quisoma.teaanytime;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class TeaDetailsActivity extends Activity implements View.OnTouchListener, Constants
{
    static private int grayLeafIds[] = {
    	R.id.gray_leaf_1,
    	R.id.gray_leaf_2,
    	R.id.gray_leaf_3,
    	R.id.gray_leaf_4,
    	R.id.gray_leaf_5,
    };

    private final String grayLeafImgFilePath = ASSETS_DIR + "gray-tea-leaf.png";
    private final String greenLeafImgFilePath = ASSETS_DIR + "green-tea-leaf.png";

    private Tea selectedTea;
    
    private void setRatingLeaves(Tea tea)
    {
		for (int k = 0; k < grayLeafIds.length; ++k)
	    {
	    	ImageView leaf = (ImageView)findViewById(grayLeafIds[k]);
	    	String imgFilePath = (k < tea.getRating()) ? greenLeafImgFilePath : grayLeafImgFilePath;
			try {
				InputStream inputStream = getApplicationContext().getResources().getAssets().open(imgFilePath);
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				leaf.setImageBitmap(bitmap);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Light);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.tea_details);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.plain_title);

		Intent intent = getIntent();
		selectedTea = (Tea)intent.getSerializableExtra(MainActivity.SELECTED_TEA);

	    TextView titleView = (TextView)this.findViewById(R.id.centered_title);
		titleView.setText(selectedTea.getName());

		// Set up listeners.
		for (int k = 0; k < grayLeafIds.length; ++k)
	    {
	    	ImageView leaf = (ImageView)findViewById(grayLeafIds[k]);
	    	leaf.setOnTouchListener(this);
	    }

	    // Set green/gray leaves based on current rating for selected tea.
		setRatingLeaves(selectedTea);

	    // Image of tea leaves.
	    ImageView teaLeaves = (ImageView)findViewById(R.id.tea_leaves);
	    String imgFilePath = ASSETS_DIR + selectedTea.getImageFileName();
		try {
			InputStream inputStream = getApplicationContext().getResources().getAssets().open(imgFilePath);
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			teaLeaves.setImageBitmap(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Text description of tea.
		TextView textView = (TextView)findViewById(R.id.tea_description);
	    textView.setText(selectedTea.getDescription());
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		ImageView grayLeaf0 = (ImageView)findViewById(grayLeafIds[0]);
		ImageView grayLeaf4 = (ImageView)findViewById(grayLeafIds[4]);

		Rect bbox = new Rect();
		bbox.left = (int)grayLeaf0.getX();
		bbox.top = (int)grayLeaf0.getY();
		bbox.right = (int)(grayLeaf4.getX() + grayLeaf4.getWidth());
		bbox.bottom = (int)(grayLeaf4.getY() + grayLeaf4.getHeight());

		// XXX Ignore event.getRawY() because it's unclear what its frame-of-reference is. 
		if ((bbox.left - grayLeaf0.getWidth()) > event.getRawX() || bbox.right < event.getRawX())
			return true;

		// Compute new rating.
		int newRating = 0;
		for (int k = grayLeafIds.length - 1; k >= 0; --k)
	    {
	    	ImageView grayLeaf = (ImageView)findViewById(grayLeafIds[k]);
	    	if (event.getRawX() > grayLeaf.getX())
	    	{
	    		newRating = k + 1;
	    		break;
	    	}
	    }

		// Turn over leaves, as needs be.  XXX I wanted to condition this logic
		// on whether ACTION_UP occurred.  Unfortunately, event.getActionMasked()
		// always returns 0.

		if (newRating != selectedTea.getRating())
		{
			selectedTea.setRating(newRating);

			// Save tea rating.
	        SharedPreferences teaRatings = getSharedPreferences(TEA_RATINGS, 0);
	        SharedPreferences.Editor editor = teaRatings.edit();
	        editor.putInt(selectedTea.getName(), selectedTea.getRating());
	        editor.commit();

	        setRatingLeaves(selectedTea);	        
		}

		return true;
	}
}
