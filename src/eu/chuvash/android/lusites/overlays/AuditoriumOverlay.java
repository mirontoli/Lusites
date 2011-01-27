package eu.chuvash.android.lusites.overlays;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import eu.chuvash.android.lusites.model.Auditorium;
import eu.chuvash.android.lusites.model.LUSite;
import eu.chuvash.android.lusites.model.LUSitesList;
import eu.chuvash.android.lusites.util.Helper;

public class AuditoriumOverlay extends LUSiteOverlay {
	// private static final String TAG = "AuditoriumOverlay";

	public AuditoriumOverlay(Context context) {
		super();
		this.context = context;
		initLUSites();
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);

	}

	// inspired by:
	// http://codemagician.wordpress.com/2010/05/06/android-google-mapview-tutorial-done-right/
	@Override
	protected boolean onTap(int index) {
		OverlayItem item = overlayItems.get(index);
		Helper.showDialog(item, context);
		return true;
	}

	@Override
	public void initLUSites() {
		LUSitesList luSites = LUSitesList.getLUSitesList(context);
		if (luSites.size() > 0) {
			Drawable defaultMarker = getMarker(eu.chuvash.android.lusites.R.drawable.auditorium_pin);
			Drawable highlightedMarker = getMarker(eu.chuvash.android.lusites.R.drawable.auditorium_red_pin);
			for (LUSite ls : luSites) {
				if (ls instanceof Auditorium) {
					GeoPoint gp = getPoint(ls.getLongitude(), ls.getLatitude());
					LUSiteOverlayItem item = new LUSiteOverlayItem(gp,
							ls.getName(), ls.getSnippet(), defaultMarker,
							highlightedMarker);
					overlayItems.add(item);
				}
			}
			populate();
		}
	}

}
