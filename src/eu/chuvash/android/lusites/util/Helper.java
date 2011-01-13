package eu.chuvash.android.lusites.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;

import com.google.android.maps.OverlayItem;

public class Helper {
	//public static final String TAG = "Helper";
	private Helper() {}

	public static String[] convertFromStreamToStringArray(InputStream is) {
		String[] lines = null;
		ArrayList<String> linesList = null;
		if (is != null) {
			// http://tutorials.jenkov.com/java-io/inputstream.html
			// http://www.kodejava.org/examples/266.html
			BufferedReader reader = null;
			linesList = new ArrayList<String>();
			String line;
			try {

				reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					linesList.add(line);
				}
			} catch (IOException e) {

			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException ioe) {
						// TODO
					}
				}
				try {
					is.close();
				} catch (IOException ioe) {
					// TODO
				}
			}
		}
		if (linesList != null && linesList.size() > 0) {
			try {
				//lines = (String[]) linesList.toArray();
				lines = new String[linesList.size()];
				for (int i = 0; i < linesList.size(); i++) {
					lines[i] = linesList.get(i);
				}
			}
			catch (Exception e) {
			}
		}
		return lines;
	}
	public static String getStringWithoutSurroundingQuotes(String s) {
		s = s.replaceAll("^\"", ""); // start quotations mark
		s = s.replaceAll("\"$", ""); // end quotation mark
		return s;
	}
	public static void showDialog(OverlayItem item, Context context) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(item.getTitle());
		dialog.setIcon(eu.chuvash.android.lusites.R.drawable.auditorium_logo);
		dialog.setMessage(item.getSnippet());
		dialog.show();
	}
}
