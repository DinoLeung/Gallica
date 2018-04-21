package com.commonsense.hkgalden.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.commonsense.hkgalden.model.IconPlus;
import com.commonsense.hkgalden.util.DatabaseManager;
import com.commonsense.hkgalden.util.ExceptionLogWriter;
import com.commonsense.hkgalden.web.WebAccess;
import com.commonsense.hkgaldenPaid.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

public class SplashScreen extends Activity{
	
	String channelJson;
	String iconJson;
	TextView status;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        status = (TextView)findViewById(R.id.status);
        new PrefetchData().execute();
    }
	
	private class PrefetchData extends AsyncTask<Void, String, Void> {
		 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls         
 
        }
        
        @Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			status.setText(values[0]);
		}
 
        @Override
        protected Void doInBackground(Void... arg0) {
        	status.setText("Getting online data...");
        	WebAccess wa = new WebAccess();
            channelJson = wa.getChannelData();
//            iconJson = wa.getGoogleDocs("https://spreadsheets.google.com/feeds/list/0ApEyQph2NSyNdDA2WmNpR2tPQkdsR2JtRnYwcy1NRHc/5/public/basic?alt=json");
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            String[][] channelData;
            if(channelJson != null){
            	publishProgress("Checking topic list...");
            	try {
            		JSONArray jsonArray = new JSONObject(channelJson).getJSONArray("lists");
                    channelData = new String[jsonArray.length()][3];
                    for (int i=0; i < jsonArray.length(); i++){
                    	JSONObject item = jsonArray.getJSONObject(i);
                    	channelData[i][0] = item.getString("ident");
                    	channelData[i][1] = item.getString("name");
                    	channelData[i][2] = item.getString("color");
                    }
                    publishProgress("Updating topic list...");
                    DatabaseManager.init(getApplication());
    				DatabaseManager.getInstance().updateChannel(channelData);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
//            if(iconJson != null){
//            	String lastUpdate = updateIcon(iconJson);
//            	SharedPreferences settings = getSharedPreferences("iconPrefs", 0);
//				settings.edit().putString("lastUpdate", lastUpdate).commit();
//            }
            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(i);
            finish();
        }
//        private void deleteFile(File fileOrDirectory) {
//    		if (fileOrDirectory.exists()) {
//    			String deleteCmd = "rm -r " + fileOrDirectory.getAbsolutePath();
//    			Runtime runtime = Runtime.getRuntime();
//    			try {
//    				runtime.exec(deleteCmd);
//    			} catch (IOException e) { ExceptionLogWriter.appendLog(e.toString()); }
//    		}
//    	}
//    	
//    	private String updateIcon(String result){
//    		Boolean boo = false;
//    		String lastUpdate = null;
//    		DatabaseManager.init(getApplication());
//    		try {
//    			publishProgress("Checking icon+ list...");
//    			JSONObject json = new JSONObject(result);
//    			JSONObject jsonData = json.getJSONObject("feed");
//    			lastUpdate = jsonData.getJSONObject("updated").get("$t").toString();
//    			SharedPreferences settings = getSharedPreferences("iconPrefs", 0);
//    			Boolean updated = lastUpdate.equals(settings.getString("lastUpdate", "0"));
////    			if (forceUpdate){
////    				updated = false;
////    			}
//    			if (!updated){
//    				publishProgress("Updating icon+...");
//    				deleteFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Gallica" + File.separator +"iconPlus"));
//    				deleteFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Gallica" + File.separator +".iconPlus"));
//    				List<IconPlus> iConList = DatabaseManager.getInstance().getAllIcon();
//    				for(IconPlus ic : iConList){
//    					DatabaseManager.getInstance().deleteIcon(ic);
//    				}
//    				JSONArray jsonArray = jsonData.getJSONArray("entry");
//    				String link = "";
//    				String fileAddress = jsonArray.getJSONObject(0).getJSONObject("content").get("$t").toString().split(": ")[1];
//    				//Log.d("fileAddress", fileAddress);
//    				//Log.d("UPDATE", "DATABASE");
//    				//update database
//    				for(int i = 1 ; i < jsonArray.length() ; i++){
//    					Log.d("FOR LOOP", "GET IMG");
//    					JSONObject jsD = jsonArray.getJSONObject(i);
//    					JSONObject jsT = jsD.getJSONObject("title");
//    					String jsTt = jsT.get("$t").toString();
//    					Log.d("SET", jsTt);
//    					publishProgress("Updating icon+ database...");
//    				//	icon = new iconPlus();
//    					IconPlus icon = new IconPlus();
//    					icon.setIconName(jsTt);
//    					JSONObject jsC = jsD.getJSONObject("content");
//    					String jsCt = jsC.get("$t").toString();
//    					String[] data = jsCt.split(", ");
//    					if (jsTt.equals("traditional")){
//    						Log.d("SET", "TRADITIONAL");
//    						icon.setRidicule("[369]");
//    						icon.setAdore("#adore#");
//    						icon.setAgree("#yup#");
//    						icon.setAngel("O:-)");
//    						icon.setAngry(":-[");
//    						icon.setAss("#ass#");
//    						icon.setBanghead("[banghead]");
//    						icon.setBiggrin(":D");
//    						icon.setBomb("[bomb]");
//    						icon.setBouncer("[bouncer]");
//    						icon.setBouncy("[bouncy]");
//    						icon.setBye("#bye#");
//    						icon.setCensored("[censored]");
//    						icon.setChicken("#cn#");
//    						icon.setClown(":o)");
//    						icon.setCry(":~(");
//    						icon.setDead("xx(");
//    						icon.setDevil(":-]");
//    						icon.setDonno("#ng#");
//    						icon.setFire("#fire#");
//    						icon.setFlowerface("[flowerface]");
//    						icon.setFrown(":-(");
//    						icon.setFuck("fuck");
//    						icon.setGhost("@_@");
//    						icon.setGood("#good#");
//    						icon.setHehe("#hehe#");
//    						icon.setHoho("#hoho#");
//    						icon.setKillleft("#kill2#");
//    						icon.setKillright("#kill#");
//    						icon.setKiss("^3^");
//    						icon.setLove("#love#");
//    						icon.setNo("#no#");
//    						icon.setOfftopic("[offtopic]");
//    						icon.setOh(":O");
//    						icon.setPhoto("[photo]");
//    						icon.setShocking("[shocking]");
//    						icon.setSlick("[slick]");
//    						icon.setSmile(":)");
//    						icon.setSosad("[sosad]");
//    						icon.setSurprise("#oh#");
//    						icon.setTongue(":P");
//    						icon.setWink(";-)");
//    						icon.setWonder("???");
//    						icon.setWould("?_?");
//    						icon.setYipes("[yipes]");
//    						icon.setZ("Z_Z");
//    					}
//
//    					else{
//    					//Log.d("SET", "OTHERS");
//    						
//
//    				//		System.out.println("link before" + link);
//    						link = data[0].split(": ")[1];
//    						System.out.println("link after" + link);
//    						icon.setRidicule("[img]"+link+"[/img]");
//    						link = data[1].split(": ")[1];
//    						icon.setAdore("[img]"+link+"[/img]");
//    						link = data[2].split(": ")[1];
//    						icon.setAgree("[img]"+link+"[/img]");
//    						link = data[3].split(": ")[1];
//    						icon.setAngel("[img]"+link+"[/img]");
//    						link = data[4].split(": ")[1];
//    						icon.setAngry("[img]"+link+"[/img]");
//    						link = data[5].split(": ")[1];
//    						icon.setAss("[img]"+link+"[/img]");
//    						link = data[6].split(": ")[1];
//    						icon.setBanghead("[img]"+link+"[/img]");
//    						link = data[7].split(": ")[1];
//    						icon.setBiggrin("[img]"+link+"[/img]");
//    						link = data[8].split(": ")[1];
//    						icon.setBomb("[img]"+link+"[/img]");
//    						link = data[9].split(": ")[1];
//    						icon.setBouncer("[img]"+link+"[/img]");
//    						link = data[10].split(": ")[1];
//    						icon.setBouncy("[img]"+link+"[/img]");
//    						link = data[11].split(": ")[1];
//    						icon.setBye("[img]"+link+"[/img]");
//    						link = data[12].split(": ")[1];
//    						icon.setCensored("[img]"+link+"[/img]");
//    						link = data[13].split(": ")[1];
//    						icon.setChicken("[img]"+link+"[/img]");
//    						link = data[14].split(": ")[1];
//    						icon.setClown("[img]"+link+"[/img]");
//    						link = data[15].split(": ")[1];
//    						icon.setCry("[img]"+link+"[/img]");
//    						link = data[16].split(": ")[1];
//    						icon.setDead("[img]"+link+"[/img]");
//    						link = data[17].split(": ")[1];
//    						icon.setDevil("[img]"+link+"[/img]");
//    						link = data[18].split(": ")[1];
//    						icon.setDonno("[img]"+link+"[/img]");
//    						link = data[19].split(": ")[1];
//    						icon.setFire("[img]"+link+"[/img]");
//    						link = data[20].split(": ")[1];
//    						icon.setFlowerface("[img]"+link+"[/img]");
//    						link = data[21].split(": ")[1];
//    						icon.setFrown("[img]"+link+"[/img]");
//    						link = data[22].split(": ")[1];
//    						icon.setFuck("[img]"+link+"[/img]");
//    						link = data[23].split(": ")[1];
//    						icon.setGhost("[img]"+link+"[/img]");
//    						link = data[24].split(": ")[1];
//    						icon.setGood("[img]"+link+"[/img]");
//    						link = data[25].split(": ")[1];
//    						icon.setHehe("[img]"+link+"[/img]");
//    						link = data[26].split(": ")[1];
//    						icon.setHoho("[img]"+link+"[/img]");
//    						link = data[27].split(": ")[1];
//    						icon.setKillleft("[img]"+link+"[/img]");
//    						link = data[28].split(": ")[1];
//    						icon.setKillright("[img]"+link+"[/img]");
//    						link = data[29].split(": ")[1];
//    						icon.setKiss("[img]"+link+"[/img]");
//    						link = data[30].split(": ")[1];
//    						icon.setLove("[img]"+link+"[/img]");
//    						link = data[31].split(": ")[1];
//    						icon.setNo("[img]"+link+"[/img]");
//    						link = data[32].split(": ")[1];
//    						icon.setOfftopic("[img]"+link+"[/img]");
//    						link = data[33].split(": ")[1];
//    						icon.setOh("[img]"+link+"[/img]");
//    						link = data[34].split(": ")[1];
//    						icon.setPhoto("[img]"+link+"[/img]");
//    						link = data[35].split(": ")[1];
//    						icon.setShocking("[img]"+link+"[/img]");
//    						link = data[36].split(": ")[1];
//    						icon.setSlick("[img]"+link+"[/img]");
//    						link = data[37].split(": ")[1];
//    						icon.setSmile("[img]"+link+"[/img]");
//    						link = data[38].split(": ")[1];
//    						icon.setSosad("[img]"+link+"[/img]");
//    						link = data[39].split(": ")[1];
//    						icon.setSurprise("[img]"+link+"[/img]");
//    						link = data[40].split(": ")[1];
//    						icon.setTongue("[img]"+link+"[/img]");
//    						link = data[41].split(": ")[1];
//    						icon.setWink("[img]"+link+"[/img]");
//    						link = data[42].split(": ")[1];
//    						icon.setWonder("[img]"+link+"[/img]");
//    						link = data[43].split(": ")[1];
//    						icon.setWould("[img]"+link+"[/img]");
//    						link = data[44].split(": ")[1];
//    						icon.setYipes("[img]"+link+"[/img]");
//    						link = data[45].split(": ")[1];
//    						icon.setZ("[img]"+link+"[/img]");							
//    					}
//    					
//    					DatabaseManager.getInstance().addIcon(icon);
//    				}
//    				//make directory
//    				File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
//    						+File.separator +"Gallica");
//    				if (!dir.exists()) {
//    					dir.mkdirs();
//    				}
//    				Log.e("BEFORE", "DONWLAOD");
//    				//download zipped package
//    				publishProgress("Downloading icon+ files...");
//    				InputStream input = null;
//    				OutputStream output = null;
//    				HttpURLConnection connection = null;
//    				try {
//    					URL url = new URL("https://docs.google.com/uc?export=download&id="+fileAddress);
//    					//			        	URL url = new URL("http://galdencommonsense.netai.net/iconPlus.zip");
//    					connection = (HttpURLConnection) url.openConnection();
//    					connection.connect();
//
//    					if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//    						boo =  false;
//    					}
//
//    					int fileLength = connection.getContentLength();
//
//    					input = connection.getInputStream();
//    					output = new FileOutputStream(
//    							Environment.getExternalStorageDirectory().getAbsolutePath() 
//    							+  File.separator +"Gallica" + File.separator +"iconPlus.zip");
//
//
//    					byte data[] = new byte[4096];
//    					long total = 0;
//    					int count;
//    					while ((count = input.read(data)) != -1) {
//    						// allow canceling with back button
////    						if (isCancelled()) {
////    							input.close();
////    							boo =  null;
////    						}
//    						total += count;
//    						if (fileLength > 0)
//    							publishProgress("Downloading icon+ files (" + (total * 100 / fileLength) + "% )...");
//    						output.write(data, 0, count);
//    						Log.e("DOWNLOAD", "100%");
//    					}
//    				} catch (Exception e) {
//    					ExceptionLogWriter.appendLog(e.toString());
//    					boo =  false;
//    				} finally {
//    					try {
//    						if (output != null)
//    							output.close();
//    						if (input != null)
//    							input.close();
//    					} catch (IOException ignored) {
//
//    					}
//
//    					if (connection != null)
//    						connection.disconnect();
//    					//     	DatabaseManager.getInstance().close();
//    				}
//    				Log.e("AFTER", "DONWLAOD");
//
//    				//extract zip
//    				publishProgress("Extracting Files...");
//    				try
//    				{
//    					FileInputStream fin = new FileInputStream(
//    							Environment.getExternalStorageDirectory().getAbsolutePath()
//    							+ File.separator + "Gallica" + File.separator +"iconPlus.zip");
//    					ZipInputStream zin = new ZipInputStream(fin);
//    					ZipEntry ze = null;
//    					while ((ze = zin.getNextEntry()) != null)
//    					{
//    						Log.v("Decompress", "Unzipping " + ze.getName());
//
//    						if(ze.isDirectory())
//    						{
//    							File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
//    									+ File.separator +"Gallica"  + File.separator + ze.getName());
//    							if(!f.isDirectory())
//    							{
//    								f.mkdirs();
//    							}
//    						}
//    						else
//    						{
//    							File f = new File (Environment.getExternalStorageDirectory().getAbsolutePath() +
//    									File.separator +"Gallica"  + File.separator  + ze.getName());
//    							if (!f.exists()) {
//    								f.createNewFile();
//    							}
//    							FileOutputStream fout = new FileOutputStream(f);
//    							//	FileOutputStream fout = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() +
//    							//			File.separator +"Gallica"  + File.separator  + ze.getName());
//    							byte[] buffer = new byte[8192];
//    							int len;
//    							while ((len = zin.read(buffer)) != -1)
//    							{
//    								fout.write(buffer, 0, len);
//    							}
//    							fout.close();
//    							zin.closeEntry();
//    						}
//    					}
//    					zin.close();
//    					
//    					new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + 
//    							"Gallica"+  File.separator +"iconPlus.zip").delete();  
//    					
//    					boo = true;
//    				}
//    				catch(Exception e)
//    				{
//
//    					ExceptionLogWriter.appendLog(e.toString());
//    					boo=  false;
//    				}
//    			}
//    		}
//    		catch(JSONException e) {
//
//    			ExceptionLogWriter.appendLog(e.toString());
//    			// TODO Auto-generated catch block
//    			boo = false;
//    		}
//    		return boo ? lastUpdate : "0";
//    	}
    }
}
