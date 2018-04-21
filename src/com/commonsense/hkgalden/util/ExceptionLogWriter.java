package com.commonsense.hkgalden.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class ExceptionLogWriter {

	public static void reportToAdmin(String[] recipents , String subject , String message ,
			File attachment ,		Context ctx) {
		// TODO Auto-generated method stub

		Intent mailIntent = new Intent(Intent.ACTION_SEND);
		mailIntent.setType("text/Message");	
		mailIntent.putExtra(Intent.EXTRA_EMAIL  , recipents);
		mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		mailIntent.putExtra(Intent.EXTRA_TEXT   , message);

		if (!attachment.exists() || !attachment.canRead()) {
			Toast.makeText(ctx, 
					"Attachment Error", 
					Toast.LENGTH_SHORT).show();
			System.out.println("ATTACHMENT ERROR");
		}
		else
		{
			Uri uri = Uri.fromFile(attachment);
			mailIntent.putExtra(Intent.EXTRA_STREAM, uri);
		}

		//Send, if valid!
		try {
			ctx.startActivity(Intent.createChooser(mailIntent, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(ctx, 
					"There are no email clients installed.", 
					Toast.LENGTH_SHORT).show();
		}


	}
	public static  void appendLog(String text)
	{       

		String happenTime = TimeReporter.getTodayAll() ;
		String name = happenTime + "_" + "log.txt" ;

		File logFile = new File("sdcard" + File.pathSeparator + name);
		if (!logFile.exists())
		{
			try
			{
				logFile.createNewFile();
				BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
				buf.append(text);
				buf.newLine();
				buf.close();
			} 
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
}
