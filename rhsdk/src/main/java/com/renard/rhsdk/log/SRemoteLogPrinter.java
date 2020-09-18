package com.renard.rhsdk.log;

import com.renard.rhsdk.util.HttpUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SRemoteLogPrinter {

	private List<SLog> logs;
	private String url;
	private int interval = 1000; //单位 毫秒
	
	private Timer timer;
	private boolean running;
	
	public SRemoteLogPrinter(){
		
	}
	
	public SRemoteLogPrinter(String remoteUrl, int interval){
		this.logs = Collections.synchronizedList(new ArrayList<SLog>());
		this.url = remoteUrl;
		this.interval = interval;
	}
	
	public void print(SLog log){
	
		start();
		synchronized (logs) {
			logs.add(log);
		}
	}
	
	public void printImmediate(String url, SLog log){
		
		Map<String, String> params = new HashMap<String,String>();
		params.put("log", log.toJSON());
		HttpUtils.httpPost(url, params);
	}
	
	public List<SLog> getAndClear(){
		synchronized (logs) {
			List<SLog> all = new ArrayList<SLog>(logs);
			logs.clear();
			return all;
		}
	}
	
	public void start(){
		if(running){
			return;
		}
		
		running = true;
		TimerTask task = new LogPrintTask();
		timer = new Timer(true);
		timer.scheduleAtFixedRate(task, 100, interval);
	}
	
	public void stop(){
		if(timer != null){
			timer.cancel();
		}
		running = false;
	}
	
	class LogPrintTask extends TimerTask{

		@Override
		public void run() {
			try{
				
				List<SLog> logs = getAndClear();
				
				if(logs.size() > 0){
					StringBuilder sb = new StringBuilder();
					sb.append("[");
					for(SLog log : logs){
						sb.append(log.toJSON()).append(",");
					}
					sb.deleteCharAt(sb.length()-1).append("]");
					
					Map<String, String> params = new HashMap<String,String>();
					params.put("log", sb.toString());
					
					HttpUtils.httpPost(url, params);
					
				}
				
			}catch(Exception e){
				e.printStackTrace();
				stop();
			}
		}

	}
}
