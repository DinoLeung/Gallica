package com.commonsense.hkgalden.web;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONException;
import org.json.JSONObject;

import com.commonsense.hkgalden.util.CollectionUtils;
import com.commonsense.hkgalden.util.GaldenUtils;

import android.os.Environment;
import android.util.Log;

public class WebAccess {

	public static String slurp(final InputStream is, final int bufferSize)
	{
		final char[] buffer = new char[bufferSize];
		final StringBuilder out = new StringBuilder();
		try {
			final Reader in = new InputStreamReader(is, "UTF-8");
			try {
				for (;;) {
					int rsz = in.read(buffer, 0, buffer.length);
					if (rsz < 0)
						break;
					out.append(buffer, 0, rsz);
				}
			}
			finally {
				in.close();
			}
		}
		catch (UnsupportedEncodingException ex) {
			ex.printStackTrace(); 
		}
		catch (IOException ex) {
			ex.printStackTrace();  
		}
		return out.toString();
	}


	// Execute HTTP Post Request
	public String getData(String channel, String page, String tm, String token) {
		// Create a new HttpClient and Post Header
		System.setProperty("http.keepAlive", "false");
		HttpParams httpParameters = new BasicHttpParams();

		int timeoutConnection = 60000 * 20;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

		int timeoutSocket = 60000 * 20;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setSocketBufferSize(httpParameters, 8*1024);


		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		//		HttpGet httppost = new HttpGet(GaldenUtils.getChannelUrl + channel);

		//set//
		GaldenUtils.ofs = page;
		GaldenUtils.ident = channel;
		//set//

		System.out.println("input page :" + page);

		System.out.println("input channel :" + channel);


		String link = GaldenUtils.getChannelUrl + channel + GaldenUtils.pageUrlParam + page + GaldenUtils.showTomato + tm;

		System.out.println("link :" + link);

		HttpGet httppost = new HttpGet(link);

		httppost.addHeader("X-GALAPI-KEY", GaldenUtils.apiKey);
		httppost.addHeader("X-GALUSER-KEY", token);
		httppost.setHeader("Cache-Control", "no-cache");

		try {	
			System.out.println("Response:"+ "start execute");
			HttpResponse response = httpclient.execute(httppost);
			System.out.println("Response:"+response.getStatusLine().getStatusCode());
			HttpEntity getResponseEntity = response.getEntity();
			InputStream httpResponseStream = getResponseEntity.getContent();
			String result = slurp(httpResponseStream , 8192);
			return result;

		} catch (ClientProtocolException e) {
			e.printStackTrace();			
			return e.getMessage();
		} catch (ConnectTimeoutException e){ 
			e.printStackTrace();			
			return e.getMessage();
		}catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} 	
	}




	public String getPostData(String topic , String page, String token) {
		// Create a new HttpClient and Post Header
		System.setProperty("http.keepAlive", "false");
		HttpParams httpParameters = new BasicHttpParams();

		int timeoutConnection = 60000 * 20;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

		int timeoutSocket = 60000 * 20;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setSocketBufferSize(httpParameters, 8*1024);


		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		//HttpGet httppost = new HttpGet("https://api.hkgalden.com/f/l?ident=bw");
		String urlString = GaldenUtils.readPostUrl + topic + GaldenUtils.pageUrlParam + page;

		System.out.println("link : " + urlString);
		HttpGet httppost = new HttpGet(urlString);

		httppost.addHeader("X-GALAPI-KEY", GaldenUtils.apiKey);
		httppost.addHeader("X-GALUSER-KEY", token);

		try {	
			System.out.println("Response:"+ "start execute");
			HttpResponse response = httpclient.execute(httppost);
			System.out.println("Response:"+response.getStatusLine().getStatusCode());
			HttpEntity getResponseEntity = response.getEntity();
			InputStream httpResponseStream = getResponseEntity.getContent();
			String result = slurp(httpResponseStream , 8192);
			return result;

		} catch (ClientProtocolException e) {
			e.printStackTrace();			
			return e.getMessage();
		} catch (ConnectTimeoutException e){ 
			e.printStackTrace();			
			return e.getMessage();
		}catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} 	
	}
	
	public String getSoundCloud(String urlString) {
		// Create a new HttpClient and Post Header
		System.setProperty("http.keepAlive", "false");
		HttpParams httpParameters = new BasicHttpParams();

		int timeoutConnection = 60000 * 20;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

		int timeoutSocket = 60000 * 20;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setSocketBufferSize(httpParameters, 8*1024);


		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		//HttpGet httppost = new HttpGet("https://api.hkgalden.com/f/l?ident=bw");
		//	String urlString = "http://api.soundcloud.com/users/59420760/playlists.json?client_id=ff9ed5093f7a0302e7633bdfa77dd3f5";
		System.out.println("link : " + urlString);
		HttpGet httppost = new HttpGet(urlString);
		//https://api.soundcloud.com/tracks/163603018/stream.json?client_id=ff9ed5093f7a0302e7633bdfa77dd3f5
		httppost.addHeader("Client ID", "ff9ed5093f7a0302e7633bdfa77dd3f5");
		httppost.addHeader("Client Secret", "13f5ba21c1e8ed11356c20804bae1ab1");


		try {	
			System.out.println("Response:"+ "start execute");
			HttpResponse response = httpclient.execute(httppost);
			System.out.println("Response:"+response.getStatusLine().getStatusCode());
			HttpEntity getResponseEntity = response.getEntity();
			InputStream httpResponseStream = getResponseEntity.getContent();
			String result = slurp(httpResponseStream , 8192);
			return result;

		} catch (ClientProtocolException e) {
			e.printStackTrace();			
			return e.getMessage();
		} catch (ConnectTimeoutException e){ 
			e.printStackTrace();			
			return e.getMessage();
		}catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} 	
	}

	public String getRate(String topic , String page) {
		// Create a new HttpClient and Post Header
		
		String resultString = "Error";
		System.setProperty("http.keepAlive", "false");
		HttpParams httpParameters = new BasicHttpParams();

		int timeoutConnection = 60000 * 20;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

		int timeoutSocket = 60000 * 20;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setSocketBufferSize(httpParameters, 8*1024);


		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		//HttpGet httppost = new HttpGet("https://api.hkgalden.com/f/l?ident=bw");
		String urlString = GaldenUtils.readPostUrl + topic + GaldenUtils.pageUrlParam + page;

		System.out.println("link : " + urlString);
		HttpGet httppost = new HttpGet(urlString);

		httppost.addHeader("X-GALAPI-KEY", GaldenUtils.apiKey);

		try {	
			System.out.println("Response:"+ "start execute");
			HttpResponse response = httpclient.execute(httppost);
			System.out.println("Response:"+response.getStatusLine().getStatusCode());
			HttpEntity getResponseEntity = response.getEntity();
			InputStream httpResponseStream = getResponseEntity.getContent();
			String result = slurp(httpResponseStream , 8192);
			JSONObject json = new JSONObject(result);
			JSONObject	topicPost = json.getJSONObject("topic");
			resultString = "正評: "+ topicPost.getString("good").toString() + " ," + "負評: "+ topicPost.getString("bad").toString();
			

		} catch (ClientProtocolException e) {
			e.printStackTrace();			
			return e.getMessage();
		} catch (ConnectTimeoutException e){ 
			e.printStackTrace();			
			return e.getMessage();
		}catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultString; 	
	}
	

	public String postData(String argument) {

		String result="";
		// Create a new HttpClient and Post Header
		HttpParams httpParameters = new BasicHttpParams();

		int timeoutConnection = 60000 * 20;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

		int timeoutSocket = 60000 * 20;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setSocketBufferSize(httpParameters, 8*1024);


		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		//HttpGet httppost = new HttpGet("https://api.hkgalden.com/f/l?ident=bw");
		Log.d("argument" , argument);

		String[] dataSet = argument.split("\\|");
		String postTitle = dataSet[0];
		String postContent = dataSet[1];
		String token = dataSet[2];

		String postChannel = dataSet[3];

		Log.d("postTitle" , postTitle);
		Log.d("postContent" , postContent);
		Log.d("postChannel" , postChannel);


		String url = GaldenUtils.postPublishUrl;

		Log.d("url" , url);
		HttpPost httppost = new HttpPost(url);


		httppost.addHeader("X-GALAPI-KEY", GaldenUtils.apiKey);
		httppost.addHeader("X-GALUSER-KEY", token);

		try {	
			System.out.println("Response:"+ "start execute");

			JSONObject json = new JSONObject();
			json.put("title",postTitle);
			json.put("content",postContent);
			json.put("ident",postChannel);    
			httppost.setEntity((HttpEntity) new ByteArrayEntity(json.toString().getBytes("UTF8")));
			httppost.setHeader( "Content-Type", "application/json" );
			HttpResponse response = httpclient.execute(httppost);
			int status = response.getStatusLine().getStatusCode();
			System.out.println("Response:"+ status);								
			result =  postChannel + "|" + String.valueOf(status);

		} catch (ClientProtocolException e) {
			e.printStackTrace();			
			return e.getMessage();
		} catch (ConnectTimeoutException e){ 
			e.printStackTrace();			
			return e.getMessage();
		}catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result; 	
	}

	public boolean uploadRecording(String directoryname , String filename , String destination) {
		// TODO Auto-generated method stub
		String destinationPath = destination;		

		File tes = new File(Environment.getExternalStorageDirectory() + File.separator + directoryname);

		File frecord = new File(tes.getAbsolutePath() + File.separator + filename);

		List< NameValuePair> httpContents = new ArrayList< NameValuePair>();
		httpContents.add(new BasicNameValuePair("file",frecord.getAbsolutePath()));	
		boolean result = false;
		HttpClient client=new DefaultHttpClient();
		HttpPost post=new HttpPost(destinationPath);
		try{
			//setup multipart entity
			MultipartEntity entity=new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

			for(int i=0;i< httpContents.size();i++){
				//identify param type by Key
				if(httpContents.get(i).getName().equals("file")){
					File f=new File(httpContents.get(i).getValue());
					FileBody fileBody=new FileBody(f);
					entity.addPart("file"+i,fileBody);
				}
			}
			post.setEntity(entity);
			//create response handler
			//execute and get response
			HttpResponse uploadReponse = client.execute(post);
			Log.d("debug" , "Response : " + uploadReponse);
			if(uploadReponse.getStatusLine().getStatusCode() == 200){
				result = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}     
		return result;
	}

	public String[] login(String email, String password){

		HttpParams httpParameters = new BasicHttpParams();

		int timeoutConnection = 60000 * 20;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

		int timeoutSocket = 60000 * 20;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setSocketBufferSize(httpParameters, 8*1024);


		//HTTP post
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);


		String url = "https://api.hkgalden.com/u/authorizev2";

		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("X-GALAPI-KEY", GaldenUtils.apiKey);

		String[] result = new String[2];

		try {
			//add HTTP parameters
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("email", email));
			nameValuePairs.add(new BasicNameValuePair("appid", "27"));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			nameValuePairs.add(new BasicNameValuePair("deviceid", "mobile_id"));
			nameValuePairs.add(new BasicNameValuePair("dname", "Galden+ mobile device"));
			HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs);
			httpPost.setEntity(entity);

			HttpResponse httpResponse = client.execute(httpPost);	

			int respondCode = httpResponse.getStatusLine().getStatusCode();

			result[0] = Integer.toString(respondCode);

			if (respondCode == 200){
				//get JSON
				HttpEntity getResponseEntity = httpResponse.getEntity();
				InputStream httpResponseStream = getResponseEntity.getContent();
				String token = slurp(httpResponseStream , 8192);
				token = token.substring(token.length()-42, token.length()-2);
				result[1] = token;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public String getUserData(String token){

		//get user info
		System.setProperty("http.keepAlive", "false");
		HttpParams httpParameters = new BasicHttpParams();

		int timeoutConnection = 60000 * 20;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

		int timeoutSocket = 60000 * 20;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setSocketBufferSize(httpParameters, 8*1024);

		//HTTP post
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		String url = "https://api.hkgalden.com/u/check";
		HttpGet httpPost = new HttpGet(url);
		httpPost.addHeader("X-GALAPI-KEY", GaldenUtils.apiKey);
		httpPost.addHeader("X-GALUSER-KEY", token);

		String result = "";
		try
		{
			HttpResponse httpResponse = client.execute(httpPost);	
			int respondCode = httpResponse.getStatusLine().getStatusCode();
			//get JSON
			HttpEntity getResponseEntity = httpResponse.getEntity();
			InputStream httpResponseStream = getResponseEntity.getContent();
			result = slurp(httpResponseStream , 8192);



		}catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public String logout(String token){

		Log.i("logout", "start");

		// Create a new HttpClient and Post Header
		System.setProperty("http.keepAlive", "false");
		HttpParams httpParameters = new BasicHttpParams();

		int timeoutConnection = 60000 * 20;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

		int timeoutSocket = 60000 * 20;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setSocketBufferSize(httpParameters, 8*1024);


		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		HttpGet httppost = new HttpGet("https://api.hkgalden.com/u/unreg?did=mobile_id");
		httppost.addHeader("X-GALAPI-KEY", GaldenUtils.apiKey);
		httppost.addHeader("X-GALUSER-KEY", token);

		String result = "";

		try {

			HttpResponse response = httpclient.execute(httppost);
			result = Integer.toString(response.getStatusLine().getStatusCode());
			HttpEntity getResponseEntity = response.getEntity();
			InputStream httpResponseStream = getResponseEntity.getContent();
			result = result + slurp(httpResponseStream , 8192);
			Log.i("logout", result);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public String getQuote(Boolean isTopic, String id, String token){
		//get quote content
		System.setProperty("http.keepAlive", "false");
		HttpParams httpParameters = new BasicHttpParams();

		int timeoutConnection = 60000 * 20;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

		int timeoutSocket = 60000 * 20;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setSocketBufferSize(httpParameters, 8*1024);


		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		String url = "";
		if (isTopic){
			url = GaldenUtils.quoteUrl + id + "&" + GaldenUtils.quoteTopicUrlParam;
		}else url = GaldenUtils.quoteUrl + id + "&" + GaldenUtils.quoteReplyUrlParam;
		Log.i("URL", url);

		HttpGet httpPost = new HttpGet(url);
		httpPost.addHeader("X-GALAPI-KEY", GaldenUtils.apiKey);
		httpPost.addHeader("X-GALUSER-KEY", token);
		String result = "";
		try
		{
			System.out.println("Response:"+ "start execute");
			HttpResponse response = httpclient.execute(httpPost);
			System.out.println("Response:"+response.getStatusLine().getStatusCode());
			HttpEntity getResponseEntity = response.getEntity();
			InputStream httpResponseStream = getResponseEntity.getContent();
			result = slurp(httpResponseStream , 8192) +"|"+String.valueOf(response.getStatusLine().getStatusCode());
		}catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public String postReply(String argument) {

		String result="";
		// Create a new HttpClient and Post Header
		HttpParams httpParameters = new BasicHttpParams();

		int timeoutConnection = 60000 * 20;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

		int timeoutSocket = 60000 * 20;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setSocketBufferSize(httpParameters, 8*1024);


		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		Log.d("argument" , argument);


		String[] dataSet = argument.split("\\|");
		String postId = dataSet[0];
		String postContent = dataSet[1];		
		String token = dataSet[2];

		Log.d("postId" , postId);
		Log.d("postContent" , postContent);

		String url = GaldenUtils.postReplyUrl;

		Log.d("url" , url);
		HttpPost httppost = new HttpPost(url);


		httppost.addHeader("X-GALAPI-KEY", GaldenUtils.apiKey);
		httppost.addHeader("X-GALUSER-KEY", token);

		try {	
			System.out.println("Response:"+ "start execute");

			JSONObject json = new JSONObject();
			json.put("t_id",postId);
			json.put("content",postContent);
			httppost.setEntity((HttpEntity) new ByteArrayEntity(json.toString().getBytes("UTF8")));
			httppost.setHeader( "Content-Type", "application/json" );
			HttpResponse response = httpclient.execute(httppost);
			int status = response.getStatusLine().getStatusCode();
			System.out.println("Response:"+ status);								
			result = String.valueOf(status);

		} catch (ClientProtocolException e) {
			e.printStackTrace();			
			return e.getMessage();
		} catch (ConnectTimeoutException e){ 
			e.printStackTrace();			
			return e.getMessage();
		}catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result; 	
	}
	
public String postRate(String argument) {

		String result="";
		// Create a new HttpClient and Post Header
		HttpParams httpParameters = new BasicHttpParams();

		int timeoutConnection = 60000 * 20;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

		int timeoutSocket = 60000 * 20;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setSocketBufferSize(httpParameters, 8*1024);


		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		Log.d("argument" , argument);


		String[] dataSet = argument.split("\\|");
		String postId = dataSet[0];
		String rate = dataSet[1];		
		String token = dataSet[2];

		Log.d("postId" , postId);
		Log.d("rate" , rate);

		String url = GaldenUtils.postRateUrl;

		Log.d("url" , url);
		HttpPost httppost = new HttpPost(url);


		httppost.addHeader("X-GALAPI-KEY", GaldenUtils.apiKey);
		httppost.addHeader("X-GALUSER-KEY", token);

		try {	
			System.out.println("Response:"+ "start execute");

			JSONObject json = new JSONObject();
			json.put("t_id",postId);
			json.put("r",rate);
			httppost.setEntity((HttpEntity) new ByteArrayEntity(json.toString().getBytes("UTF8")));
			httppost.setHeader( "Content-Type", "application/json" );
			HttpResponse response = httpclient.execute(httppost);
			int status = response.getStatusLine().getStatusCode();
			System.out.println("Response:"+ status);								
			result = String.valueOf(status);

		} catch (ClientProtocolException e) {
			e.printStackTrace();			
			return e.getMessage();
		} catch (ConnectTimeoutException e){ 
			e.printStackTrace();			
			return e.getMessage();
		}catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result; 	
	}

	public String getData(File inputString) {
		// Create a new HttpClient and Post Header
		String resultantObject = "";
		System.setProperty("http.keepAlive", "false");
	
		//String url = "https://api.na.cx/upload";
		String url = "https://api.na.cx/v2/image";
		//image
		HttpParams httpParameters = new BasicHttpParams();
	
	
	
		int timeoutConnection = 60000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		int timeoutSocket = 60000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
	
		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setSocketBufferSize(httpParameters, 8*1024);
	
		HttpClientParams.setRedirecting(httpParameters, true);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
	
		HttpPost httppost = new HttpPost(url);
	
		httppost.setHeader("User-Agent", "Mozilla/5.0");
		//httppost.addHeader("Authorization", "Client-ID e4b9ef1f0etwFhQb12161357");
		//e4b9ef1f0etwFhQb12161357
		//1bbc39q8cabb03e1L27Va80a112f43c5
	
		httppost.addHeader("Authorization", "Credentials e4b9ef1f0etwFhQb12161357");
	
		//httpclient.setHttpRequestRetryHandler(myRetryHandler);
	
		try {
			// Add your datahttp
	
			System.out.println(url);
	
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			byte[] result = CollectionUtils.convertFileToByteArray(inputString);
	
			MultipartEntity multiPart=new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			ContentBody cd = new InputStreamBody(new ByteArrayInputStream(result),inputString.getName() );			
	//		multiPart.addPart("iris-image", cd);
			multiPart.addPart("image", cd);
			httppost.setEntity(multiPart);			
			HttpResponse response = httpclient.execute(httppost);
	
			System.out.print("response code : " + response.getStatusLine().getStatusCode());
			if(response.getStatusLine().getStatusCode()==200){
				InputStream isInputStream = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(	isInputStream, "utf-8"), 8192);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
	
				resultantObject =   sb.toString();  
	
			}else{
	
			}
			//return responseBody;
			httpclient.getConnectionManager().shutdown();
	
		} catch (ClientProtocolException e) {
			e.printStackTrace();			
			return e.getMessage();
		} catch (ConnectTimeoutException e){ 
			e.printStackTrace();			
			return e.getMessage();
		}catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return resultantObject; 
	}
	
	public String getChannelData()
    {
        // Create a new HttpClient and Post Header
        System.setProperty("http.keepAlive", "false");
        HttpParams httpParameters = new BasicHttpParams();

        int timeoutConnection = 60000 * 20;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        int timeoutSocket = 60000 * 20;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
        HttpConnectionParams.setSocketBufferSize(httpParameters, 8*1024);

        DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
        String link = "https://api.hkgalden.com/f/";

        System.out.println("link :" + link);

        HttpGet httppost = new HttpGet(link);

        httppost.addHeader("X-GALAPI-KEY","d9b511eb952d7da22e7d575750766bb5807a8bd0");

        httppost.setHeader("Cache-Control", "no-cache");

        try {
            System.out.println("Response:"+ "start execute");
            HttpResponse response = httpclient.execute(httppost);
            System.out.println("Response:"+response.getStatusLine().getStatusCode());
            HttpEntity getResponseEntity = response.getEntity();
            InputStream httpResponseStream = getResponseEntity.getContent();
            String result = slurp(httpResponseStream , 8192);
            return result;

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (ConnectTimeoutException e){
            e.printStackTrace();
            return e.getMessage();
        }catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
	
	public String getGoogleDocs(String url) {
		// get dialog data
		System.setProperty("http.keepAlive", "false");
		HttpParams httpParameters = new BasicHttpParams();

		int timeoutConnection = 60000 * 20;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

		int timeoutSocket = 60000 * 20;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setSocketBufferSize(httpParameters, 8*1024);


		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		Log.i("URL", url);

		HttpGet httpPost = new HttpGet(url);
		String result = "";
		try
		{
			System.out.println("Response:"+ "start execute");
			HttpResponse response = httpclient.execute(httpPost);
			System.out.println("Response:"+response.getStatusLine().getStatusCode());
			HttpEntity getResponseEntity = response.getEntity();
			InputStream httpResponseStream = getResponseEntity.getContent();
			result = slurp(httpResponseStream , 8192);
		}catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public String getBlockedUserData(String token){

		//get user info
		System.setProperty("http.keepAlive", "false");
		HttpParams httpParameters = new BasicHttpParams();

		int timeoutConnection = 60000 * 20;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

		int timeoutSocket = 60000 * 20;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setSocketBufferSize(httpParameters, 8*1024);

		//HTTP post
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		String url = "http://api.hkgalden.com/f/b";
		HttpGet httpPost = new HttpGet(url);
		httpPost.addHeader("X-GALAPI-KEY", GaldenUtils.apiKey);
		httpPost.addHeader("X-GALUSER-KEY", token);

		String result = "";
		try
		{
			HttpResponse httpResponse = client.execute(httpPost);	
			//	int respondCode = httpResponse.getStatusLine().getStatusCode();
			//get JSON
			HttpEntity getResponseEntity = httpResponse.getEntity();
			InputStream httpResponseStream = getResponseEntity.getContent();
			result = slurp(httpResponseStream , 8192);



		}catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}


	public String postBlock(String userid , String token) {
		// Create a new HttpClient and Post Header
		String resultantObject = "";
		System.setProperty("http.keepAlive", "false");
		String url = "http://api.hkgalden.com/f/bi";
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 60000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		int timeoutSocket = 60000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setSocketBufferSize(httpParameters, 8*1024);
		HttpClientParams.setRedirecting(httpParameters, true);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		HttpPost httppost = new HttpPost(url);

		httppost.addHeader("X-GALAPI-KEY", GaldenUtils.apiKey);
		httppost.addHeader("X-GALUSER-KEY", token);	
		//httppost.addHeader("bid", userid);	

		try {	
			System.out.println("Response:"+ "start execute");

			JSONObject json = new JSONObject();
			json.put("bid",userid);
			httppost.setEntity((HttpEntity) new ByteArrayEntity(json.toString().getBytes("UTF8")));
			httppost.setHeader( "Content-Type", "application/json" );
			HttpResponse response = httpclient.execute(httppost);
			int status = response.getStatusLine().getStatusCode();
			System.out.println("Response:"+ status);								
			resultantObject = slurp(response.getEntity().getContent() , 8192);
		} catch (ClientProtocolException e) {
			e.printStackTrace();			
			return e.getMessage();
		} catch (ConnectTimeoutException e){ 
			e.printStackTrace();			
			return e.getMessage();
		}catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultantObject; 	
	}


	public String postUnblock(String userid , String token) {
		// Create a new HttpClient and Post Header
		String resultantObject = "";
		System.setProperty("http.keepAlive", "false");
		String url = "http://api.hkgalden.com/f/bu";
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 60000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		int timeoutSocket = 60000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setSocketBufferSize(httpParameters, 8*1024);
		HttpClientParams.setRedirecting(httpParameters, true);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		HttpPost httppost = new HttpPost(url);

		httppost.addHeader("X-GALAPI-KEY", GaldenUtils.apiKey);
		httppost.addHeader("X-GALUSER-KEY", token);	
		//httppost.addHeader("bid", userid);	

		try {	
			System.out.println("Response:"+ "start execute");

			JSONObject json = new JSONObject();
			json.put("bid",userid);
			httppost.setEntity((HttpEntity) new ByteArrayEntity(json.toString().getBytes("UTF8")));
			httppost.setHeader( "Content-Type", "application/json" );
			HttpResponse response = httpclient.execute(httppost);
			int status = response.getStatusLine().getStatusCode();
			System.out.println("Response:"+ status);								
			resultantObject = slurp(response.getEntity().getContent() , 8192);
		} catch (ClientProtocolException e) {
			e.printStackTrace();			
			return e.getMessage();
		} catch (ConnectTimeoutException e){ 
			e.printStackTrace();			
			return e.getMessage();
		}catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultantObject; 	
	}


	public String getPostRecord(String page , String uid , String token){

		//get user info
		System.setProperty("http.keepAlive", "false");
		HttpParams httpParameters = new BasicHttpParams();

		int timeoutConnection = 60000 * 20;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

		int timeoutSocket = 60000 * 20;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setSocketBufferSize(httpParameters, 8*1024);

		//HTTP post
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		String url = GaldenUtils.postRecordOpUrl 
				+ GaldenUtils.pageSuffixUrlPatam + page
				+ GaldenUtils.uidUrlPatam + uid;
		
				//"https://api.hkgalden.com/f/ut?page=2&uid=13767";
		HttpGet httpPost = new HttpGet(url);
		httpPost.addHeader("X-GALAPI-KEY", GaldenUtils.apiKey);
		httpPost.addHeader("X-GALUSER-KEY", token);

		String result = "";
		try
		{
			HttpResponse httpResponse = client.execute(httpPost);	
			//	int respondCode = httpResponse.getStatusLine().getStatusCode();
			//get JSON
			HttpEntity getResponseEntity = httpResponse.getEntity();
			InputStream httpResponseStream = getResponseEntity.getContent();
			result = slurp(httpResponseStream , 8192);

		}catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
