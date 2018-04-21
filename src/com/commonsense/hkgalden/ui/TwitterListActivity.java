package com.commonsense.hkgalden.ui;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.commonsense.hkgalden.adapter.TwitterAdapter;
import com.commonsense.hkgalden.twitter.Authenticated;
import com.commonsense.hkgalden.twitter.Tweet;
import com.commonsense.hkgalden.twitter.Twitter;
import com.commonsense.hkgaldenPaid.R;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.*;
import java.net.URLEncoder;

/**
 * Demonstrates how to use a twitter application keys to access a user's timeline
 */
public class TwitterListActivity extends ListActivity {

	private ListActivity activity;
	final static String ScreenName = "hkgaldentraffic";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		/*	GradientDrawable gd = new GradientDrawable(
		GradientDrawable.Orientation.TOP_BOTTOM,
		new int[] {Color.parseColor("#FF000000"), Color.parseColor("#77d4d2d2")});

//getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(colour)));
TwitterListActivity.this.getActionBar().setBackgroundDrawable(gd);*/
		TwitterListActivity.this.getActionBar().setTitle(getResources().getString(R.string.traffic));
		//Toast.makeText(getApplicationContext(), "撈緊請等陣.....", Toast.LENGTH_LONG).show();
		downloadTweets();
	}

	// download twitter timeline after first checking to see if there is a network connection
	public void downloadTweets() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			new DownloadTwitterTask(TwitterListActivity.this).execute(ScreenName);
		} else {
			Toast.makeText(activity.getApplicationContext(), "No network connection available." , Toast.LENGTH_LONG).show();
		}
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		Tweet st =  (Tweet) l.getItemAtPosition(position);	
	}


	// Uses an AsyncTask to download a Twitter user's timeline
	private class DownloadTwitterTask extends AsyncTask<String, Void, String> {
		final static String CONSUMER_KEY = "ujnfdPVl7vyDYcTFFdk9Rg";
		final static String CONSUMER_SECRET = "4KAmm8ZyhzIoFLq0RLBIZBWjG0f6pFzF08LQQyWBc6s";
		final static String TwitterTokenURL = "https://api.twitter.com/oauth2/token";
		final static String TwitterStreamURL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";
		private ProgressDialog dialog;
		private Activity activity;
		private volatile boolean running = true;
		public DownloadTwitterTask(final Activity parent) {

			activity = parent;
			dialog = new ProgressDialog(parent);
			dialog.setTitle("膠登汽車台交通直擊");
			dialog.setMessage("撈緊");
			dialog.setCancelable(true);
			dialog.setCancelable(true);			
			dialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// actually could set running = false; right here, but I'll
					// stick to contract.
					cancel(true);
				}
			});

		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			if(dialog!=null){
				dialog.dismiss();
			}
			running = false;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog.show();
		}

		@Override
		protected String doInBackground(String... screenNames) {
			String result = null;

			if (screenNames.length > 0) {
				result = getTwitterStream(screenNames[0]);
			}
			return result;
		}

		// onPostExecute convert the JSON results into a Twitter object (which is an Array list of tweets
		@Override
		protected void onPostExecute(String result) {
			Twitter twits = jsonToTwitter(result);

			// lets write the results to the console as well
			for (Tweet tweet : twits) {
				//Log.i(LOG_TAG, tweet.getText());				
			}

			// send the tweets to the adapter for rendering
			//	ArrayAdapter<Tweet> adapter = new ArrayAdapter<Tweet>(activity, android.R.layout.simple_list_item_1, twits);

			TwitterAdapter cadapt = new TwitterAdapter(
					TwitterListActivity.this, R.layout.read_traffic, twits);

			cadapt.notifyDataSetChanged();
			setListAdapter(cadapt);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			if (dialog!=null) {
				dialog=null;
			}		

		}




		// converts a string of JSON data into a Twitter object
		private Twitter jsonToTwitter(String result) {
			Twitter twits = null;
			if (result != null && result.length() > 0) {
				try {
					Gson gson = new Gson();
					twits = gson.fromJson(result, Twitter.class);
				} catch (IllegalStateException ex) {
					// just eat the exception
				}
			}
			return twits;
		}

		// convert a JSON authentication object into an Authenticated object
		private Authenticated jsonToAuthenticated(String rawAuthorization) {
			Authenticated auth = null;
			if (rawAuthorization != null && rawAuthorization.length() > 0) {
				try {
					Log.d("rawAuthorization"  , rawAuthorization);
					auth = new Gson().fromJson(rawAuthorization, Authenticated.class);
				} catch (IllegalStateException ex) {
					// just eat the exception
				}
			}
			return auth;
		}

		private String getResponseBody(HttpRequestBase request) {
			StringBuilder sb = new StringBuilder();
			try {

				DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
				HttpResponse response = httpClient.execute(request);
				int statusCode = response.getStatusLine().getStatusCode();
				String reason = response.getStatusLine().getReasonPhrase();

				if (statusCode == 200) {

					HttpEntity entity = response.getEntity();
					InputStream inputStream = entity.getContent();

					BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
					String line = null;
					while ((line = bReader.readLine()) != null) {
						sb.append(line);
					}
				} else {
					sb.append(reason);
				}
			} catch (UnsupportedEncodingException ex) {
			} catch (ClientProtocolException ex1) {
			} catch (IOException ex2) {
			}
			return sb.toString();
		}

		private String getTwitterStream(String screenName) {
			String results = null;

			// Step 1: Encode consumer key and secret
			try {
				// URL encode the consumer key and secret
				String urlApiKey = URLEncoder.encode(CONSUMER_KEY, "UTF-8");
				String urlApiSecret = URLEncoder.encode(CONSUMER_SECRET, "UTF-8");

				// Concatenate the encoded consumer key, a colon character, and the
				// encoded consumer secret
				String combined = urlApiKey + ":" + urlApiSecret;

				// Base64 encode the string
				String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);

				// Step 2: Obtain a bearer token
				HttpPost httpPost = new HttpPost(TwitterTokenURL);
				httpPost.setHeader("Authorization", "Basic " + base64Encoded);
				httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
				httpPost.setEntity(new StringEntity("grant_type=client_credentials"));
				String rawAuthorization = getResponseBody(httpPost);
				Authenticated auth = jsonToAuthenticated(rawAuthorization);

				// Applications should verify that the value associated with the
				// token_type key of the returned object is bearer
				if (auth != null && auth.token_type.equals("bearer")) {

					// Step 3: Authenticate API requests with bearer token
					HttpGet httpGet = new HttpGet(TwitterStreamURL + screenName);

					// construct a normal HTTPS request and include an Authorization
					// header with the value of Bearer <>
					httpGet.setHeader("Authorization", "Bearer " + auth.access_token);
					httpGet.setHeader("Content-Type", "application/json");
					// update the results with the body of the response
					results = getResponseBody(httpGet);
				}
			} catch (UnsupportedEncodingException ex) {
			} catch (IllegalStateException ex1) {
			}
			return results;
		}
	}
}
