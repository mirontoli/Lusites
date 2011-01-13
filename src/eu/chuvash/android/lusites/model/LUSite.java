package eu.chuvash.android.lusites.model;

import eu.chuvash.android.lusites.util.Helper;

public class LUSite {
	protected double longitude;
	protected double latitude;
	protected String name;
	protected String snippet;
	
	public LUSite(double longitude, double latitude, String name, String snippet) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.name = Helper.getStringWithoutSurroundingQuotes(name);
		this.snippet = Helper.getStringWithoutSurroundingQuotes(snippet);
	}
	
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSnippet() {
		return snippet;
	}
	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
}
