package com.commonsense.hkgalden.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CollectionUtils {

	public static byte[] convertFileToByteArray(File f)
	{
		byte[] byteArray = null;
		try
		{
			InputStream inputStream = new FileInputStream(f);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024*8];
			int bytesRead =0;

			while ((bytesRead = inputStream.read(b)) != -1)
			{
				bos.write(b, 0, bytesRead);
			}

			byteArray = bos.toByteArray();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return byteArray;
	}

	public static int searchIndex(String ident , String []  list){
		int pos = 0;
	
		for(int i = 0 ; i <list.length ; i++ ){
			if(ident.equals(list[i])){
				pos = i;
				break;
			}
		}
		return pos;
	}
	
}
