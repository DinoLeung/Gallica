package com.commonsense.hkgalden.util;

import java.security.SecureRandom;
import java.util.Calendar;

import android.content.Context;
import android.widget.Toast;

public  class SystemUtils {
	public static int generateFNumber() {
		// TODO Auto-generated method stub		
		int refno = 0;
		SecureRandom r = new SecureRandom();
		refno = r.nextInt((999-100)+1) + 100;		
		return refno;
	}
	
	public static int[] getNow() {
		final Calendar c = Calendar.getInstance();
		int[] date = new int[3];
		date[0] = c.get(Calendar.YEAR);
		date[1] = c.get(Calendar.MONTH)+1;
		date[2] = c.get(Calendar.DAY_OF_MONTH);
		return date;
	}
	
	public static int[] getExpiryDay() {
		int[] now = getNow();
		int[] time = new int[3];
		
		if ((now[1] == 1) || (now[1] == 3) || (now[1] == 5) || (now[1] == 7) || (now[1] == 8) || (now[1] == 10)){
			
			time[0] = now[0];
			time[1] = now[1]+1;
			if (now[1] == 1){
				if (now[2] > 28){
					time[2] = 28;
				}else time[2] = now[2];
			}
			else{
				if (now[2] > 30){
					time[2] = 30;
				}else time[2] = now[2];
			}
		}
		else if ((now[1] == 4) || (now[1] == 6) || (now[1] == 9) || (now[1] == 11)){
			time[0] = now[0];
			time[1] = now[1]+1;
			time[2] = now[2];
		}
		else if (now[1] == 2){
			time[0] = now[0];
			time[1] = now[1]+1;
			if (now[2] > 28){
				time[2] = 28;
			}else time[2] = now[2];
		}
		else if (now[1] == 12){
			time[0] = now[0]+1;
			time[1] = 1;
			time[2] = now[2];
		}
		return time;
	}
	
	public static void toast(Context thisContext , String message){
		Toast.makeText(thisContext, message, Toast.LENGTH_SHORT).show();
	}
	
	
	
	
}
