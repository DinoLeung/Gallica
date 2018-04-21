package com.commonsense.hkgalden.util;

import java.util.Calendar;

public class TimeReporter {

	  public static String getNow() {
		final Calendar c = Calendar.getInstance();
		String ld = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1)
				+ "/" + c.get(Calendar.DAY_OF_MONTH) + " "
				+ String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) + ":"
				+ String.format("%02d", c.get(Calendar.MINUTE));
		return ld;
	}
		
		public static String getTodayAll() {		
			return getTodaysDate() + "_" + getTodaysTime() ;
		}
		
		public static String getTodaysDate() {

				String d = "";
				final Calendar c = Calendar.getInstance();
				d = String.format("%02d", c.get(Calendar.YEAR)) 		 		
							+ String.format("%02d", c.get(Calendar.MONTH) + 1)  
								+ String.format("%02d", c.get(Calendar.DAY_OF_MONTH));
				return d;
			}
		
	
	
	public static String getTodaysTime() {

		String d = "";
		final Calendar c = Calendar.getInstance();
		d = 	String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) 		 		
					+ String.format("%02d", c.get(Calendar.MINUTE))  
						+ String.format("%02d", c.get(Calendar.SECOND));
		return d;
	}
	
	
	
}
