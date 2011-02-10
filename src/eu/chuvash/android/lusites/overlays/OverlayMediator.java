package eu.chuvash.android.lusites.overlays;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.google.android.maps.Overlay;

import eu.chuvash.android.lusites.LUSitesActivity;
import eu.chuvash.android.lusites.Settings;
import eu.chuvash.android.lusites.util.Helper;

public class OverlayMediator {
	//private static final String TAG = "OverlayMediator";
	private static OverlayMediator singleton;
	private LUSitesActivity activity;
	private LUSiteOverlayItem currentOI;
	private ArrayList<LUSiteOverlay> allLusitesOverlays;
	private String[] overlayEntries;
	private String[] overlayEntryValues;

	private OverlayMediator() {
	}

	public static OverlayMediator getInstance(Context context) {
		if (singleton == null) {
			singleton = new OverlayMediator();
		}
		if (context != null) {
			singleton.setActivity((LUSitesActivity) context);
		}
		return singleton;
	}
	private void initAllLusitesOverlays() {
		allLusitesOverlays = new ArrayList<LUSiteOverlay>();
		allLusitesOverlays.add(new AuditoriumOverlay(activity));
		allLusitesOverlays.add(new BikePumpOverlay(activity));
		allLusitesOverlays.add(new LibraryOverlay(activity));
	}
	public void bringLusitesOverlaysOnMap() {
		//TODO See if these two init methods invokes have to be invoked many times
		initAllLusitesOverlays();
		initOverlayEntriesAndValues();
		
		ArrayList<LUSiteOverlay> overlaysToShow = getOverlaysToShow();
		for (LUSiteOverlay lo : allLusitesOverlays) {
			int index = getOverlayMapIndex(lo);
			if (overlaysToShow.indexOf(lo) > -1) {
				if (index == -1) {
					activity.getMapOverlays().add(lo);
				}
			}
			else {			
				if (index != -1) {
					activity.getMapOverlays().remove(index);
				}
			}
		}
	}
	private ArrayList<LUSiteOverlay> getOverlaysToShow() {
		ArrayList<LUSiteOverlay> overlaysToShow = new ArrayList<LUSiteOverlay>();
		String[] overlaysStringsToShow = Settings.getOverlaysEntryValuesToShow(activity);
		if (overlaysStringsToShow != null) {
			for (LUSiteOverlay lo : allLusitesOverlays) {
				for (String value : overlaysStringsToShow) {
					if (lo.getSettingsEntryValue().equals(value)) {
						overlaysToShow.add(lo);
						break;
					}
				}
			}
		}
		return overlaysToShow;
	}
	private int getOverlayMapIndex(LUSiteOverlay lo) {
		int counter = 0;
		List<Overlay> olList = activity.getMapOverlays();
		while (counter < olList.size()) {
			Overlay o = olList.get(counter);
			if (o.getClass().toString().equals(lo.getSettingsEntryValue())) {
				return counter;
			}
			counter++;
		}
		return -1;
	}
	private void initOverlayEntriesAndValues() {
		int count = allLusitesOverlays.size();
		overlayEntries = new String[count];
		overlayEntryValues = new String[count];		
		for (int i = 0; i < count; i++) {
			LUSiteOverlay lo = allLusitesOverlays.get(i);
			overlayEntries[i] = lo.getSettingsEntry();
			overlayEntryValues[i] = lo.getSettingsEntryValue();
		}
	}
	public boolean searchItem(String word) {
		List<Overlay> olList = activity.getMapOverlays();
		int counter = 0;
		while (counter < olList.size()) {
			Overlay o = olList.get(counter);
			if (o instanceof LUSiteOverlay) {
				LUSiteOverlay lo = (LUSiteOverlay) o;
				LUSiteOverlayItem loi = lo.findOverlayItem(word);
				if (loi != null) {
					lo.setFocus(loi);
					setCurrentOI(loi);
					return true;
				}
			}
			counter++;
		}
		return false;
	}

	private void cleanCurrentOI() {
		if (currentOI != null) {
			currentOI.toggleHighlight();
			currentOI = null;
		}
	}
	public LUSiteOverlayItem getCurrentOI() {
		return currentOI;
	}
	public void setCurrentOI(LUSiteOverlayItem loi) {
		cleanCurrentOI();
		if (loi != null) {
			currentOI = loi;
			loi.toggleHighlight();
			Helper.showDialog(currentOI, activity);
			activity.flyTo(loi.getPoint());
		}
	}
	public void setActivity(LUSitesActivity lsa) {
		activity = null;
		activity = lsa;
	}
	public String[] getOverlaysEntries() {
		return overlayEntries;
	}
	public String[] getOverlaysEntryValues() {	
		return overlayEntryValues;
	}
}
