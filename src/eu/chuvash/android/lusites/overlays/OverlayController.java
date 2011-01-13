package eu.chuvash.android.lusites.overlays;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import eu.chuvash.android.lusites.LUSitesActivity;
import eu.chuvash.android.lusites.util.Helper;


public class OverlayController {
	//private static final String TAG = "OverlayController";
	private static OverlayController oController;
	private LUSitesActivity activity;
	private List<Overlay> olList;
	private OverlayItem currentOI;
	public LUSiteOverlay currentOverlay;
	public Drawable auditoriumMarker;
	private Drawable auditoriumRedMarker;
	private Drawable bluePinMarker;
	public Drawable pumpMarker;
	public Drawable redArrowMarker;

	private OverlayController() {
	}
	private OverlayController(Context context) {
		activity = (LUSitesActivity) context;
		initMarkers();
	}

	public static OverlayController getOverlayController(Context context) {
		//oController = null; // only one object is needed; but in order to
							// renew...
		if (oController == null) {
			oController = new OverlayController(context);			
		}
		return oController;
	}
	private void initMarkers() {
		auditoriumRedMarker = activity.getResources().getDrawable(
				eu.chuvash.android.lusites.R.drawable.auditorium_red_pin);
		// important:
		// for marker to work call setBounds
		// http://developmentality.wordpress.com/2009/10/16/android-overlayitemsetmarkerdrawable-icon/
		auditoriumRedMarker.setBounds(0, 0, auditoriumRedMarker.getIntrinsicWidth(),
				auditoriumRedMarker.getIntrinsicHeight());
		
		redArrowMarker = activity.getResources().getDrawable(
				eu.chuvash.android.lusites.R.drawable.red_arrow);
		redArrowMarker.setBounds(0,0, redArrowMarker.getIntrinsicWidth(), redArrowMarker.getIntrinsicHeight());
		
		if (auditoriumMarker == null) {
			auditoriumMarker = activity.getResources().getDrawable(
					eu.chuvash.android.lusites.R.drawable.auditorium_pin);
			auditoriumMarker.setBounds(0, 0,
					auditoriumMarker.getIntrinsicWidth(),
					auditoriumMarker.getIntrinsicHeight());
		}
		
		if (bluePinMarker == null) {
			bluePinMarker = activity.getResources().getDrawable(eu.chuvash.android.lusites.R.drawable.blue_pin);
			bluePinMarker.setBounds(0, 0, bluePinMarker.getIntrinsicWidth(),
					bluePinMarker.getIntrinsicHeight());
		}
		
		if (pumpMarker == null) {
			pumpMarker = activity.getResources().getDrawable(
					eu.chuvash.android.lusites.R.drawable.pump_pin);
			pumpMarker.setBounds(0, 0,
					pumpMarker.getIntrinsicWidth(),
					pumpMarker.getIntrinsicHeight());
		}

	}

	public OverlayItem searchItem(String word) {
		cleanCurrentOI();
		OverlayItem oi = null;
		boolean found = false;
		int counter = 0;
		updateOlList();
		while (!found && counter < olList.size()) {
			Overlay o = olList.get(counter);
			if (o instanceof LUSiteOverlay) {
				LUSiteOverlay lo = (LUSiteOverlay) o;
				oi = lo.findOverlayItem(word);
				if (oi != null) {
					found = true;					
					currentOI = oi;
					currentOverlay = lo;
				}
			}
			counter++;
		}
		return oi;
	}

	public void handleMarking() {
		currentOI.setMarker(redArrowMarker);
		Helper.showDialog(currentOI, activity);
	}

	private void cleanCurrentOI() {
		if (currentOI != null) {
			if (currentOverlay instanceof AuditoriumOverlay) {
				currentOI.setMarker(auditoriumMarker);
			}
			else if (currentOverlay instanceof BikePumpOverlay) {
				currentOI.setMarker(pumpMarker);
			}
			currentOI = null;
			//currentOverlay = null;
		}
	}
	private void updateOlList() {
		olList = activity.getMapOverlays();
	}

	public void initOverlays() {
		
		updateOlList();
//		if (olList.size() == 3) {
//			return;
//		}
		AuditoriumOverlay ao = new AuditoriumOverlay(auditoriumMarker, activity);
		activity.addOverlay(ao);
		
		BikePumpOverlay bo = new BikePumpOverlay(pumpMarker, activity);
		activity.addOverlay(bo);	
		
		updateOlList();
	}
	public void handleSearch(String word) {
		searchItem(word);
		if (currentOI != null){
			handleMarking();
		}
	}
	public OverlayItem getCurrentOI() {
		return currentOI;
	}
	public void handleMarking2(LUSitesActivity luSitesActivity) {
		currentOI.setMarker(redArrowMarker);
		Helper.showDialog(currentOI, luSitesActivity);
		
	}
}
