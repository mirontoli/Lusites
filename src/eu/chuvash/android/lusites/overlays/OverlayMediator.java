package eu.chuvash.android.lusites.overlays;

import java.util.List;

import android.content.Context;

import com.google.android.maps.Overlay;

import eu.chuvash.android.lusites.LUSitesActivity;
import eu.chuvash.android.lusites.util.Helper;

public class OverlayMediator {
	//private static final String TAG = "OverlayMediator";
	private static OverlayMediator singleton;
	private LUSitesActivity activity;
	private LUSiteOverlayItem currentOI;

	private OverlayMediator() {
	}

	public static OverlayMediator getInstance(Context context) {
		if (singleton == null) {
			singleton = new OverlayMediator();
		}
		singleton.setActivity((LUSitesActivity) context);
		return singleton;
	}
	public void initLusitesOverlays() {
		AuditoriumOverlay auditoriumOverlay = new AuditoriumOverlay(activity);
		activity.getMapOverlays().add(auditoriumOverlay);
		
		BikePumpOverlay bpOverlay = new BikePumpOverlay(activity);
		activity.getMapOverlays().add(bpOverlay);
		
		LibraryOverlay lOverlay = new LibraryOverlay(activity);
		activity.getMapOverlays().add(lOverlay);
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
}
