package eu.chuvash.android.lusites.overlays;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.google.android.maps.Overlay;

import eu.chuvash.android.lusites.LUSitesActivity;
import eu.chuvash.android.lusites.util.Helper;

public class OverlayMediator {
	private static final String TAG = "OverlayMediator";
	private static OverlayMediator singleton;
	private LUSitesActivity activity;
	private LUSiteOverlayItem currentOI;

	private OverlayMediator() {
	}

	private OverlayMediator(Context context) {
		activity = (LUSitesActivity) context;
	}

	public static OverlayMediator getInstance(Context context) {
		if (singleton == null) {
			singleton = new OverlayMediator();
		}
		singleton.setActivity((LUSitesActivity) context);
		return singleton;
	}

	public boolean searchItem(String word) {
		Log.d(TAG, "searchItem. activity toString: " + activity.toString());
		List<Overlay> olList = activity.getMapOverlays();
		Log.d(TAG, "searchItem. olList size: " + olList.size());
		int counter = olList.size() - 1;
		while (counter > -1) {
			Overlay o = olList.get(counter);
			if (o instanceof LUSiteOverlay) {
				LUSiteOverlay lo = (LUSiteOverlay) o;
				LUSiteOverlayItem loi = lo.findOverlayItem(word);
				if (loi != null) {
					lo.setFocus(loi);
					Log.d(TAG, "searchItem. loi to String: " + loi.toString() + "; title: " + loi.getTitle());
					//lo.simulateOnTap(loi);
					//setCurrentOI(loi);
					return true;
				}
			}
			counter--;
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
			//Helper.showDialog(currentOI, activity);
			Log.d(TAG, "setCurrentOI after loi.toggleHighlight");
			activity.flyTo(loi.getPoint());
			Log.d(TAG, "setCurrentOI after activity.flyTo");
		}
	}
	public void setActivity(LUSitesActivity lsa) {
		activity = null;
		activity = lsa;
	}
}
