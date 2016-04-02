package com.xiaoxu.music.community.entity;

import java.io.Serializable;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.db.annotation.Unique;

@Table(name = "music")
public class SongInfoEntity implements Serializable {
	@Id(column = "id")
	private int id;
	private String music_id;
	private String user_id;
	private String music_name;
	private String music_category;
	private String music_coverimg;
	private String music_url;
	@Unique
	private String music_md5;
	private String music_summary;
	private String music_lyric;
	private String music_lyric_url;
	private String music_lengthtime;
	private String music_size;
	private String music_singing;
	private String music_singing0;
	private String music_composer;
	private String music_lyricser;
	private String music_arranger;
	private String music_mixed;
	private String music_tag_int;
	private String c_view;
	private String c_favorite;
	private String c_praise;
	private String c_download;
	private String c_share;
	private String c_play;
	private String is_xx;
	private String favorite;
	private String time_create;
	private String is_timelyric;
	
	@Transient
	private SimpleUserEntity user; //不写入表结构
	
	private String user_nick;
	private String user_sex;
	private String user_img;
	private String is_attention;
	private String user_summary;
	
	private String share_url;
	
	
	public String getIs_timelyric() {
		return is_timelyric;
	}

	public void setIs_timelyric(String is_timelyric) {
		this.is_timelyric = is_timelyric;
	}

	public String getUser_summary() {
		return user_summary;
	}

	public void setUser_summary(String user_summary) {
		this.user_summary = user_summary;
	}

	public String getC_play() {
		return c_play;
	}

	public void setC_play(String c_play) {
		this.c_play = c_play;
	}

	public String getUser_nick() {
		return user_nick;
	}

	public void setUser_nick(String user_nick) {
		this.user_nick = user_nick;
	}

	public String getUser_sex() {
		return user_sex;
	}

	public void setUser_sex(String user_sex) {
		this.user_sex = user_sex;
	}

	public String getUser_img() {
		return user_img;
	}

	public void setUser_img(String user_img) {
		this.user_img = user_img;
	}

	public String getIs_attention() {
		return is_attention;
	}

	public void setIs_attention(String is_attention) {
		this.is_attention = is_attention;
	}

	@Transient
	private boolean checked = false;
	
	private String savePath;//保存路径
	private boolean isfinish;//是否下载完毕
	
	
	public SongInfoEntity() {
		super();
	}

	public SongInfoEntity(String music_id) {
		super();
		this.music_id = music_id;
	}
	
	public SongInfoEntity(int id, String music_id, String user_id,
			String music_name, String music_category, String music_coverimg,
			String music_url, String music_md5, String music_summary,
			String music_lyric, String music_lyric_url,
			String music_lengthtime, String music_size, String music_singing,
			String music_singing0, String music_composer,
			String music_lyricser, String music_arranger, String music_mixed,
			String music_tag_str, String c_view, String c_favorite,
			String c_praise, String c_download, String c_share, String c_play,
			String is_xx, String favorite, String time_create,
			SimpleUserEntity user, String user_nick, String user_sex,
			String user_img, String is_attention, boolean checked,
			String savePath, boolean isfinish) {
		super();
		this.id = id;
		this.music_id = music_id;
		this.user_id = user_id;
		this.music_name = music_name;
		this.music_category = music_category;
		this.music_coverimg = music_coverimg;
		this.music_url = music_url;
		this.music_md5 = music_md5;
		this.music_summary = music_summary;
		this.music_lyric = music_lyric;
		this.music_lyric_url = music_lyric_url;
		this.music_lengthtime = music_lengthtime;
		this.music_size = music_size;
		this.music_singing = music_singing;
		this.music_singing0 = music_singing0;
		this.music_composer = music_composer;
		this.music_lyricser = music_lyricser;
		this.music_arranger = music_arranger;
		this.music_mixed = music_mixed;
		this.music_tag_int = music_tag_int;
		this.c_view = c_view;
		this.c_favorite = c_favorite;
		this.c_praise = c_praise;
		this.c_download = c_download;
		this.c_share = c_share;
		this.c_play = c_play;
		this.is_xx = is_xx;
		this.favorite = favorite;
		this.time_create = time_create;
		this.user = user;
		this.user_nick = user_nick;
		this.user_sex = user_sex;
		this.user_img = user_img;
		this.is_attention = is_attention;
		this.checked = checked;
		this.savePath = savePath;
		this.isfinish = isfinish;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMusic_id() {
		return music_id;
	}

	public void setMusic_id(String music_id) {
		this.music_id = music_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getMusic_name() {
		return music_name;
	}

	public void setMusic_name(String music_name) {
		this.music_name = music_name;
	}

	public String getMusic_category() {
		return music_category;
	}

	public void setMusic_category(String music_category) {
		this.music_category = music_category;
	}

	public String getMusic_coverimg() {
		return music_coverimg;
	}

	public void setMusic_coverimg(String music_coverimg) {
		this.music_coverimg = music_coverimg;
	}

	public String getMusic_url() {
		return music_url;
	}

	public void setMusic_url(String music_url) {
		this.music_url = music_url;
	}

	public String getMusic_md5() {
		return music_md5;
	}

	public void setMusic_md5(String music_md5) {
		this.music_md5 = music_md5;
	}

	public String getMusic_summary() {
		return music_summary;
	}

	public void setMusic_summary(String music_summary) {
		this.music_summary = music_summary;
	}

	public String getMusic_lyric() {
		return music_lyric;
	}

	public void setMusic_lyric(String music_lyric) {
		this.music_lyric = music_lyric;
	}

	public String getMusic_lyric_url() {
		return music_lyric_url;
	}

	public void setMusic_lyric_url(String music_lyric_url) {
		this.music_lyric_url = music_lyric_url;
	}

	public String getMusic_lengthtime() {
		return music_lengthtime;
	}

	public void setMusic_lengthtime(String music_lengthtime) {
		this.music_lengthtime = music_lengthtime;
	}

	public String getMusic_size() {
		return music_size;
	}

	public void setMusic_size(String music_size) {
		this.music_size = music_size;
	}

	public String getMusic_singing() {
		return music_singing;
	}

	public void setMusic_singing(String music_singing) {
		this.music_singing = music_singing;
	}

	public String getMusic_singing0() {
		return music_singing0;
	}

	public void setMusic_singing0(String music_singing0) {
		this.music_singing0 = music_singing0;
	}

	public String getMusic_composer() {
		return music_composer;
	}

	public void setMusic_composer(String music_composer) {
		this.music_composer = music_composer;
	}

	public String getMusic_lyricser() {
		return music_lyricser;
	}

	public void setMusic_lyricser(String music_lyricser) {
		this.music_lyricser = music_lyricser;
	}

	public String getMusic_arranger() {
		return music_arranger;
	}

	public void setMusic_arranger(String music_arranger) {
		this.music_arranger = music_arranger;
	}

	public String getMusic_mixed() {
		return music_mixed;
	}

	public void setMusic_mixed(String music_mixed) {
		this.music_mixed = music_mixed;
	}

	public String getMusic_tag_int() {
		return music_tag_int;
	}

	public void setMusic_tag_int(String music_tag_int) {
		this.music_tag_int = music_tag_int;
	}

	public String getC_view() {
		return c_view;
	}

	public void setC_view(String c_view) {
		this.c_view = c_view;
	}

	public String getC_favorite() {
		return c_favorite;
	}

	public void setC_favorite(String c_favorite) {
		this.c_favorite = c_favorite;
	}

	public String getC_praise() {
		return c_praise;
	}

	public void setC_praise(String c_praise) {
		this.c_praise = c_praise;
	}

	public String getC_download() {
		return c_download;
	}

	public void setC_download(String c_download) {
		this.c_download = c_download;
	}

	public String getC_share() {
		return c_share;
	}

	public void setC_share(String c_share) {
		this.c_share = c_share;
	}

	public String getIs_xx() {
		return is_xx;
	}

	public void setIs_xx(String is_xx) {
		this.is_xx = is_xx;
	}

	public String getFavorite() {
		return favorite;
	}

	public void setFavorite(String favorite) {
		this.favorite = favorite;
	}

	public String getTime_create() {
		return time_create;
	}

	public void setTime_create(String time_create) {
		this.time_create = time_create;
	}

	public SimpleUserEntity getUser() {
		return user;
	}

	public void setUser(SimpleUserEntity user) {
		this.user = user;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public boolean isIsfinish() {
		return isfinish;
	}

	public void setIsfinish(boolean isfinish) {
		this.isfinish = isfinish;
	}

	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getShare_url() {
		return share_url;
	}

	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}

	@Override
	public String toString() {
		return "SongInfoEntity [id=" + id + ", music_id=" + music_id
				+ ", user_id=" + user_id + ", music_name=" + music_name
				+ ", music_category=" + music_category + ", music_coverimg="
				+ music_coverimg + ", music_url=" + music_url + ", music_md5="
				+ music_md5 + ", music_summary=" + music_summary
				+ ", music_lyric=" + music_lyric + ", music_lyric_url="
				+ music_lyric_url + ", music_lengthtime=" + music_lengthtime
				+ ", music_size=" + music_size + ", music_singing="
				+ music_singing + ", music_singing0=" + music_singing0
				+ ", music_composer=" + music_composer + ", music_lyricser="
				+ music_lyricser + ", music_arranger=" + music_arranger
				+ ", music_mixed=" + music_mixed + ", music_tag_str="
				+ music_tag_int + ", c_view=" + c_view + ", c_favorite="
				+ c_favorite + ", c_praise=" + c_praise + ", c_download="
				+ c_download + ", c_share=" + c_share + ", c_play=" + c_play
				+ ", is_xx=" + is_xx + ", favorite=" + favorite
				+ ", time_create=" + time_create + ", is_timelyric="
				+ is_timelyric + ", user=" + user + ", user_nick=" + user_nick
				+ ", user_sex=" + user_sex + ", user_img=" + user_img
				+ ", is_attention=" + is_attention + ", user_summary="
				+ user_summary + ", share_url=" + share_url + ", checked="
				+ checked + ", savePath=" + savePath + ", isfinish=" + isfinish
				+ "]";
	}

}
