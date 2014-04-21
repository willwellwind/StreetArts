package com.elestar.streetarts;




import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {
	public WebView webview ;
	int lv_id;
	boolean changeArt;
	Context mContext;
	AudioManager audioManager;
	Vibrator v; 
	@SuppressWarnings("static-access")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		lv_id = 9999;
		changeArt=false;
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		webview = (WebView) findViewById(R.id.webview);
		webview.setWebChromeClient(new WebChromeClient());
		WebSettings webSettings = webview.getSettings();
		audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		webview.getSettings().setSupportZoom(true);
		webview.getSettings().setBuiltInZoomControls(false);
		//webview.loadData(readTextFromResource(R.raw.index), "text/html", "utf-8");
		webSettings.setJavaScriptEnabled(true);
		webview.addJavascriptInterface(new WebAppInterface(this), "Android");
		webview.loadUrl("file:///android_asset/index.html");
		getWindow().getDecorView()
        .setSystemUiVisibility(
        		webview.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | webview.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | webview.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | webview.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | webview.SYSTEM_UI_FLAG_FULLSCREEN
                        | webview.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        );
		
		// register a listener for when the navigation bar re-appears

		getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(

		new OnSystemUiVisibilityChangeListener() {

		public void onSystemUiVisibilityChange(int visibility) {

		if (visibility == 0) {

		// the navigation bar re-appears, let¡¯s hide it

		// after 2 seconds

		mHideHandler.postDelayed(mHideRunnable, 2000);

		}

		}

		});

		v= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	}
	
	@SuppressLint("NewApi")
	private void hideSystemUi() {

		getWindow().getDecorView().setSystemUiVisibility(

		View.SYSTEM_UI_FLAG_LAYOUT_STABLE

		| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

		| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

		| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

		| View.SYSTEM_UI_FLAG_FULLSCREEN

		| View.SYSTEM_UI_FLAG_IMMERSIVE);

		}
	
	Handler mHideHandler = new Handler();

	Runnable mHideRunnable = new Runnable() {

	@Override

	public void run() {

	hideSystemUi();

	}

	};
	
	@SuppressLint("NewApi")
	@SuppressWarnings("static-access")
	protected void onResume() {
	    super.onResume();
		getWindow().getDecorView()
        .setSystemUiVisibility(
        		webview.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | webview.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | webview.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | webview.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | webview.SYSTEM_UI_FLAG_FULLSCREEN
                        | webview.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		String webUrl = webview.getUrl();
		if (webUrl.equals("file:///android_asset/index.html") && (keyCode == KeyEvent.KEYCODE_BACK))
			System.exit(0);
	    // Check if the key event was the Back button and if there's history
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
	        webview.goBack();
	        return true;
	    }
	    // If it wasn't the Back key or there's no web page history, bubble up to the default
	    // system behavior (probably exit the activity)
	    return super.onKeyDown(keyCode, event);
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("static-access")
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
	super.onSaveInstanceState(savedInstanceState);
	webview.restoreState(savedInstanceState);
	webview.addJavascriptInterface(new WebAppInterface(this), "Android");
	getWindow().getDecorView()
    .setSystemUiVisibility(
    		webview.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | webview.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | webview.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | webview.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | webview.SYSTEM_UI_FLAG_FULLSCREEN
                    | webview.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    );
	

	}
	
	public void setArt(int id){
		lv_id=id;
		changeArt=true;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
		  {
		    super.onSaveInstanceState(outState);
		 
		    // Save the state of the WebView
		    webview.saveState(outState);
		  }
	
	private final Runnable hideSystemUiCallback = new Runnable() {
        @SuppressLint("NewApi")
		@Override
        public void run() {
        	getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
	};
	

	@SuppressLint("NewApi")
	public void setWebArt(){

		if (changeArt){
			runOnUiThread(new Runnable() {
	    		public void run(){
	    	 webview.loadUrl("javascript:setArt("+lv_id+")");
	    		
	    	}
	    });
		changeArt=false;
		}
		
	}
	
	public void mapIntent(int id){
		Intent intent = new Intent(this, Map.class);
		intent.putExtra("ID", id);
		startActivity(intent);
		//Toast.makeText(getApplicationContext(), id+"", Toast.LENGTH_LONG).show();
	}
	public void mapIntent(){
		Intent intent = new Intent(this, Map.class);		
		startActivity(intent);
	}	
	public void playClick(){
		audioManager.playSoundEffect(SoundEffectConstants.CLICK);
		v.vibrate(50);
	}

}
