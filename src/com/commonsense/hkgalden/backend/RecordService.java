package com.commonsense.hkgalden.backend;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import com.commonsense.hkgalden.util.SystemUtils;



public class RecordService  extends Service implements OnInfoListener {


	private MediaRecorder recorder;
	private String randomString;
	private SecureRandom random = new SecureRandom();
	private File recorderFile ;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void onDestroy() {
		Log.d("debug" , "Recording Stop Calling ");		
		stopRecording();
		super.onDestroy();
	}


	@Override
	public void onStart(Intent intent, int startId) {		
		startRecording();		
		super.onStart(intent, startId);
	}

	private void startRecording(){

		try {

			recorder = new MediaRecorder();
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);			
			recorder.setAudioSamplingRate(16);
			recorder.setAudioEncodingBitRate(44100);
			String state = Environment.getExternalStorageState();
			if(state.equals("shared"))
			{
				recorder=null;
				this.stopSelf();
			}
			else
			{
				File instanceRecordDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "Galden_Recording");
				if(!instanceRecordDirectory.exists()){
					instanceRecordDirectory.mkdirs();
				}
				randomString = nextRandom();
				File instanceRecord = new File(instanceRecordDirectory.getAbsolutePath() + File.separator +  randomString +".mp4");
				if(!instanceRecord.exists()){
					instanceRecord.createNewFile();
				}

				recorder.setOutputFile(instanceRecord.getAbsolutePath());				
				recorderFile = instanceRecord;
				recorder.setMaxDuration(1000*60*3);  //3 minutes maximum
				recorder.setOnInfoListener(this);
				recorder.prepare();
				recorder.start();
			}


		} catch (IllegalStateException e) {
			e.printStackTrace();
		//	ExceptionLogWriter.writeLog(e);
		} catch (IOException e) {
			e.printStackTrace();
			//ExceptionLogWriter.writeLog(e);
		} catch (RuntimeException e) {
			e.printStackTrace();
			//ExceptionLogWriter.writeLog(e);			
		} catch (Exception e){
			e.printStackTrace();
		//	ExceptionLogWriter.writeLog(e);			
		}
	}

	private void stopRecording() {
		if (recorder != null) {       
			recorder.stop();
			recorder.reset();
			recorder.release();
			recorder = null;
			
			startUpload(recorderFile);
		}
	}



	private void startUpload(File recorderFile2) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onInfo(MediaRecorder mr, int whatStatus, int extra) {
		// TODO Auto-generated method stub
		   if (whatStatus == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {		         
			   	stopRecording();
		         SystemUtils.toast(getApplicationContext(), "Recording is finished. The maximum recording period is 3 minutes.");
		      }
	}


	  public String nextRandom()
	  {
	    return new BigInteger(130, random).toString(32);
	  }


}
