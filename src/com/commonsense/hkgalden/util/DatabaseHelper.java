package com.commonsense.hkgalden.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.commonsense.hkgalden.model.BlockedUser;
import com.commonsense.hkgalden.model.Channel;
import com.commonsense.hkgalden.model.Favourite;
import com.commonsense.hkgalden.model.History;
import com.commonsense.hkgalden.model.Lm;
import com.commonsense.hkgalden.model.IconPlus;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
	private static final String DATABASE_NAME = "hkgalden.sqlite";
	private static final int DATABASE_VERSION = 7;

	private ConnectionSource connectionSource = null;
	private Dao<History, Integer> hDao = null;
	private RuntimeExceptionDao<History, Integer> hRuntimeDao =null;
	private Dao<Favourite, Integer> fDao = null;
	private RuntimeExceptionDao<Favourite, Integer> fRuntimeDao =null;
	private Dao<Lm, Integer> lDao = null;
	private RuntimeExceptionDao<Lm, Integer> lRuntimeDao =null;
	private Dao<IconPlus, Integer> iDao = null;
	private RuntimeExceptionDao<IconPlus, Integer> iRuntimeDao =null;
	private Dao<BlockedUser, Integer> buDao = null;
	private RuntimeExceptionDao<BlockedUser, Integer> buRuntimeDao =null;
	private Dao<Channel, Integer> cDao = null;
	private RuntimeExceptionDao<Channel, Integer> cRuntimeDao =null;
	
	
	private static String DATABASE_INT_PATH = 
			String.format(	Environment.getDataDirectory() + "//data//%s//databases//", new Object[] {GaldenUtils.APP_KEY});
	private static String DATABASE_EXT_PATH = 
			Environment.getExternalStorageDirectory() + File.pathSeparator + GaldenUtils.FOLDER_NAME + File.pathSeparator; 					
	

	public DatabaseHelper(Context context)
	{// to finalise the helper to create / config
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database,ConnectionSource connectionSource)
	{
		try
		{
			Log.i(DatabaseHelper.class.getSimpleName(), "onCreate()");
			TableUtils.createTable(connectionSource, History.class);
			TableUtils.createTable(connectionSource, Favourite.class);
			TableUtils.createTable(connectionSource, Lm.class); 
			TableUtils.createTable(connectionSource, IconPlus.class);     
			TableUtils.createTable(connectionSource, BlockedUser.class);
			TableUtils.createTable(connectionSource, Channel.class);
		}
		catch (SQLException e)
		{
			Log.e(DatabaseHelper.class.getSimpleName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
		catch (java.sql.SQLException e)
		{
			e.printStackTrace();
		}
	}

	
	
	@Override
	public void onUpgrade(SQLiteDatabase db,ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		try
		{
			Log.i(DatabaseHelper.class.getSimpleName(), "onUpgrade()");
			TableUtils.dropTable(connectionSource, History.class, true);
			TableUtils.dropTable(connectionSource, Favourite.class, true);
			TableUtils.dropTable(connectionSource, Lm.class, true);
			TableUtils.dropTable(connectionSource, IconPlus.class, true);
			TableUtils.dropTable(connectionSource, BlockedUser.class, true);

			onCreate(db, connectionSource);
		}
		catch (Exception e)
		{
			Log.e(DatabaseHelper.class.getName(), "exception during onUpgrade", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public ConnectionSource getConnectionSource() {
	    if (connectionSource == null) {
	        connectionSource = super.getConnectionSource();
	    }
	    return connectionSource;
	}
	
	

	public void cleanUpIcon()
	{
		try{
			ConnectionSource connectionsource = getConnectionSource();		
			TableUtils.dropTable(connectionsource, IconPlus.class, true);
			TableUtils.createTable(connectionSource, IconPlus.class);       
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void exportDB() {
		// TODO Auto-generated method stub

		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String  currentDBPath= DATABASE_INT_PATH + DATABASE_NAME ;
				String backupDBPath  = DATABASE_EXT_PATH + DATABASE_NAME;
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				FileChannel src = null;
				FileChannel dst = null;
				try {
					src = new FileInputStream(currentDB).getChannel();
					dst = new FileOutputStream(backupDB).getChannel();
					dst.transferFrom(src, 0, src.size());
				}finally{
					src.close();
					dst.close();
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void importDB() {
		// TODO Auto-generated method stub

		try {
			File sd = Environment.getExternalStorageDirectory();
			File data  = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String  currentDBPath= DATABASE_INT_PATH + DATABASE_NAME ;
				String backupDBPath  = DATABASE_EXT_PATH + DATABASE_NAME;
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				FileChannel src = null;
				FileChannel dst = null;
				try {
					src = new FileInputStream(currentDB).getChannel();
					dst = new FileOutputStream(backupDB).getChannel();
					dst.transferFrom(src, 0, src.size());
				}finally{
					src.close();
					dst.close();
				}		
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close()
	{
		super.close();
		hDao = null;
		fDao = null;
		lDao = null;
		iDao = null;
		buDao = null;
		cDao = null;
	}

	public Dao<History, Integer> getHistoryDao()
	{
		if (null == hDao)
		{
			try
			{
				hDao = getDao(History.class);
			}
			catch (java.sql.SQLException e)
			{
				e.printStackTrace();
			}
		}

		return hDao;
	}

	
	public RuntimeExceptionDao<History, Integer> getHistoryRuntimeExDao()
	{
		if (null == hRuntimeDao)
		{
			hRuntimeDao = getRuntimeExceptionDao(History.class);
		}

		return hRuntimeDao;
	}
	
	public Dao<Favourite, Integer> getFavouriteDao()
	{
		if (null == fDao)
		{
			try
			{
				fDao = getDao(Favourite.class);
			}
			catch (java.sql.SQLException e)
			{
				e.printStackTrace();
			}
		}

		return fDao;
	}

	public RuntimeExceptionDao<Favourite, Integer> getFavouriteRuntimeExDao()
	{
		if (null == fRuntimeDao)
		{
			fRuntimeDao = getRuntimeExceptionDao(Favourite.class);
		}

		return fRuntimeDao;
	}
	
	public Dao<Lm, Integer> getLmDao()
	{
		if (null == lDao)
		{
			try
			{
				lDao = getDao(Lm.class);
			}
			catch (java.sql.SQLException e)
			{
				e.printStackTrace();
			}
		}

		return lDao;
	}

	public RuntimeExceptionDao<Lm, Integer> getLmRuntimeExDao()
	{
		if (null == lRuntimeDao)
		{
			lRuntimeDao = getRuntimeExceptionDao(Lm.class);
		}

		return lRuntimeDao;
	}
	
	
	public Dao<IconPlus, Integer> getIconPlusDao()
	{
		if (null == iDao)
		{
			try
			{
				iDao = getDao(IconPlus.class);
			}
			catch (java.sql.SQLException e)
			{
				e.printStackTrace();
			}
		}

		return iDao;
	}
	
	public RuntimeExceptionDao<IconPlus, Integer> getIconPlusRuntimeExDao()
	{
		if (null == iRuntimeDao)
		{
			iRuntimeDao = getRuntimeExceptionDao(IconPlus.class);
		}

		return iRuntimeDao;
	}
	
	public Dao<BlockedUser, Integer> getBlockedUserDao()
	{
		if (null == buDao)
		{
			try
			{
				buDao = getDao(BlockedUser.class);
			}
			catch (java.sql.SQLException e)
			{
				e.printStackTrace();
			}
		}

		return buDao;
	}

	public RuntimeExceptionDao<BlockedUser, Integer> getBlockedUserRuntimeExDao()
	{
		if (null == buRuntimeDao)
		{
			buRuntimeDao = getRuntimeExceptionDao(BlockedUser.class);
		}

		return buRuntimeDao;
	}
	
	public Dao<Channel, Integer> getChannelDao()
	{
		if (null == cDao)
		{
			try
			{
				cDao = getDao(Channel.class);
			}
			catch (java.sql.SQLException e)
			{
				e.printStackTrace();
			}
		}

		return cDao;
	}
	
	public RuntimeExceptionDao<Channel, Integer> getChannelRuntimeExDao()
	{
		if (null == cRuntimeDao)
		{
			cRuntimeDao = getRuntimeExceptionDao(Channel.class);
		}

		return cRuntimeDao;
	}
	
}