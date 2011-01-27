package eu.chuvash.android.lusites.overlays;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class LUSiteOverlayItem extends OverlayItem {
	//private static final String TAG = "LUSiteOverlayItem";
	private Drawable defaultMarker;
	private Drawable highlightedMarker;
	private boolean highlighted = false;

	public LUSiteOverlayItem(GeoPoint point, String title, String snippet) {
		super(point, title, snippet);
	}

	public LUSiteOverlayItem(GeoPoint point, String title, String snippet,
			Drawable defaultMarker, Drawable highlightedMarker) {
		super(point, title, snippet);
		this.defaultMarker = defaultMarker;
		this.highlightedMarker = highlightedMarker;
	}
	@Override
	public Drawable getMarker(int stateBitset) {
		Drawable result=(highlighted ? highlightedMarker : defaultMarker);	
		setState(result, stateBitset);
		return(result);
	}
	public void toggleHighlight() {
		if (defaultMarker != null && highlightedMarker != null) {
			Drawable marker = (highlighted ? highlightedMarker : defaultMarker);
			this.setMarker(marker);
			highlighted = !highlighted;
		}
	}

}
