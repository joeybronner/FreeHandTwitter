package fr.joeybronner.freehandtwitter.util;

import java.util.Arrays;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;

import fr.joeybronner.freehandtwitter.api.TwitterStatus;

public class Constants {
	
	// General
	public static String USER_LANGAGE;
	public static List<String> AVAILABLE_LANGAGES = Arrays.asList("fr","en","de","it","es");
	
	// Twitter app credentials
	final public static String TWITTER_TOKEN_URL = "https://api.twitter.com/oauth2/token";
    final public static String TWITTER_API_KEY = "zOzdMJLSH1uNEF37Gvzz1No3M";
    final public static String TWITTER_API_SECRET = "lwSxPa55Z3spqaMr30aqbINV802eaY1mFzsdvv8Yqxst6Rrrk4";
	
	// Twitter API's URLs
	final public static String TWITTER_USERTIMELINE_URL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";
	final public static String TWITTER_SEARCHTWEETS_URL = "https://api.twitter.com/1.1/search/tweets.json?q=";
	final public static String TWITTER_SEARCH_COUNT = "&count=200";
	final public static String TWITTER_SEARCH_LANG = "&lang=";
	final public static String TWITTER_NOT_RETRIEVE_RTS = "&include_rts=false";
	public static String TWITTER_RESULT_TYPE = "&result_type=popular";
	public static String TWITTER_USER_SEARCH = "";
	
	public static List<TwitterStatus> twit;
	
	public static Typeface tf;
	
	public static int SCROLL_SPEED = 50;
}
 	