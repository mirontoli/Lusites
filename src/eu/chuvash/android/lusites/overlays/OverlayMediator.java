package eu.chuvash.android.lusites.overlays;

import java.util.List;

import android.content.Context;
import com.google.android.maps.Overlay;

import eu.chuvash.android.lusites.LUSitesActivity;

public class OverlayMediator {
	// private static final String TAG = "OverlayController";
	private static OverlayMediator singleton;
	private LUSitesActivity activity;
	private List<Overlay> olList;
	private LUSiteOverlayItem currentOI;
	public LUSiteOverlay currentOverlay;

	private OverlayMediator() {
	}

	private OverlayMediator(Context context) {
		activity = (LUSitesActivity) context;
	}

	public static OverlayMediator getInstance(Context context) {
		if (singleton == null) {
			singleton = new OverlayMediator(context);
		}
		return singleton;
	}

	public LUSiteOverlayItem searchItem(String word) {
		// cleanCurrentOI();
		LUSiteOverlayItem loi = null;
		boolean found = false;
		int counter = 0;
		updateOlList();
		while (!found && counter < olList.size()) {
			Overlay o = olList.get(counter);
			if (o instanceof LUSiteOverlay) {
				LUSiteOverlay lo = (LUSiteOverlay) o;
				loi = lo.findOverlayItem(word);
				if (loi != null) {
					found = true;
					setCurrentOI(loi);
					currentOverlay = lo;
				}
			}
			counter++;
		}
		return loi;
	}

	// public void handleMarking() {
	// currentOI.toggleHighlight();
	// Helper.showDialog(currentOI, activity);
	// }

	private void cleanCurrentOI() {
		if (currentOI != null) {
			currentOI.toggleHighlight();
			currentOI = null;
			// currentOverlay = null;
		}
	}

	private void updateOlList() {
		olList = activity.getMapOverlays();
	}

	// public void handleSearch(String word) {
	// searchItem(word);
	// if (currentOI != null){
	// handleMarking();
	// }
	// }
	// public OverlayItem getCurrentOI() {
	// return currentOI;
	// }
	public void setCurrentOI(LUSiteOverlayItem loi) {
		cleanCurrentOI();
		if (loi != null) {
			currentOI = loi;
			currentOI.toggleHighlight();
		}
	}
}
