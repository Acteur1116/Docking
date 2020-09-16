package com.renard.rhsdk.log;

public interface LogI {

	public void d(String tag, String msg);
	public void i(String tag, String msg);
	public void w(String tag, String msg);
	public void w(String tag, String msg, Throwable e);
	public void e(String tag, String msg);
	public void e(String tag, String msg, Throwable e);
	
	public void destory();
}
