package eu.chuvash.android.lusites;
/*
 * TODO it has to be implemented
 * This is a customized subclass of MapView
 * in order to handle longclick
 * Inspired by Juri Strumpflohner
 * http://blog.js-development.com/2009/12/mapview-doesnt-fire-onlongclick-event.html
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.maps.MapView;

public class LuSitesMap extends MapView {
	private long startTime;
	private long endTime;
	public LuSitesMap(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	   @Override
	   public boolean onTouchEvent(MotionEvent ev) {
	      
		if(ev.getAction() == MotionEvent.ACTION_DOWN){
	         //record the start time
	         //startTime = 
			ev.getEventTime();
	      }else if(ev.getAction() == MotionEvent.ACTION_UP){
	         //record the end time
	         endTime = ev.getEventTime();
	      }

	      //verify
	      if(endTime - startTime > 1000){
	         //we have a 1000ms duration touch
	         //propagate your own event
	         return true; //notify that you handled this event (do not propagate)
	      }
		return false;
	   }

}
