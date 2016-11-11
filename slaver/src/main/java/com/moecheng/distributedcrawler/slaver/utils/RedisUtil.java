package com.moecheng.distributedcrawler.slaver.utils;


import redis.clients.jedis.Jedis;

public class RedisUtil {
	static Jedis jedis = null;

	
	private RedisUtil(){
		
	}
	
	public static Jedis getInstance(){
		if(jedis == null){
			//			String[] connstr = PageUtil.readFile(new File("redis.conf")).split(":");
			String ip = "127.0.0.1";
//			int port = Integer.parseInt(connstr[1]);
			int port = 6379;
			jedis = new Jedis(ip, port);
		}
		return jedis;
	}
	
	public static Jedis getInstance(String ip, int port){
		if(jedis == null){
			jedis = new Jedis(ip, port);
		}
		return jedis;
	}
	
	public static void close(){
		if(jedis != null){
			jedis.disconnect();
			jedis = null;
		}
	}
}
