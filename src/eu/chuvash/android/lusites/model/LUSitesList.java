package eu.chuvash.android.lusites.model;

import java.io.InputStream;
import java.util.ArrayList;

import eu.chuvash.android.lusites.util.Helper;

import android.content.Context;

public class LUSitesList extends ArrayList<LUSite> {
	public static String[] LUSITES_NAMES;
	private static LUSitesList luSitesList;
	private static final long serialVersionUID = 1L;

	// private static final String TAG = "LUSitesList";
	private LUSitesList() {
	}

	private static LUSitesList getLUSites(Context context, int resourceID) {
		LUSitesList luSites = new LUSitesList();
		InputStream is = context.getResources().openRawResource(resourceID);
		String[] lines = Helper.convertFromStreamToStringArray(is);
		if (lines != null) {
			for (String line : lines) {
				// inspired by:
				// http://stackoverflow.com/questions/1757065/java-splitting-a-comma-separated-string-but-ignoring-commas-in-quotes
				String[] fields = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				if (fields.length > 3) { // not == 4: even possible with more
											// fields in csv
					double longitude = 0;
					try {
						longitude = Double.parseDouble(fields[0].trim());
					} catch (NumberFormatException nfe) {
						// TODO
					}
					double latitude = 0;
					try {
						latitude = Double.parseDouble(fields[1].trim());
					} catch (NumberFormatException nfe) {
						// TODO
					}
					// if succeeded preceed
					if (longitude != 0 && latitude != 0) {
						String title = fields[2].trim();
						String snippet = fields[3].trim();
						LUSite ls = new LUSite(longitude, latitude, title,
								snippet);
						luSites.add(ls);
					}
				}
			}
		}
		return luSites;
	}

	public static LUSitesList getLUSitesList(Context context) {
		if (luSitesList == null) {
			initLUSitesList(context);
			initLUSITES_NAMES();
		}
		return luSitesList;
	}

	private static void initLUSITES_NAMES() {
		LUSITES_NAMES = new String[luSitesList.size()];
		for (int i = 0; i < luSitesList.size(); i++) {
			LUSITES_NAMES[i] = luSitesList.get(i).getName();
		}
	}

	private static void initLUSitesList(Context context) {
		luSitesList = new LUSitesList();
		loadAuditoriums(context);
		loadBikePumps(context);	
		loadLibraries(context);
		loadNations(context);
	}
	private static void loadAuditoriums(Context context) {
		LUSitesList luSites = getLUSites(context, eu.chuvash.android.lusites.R.raw.lusites_utf8);
		for (LUSite ls : luSites) {
			Auditorium au = new Auditorium(ls.getLongitude(),ls.getLatitude(), ls.getName(), ls.getSnippet());
			luSitesList.add(au);
		}
	}
	private static void loadBikePumps(Context context) {
		LUSitesList luSites = getLUSites(context, eu.chuvash.android.lusites.R.raw.bike_pumps);
		for (LUSite ls : luSites) {
			BikePump bp = new BikePump(ls.getLongitude(),ls.getLatitude(), ls.getName(), ls.getSnippet());
			luSitesList.add(bp);
		}		
	}
	private static void loadLibraries(Context context) {
		LUSitesList luSites = getLUSites(context, eu.chuvash.android.lusites.R.raw.libraries_utf8);
		for (LUSite ls : luSites) {
			Library l = new Library(ls.getLongitude(), ls.getLatitude(), ls.getName(), ls.getSnippet());
			luSitesList.add(l);
		}
	}
	private static void loadNations(Context context) {
		LUSitesList luSites = getLUSites(context, eu.chuvash.android.lusites.R.raw.nations_utf8);
		for (LUSite ls : luSites) {
			Nation n = new Nation(ls.getLongitude(), ls.getLatitude(), ls.getName(), ls.getSnippet());
			luSitesList.add(n);
		}
	}
}
