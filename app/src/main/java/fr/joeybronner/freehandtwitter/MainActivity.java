package fr.joeybronner.freehandtwitter;

import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import fr.joeybronner.freehandtwitter.api.TwitterAsyncTask;
import fr.joeybronner.freehandtwitter.util.AndroidNetworkUtility;
import fr.joeybronner.freehandtwitter.util.Constants;
import pl.droidsonroids.gif.GifImageView;

@SuppressLint("DefaultLocale") public class MainActivity extends Activity {

	public static Button btSearch, btSettings, btMore;
	public static GifImageView gifLoader;
	Spinner spinnerResultType;
	TextView tvSpeed;
	String[] spinnerValues, resultType;
	int spinnerImages[] = { R.drawable.france, R.drawable.unitedk, R.drawable.spain, R.drawable.italy, R.drawable.german };
	int resultsTypeImages[] = { R.drawable.popular, R.drawable.recent, R.drawable.mixed };
	public static Dialog dialog;
	static Context c;
	MainActivity ma;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Langage
		if (Constants.AVAILABLE_LANGAGES.contains(Locale.getDefault().getLanguage())) {
			Constants.USER_LANGAGE = Locale.getDefault().getLanguage();
		} else {
			Constants.USER_LANGAGE = "en";
		}

		ma = this;
		c = this.getApplicationContext();

		spinnerValues = getResources().getStringArray(R.array.countries);
		resultType = getResources().getStringArray(R.array.reuslt_type);

		//spinnerResultType = (Spinner) findViewById(R.id.spinnerResultType);
		//spinnerResultType.setAdapter(new MyResultTypes(this, R.layout.custom_spinner_resulttype, resultType));
		selectionOfMixedByDefault();

		final EditText etSearch = (EditText) findViewById(R.id.etSearch);

		btSettings = (Button) findViewById(R.id.btSettings);
		btSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showSettingsActivity();
			}
		});

		//btSettings = (Button) findViewById(R.id.btSettings);
		/*btSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showSettingsActivity();
			}
		});*/

		gifLoader = (GifImageView) findViewById(R.id.gifLoader);

		btSearch = (Button) findViewById(R.id.btSearch);
		btSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (searchFieldIsValid(etSearch.getText().toString())) {
					showLoader(true);
					//Intent intent = new Intent(MainActivity.this, ResultActivity.class);
					//intent.putExtra("search",etSearch.getText().toString().replaceAll("\\s+",""));
					Constants.TWITTER_RESULT_TYPE = "&result_type=fr";
					Constants.TWITTER_USER_SEARCH = etSearch.getText().toString().replaceAll("\\s+","");
					/*dialog = new Dialog(btSearch.getContext()); // Create the new dialog
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // No title
					dialog.setCancelable(false); // No cancelable
					dialog.setContentView(R.layout.activity_loading); // Content of the dialog
					dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
					dialog.show();*/
					//startActivity(intent);
					AndroidNetworkUtility androidNetworkUtility = new AndroidNetworkUtility();
					if (androidNetworkUtility.isConnected(c)) {
						try {
							new TwitterAsyncTask().execute(etSearch.getText().toString().replaceAll("\\s+",""),this)/*.get(20000, TimeUnit.MILLISECONDS)*/;
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_timeout), Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}
					} else {
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_noconnection), Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.emptysearch), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private boolean searchFieldIsValid(String search) {
		if (!search.isEmpty() && !search.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	private void showSettingsActivity() {
		// Create the new dialog
		final Dialog dialog = new Dialog(btSettings.getContext());
		// No title
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		dialog.setCancelable(true);

		// Content of the dialog
		dialog.setContentView(R.layout.activity_settings);
		dialog.show();

		// Load data
		tvSpeed = (TextView) dialog.findViewById(R.id.tvScrollSpeed);
		updateSpeedTextView(Constants.SCROLL_SPEED);

		Spinner mySpinner = (Spinner) dialog.findViewById(R.id.spinnerCountry); 
		mySpinner.setAdapter(new MyAdapter(this, R.layout.custom_spinner, spinnerValues));

		if (Constants.USER_LANGAGE.equals("fr")) {
			mySpinner.setSelection(0);
		} else if (Constants.USER_LANGAGE.equals("en")) {
			mySpinner.setSelection(1);
		} else if (Constants.USER_LANGAGE.equals("es")) {
			mySpinner.setSelection(2);
		} else if (Constants.USER_LANGAGE.equals("it")) {
			mySpinner.setSelection(3);
		} else if (Constants.USER_LANGAGE.equals("de")) {
			mySpinner.setSelection(4);
		}

		mySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				switch (pos) {
				case 0:  Constants.USER_LANGAGE = "fr";
				break;
				case 1:  Constants.USER_LANGAGE = "en";
				break;
				case 2:  Constants.USER_LANGAGE = "sp";
				break;
				case 3:  Constants.USER_LANGAGE = "it";
				break;
				case 4:  Constants.USER_LANGAGE = "de";
				break;
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});

		final SeekBar sk=(SeekBar) dialog.findViewById(R.id.sbScrollSpeed);
		sk.setProgress(Constants.SCROLL_SPEED);
		sk.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override       
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {     
				updateSpeedTextView(progress);
				// Update scroll speed
				Constants.SCROLL_SPEED = progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {	}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {	}       
		});
	}

	public static void showLoader(boolean show) {
		if (show) {
			btSearch.setVisibility(View.INVISIBLE);
			gifLoader.setVisibility(View.VISIBLE);
		} else {
			btSearch.setVisibility(View.VISIBLE);
			gifLoader.setVisibility(View.INVISIBLE);
			Intent i = new Intent(c, TweetFlipperActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			c.startActivity(i);
		}
	}

	private void updateSpeedTextView(int progress) {
		if (progress < 20) {
			tvSpeed.setText(getResources().getString(R.string.speed_veryslow));
		} else if (progress >= 20 && progress < 40) {
			tvSpeed.setText(getResources().getString(R.string.speed_slow));
		} else if (progress >= 40 && progress < 60) {
			tvSpeed.setText(getResources().getString(R.string.speed_normal));
		} else if (progress >= 60 && progress < 80) {
			tvSpeed.setText(getResources().getString(R.string.speed_fast));
		} else if (progress >= 80) {
			tvSpeed.setText(getResources().getString(R.string.speed_veryfast));
		}
	}

	private void selectionOfMixedByDefault() {
		try {
			spinnerResultType.setSelection(2);
		} catch (Exception e) { }
	}

	public class MyAdapter extends ArrayAdapter<String> { 

		public MyAdapter(Context ctx, int txtViewResourceId, String[] objects) { 
			super(ctx, txtViewResourceId, objects); 
		} 

		@Override public View getDropDownView(int position, View cnvtView, ViewGroup prnt) { 
			return getCustomView(position, cnvtView, prnt); 
		} 

		@Override public View getView(int pos, View cnvtView, ViewGroup prnt) { 
			return getCustomView(pos, cnvtView, prnt); 
		} 

		public View getCustomView(int position, View convertView, ViewGroup parent) { 
			LayoutInflater inflater = getLayoutInflater(); 
			View mySpinner = inflater.inflate(R.layout.custom_spinner, parent, false); 
			TextView main_text = (TextView) mySpinner .findViewById(R.id.text_main_seen); 
			main_text.setText(spinnerValues[position]); 
			ImageView left_icon = (ImageView) mySpinner .findViewById(R.id.left_pic); 
			left_icon.setImageResource(spinnerImages[position]); return mySpinner; 
		} 
	}

	/*private void showMoreActivity() {
		// Create the new dialog
		final Dialog dialog = new Dialog(btMore.getContext());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(true);

		// Content of the dialog
		dialog.setContentView(R.layout.activity_more);
		dialog.show();
	}*/

	/*
	public class MyResultTypes extends ArrayAdapter<String> { 

		public MyResultTypes(Context ctx, int txtViewResourceId, String[] objects) { 
			super(ctx, txtViewResourceId, objects); 
		} 

		@Override public View getDropDownView(int position, View cnvtView, ViewGroup prnt) { 
			return getCustomView(position, cnvtView, prnt); 
		} 

		@Override public View getView(int pos, View cnvtView, ViewGroup prnt) { 
			return getCustomView(pos, cnvtView, prnt); 
		}

		public View getCustomView(int position, View convertView, ViewGroup parent) { 
			LayoutInflater inflater = getLayoutInflater(); 
			View mySpinner = inflater.inflate(R.layout.custom_spinner_resulttype, parent, false); 
			TextView main_text = (TextView) mySpinner .findViewById(R.id.text_main_seen); 
			main_text.setText(resultType[position]); 
			ImageView left_icon = (ImageView) mySpinner .findViewById(R.id.left_pic);
			left_icon.setImageResource(resultsTypeImages[position]);
			return mySpinner;
		} 
	}*/
}
