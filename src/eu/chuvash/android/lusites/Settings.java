package eu.chuvash.android.lusites;

import eu.chuvash.android.lusites.overlays.OverlayMediator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Settings extends PreferenceActivity {
	//private static final String TAG = "Settings";
	private static final String OPT_OVERLAYS = "overlays_to_show";
	private static final String OPT_OVERLAYS_DEFAULT = "ALL";
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		initOverlaysPref();
	}

	private void initOverlaysPref() {
		ListPreferenceMultiSelect overlaysPref = (ListPreferenceMultiSelect) findPreference("overlays_to_show");
		OverlayMediator oMediator = OverlayMediator.getInstance(null);
		String[] entries = oMediator.getOverlaysEntries();
		String[] entryValues = oMediator.getOverlaysEntryValues();
		overlaysPref.setEntries(entries);
		overlaysPref.setEntryValues(entryValues);
	}
	public static String[] getOverlaysEntryValuesToShow(Context context) {
		String overlayString = getPref(context).getString(OPT_OVERLAYS, OPT_OVERLAYS_DEFAULT);
		if (overlayString.equals("ALL")) {
			createOverlaysDefaultPreference(context);
		}
		overlayString = getPref(context).getString(OPT_OVERLAYS, OPT_OVERLAYS_DEFAULT);
		String[] overlays = ListPreferenceMultiSelect.parseStoredValue(overlayString); 
			//overlayString.split(ListPreferenceMultiSelect.SEPARATOR);
		return overlays;
	}

	private static void createOverlaysDefaultPreference(Context context) {
		OverlayMediator oMediator = OverlayMediator.getInstance(null);
		String[] entryValues = oMediator.getOverlaysEntryValues();
		String valueToStore = ListPreferenceMultiSelect.prepareValueToStore(entryValues);
		//boolean wentItWell = 
			getPrefEditor(context).putString(OPT_OVERLAYS, valueToStore).commit();
		
	}
	private static SharedPreferences getPref(Context context) {
		SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
		return p;
	}
	 private static SharedPreferences.Editor getPrefEditor(Context context) {
		 SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
		 SharedPreferences.Editor editor = p.edit();
		 return editor;
	 }
}