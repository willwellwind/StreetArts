package com.elestar.streetarts;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface extends Activity {
	Context mContext;
	MainActivity parentActivity;
	Map mapActivity;
	

    /** Instantiate the interface and set the context */
    WebAppInterface( MainActivity activity) {
        parentActivity = activity;
        mContext=parentActivity.getBaseContext();
    }
    
    WebAppInterface( Map activity) {
        mapActivity = activity;
        mContext= mapActivity.getBaseContext();
    }


	/** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        /*Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
                Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
                startActivity(intent);*/
    }
    
    @JavascriptInterface
    public void setArt(String id){
    	 //Toast.makeText(mContext, id, Toast.LENGTH_SHORT).show();
    	 this.parentActivity.setArt(Integer.parseInt(id));
    }
    

    @JavascriptInterface
    public void callMap(){
    	this.parentActivity.mapIntent();
    }
    @JavascriptInterface
    public void getArt(){

    	 this.parentActivity.setWebArt();
    }
    @JavascriptInterface
    public void triggerSound(){

    	 this.parentActivity.playClick();
    }   
    @JavascriptInterface
    public void artistUnknown(){
    	String toast = "Artist Unknown";
    	Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    } 
    
	@JavascriptInterface
    public void getDialog(int id){
    	this.mapActivity.disp(id);

    }
}
