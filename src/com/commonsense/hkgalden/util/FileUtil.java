package com.commonsense.hkgalden.util;

import android.content.*;
import android.content.res.*;
import android.graphics.drawable.Drawable;

import java.io.*;

public class FileUtil
{
	public static Drawable readImage(Context context, String name)
	{
		Drawable drawable = null;

		try
		{
			AssetManager assetManager = context.getAssets();
			InputStream is = assetManager.open(name);			
			drawable = Drawable.createFromStream(is, null);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}

		return drawable;
	}

	public static void saveFile(Context context, String name)
	{

	}

	public static FileDescriptor readVideoFile(Context context, String name)
	{
		AssetFileDescriptor descriptor = null;

		try
		{
			descriptor = context.getAssets().openFd(name);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}

		return descriptor.getFileDescriptor();
	}

	public static String readVideo(Context context, long name)
	{
		String path = "android.resource://" + context.getPackageName() + "/" + name;


		return path;
	}

	public static String readFile(Context context, String name)
	{
		String result = null;

		try
		{
			AssetManager assetManager = context.getAssets();
			InputStream is = assetManager.open(name);			
			BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null)
			{
				sb.append(line);
			}
			is.close();

			result = sb.toString();

			System.out.println(result);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}

		return result;
	}
	
	public static void deleteDirectory( File dir ){

		if (dir.isDirectory()){
			String [] children = dir.list();
			for ( int i = 0 ; i < children.length ; i ++ )
			{
				File child = new File( dir , children[i] );
				if(child.isDirectory()){
					deleteDirectory( child );
					child.delete();
				}else{
					child.delete();

				}
			}
			dir.delete();
		}
	}


	/*
	public static AssetFileDescriptor readVideo(Context context, String name)
	{
		AssetFileDescriptor descriptor = null;

		try
		{
			descriptor = context.getAssets().openFd(name);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}

		return descriptor;
	}
	 */
}
