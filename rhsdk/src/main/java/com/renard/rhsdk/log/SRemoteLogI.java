package com.renard.rhsdk.log;

/**
 * 
 * 远程日志实现
 *
 */
public class SRemoteLogI implements LogI {
	
	private SRemoteLogPrinter printer;
	
	public SRemoteLogI(String url, int interval){
		printer = new SRemoteLogPrinter(url, interval);
	}
	
	@Override
	public void d(String tag, String msg) {
		printer.print(new SLog(SLog.L_DEBUG, tag, msg));
	}

	@Override
	public void i(String tag, String msg) {
		printer.print(new SLog(SLog.L_INFO, tag, msg));
	}

	@Override
	public void w(String tag, String msg) {
		printer.print(new SLog(SLog.L_WARN, tag, msg));
	}

	@Override
	public void w(String tag, String msg, Throwable e) {
		printer.print(new SLog(SLog.L_WARN, tag, msg, e));
	}

	@Override
	public void e(String tag, String msg) {
		printer.print(new SLog(SLog.L_ERROR, tag, msg));
	}

	@Override
	public void e(String tag, String msg, Throwable e) {
		printer.print(new SLog(SLog.L_ERROR, tag, msg, e));
	}

	@Override
	public void destory() {
		printer.stop();
	}

}
