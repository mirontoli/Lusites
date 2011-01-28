package eu.chuvash.android.lusites;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import eu.chuvash.android.lusites.overlays.AuditoriumOverlay;
import eu.chuvash.android.lusites.overlays.BikePumpOverlay;
import eu.chuvash.android.lusites.overlays.LUSiteOverlayItem;
import eu.chuvash.android.lusites.overlays.OverlayMediator;
import eu.chuvash.android.lusites.util.Helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import static eu.chuvash.android.lusites.model.LUSitesList.LUSITES_NAMES;

public class LUSitesActivity extends MapActivity {
	//private static final String TAG = "LUSitesActivity";
	private static final int FIND_FIELD_THRESHOLD = 1;// after how many characters
												// dropdown is shown
	private MapView lusitesMap;
	private MapController controller;
	private AutoCompleteTextView findField;
	private Button findButton;
	private OverlayMediator oMediator;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initLUSitesMap();
		initMyLocation();
		initLusitesOverlays();
		initPrefChangeListener();
		initFindField();
		initFindButton();
	}
	private void initFindField() {
		findField = (AutoCompleteTextView) findViewById(R.id.find_field);
		if (LUSITES_NAMES != null) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, LUSITES_NAMES);
			findField.setThreshold(FIND_FIELD_THRESHOLD);
			findField.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					LUSitesActivity.this.showFindResult();
				}
			});
			findField.setAdapter(adapter);
		}
	}

	private void initFindButton() {
		findButton = (Button) findViewById(R.id.find_button);
		findButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				LUSitesActivity.this.showFindResult();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings_menu:
			startSettings();
		}
		return false;
	}

	private void initPrefChangeListener() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		pref.registerOnSharedPreferenceChangeListener(prefListener);
	}

	private SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
		@Override
		public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
			boolean showSatellite = sp.getBoolean("satellite", false);
			lusitesMap.setSatellite(showSatellite);
		}
	};

	@Override
	protected boolean isRouteDisplayed() {
		// required by MapActivity
		return false;
	}

	private void initLUSitesMap() {
		lusitesMap = (MapView) findViewById(R.id.lusites_map);

		controller = lusitesMap.getController();
		// lusitesMap.setEnabled(true);
		lusitesMap.setBuiltInZoomControls(true);
		//lusitesMap.setStreetView(true);

		// //test
		// lusitesMap.setLongClickable(true);
		// lusitesMap.setOnLongClickListener(new View.OnLongClickListener() {
		//
		// @Override
		// public boolean onLongClick(View v) {
		// Toast.makeText(LUSites.this, "Long clicked ",
		// Toast.LENGTH_LONG).show();
		// return true;
		// }
		// });
	}

	/*
	 * This method gets you to your location in the mapview inspired by Ed
	 * Burnette Hello, Android 1, p 158
	 */
	private void initMyLocation() {
		final GeoPoint luMainBuildingGP = this.getCenterLocation();
		controller.animateTo(luMainBuildingGP);
		controller.setZoom(16);

		final MyLocationOverlay overlay = new MyLocationOverlay(this,
				lusitesMap);

		overlay.enableMyLocation();
		overlay.enableCompass();
		overlay.runOnFirstFix(new Runnable() {
			public void run() {
				GeoPoint myLoc = overlay.getMyLocation();
				float[] results = new float[3];
				Location.distanceBetween(
						luMainBuildingGP.getLatitudeE6() / 1E6,
						luMainBuildingGP.getLongitudeE6() / 1E6,
						myLoc.getLatitudeE6() / 1E6,
						myLoc.getLongitudeE6() / 1E6, results);
				if (results[0] < 3000) {
					controller.animateTo(myLoc);
				} else {
					// doesn't work from the Runnable
					// LUSites.this.showOutsideWarning();
				}
			}
		});
		// without adding to overlays it doesn't work
		lusitesMap.getOverlays().add(overlay);
	}

	private void initLusitesOverlays() {
		oMediator = OverlayMediator.getInstance(this);
		AuditoriumOverlay auditoriumOverlay = new AuditoriumOverlay(this);
		lusitesMap.getOverlays().add(auditoriumOverlay);
		
		BikePumpOverlay bpOverlay = new BikePumpOverlay(this);
		lusitesMap.getOverlays().add(bpOverlay);
	}

	private void startSettings() {
		Intent intent = new Intent(this, Settings.class);
		startActivity(intent);
	}

	// private void showOutsideWarning() {
	// Toast.makeText(this,
	// "Oops, you are outside of Lund.\nWe'll center on LU main building.",
	// Toast.LENGTH_LONG).show();
	// }
	/*
	 * The center in Lund University is the main building See here:
	 * http://maps.google
	 * .com/maps?f=q&source=s_q&hl=sv&geocode=&q=55.705829655611645
	 * ,+13.193483054637909
	 * &sll=37.0625,-95.677068&sspLog.d(TAG, "addOverlay");n=33.29802,79.013672&ie=UTF8&z=15
	 */
	private GeoPoint getCenterLocation() {
		int latE6 = (int) (55.705829655611645 * 1E6);
		int longE6 = (int) (13.193483054637909 * 1E6);
		return new GeoPoint(latE6, longE6);
	}

	private void showFindResult() {
		hideKeyboard();
		String word = findField.getText().toString().trim().toLowerCase();
		if (word.equals("")) {
			Toast.makeText(LUSitesActivity.this,
					"Write an auditorium first" + word, Toast.LENGTH_LONG)
					.show();
			return;
		}
		//oController.handleSearch(word);
		//OverlayItem currentOI = oController.getCurrentOI();
		LUSiteOverlayItem currentOI = oMediator.searchItem(word); //test
		
		
		if (currentOI != null) {
			Helper.showDialog(currentOI, this); //test
			controller.animateTo(currentOI.getPoint());
			// controller.zoomToSpan(oi.getPoint().getLatitudeE6(),
			// oi.getPoint().getLongitudeE6());
			// controller.setZoom(17);
		} else {
			Toast.makeText(LUSitesActivity.this, "Sorry, not found: " + word,
					Toast.LENGTH_LONG).show();
		}
	}
	// inspired by:
	// http://www.androidguys.com/2009/11/12/how-to-showhide-soft-keyboard-programmatically-dev-tips-tools/
	private void hideKeyboard() {
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(findField.getWindowToken(), 0);
	}
	public List<Overlay> getMapOverlays() {
		return lusitesMap.getOverlays();
	}
//	public void addOverlay(Overlay o) {
//		Log.d(TAG, "addOverlay");
//		lusitesMap.getOverlays().add(o);
//		lusitesMap.invalidate();
//	}
}