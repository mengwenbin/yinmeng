package com.xiaoxu.music.community.dao;

import java.util.List;
import android.content.Context;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.util.ActivityUtil;

public class DownLoadDB implements DbUtils.DbUpgradeListener {
	
	private Context context;
	private DbUtils dbutil;
	
	public DownLoadDB(Context context) {
		super();
		this.context = context;
		dbutil = DbUtils.create(context, "download_music.db",ActivityUtil.getOldVersion(context), this);
		dbutil.configAllowTransaction(true);
		dbutil.configDebug(true);
	}
	
	/**
	 * 添加
	 * @param info
	 */
	public void add(SongInfoEntity info) {
		try {
			dbutil.save(info);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加多条数据
	 * @param infos
	 */
	public void addAll(List<SongInfoEntity> infos) {
		try {
			dbutil.saveAll(infos);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 删除单条数据
	 * @param music_id
	 */
	public void deleteById(String music_id) {
		try {
			dbutil.delete(SongInfoEntity.class,WhereBuilder.b("music_id", "=", music_id));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除多条数据
	 * @param music_id
	 */
	public void deleteAll(List<SongInfoEntity> list) {
		try {
			dbutil.deleteAll(list);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 修改数据
	 * @param info
	 */
	public void updateById(SongInfoEntity info) {
		try {
			String music_id = info.getMusic_id();
			String[] cul = new String[] { "isfinish", "savePath" };
			dbutil.update(info, WhereBuilder.b("music_id", "=", music_id),cul);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 修改数据
	 * @param info
	 */
	public void updateFavorite(SongInfoEntity info) {
		try {
			String music_id = info.getMusic_id();
			String[] cul = new String[] { "favorite"};
			dbutil.update(info, WhereBuilder.b("music_id", "=", music_id),cul);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询单条数据(已经下载完毕)
	 * @param music_id
	 * @return
	 */
	public SongInfoEntity getMusicInfoById(String music_id) {
		try {
			SongInfoEntity in = dbutil.findFirst(Selector.from(SongInfoEntity.class).where("music_id", "=", music_id));
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
	public SongInfoEntity getMusicInfoById2(String music_id) {
		try {
			SongInfoEntity in = dbutil.findFirst(Selector.from(SongInfoEntity.class).where("music_id", "=", music_id));
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
	
	public SongInfoEntity getMusicInfoByUrl(String music_url) {
		try {
			SongInfoEntity in = dbutil.findFirst(Selector.from(SongInfoEntity.class).where("music_url", "=", music_url));
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
	 * @return
	 */
	public List<SongInfoEntity> getMusicList() {
		try {
			List<SongInfoEntity> infos = dbutil.findAll(SongInfoEntity.class);
			return infos;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询所有的下载完的数据
	 * @return
	 */
	public List<SongInfoEntity> getMusicListIsDownLoad() {
		try {
			List<SongInfoEntity> infos = dbutil.findAll(Selector.from(SongInfoEntity.class).where("isfinish", "=", true));
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
