package com.elestar.streetarts;



import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Map extends Activity implements LocationListener{
	WebView webview ;
	public static final String PREFS_NAME = "MyPrefsFile";
	public static final double[] Xs ={100.338239,100.336493,100.338891,0,100.339076,100.338727,100.338824,0,100.338915, 100.339738, 100.340546, 100.337877,0, 100.339140, 100.335774, 100.338732, 100.340602, 100.340696,100.33377,100.338655, 100.339446,100.33752,100.338867, 100.337952, 100.336876,100.33862,100.333086,100.326762};
	public static final double[] Ys ={5.414705,5.414814,5.415378,0,5.415559,5.414211,5.414235,0,5.414136,5.414993,5.413869,5.415057,0,5.413613,5.419778,5.413981,5.414243,5.414283,5.421332,5.413487,5.414056,5.415306,5.41557,5.415287,5.414238,5.415063,5.420304,5.415573};
	public static final String[] artTitle={"Kids On a Bicycle","Reaching Up", "Boy on a Bike","UNKNOWN", "The Bruce Lee Cat Mural", "Skippy come to Penang", "Giant Rat Mural", "The 102nd Cat Mural", "Please Care & Bathe Me", "Cat Walking for Animal Awareness",
										   "Children Playing Basketball", "I Can Help Catch Rats", "The Pau Seller", "I want Pau!", "Little Girl in Blue", "Fortune Cat", "Kung fu Minion", "Brother & Sister on Swing", "The Awaiting Trishaw Pedaler", "Shade Me If You Love Me",
										   "Mama Cat", "Wo Ai Nee, Chinese Malay Indian", "Little Blue Kitty", "No Animal Discrimination Please", "Children at Play", "Cats & Humans happily living together", "Nostalgic Meal Order"};
	Boolean[] opArray;
	Boolean gpsEnabled;
	private LocationManager locationManager;
	private String provider;
	
	@SuppressWarnings("static-access")
	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		setContentView(R.layout.activity_map);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		webview = (WebView) findViewById(R.id.mapview);
		webview.setWebChromeClient(new WebChromeClient());
		WebSettings webSettings = webview.getSettings();
		webview.getSettings().setSupportZoom(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.getSettings().setDisplayZoomControls(false);
		webview.getSettings().setUseWideViewPort(true);
		webview.addJavascriptInterface(new WebAppInterface(this), "Android");
		webSettings.setJavaScriptEnabled(true);
		webview.loadUrl("file:///android_asset/map.html");
		getWindow().getDecorView()
        .setSystemUiVisibility(
        		webview.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | webview.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | webview.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | webview.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | webview.SYSTEM_UI_FLAG_FULLSCREEN
                        | webview.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        );


		gpsEnabled=false;
		getLocation();
		 // Get the location manager
			


	}
	
	private final class CancelOnClickListener implements
	  DialogInterface.OnClickListener {
	  public void onClick(DialogInterface dialog, int which) {
	    Toast.makeText(getApplicationContext(), "Activity will continue",
	    Toast.LENGTH_LONG).show();
	  }
	}

	private final class OkOnClickListener implements
	  DialogInterface.OnClickListener {
	  public void onClick(DialogInterface dialog, int which) {
	  Map.this.finish();
	  }
	} 
	
	public void callIn(){
		Toast.makeText(getApplicationContext(), "Your Location is - \nLat: \nLong: " , Toast.LENGTH_LONG).show();  
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    super.onPrepareOptionsMenu(menu);
	    
	    return true;
	}

	public void getLocation(){
		if (!gpsEnabled){
		    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		    // Define the criteria how to select the locatioin provider -> use
		    // default
		    Criteria criteria = new Criteria();
		    provider = locationManager.getBestProvider(criteria, false);
		    boolean gps_enabled=false;
		    boolean network_enabled=false;
		    gps_enabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	        network_enabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	        if (gps_enabled == false || network_enabled == false){
	        	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			    startActivity(intent);
			    gpsEnabled=false;
			    locationManager.removeUpdates(this);
			    Toast.makeText(this, "After turning on location access, select my location again to enable it.",Toast.LENGTH_SHORT).show();
	        }
	        else{
	        gpsEnabled=true;
		    Location location = locationManager.getLastKnownLocation(provider);
		    if (gps_enabled)
	        locationManager.requestLocationUpdates((LocationManager.GPS_PROVIDER), 400, 1, this);
		    if (network_enabled)
		    locationManager.requestLocationUpdates((LocationManager.NETWORK_PROVIDER), 400, 1, this);
		    // Initialize the location fields
		    if (location != null) {
		    	
		    	System.out.println("Provider " + provider + " has been selected.");
		    	System.out.println(gpsEnabled);
		    	onLocationChanged(location);
		    	
		    }
	        }
		}
		else{
		    locationManager.removeUpdates(this);
		    gpsEnabled=false;
		    webview.loadUrl("javascript:Invisible('myloc')");
		}
	}
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
      
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
  
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
  
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
               getApplicationContext().startActivity(intent);
            }
        });
  
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
  
        // Showing Alert Message
        alertDialog.show();
    }
	public int convertedLat(double lat){
		lat = lat -5.424414;
		return (int) Math.round(lat*-1*58217.8294);
	}
	
	public int convertedLong(double longi){
		longi = longi -100.325679;
		return (int) Math.round(longi*1*57970.6407);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState )
	{
	super.onSaveInstanceState(outState);
	webview.saveState(outState);
	}
	
	@Override
    protected void onStop(){
		super.onStop();

		 if(gpsEnabled)
		 locationManager.removeUpdates(this);
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
	super.onSaveInstanceState(savedInstanceState);
	webview.restoreState(savedInstanceState);
	getWindow().getDecorView()
    .setSystemUiVisibility(
    		View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    );
	}

	@SuppressLint("NewApi")
	@Override
	  protected void onResume() {
		getWindow().getDecorView()
        .setSystemUiVisibility(
        		View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        );
	    super.onResume();
	    if(gpsEnabled)
	    locationManager.requestLocationUpdates(provider, 400, 1, this);
	  }

	  /* Remove the locationlistener updates when Activity is paused */
	  @Override
	  protected void onPause() {
	    super.onPause();
	    if(gpsEnabled)
	    locationManager.removeUpdates(this);
	  }

	  @Override
	  public void onLocationChanged(Location location) {
	   if(convertedLat(location.getLatitude()) <= 832 && convertedLat(location.getLatitude()) >= 0 && convertedLong(location.getLongitude()) >= 0 &&convertedLong(location.getLongitude()) <= 1203)
		   webview.loadUrl("javascript:changeLocation("+location.getLatitude()+", "+location.getLongitude()+", "+convertedLat(location.getLatitude())+", "+convertedLong(location.getLongitude())+")");
	   //}
	  else{
		  Toast.makeText(this, "You are outside the range of the map.",Toast.LENGTH_SHORT).show();
		   //webview.loadUrl("javascript:changeLocation("+location.getLatitude()+", "+location.getLongitude()+", "+convertedLat(location.getLatitude())+", "+convertedLong(location.getLongitude())+")");
		   webview.loadUrl("javascript:showLocation("+location.getLatitude()+", "+location.getLongitude()+", "+convertedLat(location.getLatitude())+", "+convertedLong(location.getLongitude())+")");
	   }
	  }

	  @Override
	  public void onStatusChanged(String provider, int status, Bundle extras) {
	    // TODO Auto-generated method stub

	  }

	  @Override
	  public void onProviderEnabled(String provider) {
	    Toast.makeText(this, "Enabled new provider " + provider,
	        Toast.LENGTH_SHORT).show();

	  }

	  @Override
	  public void onProviderDisabled(String provider) {
	    Toast.makeText(this, "Disabled provider " + provider,
	        Toast.LENGTH_SHORT).show();
	  }
	  
	  public void disp(int id){
		// custom dialog
		  final int b=id;
		  
          final Dialog dialog = new Dialog(this);
          // Include dialog.xml file
          dialog.setContentView(R.layout.dialog);
          // Set dialog title
          dialog.setTitle("Navigate");
  	   
          // set values for custom dialog components - text, image and button
          TextView text = (TextView) dialog.findViewById(R.id.textView1);
          text.setText(artTitle[id-1]);
          ImageView image = (ImageView) dialog.findViewById(R.id.imageView1);
          switch(id){
          case 1:image.setImageResource(R.drawable.i1); break;
          case 2:image.setImageResource(R.drawable.i2); break;
          case 3:image.setImageResource(R.drawable.i3); break;
          case 4:image.setImageResource(R.drawable.i4); break;
          case 5:image.setImageResource(R.drawable.i5); break;
          case 6:image.setImageResource(R.drawable.i6); break;
          case 7:image.setImageResource(R.drawable.i7); break;
          case 8:image.setImageResource(R.drawable.i8); break;
          case 9:image.setImageResource(R.drawable.i9); break;
          case 10:image.setImageResource(R.drawable.i10); break;
          case 11:image.setImageResource(R.drawable.i11); break;
          case 12:image.setImageResource(R.drawable.i12); break;
          case 13:image.setImageResource(R.drawable.i13); break;
          case 14:image.setImageResource(R.drawable.i14); break;
          case 15:image.setImageResource(R.drawable.i15); break;
          case 16:image.setImageResource(R.drawable.i16); break;
          case 17:image.setImageResource(R.drawable.i17); break;
          case 18:image.setImageResource(R.drawable.i18); break;
          case 19:image.setImageResource(R.drawable.i19); break;
          case 20:image.setImageResource(R.drawable.i20); break;
          case 21:image.setImageResource(R.drawable.i21); break;
          case 22:image.setImageResource(R.drawable.i22); break;
          case 23:image.setImageResource(R.drawable.i23); break;
          case 24:image.setImageResource(R.drawable.i24); break;
          case 25:image.setImageResource(R.drawable.i25); break;
          case 26:image.setImageResource(R.drawable.i26); break;
          case 27:image.setImageResource(R.drawable.i27); break;
          }
          Button goButton = (Button) dialog.findViewById(R.id.goButton);
          // if decline button is clicked, close the custom dialog
          goButton.setOnClickListener(new OnClickListener() {
              @Override
              public void onClick(View v) {
            	  String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f", Ys[b-1], Xs[b-1]);
            	  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            	  intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            	  startActivity(intent);
              }
          });
          
          Button declineButton = (Button) dialog.findViewById(R.id.cancelButton);
          // if decline button is clicked, close the custom dialog
          declineButton.setOnClickListener(new OnClickListener() {
              @Override
              public void onClick(View v) {
                  // Close dialog
                  dialog.dismiss();
              }
          });

          dialog.show();

          // if decline button is clicked, close the custom dialog

	  }
	  


}
