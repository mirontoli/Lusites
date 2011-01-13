package eu.chuvash.android.lusites.overlays;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public abstract class LUSiteOverlay extends ItemizedOverlay<OverlayItem> {
	//private static final String TAG = "LUSiteOverlay";
	protected static int MILLION = 1000000;
	protected Context context;
	protected ArrayList<OverlayItem> overlayItems;
	public LUSiteOverlay(Drawable defaultMarker) {
		super(defaultMarker);
	}
	protected GeoPoint getPoint(double longitude, double latitude) {
		int longE6 = (int) (longitude * MILLION);
		int latE6 = (int) (latitude * MILLION);
		return new GeoPoint(latE6, longE6);
	}
	public abstract void initLUSites();
	public OverlayItem findOverlayItem(String searchName) {
		OverlayItem overlayItem = null;
		searchName = searchName.trim().toLowerCase();
		int counter = 0;
		int limit = overlayItems.size();
		while (overlayItem == null && counter < limit) {
			OverlayItem oi = overlayItems.get(counter);
			String title = oi.getTitle().toLowerCase();
			if (searchName.equals(title)) {
				overlayItem = oi;
			}
			counter++;
		}
		return overlayItem;
	}
}
