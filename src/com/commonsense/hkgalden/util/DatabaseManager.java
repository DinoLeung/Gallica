package com.commonsense.hkgalden.util;

import java.sql.SQLException;
import java.util.*;

import android.content.Context;
import android.util.Log;

import com.commonsense.hkgalden.model.BlockedUser;
import com.commonsense.hkgalden.model.Channel;
import com.commonsense.hkgalden.model.Favourite;
import com.commonsense.hkgalden.model.History;
import com.commonsense.hkgalden.model.Lm;
import com.commonsense.hkgalden.model.IconPlus;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.PreparedQuery;

public class DatabaseManager
{
	static private DatabaseManager instance;

	Context mCtx;
	
	static public DatabaseManager getInstance()
	{
		return instance;
	}

	static public void init(Context ctx)
	{
		if (null == instance)
		{
			instance = new DatabaseManager(ctx);
		}
	}

	private DatabaseHelper helper;

	private DatabaseManager(Context ctx)
	{
		helper = new DatabaseHelper(ctx);
		this.mCtx  = ctx;
	}

	private synchronized DatabaseHelper getHelper()
	{
		return helper;
	}

	public void close(){
		getHelper().close();
	}
	public List<History> getAllHistory()
	{
		List<History> records = new ArrayList<History>();

		try
		{
			QueryBuilder<History, Integer> queryBuilder = getHelper().getHistoryDao().queryBuilder();
			PreparedQuery<History> preparedQuery = queryBuilder.orderBy("date", false).prepare();
			records = getHelper().getHistoryDao().query(preparedQuery);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return records;
	}

	public History getHistory(String id)
	{
		List<History> records = new ArrayList<History>();

		try
		{
			QueryBuilder<History, Integer> queryBuilder = getHelper().getHistoryDao().queryBuilder();
			PreparedQuery<History> preparedQuery = queryBuilder.where().eq("t_id", id).prepare();
			records = getHelper().getHistoryDao().query(preparedQuery);
			if (records.size()>0)
				return records.get(0);
			else
				return null;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void addHistory(History history){
		try
		{
			getHelper().getHistoryDao().createOrUpdate(history);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	public void delHistory(History history){

		try
		{
			getHelper().getHistoryDao().delete(history);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	public List<Favourite> getAllFavourite()
	{
		List<Favourite> records = new ArrayList<Favourite>();

		try
		{
			QueryBuilder<Favourite, Integer> favouriteQB = getHelper().getFavouriteDao().queryBuilder();
			PreparedQuery<Favourite> favouriteQ = favouriteQB.orderBy("date", false).prepare();
			records = getHelper().getFavouriteDao().query(favouriteQ);

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return records;
	}

	public Favourite getFavourite(String id)
	{
		List<Favourite> records = new ArrayList<Favourite>();

		try
		{
			QueryBuilder<Favourite, Integer> favouriteQB = getHelper().getFavouriteDao().queryBuilder();
			PreparedQuery<Favourite> favouriteQ = favouriteQB.where().eq("t_id", id).prepare();
			records = getHelper().getFavouriteDao().query(favouriteQ);

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return records.get(0);
	}


	public void addFavourite(Favourite f)
	{
		try
		{
			getHelper().getFavouriteDao().createOrUpdate(f);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void delFavourite(Favourite f){
		try
		{
			getHelper().getFavouriteDao().delete(f);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public List<Lm> getAllLM()
	{
		List<Lm> records = new ArrayList<Lm>();

		try
		{
			QueryBuilder<Lm, Integer> lmQB = getHelper().getLmDao().queryBuilder();
			PreparedQuery<Lm> lmQ = lmQB.orderBy("date", false).prepare();
			records = getHelper().getLmDao().query(lmQ);

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return records;
	}

	public Lm getLM(String id)
	{
		List<Lm> records = new ArrayList<Lm>();

		try
		{
			QueryBuilder<Lm, Integer> lmQB = getHelper().getLmDao().queryBuilder();
			PreparedQuery<Lm> lmQ = lmQB.where().eq("t_id", id).prepare();
			records = getHelper().getLmDao().query(lmQ);

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return records.get(0);
	}

	public void addLm(Lm l)
	{
		try
		{
			getHelper().getLmDao().createOrUpdate(l);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void delLm(Lm l){
		try
		{
			getHelper().getLmDao().delete(l);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public List<IconPlus> getAllIcon()
	{
		List<IconPlus> records = new ArrayList<IconPlus>();

		try
		{
			QueryBuilder<IconPlus, Integer> IconQB = getHelper().getIconPlusDao().queryBuilder();
			PreparedQuery<IconPlus> IconQ = IconQB.prepare();
			records = getHelper().getIconPlusDao().query(IconQ);				
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return records;
	}



	public void addIcon(IconPlus l)
	{
		try
		{
			getHelper().getIconPlusDao().createOrUpdate(l);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	public void deleteIcon(IconPlus l)
	{
		try
		{
			getHelper().getIconPlusDao().delete(l);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public List<BlockedUser> getAllBlockedUser()
	{
		List<BlockedUser> records = new ArrayList<BlockedUser>();

		try
		{
			QueryBuilder<BlockedUser, Integer> blockQB = getHelper().getBlockedUserDao().queryBuilder();
			PreparedQuery<BlockedUser> blockQ = blockQB.prepare();
			records = getHelper().getBlockedUserDao().query(blockQ);				
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return records;
	}

	
	public boolean addBlockedUser(BlockedUser buser){
		boolean result = false;
		try
		{
			
			result = getHelper().getBlockedUserDao().create(buser) > 0;
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return result;
	}

	public void deleteBlockedUser(BlockedUser buser){

		try
		{
			getHelper().getBlockedUserDao().delete(buser);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public boolean IsBlockedUserExist(int id)
	{
		
		List<BlockedUser> records = new ArrayList<BlockedUser>();

		try
		{
			QueryBuilder<BlockedUser, Integer> queryBuilder = getHelper().getBlockedUserDao().queryBuilder();
			PreparedQuery<BlockedUser> preparedQuery = queryBuilder.where().eq("u_id", id).prepare();
			records = getHelper().getBlockedUserDao().query(preparedQuery);

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return records!=null ? true : false;
	}
	
	public List<Channel> getAllChannel()
	{
		List<Channel> records = new ArrayList<Channel>();

		try
		{
			QueryBuilder<Channel, Integer> queryBuilder = getHelper().getChannelDao().queryBuilder();
			
			PreparedQuery<Channel> preparedQuery = queryBuilder.prepare();
			records = getHelper().getChannelDao().query(preparedQuery);
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return records;
	}
	
	public String[] getAllChannelName(){
		List<Channel> record = getAllChannel();
		String[] result = new String[record.size()];
		for (int i=0; i<record.size(); i++)
			result[i] = record.get(i).getName();
		return result;
	}
	
	public String[] getAllChannelIdent(){
		List<Channel> record = getAllChannel();
		String[] result = new String[record.size()];
		for (int i=0; i<record.size(); i++)
			result[i] = record.get(i).getIdent();
		return result;
	}
	
	public Channel getChannel(String id)
	{
		List<Channel> records = new ArrayList<Channel>();

		try
		{
			QueryBuilder<Channel, Integer> queryBuilder = getHelper().getChannelDao().queryBuilder();
			PreparedQuery<Channel> preparedQuery = queryBuilder.where().eq("ident", id).prepare();
			records = getHelper().getChannelDao().query(preparedQuery);
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return records.size()!=0 ? records.get(0) : null;
	}
	
	public void updateChannel(String[][] data){
		try
		{
			Dao<Channel, Integer> dao = getHelper().getChannelDao();
			
			//make new channel data
			List<Channel> newData = new ArrayList<Channel>();
			for (int i = 0; i<data.length; i++){
				Channel channel = new Channel();
				channel.setIdent(data[i][0]);
				channel.setName(data[i][1]);
				channel.setColor(data[i][2]);
				newData.add(channel);
			}
			
			List<Channel> localDB = getAllChannel();
			
			if(! channelMatch(localDB, newData)){
				delChannel();
				for (int i = 0; i<newData.size(); i++)
					dao.create(newData.get(i));
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	private boolean channelMatch(List<Channel> A, List<Channel> B){
		
		if (A.size() != B.size())
			return false;
		for (int i = 0; i<A.size(); i++){
			if (!	A.get(i).getIdent().equals(B.get(i).getIdent())&&
					A.get(i).getName().equals(B.get(i).getName())&&
					A.get(i).getColor().equals(B.get(i).getColor())){
				return false;
			}
		}
		
		return true;
	}
	
	public void delChannel(){
		try
		{
			List<Channel> localDB = getAllChannel();
			for (int i = 0; i<localDB.size(); i++)
				if (getChannel(localDB.get(i).getIdent()) != null)
					getHelper().getChannelDao().delete(localDB.get(i));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
}
