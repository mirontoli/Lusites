package eu.chuvash.android.lusites.overlays;

import java.util.List;

import android.content.Context;
import com.google.android.maps.Overlay;

import eu.chuvash.android.lusites.LUSitesActivity;

public class OverlayController {
	// private static final String TAG = "OverlayController";
	private static OverlayController oController;
	private LUSitesActivity activity;
	private List<Overlay> olList;
	private LUSiteOverlayItem currentOI;
	public LUSiteOverlay currentOverlay;

	private OverlayController() {
	}

	private OverlayController(Context context) {
		activity = (LUSitesActivity) context;
	}

	public static OverlayController getOverlayController(Context context) {
		// oController = null; // only one object is needed; but in order to
		// renew...
		if (oController == null) {
			oController = new OverlayController(context);
		}
		return oController;
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
