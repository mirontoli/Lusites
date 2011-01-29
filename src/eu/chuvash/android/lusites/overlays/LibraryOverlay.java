package eu.chuvash.android.lusites.overlays;

import com.google.android.maps.GeoPoint;

import eu.chuvash.android.lusites.model.LUSite;
import eu.chuvash.android.lusites.model.LUSitesList;
import eu.chuvash.android.lusites.model.Library;
import android.content.Context;
import android.graphics.drawable.Drawable;

public class LibraryOverlay extends LUSiteOverlay {
	public LibraryOverlay(Context context) {
		super();
		this.context = context;
		initLUSites();
	}
	@Override
	public void initLUSites() {
		LUSitesList luSites = LUSitesList.getLUSitesList(context);
		if (luSites.size() > 0) {
			Drawable defaultMarker = getMarker(eu.chuvash.android.lusites.R.drawable.blue_pin);
			Drawable highlightedMarker = getMarker(eu.chuvash.android.lusites.R.drawable.red_arrow);
			for (LUSite ls : luSites) {
				if (ls instanceof Library) {
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
