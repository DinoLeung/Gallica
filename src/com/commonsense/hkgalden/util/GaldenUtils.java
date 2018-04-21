package com.commonsense.hkgalden.util;

public final class GaldenUtils {

	public static final int SessionTime = 1*24*60*60*1000;
	public static final String deadTime = "11:33:21";
	////////////////////////////////////////////////////////////////////////////////
	//CommonSense
	public static final String apiKey = "ecb954a155e6bbd9fe3fe166940feb102c80ae90";
	//larrylo
	//	public static final String apiKey =  "24de1064dbe1c6f63840fdadb0e18d8afe231880";
	////////////////////////////////////////////////////////////////////////////////	
	public static final String getForumListUrl = "https://api.hkgalden.com/f";
	public static final String getChannelUrl = "https://api.hkgalden.com/f/l?ident=";
	public static final String readPostUrl = "https://api.hkgalden.com/f/t?id=";
	public static final String quoteUrl = "https://api.hkgalden.com/f/q?q_id=";
	public static final String postPublishUrl = "https://api.hkgalden.com/f/t";
	public static final String postReplyUrl = "https://api.hkgalden.com/f/r";
	public static final String postRateUrl = "https://api.hkgalden.com/f/rate";
	public static final String postRecordOpUrl ="https://api.hkgalden.com/f/ut";
	
	public static final String quoteTopicUrlParam = "q_type=t";
	public static final String quoteReplyUrlParam = "q_type=r";

	public static final String postPublishTitleUrlParam = "&title=";
	public static final String postPublishContentUrlParam = "&content=";
	public static final String postPublishChannelUrlParam = "&ident=";
	
	public static final String pageSuffixUrlPatam = "?page=";
	public static final String uidUrlPatam = "&uid=";
	
	public static final String postExternalLink = "https://hkgalden.com/view/";
	public static final String pageUrlParam = "&ofs=";
	public static final String showTomato = "&tm=";

    public static final String APP_KEY = "com.commonsense.hkgalden";    
    public static final String FOLDER_NAME = "Galden";    
	public static final String  RATE_BROADCAST_ACTION ="com.commonsense.hkgalden.received";
	public static final String  BLOCK_BROADCAST_ACTION ="com.commonsense.hkgalden.block";
	public static final String  UNBLOCK_BROADCAST_ACTION ="com.commonsense.hkgalden.unblock";
	public static String ident = "";
	public static String ofs = "";
	////////////////////////////////////////////////////////////////////////////////
	//	CommonSense userKey
	//	public static final String userKey = "6cd3b33a675afc54e79fdd6007d48ac3705809e1";
	//larrylo Userkey
	//	public static final String userKey =  "72ece854361efbf296ebfcd90f73545b8264fc45";
	///////////////////////////////////////////////////////////////////////////////

	public static final String urlAnd = "&"; 


	public static String getIdent(){
		return ident;
	}

	public static String getOfs(){
		return ofs;
	}


}
