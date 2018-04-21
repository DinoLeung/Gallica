package com.commonsense.hkgalden.util;

import java.util.ArrayList;
import java.util.Arrays;

import com.commonsense.hkgaldenPaid.R;
import com.commonsense.hkgalden.model.Channel;

import android.content.Context;
import android.graphics.Color;


public class ColorUtils {
	
	private Context context;
	private static String[] ident;
	private static String[] color;
	
	public static String getColorByIdent(Context context, String id){
		
		DatabaseManager.init(context);
		Channel data = DatabaseManager.getInstance().getChannel(id);
		return "#"+data.getColor();
	}
//	public static String getColor(Context context, int i){
//		
//		color = context.getResources().getStringArray(R.array.colour_array);
//		
//		return color[i];
//	}
//	public static String getIdent(Context context, int i){
//	
//		ident = context.getResources().getStringArray(R.array.ident_array);
//		
//		return ident[i];
//	}
	public static int colorTone(String hexColor, float whiteFactor, float blackFactor){
		float[] hsv = new float[3];
		int color = Color.parseColor(hexColor);
		Color.colorToHSV(color, hsv);
		hsv[2] *= blackFactor; // darker  0.8f    (1.0f for no change)
		hsv[1] *= whiteFactor;// brighter 0.4f    (1.0f for no change)
		color = Color.HSVToColor(hsv);
		return color;
	}
}
