package com.xiaoxu.music.community.dao;

import java.util.List;

import android.content.Context;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.xiaoxu.music.community.util.ActivityUtil;

public class MusicDB implements DbUtils.DbUpgradeListener {

	private Context context;
	private DbUtils dbutil;
	
	public MusicDB(Context context) {
		super();
		this.context = context;
		dbutil = DbUtils.create(context, "music.db",ActivityUtil.getOldVersion(context), this);
		dbutil.configAllowTransaction(true);
		dbutil.configDebug(true);
	}
	
	/**
	 * 添加
	 * @param info
	 */
	public void add(DownLoadInfo info) {
		try {
			dbutil.save(info);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 添加多条数据
	 * 
	 * @param infos
	 */
	public void addAll(List<DownLoadInfo> infos) {
		try {
			dbutil.saveAll(infos);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 删除单条数据
	 * 
	 * @param music_id
	 */
	public void deleteByMd5(String music_md5) {
		try {
			dbutil.delete(DownLoadInfo.class,
					WhereBuilder.b("music_md5", "=", music_md5));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 删除多条数据
	 * 
	 * @param music_id
	 */
	public void deleteAll(List<DownLoadInfo> list) {
		try {
			dbutil.deleteAll(list);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 修改数据
	 * 
	 * @param info
	 */
	public void updateByMd5(DownLoadInfo info) {
		try {
			String music_id = info.getMusic_id();
			String[] cul = new String[] { "isfinish", "music_path" };
			dbutil.update(info, WhereBuilder.b("music_md5", "=", music_id),
					cul);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询单条数据(已经下载完毕)
	 * @param music_id
	 * @return
	 */
	public DownLoadInfo getMusicInfoByMd5(String music_md5) {
		try {
			DownLoadInfo in = dbutil.findFirst(Selector
					.from(DownLoadInfo.class)
					.where("music_md5", "=", music_md5));
			if (in == null || !in.isIsfinish()) {
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
	 * 查询单条数据
	 * @param music_md5
	 * @return
	 */
	public DownLoadInfo getMusicInfoByMd52(String music_md5) {
		try {
			DownLoadInfo in = dbutil.findFirst(Selector
					.from(DownLoadInfo.class)
					.where("music_md5", "=", music_md5));
			if (in == null) {
				return null;
			}
			return in;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public DownLoadInfo getMusicInfoByUrl(String music_url) {
		try {
			DownLoadInfo in = dbutil.findFirst(Selector
					.from(DownLoadInfo.class)
					.where("music_url", "=", music_url));
			if (in == null || !in.isIsfinish()) {
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
	 * 
	 * @return
	 */
	public List<DownLoadInfo> getMusicList() {
		try {
			List<DownLoadInfo> infos = dbutil.findAll(DownLoadInfo.class);
			return infos;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<DownLoadInfo> getMusicListIsDownLoad() {
		try {
			List<DownLoadInfo> infos = dbutil.findAll(Selector.from(
					DownLoadInfo.class).where("isfinish", "=", true));
			return infos;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// 数据库升级
	@Override
	public void onUpgrade(DbUtils dbUtils, int version1, int version2) {
		// TODO Auto-generated method stub
//		try {
//			if (version1 < version2) { // 旧的版本小于新的版本
//				dbUtils.dropTable(DownLoadInfo.class);
//			}
//		} catch (DbException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}
