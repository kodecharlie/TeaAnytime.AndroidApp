package com.quisoma.teaanytime;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TeaArrayAdapter extends ArrayAdapter<Tea> implements Constants
{
	private static final String tag = "TeaArrayAdapter";

	private Context context;
	private List<Tea> teas = new ArrayList<Tea>();

	private ImageView teaLeaves;
	private TextView teaName;
	private TextView teaGroup;
	private TextView teaRating;

	public TeaArrayAdapter(
			Context context,
			int textViewResourceId,
			List<Tea> objects
			)
	{
		super(context, textViewResourceId, objects);
		this.context = context;
		this.teas.addAll(objects);
	}

	public int getCount() {
		return this.teas.size();
	}

	public Tea getItem(int index) {
		return this.teas.get(index);
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{		
		View row = convertView;
		if (row == null) {
			// ROW INFLATION
			Log.d(tag, "Starting XML Row Inflation ... ");
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.tea_listitem, parent, false);
			Log.d(tag, "Successfully completed XML Row Inflation!");
		}

		Tea tea = getItem(position);
		teaLeaves = (ImageView)row.findViewById(R.id.tea_leaves);
		teaName = (TextView) row.findViewById(R.id.tea_name);
		teaGroup = (TextView) row.findViewById(R.id.tea_group);
		teaRating = (TextView) row.findViewById(R.id.tea_rating);

		teaName.setText(tea.getName());
		teaGroup.setText(tea.getGroup());
		if (tea.getRating() > 0)
			teaRating.setText(Integer.toString(tea.getRating()));
		else
			teaRating.setText("");

		String imgFilePath = ASSETS_DIR + tea.getImageFileName();
		try {
			InputStream inputStream = this.context.getResources().getAssets().open(imgFilePath);
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			teaLeaves.setImageBitmap(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return row;
	}
}
