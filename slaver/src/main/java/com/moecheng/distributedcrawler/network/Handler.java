package com.moecheng.distributedcrawler.network;


import com.moecheng.distributedcrawler.network.model.Command;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 每一个handler对应一个slave
 * @author yuanyun
 *
 */
public class Handler implements Runnable{

	private Socket socket = null;

	private InetAddress inetAddress = null;
	
	/**
	 * 从机在SocketThreadPool中的key
	 */
	private String serverId = null;

	private OnAsyncTaskListener onAsyncTaskListener = null;

	public Handler(Socket socket) {
		this.socket = socket;
		this.inetAddress = socket.getInetAddress();
		this.serverId = inetAddress.getHostAddress() + ":" + socket.getPort();
	}
	
	public Socket getSocket(){
		return this.socket;
	}
	
	public InetAddress getInetAddress() {
		return inetAddress;
	}

	public String getServerId() {
		return serverId;
	}

	/**
	 * 设置监听器，用来接受命令
	 * @param onAsyncTaskListener
	 */
	public void setOnAsyncTaskListener(OnAsyncTaskListener onAsyncTaskListener){
		this.onAsyncTaskListener = onAsyncTaskListener;
	}
	
	/**
	 * 发送数据
	 * 
	 * @param command
	 * @return
	 */
	public boolean send(Command command) {
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(
					new BufferedOutputStream(socket.getOutputStream()));

			outputStream.writeObject(command);
			outputStream.flush();

			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void run() {
		try {
			ObjectInputStream inputStream = new ObjectInputStream(
					socket.getInputStream());

			Object obj = null;
			
			while ((obj = inputStream.readObject()) != null) {
				Command command = (Command) obj;
				if (onAsyncTaskListener != null) {
					onAsyncTaskListener.onReceive(Handler.this, command);
				}
				
				//解决eof异常
				try{
					inputStream = new ObjectInputStream(socket.getInputStream());
				}catch(EOFException e){
					continue;
				}
			}
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally{
			if(socket != null){
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		onAsyncTaskListener.onClose(serverId);
	}

	public interface OnAsyncTaskListener {

		/**
		 * 接收到命令的时候调用
		 * 
		 * @param command
		 */
		public void onReceive(Handler handler, Command command);
		
		public void onClose(String serverId);
	}
}
