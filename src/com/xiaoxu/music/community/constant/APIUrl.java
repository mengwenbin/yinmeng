package com.xiaoxu.music.community.constant;

import java.io.File;

import android.os.Environment;

public class APIUrl {
	
	public static final String cachePath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + "yinmen" + File.separator+ "cacheImage" + File.separator;
	public static final int maxMemorySize = (int) (Runtime.getRuntime().maxMemory() / 8);
	
	public static final String ACCESS_TOKEN = "4629824ad896b2061c7110f2d87656de";
	
//	public static final String BASE = "http://192.168.1.201/music_adorkable";   // 内网
	public static final String BASE = "http://www.inmoe.cn/";                   // 外网 【http://101.200.179.240】
	public static final String BASE_URL = BASE + "/api";                        //访问路径的前缀
	
	// 版本更新    统计   意见反馈
	public static final String APP_VERSION = "/version";						//api/Version
	public static final String App_TRACK = "/track/android";					//跟踪
	public static final String FEEDBACK_IDEA = "/faq/add";						// 意见反馈
	public static final String SYSTEM_INFROM = "/message/listSysAndroid";		// 系统通知
	
	public static final String SEARCH_TAG = "/settings/read";//search tag
	public static final String SEARCH_USER = "/Search/SearchUser";//歌曲搜素
	public static final String SEARCH_MUSIC = "/Search/SearchMusic";//搜用户
	
	/**
	 * 用户信息  ：登录，注册，修改用户信息，重置密码，修改密码，上传头像 */
	public static final String USER_LOGIN = "/user/login";						// 登陆
	public static final String USER_REGIST = "/user/regedit";					// 注册
	public static final String USER_INFO = "/user/show";						//用户信息读取
	public static final String USER_UPDATE = "/user/update";					// 修改
	public static final String USER_RESETPWD = "/user/ResetPasswordBymobile";	// 重置密码
	public static final String USER_UPDATE_PWD = "/user/ModifyPassword";		// 修改密码
	
	//上传
	public static final String UPLOAD_HEAD = "/user/UploadImg";					// 上传用户头像
	public static final String UPLOAD_COVER = "/MusicUser/AddMusicImg";			// 上传封面图
	public static final String UPLOAD_MUSIC = "/MusicUser/AddMusicMp3";			// 上传音乐
	public static final String RELESE_MUSIC = "/MusicUser/AddMusic";			// 发布音乐
	public static final String MUSIC_CATEGORY = "/musicUser/ShowSystemTagAndMusicCategory";			// 得到歌曲类别
	public static final String MUSIC_DETAIL = "/Music/ShowMusic";               //音乐详情
	
	//我的
    public static final String MINE_SINGLE = "/musicUser/MyMusic";              // 我的作品（歌曲）
    public static final String UPDATE_MINE_SINGLE = "/MusicUser/UpdateMusic";   // 修改我的作品（歌曲）
    public static final String DELETE_MINE_SINGLE = "/MusicUser/DelMusic";   	// 删除我的作品（歌曲）
    public static final String MINE_FAVORITE = "/favorite/ListMusic";           // 我的收藏单曲
    public static final String MINE_SONGMENU = "/favorite/ListMusicList";       // 我的收藏歌单
    public static final String DELETE_SONGMENU = "/favorite/DeleteAndList";     // 我的收藏歌单
    public static final String MINE_INVITATION = "/blog2/ListBlogThreadMy";      // 我的帖子
    public static final String DELETE_INVITATION = "/blog2/DelBlogThread";       // 删除帖子
    public static final String MINE_ATTENTION= "/Attention/ListAttention";      // 我关注的列表
    public static final String MINE_FANS= "/Attention/ListFans";                // 我的粉丝列表
    public static final String MINE_SONG= "/MusicList/MyMusicList";             // 我的歌单列表
    public static final String UPDATE_MINE_SONG= "/musicList/UpdateMusicList";  // 修改我的歌单
    public static final String DELETE_MINE_SONG= "/musicUser/PopMusicList";     // 移除我的歌单
	
	
	//音乐人
	public static final String LIST_MUSIC_MINE = "/Music/MusicByCategory";		//我的乐库歌单分类列表
	
	public static final String SEND_BARRAGE = "/Barrage/MusicBarrageAdd";		// 发送弹幕消息
	public static final String BARRAGE_LIST = "/Barrage/ListBarrage";			// 弹幕信息列表
	public static final String MUSIC_USER_RANDOM = "/user/ListMusicUserByRand";		// 音乐人随机列表
	public static final String MUSIC_USER_MOODS = "/user/ListMusicUserByFans";		// 音乐人人气列表
	public static final String MUSIC_USER_APPROVE = "/user/ListMusicUserByNewAuth";// 音乐人最新认证列表
	public static final String MUSIC_USER_INFO = "/user/showUser";				// 音乐人信息
	public static final String MUSIC_USER_MUSIC = "/musicUser/OtherUserMusic";	// 音乐人的作品（歌曲）
	
	//圈子
	public static final String CIRCLE_FIRST_CLASS = "/Category/ListBlog1";		//一级分类
	public static final String CIRCLE_SECOND_CLASS = "/Category/ListBlog2";		//二级分类
	public static final String CIRCLE_POSTS = "/blog2/ListBlogThread";			//圈子*帖子
	public static final String CIRCLE_REPLY_POSTS = "/blog2/ListBlogThreadReply";//回帖子列表
	public static final String CIRCLE_REPLY_POST = "/blog2/ReplyBlogThread";     //回帖
	public static final String CIRCLE_PRAISE_POST = "/praise/ThreadAdd";		//赞帖子
	public static final String CIRCLE_RELEASE_POST = "/blog2/AddBlogThread";		//发布帖子
	public static final String UPLOAD_POSTIMAGE = "/Upload/AddImg";				//上传帖子图片
//	public static final String DELETE_POSTIMAGE = "/Upload/Delimg";				//删除上传的帖子图片
//	public static final String POSTIMAGE_LIST = "/Upload/Listimg";				//已经上传帖子图片list
//	public static final String CIRCLE_DELETE_POST = "/blog/DelBlogThread";		//删除帖子
	
	//乐库
	public static final String MUSICLIB_CATEGORY = "/MusicList/MusicDbIndexCategory";//乐库12分类展示
	public static final String MUSICLIB_MUSIC_BYMUSICLIST = "/Music/MusicByMusicList";//按歌单提取歌曲列表
	public static final String MUSICLIB_MUSIC_BYCATEGORY = "/Music/MusicByCategory";//按分类提取歌曲列表
	
	//评论
	public static final String COMMENT_MUSIC_LIST = "/Comment/ListCommentMusic";//歌曲评论列表
	public static final String COMMENT_MUSIC = "/Comment/CommentMusic";//评论歌曲
	
	//收藏
	public static final String COLLECT_MUSIC = "/favorite/MusicAdd"; //添加收藏（音乐）
	public static final String COLLECT_SONGMENU = "/favorite/MusicListAdd"; //添加收藏歌单
	public static final String COLLECT_SONGLIST = "/favorite/ListMusic"; //收藏歌单列表
	
	//关注
	public static final String ATTENTION_PERSON = "/Attention/AttentionUser"; //关注
	public static final String ATTENTION_CANCEL = "/Attention/DelAttentionUser"; //取消关注
	public static final String ATTENTION_PERSON_LIST = "/Attention/ListAttention"; //关注的人列表
	
	//粉丝
	public static final String FANS_PERSON_LIST = "/Attention/ListFans"; //粉丝人列表
	
	//首页
	public static final String HOMEPAGE_AD = "/index/listAd"; //首页广告
	public static final String MUSICUSER_RECOMMEND_LIST = "/index/ListMusicUser"; //萌主推荐列表
	public static final String MUSIC_RECOMMEND_LIST = "/index/ListMusic"; //原唱推荐列表
	
	//推荐
	public static final String RECOMMEND_MUSIC_USER = "/user/ListMusicUserSList"; //推荐音乐人列表
	public static final String RECOMMEND_MUSIC = "/Music/MusicSListByCategory"; //推荐音乐人列表
	public static final String RECOMMEND_AD = "/res/ListIndexAdBootAll"; //推荐音乐人列表
	
	public static final String VIDEO_LIST = "/res/ListIndexVideoAll"; //视频列表
	
	//歌单
	public static final String SONG_MENU_MY = "/MusicList/MyMusicList";//我的歌单列表
	public static final String SONG_MENU_NEW = "/MusicList/AddMusicList";//新建歌单
	public static final String SONG_MENU_DELETE = "/MusicList/DelMusicList";//删除歌单
	public static final String SONG_MENU_UPDATE = "/MusicList/UpdateMusicList";//修改歌单
	public static final String SONG_MENU_ADD = "/musicUser/PushMusicList";//添加歌曲到歌单
	public static final String SONG_MENU_REMOVE = "/musicUser/PopMusicList";//从歌单移除歌曲
	public static final String SONG_MENU_MORE = "/MusicList/ListMusicList";//专题推荐-更多推荐
	
	//榜单
	public static final String SORT_LIST_HOT = "/MusicCount/MusicByCount24";//榜单 热门
	public static final String SORT_LIST_WEEK = "/MusicCount/MusicByCountYearWeek";//榜单 周榜
	public static final String SORT_LIST_MONTH = "/MusicCount/MusicByCountYearMonth";//榜单 月榜
	public static final String SORT_LIST_ALL = "/MusicCount/MusicByCountAllTime";//榜单 总榜
	
	public static final String MORE_RECOMMEND_NEW = "/Music/MusicRecommendByNew";//更多推荐 最新
	public static final String MORE_RECOMMEND_HOT = "/MusicCount/MusicRecommendByHot";//更多推荐 热门
	public static final String MORE_RECOMMEND_POPULAR = "/MusicCount/MusicRecommendByCountWeek";//更多推荐 人气
	public static final String MORE_RECOMMEND_PUBLICITY = "/MusicCount/MusicRecommendByCountShare";//更多推荐 传播
	
	//统计
	public static final String MUSIC_COUNT_PLAY = "/musicCount/countPlay";
	public static final String MUSIC_COUNT_SHARE = "/musicCount/countShare";
	
}
