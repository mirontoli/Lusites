package eu.chuvash.android.lusites.overlays;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

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
		OverlayItem oi = overlayItems.get(i);
		return (LUSiteOverlayItem) oi;
	}
	@Override
	public int size() {
		return overlayItems.size();
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
		LUSiteOverlayItem overlayItem = null;
		searchName = searchName.trim().toLowerCase();
		int counter = 0;
		int limit = overlayItems.size();
		while (overlayItem == null && counter < limit) {
			LUSiteOverlayItem loi = overlayItems.get(counter);
			String title = loi.getTitle().toLowerCase();
			if (searchName.equals(title)) {
				overlayItem = loi;
			}
			counter++;
		}
		return overlayItem;
	}

}
