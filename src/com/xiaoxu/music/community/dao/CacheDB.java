package com.xiaoxu.music.community.dao;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.umeng.socialize.utils.Log;
import com.xiaoxu.music.community.entity.CategoryEntity;
import com.xiaoxu.music.community.entity.CategorysInfoEntity;
import com.xiaoxu.music.community.parser.ParseUtil;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.TimeUtil;

public class CacheDB implements DbUtils.DbUpgradeListener {
	
	public static long time_circle = 30*60*1000;
	public static long time_version = 30*60*1000;
	public static long time_track = 30*60*1000;
	public static long time_advertisement = 30*60*1000;
	public static long time_musicrecommend = 30*60*1000;
	public static long time_musicuserrecommend = 30*60*1000;
	
	public static long time_musiclib = 30*60*1000;
	
	private Context context;
	private DbUtils dbutil;
	private String cache_name;
	private String result;
	private long time;
//	private CacheCallBack callback;
	private Class classOfT;
	//read
	private CacheData cacheData = null;
	Thread th = null;
	
	public CacheDB(Context context) {
		super();
		this.context = context;
		dbutil = DbUtils.create(context, "cache.db",ActivityUtil.getOldVersion(context), this);
		dbutil.configAllowTransaction(true);
		dbutil.configDebug(true);
	}
	
//	public CacheDB(Context context, CacheCallBack callback) {
//		super();
//		this.context = context;
//		this.callback = callback;
//		dbutil = DbUtils.create(context, "cache.db",ActivityUtil.getOldVersion(context), this);
//		dbutil.configAllowTransaction(true);
//		dbutil.configDebug(true);
//	}
	
	public void cacheSavaOrUpdate(String cache_name, String result, long time){
		this.cache_name = cache_name;
		this.result = result;
		this.time = time;
		th = new Thread(write_cache);
		th.start();
	}
	
	Runnable write_cache = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//缓存到数据库
			CacheData cacheinfo = getCacheDataByName(cache_name);
			if(cacheinfo==null){
				cacheinfo = new CacheData(cache_name, result, TimeUtil.getCurrentTime()+ time);
				add(cacheinfo);
			}else{
				cacheinfo.setCache_data(result);
				cacheinfo.setCache_time(TimeUtil.getCurrentTime()+ time);
				update(cacheinfo);
			}
		}
	};
	
	/**
	 * 添加
	 * @param info
	 */
	public void add(CacheData info) {
		try {
			dbutil.save(info);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加多条数据
	 * @param infos
	 */
	public void addAll(List<CacheData> infos) {
		try {
			dbutil.saveAll(infos);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 修改数据
	 * @param info
	 */
	public void update(CacheData info) {
		try {
			String cache_name = info.getCache_name();
			String[] cul = new String[] {"cache_data", "cache_time" };
			dbutil.update(info, WhereBuilder.b("cache_name", "=", cache_name), cul);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 查询单条数据（by name）
	 * @return
	 */
	public CacheData getCacheDataByName(String name){
		try {
			CacheData in = dbutil.findFirst(Selector.from(CacheData.class).where("cache_name","=",name));
			if(in==null){
				return null;
			}
			return in;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询所有的数据
	 * @return
	 */
	public List<CacheData> getCacheList() {
		try {
			List<CacheData> infos = dbutil.findAll(CacheData.class);
			return infos;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override //版本更新
	public void onUpgrade(DbUtils dbUtils, int version1, int version2) {
//		// TODO Auto-generated method stub
//		try {
//			if (version1 < version2) { //旧的版本小于新的版本
//				dbUtils.dropTable(CacheInfo.class);
//			}
//		} catch (DbException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
//	public void setCacheCallback(CacheCallBack callback) {
//		this.callback = callback;
//	}
	
//	public interface CacheCallBack{
//		/**
//		 * @param result 缓存数据
//		 * @param isCache 是否有缓存
//		 */
//		public void cacheData(boolean isCache, Object result);
//	}
	
}
