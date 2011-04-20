package eu.chuvash.android.lusites;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import eu.chuvash.android.lusites.overlays.OverlayMediator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.MenuInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
//TODO move LUSITES_NAMES to OverlayMediator
import static eu.chuvash.android.lusites.model.LUSitesList.LUSITES_NAMES;

public class LUSitesActivity extends MapActivity {
	private static final String TAG = "LUSitesActivity";
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
		addLusitesOverlays();
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
		//return true;		
	
		SubMenu langMenu = menu.addSubMenu(0, 200, 2, "Language settings").setIcon(android.R.drawable.ic_menu_rotate);
        langMenu.add(1, 201, 0, "Svenska");
        langMenu.add(1, 202, 0, "English");

        return super.onCreateOptionsMenu(menu);
	
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings_menu:
			startSettings();
			break;
		case R.id.events_menu:
			startEventsActivity();
			break;
		}
			
		return false;
	}
	  
	private void startEventsActivity() {
		Intent intent = new Intent(this, EventsActivity.class);
		startActivity(intent);		
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
			oMediator.bringLusitesOverlaysOnMap();
			lusitesMap.invalidate();
		}
	};

	@Override
	protected boolean isRouteDisplayed() {
		// required by MapActivity
		return false;
	}

	private void initLUSitesMap() {
		lusitesMap = (MapView) findViewById(R.id.lusites_map);
		lusitesMap.getOverlays().clear();

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

	private void addLusitesOverlays() {
		oMediator = OverlayMediator.getInstance(this);
		oMediator.bringLusitesOverlaysOnMap();
		lusitesMap.invalidate();
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
	 * .com/maps?f=q&source=s_q&hl=stv&geocode=&q=55.705829655611645
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
			String warning = getString(R.string.warning_empty_find_field);
			Toast.makeText(LUSitesActivity.this, warning, Toast.LENGTH_LONG)
					.show();
			return;
		}

		boolean found = oMediator.searchItem(word); // test

		if (!found) {
			String msg = getString(R.string.message_lusite_not_found);
			Toast.makeText(LUSitesActivity.this, msg + ": " + word, Toast.LENGTH_LONG)
					.show();
		}
		if (found) {
			lusitesMap.invalidate();
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
	public void flyTo(GeoPoint gp) {
		controller.animateTo(gp);
		//controller.zoomToSpan(gp.getLatitudeE6(), gp.getLongitudeE6());
		//controller.setZoom(17);
	}
	
	
	
   /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

        case 201:

            Locale locale = new Locale("sv"); 
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            Toast.makeText(this, "Svensk inställning", Toast.LENGTH_LONG).show();
            break;

        case 202:

            Locale locale2 = new Locale("en"); 
            Locale.setDefault(locale2);
            Configuration config2 = new Configuration();
            config2.locale = locale2;
            getBaseContext().getResources().updateConfiguration(config2, getBaseContext().getResources().getDisplayMetrics());

            Toast.makeText(this, "English setting", Toast.LENGTH_LONG).show();
            break;  

        }
        return super.onOptionsItemSelected(item);
    }*/
}