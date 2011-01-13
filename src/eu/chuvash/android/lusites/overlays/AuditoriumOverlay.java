package eu.chuvash.android.lusites.overlays;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.drawable.Drawable;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import eu.chuvash.android.lusites.model.Auditorium;
import eu.chuvash.android.lusites.model.LUSite;
import eu.chuvash.android.lusites.model.LUSitesList;
import eu.chuvash.android.lusites.util.Helper;

public class AuditoriumOverlay extends LUSiteOverlay {
	//private static final String TAG = "AuditoriumOverlay";
	
	public AuditoriumOverlay(Drawable marker, Context context) {
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
	// inspired by: 
	//http://codemagician.wordpress.com/2010/05/06/android-google-mapview-tutorial-done-right/
	@Override
	protected boolean onTap(int index) {
		OverlayItem item = overlayItems.get(index);
		Helper.showDialog(item, context);
		return true;
	}
	@Override
	public void initLUSites() {
		overlayItems = new ArrayList<OverlayItem>();
		LUSitesList luSites = LUSitesList.getLUSitesList(context);
		
		if (luSites.size() > 0) {
			for (LUSite ls : luSites) {
				if (ls instanceof Auditorium) {
					GeoPoint gp = getPoint(ls.getLongitude(), ls.getLatitude());
					OverlayItem item = new OverlayItem(gp, ls.getName(), ls.getSnippet());
					overlayItems.add(item);
				}
			}
			populate();
		}
	}

}
