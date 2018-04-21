package com.commonsense.hkgalden.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.commonsense.hkgalden.util.ColorUtils;

public class ContentAdapter {
	
	private Map<String,String> bbMap;
	private Context context;
	private boolean isDark;
	private String token;
	private String textCol;
	
	public ContentAdapter(Context context, boolean isDark , String token) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.isDark = isDark;
		this.token = token;
		//bbcode formats
		this.bbMap = new HashMap<String , String>();
		
		bbMap.put("\n", "<br />");
		
		if(isDark){
			bbMap.put("\\[(url|URL)\\]((http|ftp)+(s)?:\\/\\/[^\\[]*)\\[\\/(url|URL)\\]", "<a style=\"word-break:break-all; word-wrap:break-word; max-width:100%;color:#00DDFF\" href='$2'>$2</a>");
		}
		else{
			bbMap.put("\\[(url|URL)\\]((http|ftp)+(s)?:\\/\\/[^\\[]*)\\[\\/(url|URL)\\]", "<a style=\"word-break:break-all; word-wrap:break-word; max-width:100%;\" href='$2'>$2</a>");
		}
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		boolean text  =  preferences.getBoolean("textonly", false);	
		if(text==true){    
			bbMap.put("\\[(img|IMG)\\]((http|ftp)+(s)?:\\/\\/[^\\[]*)\\[\\/(img|IMG)\\]", "<a href='$2'>$2</a>");
		}else{
			bbMap.put("\\[(img|IMG)\\]((http|ftp)+(s)?:\\/\\/[^\\[]*)\\[\\/(img|IMG)\\]", "<a href=\"$2\"><img style=\"border:0;max-width:100%;height:auto;\" src=\"$2\"/></a>");
		}
		
		bbMap.put("\\[(center|CENTER)\\]", "<P ALIGN=\"CENTER\">");bbMap.put("\\[/(center|CENTER)\\]", "</P>");
		bbMap.put("\\[(left|LEFT)\\]", "<P ALIGN=\"LEFT\">");bbMap.put("\\[/(left|LEFT)\\]", "</P>");
		bbMap.put("\\[(right|RIGHT)\\]", "<P ALIGN=\"RIGHT\">");bbMap.put("\\[/(right|RIGHT)\\]", "</P>");
		
        bbMap.put("\\[(b|B)\\]", "<b>");bbMap.put("\\[/(b|B)\\]", "</b>");
        bbMap.put("\\[(i|I)\\]", "<i>");bbMap.put("\\[/(i|I)\\]", "</i>");
        bbMap.put("\\[(u|U)\\]", "<u>");bbMap.put("\\[/(u|U)\\]", "</u>");
        bbMap.put("\\[(s|S)\\]", "<STRIKE>");bbMap.put("\\[/(s|S)\\]", "</STRIKE>");
        
        bbMap.put("\\[(size|SIZE)=(.*?)\\]", "<font size=\"$2\">");bbMap.put("\\[/(size|SIZE)=(.*?)\\]", "</font>");
        
        bbMap.put("\\[(list|LIST)\\]", "<ul>");bbMap.put("\\[/(list|LIST)\\]", "</ul>");
        bbMap.put("\\[\\*\\](.*?)", "<li>$1</li>");
        
        //bbcode icons
        bbMap.put("\\[369\\]", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/369.gif\"/>");
        bbMap.put("#adore#", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/adore.gif\"/>");
        bbMap.put("\\[sosad\\]", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/sosad.gif\"/>");
        bbMap.put("#yup#", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/agree.gif\"/>");
        bbMap.put("O:-\\)", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/angel.gif\"/>");
        bbMap.put(":-\\[", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/angry.gif\"/>");
        bbMap.put("#ass#", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/ass.gif\"/>");
        bbMap.put("\\[banghead\\]", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/banghead.gif\"/>");
        bbMap.put("\\[yipes\\]", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/yipes.gif\"/>");
        bbMap.put("#kill#", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/kill.gif\"/>");
        bbMap.put("\\[photo\\]", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/photo.gif\"/>");
        bbMap.put("\\[bomb\\]", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/bomb.gif\"/>");
        bbMap.put("\\[bouncer\\]", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/bouncer.gif\"/>");
        bbMap.put("\\[bouncy\\]", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/bouncy.gif\"/>");
        bbMap.put("#bye#", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/bye.gif\"/>");
        bbMap.put("\\[censored\\]", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/censored.gif\"/>");
        bbMap.put("#cn#", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/chicken.gif\"/>");
        bbMap.put(":o\\)", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/clown.gif\"/>");
        bbMap.put(":~\\(", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/cry.gif\"/>");
        bbMap.put("xx\\(", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/dead.gif\"/>");
        bbMap.put(":-\\]", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/devil.gif\"/>");
        bbMap.put("#ng#", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/donno.gif\"/>");
        bbMap.put("#fire#", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/fire.gif\"/>");
        bbMap.put("\\[flowerface\\]", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/flowerface.gif\"/>");
        bbMap.put(":-\\(", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/frown.gif\"/>");
        bbMap.put("@_@", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/@.gif\"/>");
        bbMap.put("#good#", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/good.gif\"/>");
        bbMap.put("#hehe#", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/hehe.gif\"/>");
        bbMap.put("#hoho#", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/hoho.gif\"/>");
        bbMap.put("#kill2#", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/kill2.gif\"/>");
        bbMap.put("\\^3\\^", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/kiss.gif\"/>");
        bbMap.put("#love#", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/love.gif\"/>");
        bbMap.put("#no#", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/no.gif\"/>");
        bbMap.put("\\[offtopic\\]", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/offtopic.gif\"/>");
        bbMap.put(":O", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/oh.gif\"/>");
        bbMap.put("\\[shocking\\]", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/shocking.gif\"/>");
        bbMap.put("\\[slick\\]", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/slick.gif\"/>");
        bbMap.put(":\\)", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/smile.gif\"/>");
        bbMap.put(":D", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/biggrin.gif\"/>");
        bbMap.put("#oh#", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/surprise.gif\"/>");
        bbMap.put(":P", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/tongue.gif\"/>");
        bbMap.put(";-\\)", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/wink.gif\"/>");
        bbMap.put("\\?\\?\\?", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/wonder2.gif\"/>");
        bbMap.put("\\?_\\?", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/wonder.gif\"/>");
        bbMap.put("Z_Z", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/z.gif\"/>");
        
	}
	
	
	public String toHTML(ArrayList<Content> ctList, String col){
		String result = "";
		String headColor = "";
		String bodyColor = "";
		String quoteColor = "";
		textCol = "";
		if(isDark){
			headColor = String.format("#%06X", (0xFFFFFF & ColorUtils.colorTone(col, 1f, 0.5f)));
			bodyColor = String.format("#%06X", (0xFFFFFF & ColorUtils.colorTone(col, 1f, 0.1f)));
			quoteColor = String.format("#%06X", (0xFFFFFF & ColorUtils.colorTone(col, 1f, 0.25f)));
			textCol = "#FFFFFF";
		}
		else{
			headColor = col;
			bodyColor = String.format("#%06X", (0xFFFFFF & ColorUtils.colorTone(col, 0.2f, 1.45f)));
			quoteColor = String.format("#%06X", (0xFFFFFF & ColorUtils.colorTone(col, 0.3f, 1.2f)));
			textCol = "#000000";
		}
		
		for (int i = 0; i < ctList.size(); i++){
			Content temp = ctList.get(i);
			
			String badge = "";
			if (temp.getBadge().equals("lv1"))
				badge = "<div style=\"display:inline-block;padding:1%;background-color:#555;color:#ccc\">普通會員</div>&nbsp;&nbsp;";
			else if (temp.getBadge().equals("lv2"))
				badge = "<div style=\"display:inline-block;padding:1%;background-color:#a03d3d;color:#ccc\">高級會員</div>&nbsp;&nbsp;";
			else if (temp.getBadge().equals("lv3"))
				badge = "&nbsp;&nbsp;<div style=\"display:inline-block;padding:1%;background-color:#54c;color:#ccc\">助理員</div>&nbsp;&nbsp;&nbsp;";
			else if (temp.getBadge().equals("lv3"))
				badge = "<div style=\"display:inline-block;padding:1%;background-color:#000;color:#ccc\">未知物種</div>&nbsp;&nbsp;";
			else if (temp.getBadge().equals("lv5"))
				badge = "&nbsp;&nbsp;<div style=\"display:inline-block;padding:1%;background-color:"+String.format("#%06X", (0xFFFFFF & ColorUtils.colorTone(col, 0.2f, 1.45f)))+";color:#ccc\">管理員</div>&nbsp;&nbsp;&nbsp;";
			
			if (temp.getSex().equals("male"))
				result += "<div class=\"titleBar\">" + badge +
						"<a href=\"gallica://getPosts/" + temp.getUserId() + "\" style=\"color:#A2D2FF;text-decoration:none\">" + temp.getUsername()  + "</a>" +
						"&nbsp;&nbsp;<a href=\"gallica://blockUser/" + temp.getUserId() + "\" style='color:#ccc;text-decoration:none'>[" + temp.getUserId() + "]</a></div>";
			else
				result += "<div class=\"titleBar\">" + badge +
						"<a href=\"gallica://getPosts/" + temp.getUserId() + "\" style=\"color:#FF8DAF;text-decoration:none\">" + temp.getUsername() + "</a>" +
						"&nbsp;&nbsp;<a href=\"gallica://blockUser/" + temp.getUserId() + "\" style='color:#ccc;text-decoration:none'>[" + temp.getUserId() + "]</a></div>";
			
			//is topic
			if (temp.getT_id().equals(temp.getQ_id()))
				result += "<div class=\"content\">" + parseBBcode(temp.getContent()) +
						"<br><br><div style=\"display:inline-block;padding:1%;background-color:"+ quoteColor +";float:right\">" +
						"<a style=\"color:"+ textCol +";text-decoration:none\" href=\"gallica://quote/"+temp.getT_id()+"/true"+"\">Quote</a>" +
						"</div><br>" +
						"<P ALIGN=\"RIGHT\" style=\"color:#948761;\">"+temp.getTime()+"</P></div>";
			else
				result += "<div class=\"content\">" + parseBBcode(temp.getContent()) +
						"<br><br><div style=\"display:inline-block;padding:1%;background-color:"+ quoteColor +";float:right\">" +
						"<a style=\"color:"+ textCol +";text-decoration:none\" href=\"gallica://quote/"+temp.getQ_id()+"/false"+"\">Quote</a>" +
						"</div><br>" +
						"<P ALIGN=\"RIGHT\" style=\"color:#948761;\">"+temp.getTime()+"</P></div>";
		}
		
		String htmlPreffix = 
				"<html><head>" +
				"<meta http-equiv=\"Content-Type\" content=\"text/html\" charset=\"UTF-8\" />" +
				"<meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0; maximum-scale=1.0; minimum-scale=1.0; user-scalable=0;\" />" +
				"<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />"+
				
				"<style type=\"text/css\">" +
				"blockquote{margin:6;color:#948761;}" +
				"img{max-width:100%;height:auto;}" +
				"a{word-break:break-all; word-wrap:break-word; max-width:100%;}" +
				"body {margin:0;padding:0;background: "+ bodyColor +";color:"+ textCol +";width:100%;}" +
				".titleBar {background: "+ headColor +";padding: 1%;}" +
				".content {padding: 2%;word-break:break-all; word-wrap:break-word; max-width:100%;}" +
				"pre{font-family: Consolas; white-space: pre-wrap;}" +
				"</style>" +
				
				"</head><body>";
		String htmlSuffixString = "</body></html>";
		return htmlPreffix + result + htmlSuffixString;
	}
	
	
	
	public String parseBBcode(String bbcode){
		
		String html = bbcode;
		
		html = html.replaceAll("&", "&#38").replaceAll("<", "&#60").replaceAll(">", "&#62");
		
        //parse bcode format
		for (Map.Entry entry: bbMap.entrySet()) {
        	Pattern pattern = Pattern.compile(entry.getKey().toString());
        	Matcher matcher = pattern.matcher(html);
        	while (matcher.find()){
        		html = html.replaceAll(entry.getKey().toString(), entry.getValue().toString());
        		matcher = pattern.matcher(html);
        	}
	    }
		//parse fuck
		html = html.replaceAll("fuck", "<img style=\"border:0;\" src=\"https://hkgalden.com/face/hkg/fuck.gif\"/>");
		
		//parase code
		html = html.replaceAll("\\[(code|CODE)\\]", "<pre style=\"border:2px dotted;font-family:Consolas;white-space:pre-wrap;\"><code>").replaceAll("\\[/(code|CODE)\\]", "</code></pre>");
		
		//parase quote
		html = html.replaceAll("\\[(quote|QUOTE)\\]", "<blockquote style=\"margin:6;color:#948761;\">").replaceAll("\\[/(quote|QUOTE)\\]", "</blockquote>");
		
		//parase color
		html = html.replaceAll("\\[#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})\\]", "<span style='color:#$1;'>").replaceAll("\\[\\/#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})\\]", "</span>");
		
		//parse hide
		if(token.length() > 0){
			html = html.replaceAll("\\[(hide|HIDE)\\]", "<div style=\"border:2px dashed;width:95%;padding:5;font-size:15px;color:"+textCol+"\"><p style=\"display:inline;\">").replaceAll("\\[/(hide|HIDE)\\]", "</p></div>");
		}else{
			html = html.replaceAll("\\[(hide|HIDE)\\](.*?)\\[/(hide|HIDE)\\]", "<div style=\"border:2px dashed;width:95%;padding:5;font-size:15px;color:"+textCol+"\"><p style=\"display:inline;\">***唔講得***</p></div>");	
		}
		
		return html;
	}
	
	
}
