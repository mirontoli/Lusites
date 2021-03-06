package eu.chuvash.android.lusites;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class EventsActivity extends ListActivity {
	//private static final String TAG = "EventsActivity";
	private List<String> titles = new ArrayList<String>();
	private List<String> links = new ArrayList<String>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.events);
 

		//TODO extract this code to a an own class
		try {
			URL rssUrl = new URL("http://studentlund.se/feed/");
			SAXParserFactory mySAXParserFactory = SAXParserFactory.newInstance();
			SAXParser mySAXParser = mySAXParserFactory.newSAXParser();
			XMLReader myXMLReader = mySAXParser.getXMLReader();
			RSSHandler myRSSHandler = new RSSHandler();
			myXMLReader.setContentHandler(myRSSHandler);
			InputSource myInputSource = new InputSource(rssUrl.openStream());
			myXMLReader.parse(myInputSource);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayAdapter<String> itemList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
		setListAdapter(itemList);
		//test http://developer.android.com/guide/tutorials/views/hello-listview.html
		getListView().setTextFilterEnabled(true);
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> aw, View v, int position,
					long arg3) {
				//aw.getItemAtPosition(position);
				//browser intent
				//http://stackoverflow.com/questions/3004515/android-sending-an-intent-to-browser-to-open-specific-url
				String url = links.get(position);
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
	}
	private class RSSHandler extends DefaultHandler
	{
		final int stateUnknown = 0;
		final int stateTitle = 1;
		final int stateLink = 2;
		int state = stateUnknown;
		boolean firstTitleSeen = false;//the first title is a xml document title, not a news
		boolean firstLinkSeen = false;

		@Override
		public void startDocument() throws SAXException {
			// TODO Auto-generated method stub
		}

		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			// TODO Auto-generated method stub

			if (localName.equalsIgnoreCase("title")) {
				state = stateTitle;
			}
			else if (localName.equalsIgnoreCase("link")) {
				state = stateLink;
			}
			else {
				state = stateUnknown;
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
		throws SAXException {
			// TODO Auto-generated method stub
			state = stateUnknown;
		}

		@Override
		public void characters(char[] ch, int start, int length)
		throws SAXException {
			// TODO Auto-generated method stub
			String strCharacters = new String(ch, start, length);
			if (state == stateTitle) {
				if (firstTitleSeen) {
					titles.add(strCharacters);
				}
				else firstTitleSeen = true;
			}
			else if (state == stateLink) {
				if (firstLinkSeen) {
					links.add(strCharacters);
				}
				else firstLinkSeen = true;
			}
		}

	}
}
