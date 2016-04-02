package com.xiaoxu.music.community.model.impl;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class HttpRequestUtils {
	
	private HttpUtils httputils = new HttpUtils();
	
	private static HttpRequestUtils hru;
	
	public HttpRequestUtils(){}
	
	/**
	 * 单例请求，用于单次请求服务器的时候
	 * @return HttpRequestUtils
	 */
	public static HttpRequestUtils getIntence(){
		if(null == hru){
			hru = new HttpRequestUtils();
		}
		return hru;
	}
	
	/**
	 * 多线程请求时，调用此方法，创建多个请求对象，同步请求
	 * @return HttpRequestUtils
	 */
	public static HttpRequestUtils getNewIntence(){
		return new HttpRequestUtils();
	}
	
	public void HttpWithPost(RequestParams params, String url, RequestCallBack<String> callback){
		httputils.configSoTimeout(50000);
		httputils.configCurrentHttpCacheExpiry(1000*20);//设置当前缓存时间
		httputils.send(HttpMethod.POST, url, params, callback);
	}
}
