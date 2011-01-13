package eu.chuvash.android.lusites.overlays;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import eu.chuvash.android.lusites.model.BikePump;
import eu.chuvash.android.lusites.model.LUSite;
import eu.chuvash.android.lusites.model.LUSitesList;
import eu.chuvash.android.lusites.util.Helper;

public class BikePumpOverlay extends LUSiteOverlay {

	public BikePumpOverlay(Drawable marker, Context context) {
		super(boundCenterBottom(marker));
		this.context = context;
		initLUSites();
	}

	@Override
	protected OverlayItem createItem(int i) {
		OverlayItem oi = overlayItems.get(i);
		return oi;
	}

	@Override
	public int size() {
		return overlayItems.size();
	}
	@Override
	protected boolean onTap(int index) {
		OverlayItem item = overlayItems.get(index);
		Helper.showDialog(item,context);
		return true;
	}
	@Override
	public void initLUSites() {
		overlayItems = new ArrayList<OverlayItem>();
		LUSitesList luSites = LUSitesList.getLUSitesList(context);
		if (luSites.size() > 0) {
			for (LUSite ls : luSites) {
				if (ls instanceof BikePump){
					GeoPoint gp = getPoint(ls.getLongitude(), ls.getLatitude());
					OverlayItem item = new OverlayItem(gp, ls.getName(), ls.getSnippet());
					overlayItems.add(item);
				}
			}
			populate();
		}	
	}

}
