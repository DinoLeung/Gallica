package com.commonsense.hkgalden.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public  class BBCodeHandler {

	public static void getAllBBCodeMatches(Pattern p, String in, List<String> out)
	{
		Matcher m = p.matcher(in);
		while (m.find()) 
		{	                   
			// 1,3,4
			out.add(m.group(2));
			getAllBBCodeMatches(p, m.group(2), out);
		}	            	          
	}

	public ArrayList<String> parseString (String s){
		Pattern p = Pattern.compile("([^\\[]*)\\[([^\\]]+)\\](.+?)\\[/\\2\\]([^\\[]*)");
		ArrayList<String> out = new ArrayList<String>();

		getAllBBCodeMatches(p, s, out);

		// input stuff checking
		//System.out.println("IN: "+s+"\n-----------");
		// output stuff checking
		//for (String o : out) { System.out.println(o); }

		return out;

	}

	public String parseResultStringArray(ArrayList<String> ast) {

		StringBuffer resultSet = new StringBuffer (""); 
		for(String s : ast ){
			resultSet.append(s);
		}
		return resultSet.toString();
	}

	public String replaceColor(String text , boolean open ){

		//"\\[("+bbcode+")\\]" for [369] , [sosad] 

		// String imageLocation = "file:///android_asset/smileyguy.png";
		// builder.append("<img src=\"" + imageLocation + "\" />");

		StringBuffer imageBuffer = new StringBuffer (""); 

		String bbcode;
		if(!open){
			bbcode = "\\[/(#\\w{6})]";
		}else{
			bbcode = "\\[(#[A-Fa-f0-9]{3}([A-Fa-f0-9]{3})?)\\]";
		}

		Pattern pattern = Pattern.compile(bbcode);
		Matcher matcher = pattern.matcher(text);

		//populate the replacements map ...
		StringBuilder builder = new StringBuilder();
		int i = 0;
		while (matcher.find()) {

			//String orginal = replacements.get(matcher.group(1));
			imageBuffer.append("");
			String replacement = imageBuffer.toString();
			builder.append(text.substring(i, matcher.start()));

			if (replacement == null) {
				builder.append(matcher.group(0));
			} else {
				if(!open){
					//bbcode = "\\[/(#\\w{6})]";
					builder.append("</font>");
				}else{
					builder.append("<font color=" + matcher.group(1) + ">");	
				}
			}
			i = matcher.end();
		}

		builder.append(text.substring(i, text.length()));
		return builder.toString();
	}

	public String replaceBBCode(String text , String bbcode , String imageLocation ){
		return text.replaceAll(Pattern.quote(bbcode), "<img src=\"" + imageLocation + "\" />");
	}
	
	public String replaceBBCode(String text , String[] bbcodes , String imageLocation ){
		String result = "";
		for(String bbcode: bbcodes){
			result = text.replaceAll(Pattern.quote(bbcode), "<img src=\"" + imageLocation + "\" />");
		}
		return result;
	}

	public String replace(String text , String bbcode , String imageLocation ){

		//"\\[("+bbcode+")\\]" for [369] , [sosad] 

		// String imageLocation = "file:///android_asset/smileyguy.png";
		// builder.append("<img src=\"" + imageLocation + "\" />");

		StringBuffer imageBuffer = new StringBuffer (""); 
		//			Pattern pattern = Pattern.compile("\\"+bbcode);
		Pattern pattern = Pattern.compile(Pattern.quote(bbcode));
		Matcher matcher = pattern.matcher(text);

		//populate the replacements map ...
		StringBuilder builder = new StringBuilder();
		int i = 0;
		while (matcher.find()) {

			//String orginal = replacements.get(matcher.group(1));
			imageBuffer.append("<img src=\"" + imageLocation + "\" />");
			String replacement = imageBuffer.toString();
			builder.append(text.substring(i, matcher.start()));

			if (replacement == null) {
				builder.append(matcher.group(0));					
			} else {
				builder.append(replacement);
			}

			i = matcher.end();
		}

		builder.append(text.substring(i, text.length()));
		return builder.toString();
	}

	public String replaceCode(String text , String bbcode , String imageLocation ){

		//"\\[("+bbcode+")\\]" for [369] , [sosad] 

		// String imageLocation = "file:///android_asset/smileyguy.png";
		// builder.append("<img src=\"" + imageLocation + "\" />");

		StringBuffer imageBuffer = new StringBuffer (""); 
		//			Pattern pattern = Pattern.compile("\\"+bbcode);
		Pattern pattern = Pattern.compile(Pattern.quote(bbcode));
		Matcher matcher = pattern.matcher(text);
		HashMap<String, String> replacements = new HashMap<String, String>();

		//populate the replacements map ...
		StringBuilder builder = new StringBuilder();
		int i = 0;
		while (matcher.find()) {

			//String orginal = replacements.get(matcher.group(1));
			imageBuffer.append( imageLocation);
			String replacement = imageBuffer.toString();
			builder.append(text.substring(i, matcher.start()));

			if (replacement == null) {
				builder.append(matcher.group(0));
			} else {
				builder.append(replacement);
			}
			i = matcher.end();
		}

		builder.append(text.substring(i, text.length()));
		return builder.toString();
	}


}
