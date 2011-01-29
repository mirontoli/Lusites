package eu.chuvash.android.lusites.overlays;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;

public abstract class LUSiteOverlay extends ItemizedOverlay<LUSiteOverlayItem> {
	//private static final String TAG = "LUSiteOverlay";
	protected static int MILLION = 1000000;
	protected Context context;
	protected ArrayList<LUSiteOverlayItem> overlayItems = new ArrayList<LUSiteOverlayItem>();
	public LUSiteOverlay() {
		super(null);
	}
	@Override
	protected LUSiteOverlayItem createItem(int i) {
		LUSiteOverlayItem oi = overlayItems.get(i);
		return oi;
	}
	@Override
	public int size() {
		return overlayItems.size();
	}
	// inspired by:
	// http://codemagician.wordpress.com/2010/05/06/android-google-mapview-tutorial-done-right/
	@Override
	protected boolean onTap(int index) {
		markOI(index);
		return true;
	}
	public void markOI(int index) {
		LUSiteOverlayItem loi = createItem(index);
		OverlayMediator.getInstance(context).setCurrentOI(loi);
	}
	protected GeoPoint getPoint(double longitude, double latitude) {
		int longE6 = (int) (longitude * MILLION);
		int latE6 = (int) (latitude * MILLION);
		return new GeoPoint(latE6, longE6);
	}
	protected Drawable getMarker(int resource) {
		Drawable marker=context.getResources().getDrawable(resource);	
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
											marker.getIntrinsicHeight());
		boundCenter(marker);
		return(marker);
	}
	public abstract void initLUSites();
	public LUSiteOverlayItem findOverlayItem(String searchName) {
		searchName = searchName.trim().toLowerCase();
		int counter = 0;
		while (counter < size()) {
			LUSiteOverlayItem loi = createItem(counter);
			String title = loi.getTitle().toLowerCase();
			if (searchName.equals(title)) {
				return loi;
			}
			counter++;
		}
		return null;
	}
}
