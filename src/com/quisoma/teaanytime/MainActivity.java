package com.quisoma.teaanytime;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemClickListener, View.OnClickListener, Constants
{
	private List<Tea> teas;
	private TeaArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Light);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_activity_title);

		TextView titleView = (TextView)this.findViewById(R.id.centered_title);
		titleView.setText(R.string.Teas);

		TextView aboutBtn = (TextView)this.findViewById(R.id.about_button);
		aboutBtn.setOnClickListener(this);

		TeaParser teaParser = new TeaParser(getResources().getAssets());
		this.teas = teaParser.parse();

		this.adapter = new TeaArrayAdapter(
				getApplicationContext(), R.layout.tea_listitem, this.teas);
		ListView listView = (ListView) this.findViewById(R.id.teaLV);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (adapter != null)
		{
			// Populate tea ratings if non exists.
			SharedPreferences teaRatings = getSharedPreferences(TEA_RATINGS, MODE_PRIVATE);
			for (Tea curTea : this.teas)
			{
				int curRating = teaRatings.getInt(curTea.getName(), 0);
				curTea.setRating(curRating);
			}
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id)
	{
		((ListView)parent).setItemChecked(position, false);

		Intent intent = new Intent(this, TeaDetailsActivity.class);
	    Tea tea = teas.get(position);
	    intent.putExtra(SELECTED_TEA, tea);
	    startActivity(intent);
	}

	public void onClick(View v)
	{
		Intent intent = new Intent(this, AboutActivity.class);
	    startActivity(intent);
	}
}
