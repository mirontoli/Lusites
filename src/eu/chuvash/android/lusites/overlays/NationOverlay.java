package eu.chuvash.android.lusites.overlays;

import com.google.android.maps.GeoPoint;

import eu.chuvash.android.lusites.model.LUSite;
import eu.chuvash.android.lusites.model.LUSitesList;
import eu.chuvash.android.lusites.model.Library;
import eu.chuvash.android.lusites.model.Nation;
import android.content.Context;
import android.graphics.drawable.Drawable;

public class NationOverlay extends LUSiteOverlay {

	public NationOverlay(Context context) {
		super(context);
	}
	@Override
	public void initLUSites() {
		LUSitesList luSites = LUSitesList.getLUSitesList(context);
		if (luSites.size() > 0) {
			Drawable defaultMarker = getMarker(eu.chuvash.android.lusites.R.drawable.nation_pin);
			Drawable highlightedMarker = getMarker(eu.chuvash.android.lusites.R.drawable.nation_red_pin);
			for (LUSite ls : luSites) {
				if (ls instanceof Nation) {
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

	@Override
	public String getSettingsEntry() {
		return context.getString(eu.chuvash.android.lusites.R.string.settings_overlays_item_nations);
	}

}
