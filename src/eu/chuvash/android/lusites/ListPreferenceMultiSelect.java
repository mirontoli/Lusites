package eu.chuvash.android.lusites;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;
/**
 * 
 * @author A Chuvash Guy
 * Inspired by:
 * http://droidweb.com/2009/08/developer-tip-9-enabling-multichoice-checkbox-menus-in-your-android-progra/
 * http://blog.350nice.com/wp/wp-content/uploads/2009/07/listpreferencemultiselect.java
 *
 */
public class ListPreferenceMultiSelect extends ListPreference {
	private static final String SEPARATOR = "OV=I=XseparatorX=I=VO";
	private boolean[] mClickedDialogEntryIndices;

	public ListPreferenceMultiSelect(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (getEntries() != null) {
			mClickedDialogEntryIndices = new boolean[getEntries().length];
		}
	}
    public ListPreferenceMultiSelect(Context context) {
        this(context, null);
    }
    @Override
    public void setEntries(CharSequence[] entries) {
    	super.setEntries(entries);
        mClickedDialogEntryIndices = new boolean[entries.length];
    }
    @Override
    protected void onPrepareDialogBuilder(Builder builder) {
    	CharSequence[] entries = getEntries();
    	CharSequence[] entryValues = getEntryValues();
    	
        if (entries == null || entryValues == null || entries.length != entryValues.length ) {
            throw new IllegalStateException(
                    "ListPreference requires an entries array and an entryValues array which are both the same length");
        }

        restoreCheckedEntries();
        builder.setMultiChoiceItems(entries, mClickedDialogEntryIndices, 
                new DialogInterface.OnMultiChoiceClickListener() {
					public void onClick(DialogInterface dialog, int which, boolean val) {
                    	mClickedDialogEntryIndices[which] = val;
					}
        });
    }
    public static String[] parseStoredValue(CharSequence val) {
		if ( "".equals(val) )
			return null;
		else
			return ((String)val).split(SEPARATOR);
    }
    // the opposite one of parseStoredValue method
    public static String prepareValueToStore(String[] values) {
    	StringBuilder sb = new StringBuilder("");
    	for (String v : values) {
    		sb.append(v + SEPARATOR);
    	}
    	return sb.toString();
    }
    private void restoreCheckedEntries() {
    	CharSequence[] entryValues = getEntryValues();
    	
    	String[] vals = parseStoredValue(getValue());
    	if ( vals != null ) {
        	for ( int j=0; j<vals.length; j++ ) {
        		String val = vals[j].trim();
            	for ( int i=0; i<entryValues.length; i++ ) {
            		CharSequence entry = entryValues[i];
                	if ( entry.equals(val) ) {
            			mClickedDialogEntryIndices[i] = true;
            			break;
            		}
            	}
        	}
    	}
    }
	@Override
    protected void onDialogClosed(boolean positiveResult) {
//        super.onDialogClosed(positiveResult);
        
    	CharSequence[] entryValues = getEntryValues();
        if (positiveResult && entryValues != null) {
        	StringBuffer value = new StringBuffer();
        	for ( int i=0; i<entryValues.length; i++ ) {
        		if ( mClickedDialogEntryIndices[i] ) {
        			value.append(entryValues[i]).append(SEPARATOR);
        		}
        	}
        	
            if (callChangeListener(value)) {
            	String val = value.toString();
            	if ( val.length() > 0 )
            		val = val.substring(0, val.length()-SEPARATOR.length());
            	setValue(val);
            }
        }
    }

}
