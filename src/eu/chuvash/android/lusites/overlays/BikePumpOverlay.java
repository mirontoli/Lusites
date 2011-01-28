package eu.chuvash.android.lusites.overlays;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;

import eu.chuvash.android.lusites.model.BikePump;
import eu.chuvash.android.lusites.model.LUSite;
import eu.chuvash.android.lusites.model.LUSitesList;

public class BikePumpOverlay extends LUSiteOverlay {

	public BikePumpOverlay(Context context) {
		super();
		this.context = context;
		initLUSites();
	}

	@Override
	public void initLUSites() {
		LUSitesList luSites = LUSitesList.getLUSitesList(context);
		if (luSites.size() > 0) {
			Drawable defaultMarker = getMarker(eu.chuvash.android.lusites.R.drawable.pump_pin);
			Drawable highlightedMarker = getMarker(eu.chuvash.android.lusites.R.drawable.red_arrow);
			for (LUSite ls : luSites) {
				if (ls instanceof BikePump) {
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
