package com.moecheng.distributedcrawler;

import com.alibaba.fastjson.JSON;
import com.moecheng.distributedcrawler.network.model.Command;
import com.moecheng.distributedcrawler.network.Handler;
import com.moecheng.distributedcrawler.network.SocketClient;
import com.moecheng.distributedcrawler.network.model.Config;
import com.moecheng.distributedcrawler.network.model.ServerNode;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by mengchenyun on 2016/11/10.
 */
public class StartSlaver {

    private SocketClient slaverClient;
    
    private ServerNode masterNode;

    private Config config;

    private StartSlaver(String ip, int port) {
        initSlaver(ip, port);
        masterNode = new ServerNode(ip, port);
        bindAsyncEvent();
        System.out.println("Slaver server start to listen...");
    }
    private static StartSlaver mContext = null;

    public static StartSlaver getInstance() {
        if(mContext == null) {
            return new StartSlaver("127.0.0.1", 9090);
        }
        return mContext;
    }

    private static Options opts = new Options();

    static {
        // 配置两个参数
        // -h --help 帮助文档
        // -f --file file参数
        opts.addOption("h", "help", false, "The command help.");
        opts.addOption("start", "start", true,
                "Start slaver server and listen on the ip:port");
    }

    /**
     * 提供程序的帮助文档
     */
    private static void printHelp(Options opts) {
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("The crawler app. Show how to use crawler app.", opts);
    }

    public static void main(String[] args) throws ParseException {
        StartSlaver startSlaver = null;
        // 解析参数
        CommandLineParser parser = new PosixParser();
        CommandLine cl = parser.parse(opts, args);
        if(cl.hasOption("h")) {
            printHelp(opts);
            return;
        }
        if(cl.hasOption("start")) {
            String optionValue = cl.getOptionValue("start");
            String ip = null;
            int port = 0;
            try {
                ip = optionValue.split(":")[0];
                port = Integer.parseInt(optionValue.split(":")[1]);
                startSlaver = new StartSlaver(ip, port);
                startSlaver.slaverClient.start();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private void initSlaver(String masterIp, int masterPort) {
        try {
            //默认绑定9090端口
            this.slaverClient = new SocketClient(masterIp, masterPort);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void bindAsyncEvent(){
        //listening slave socket
        slaverClient.setOnAsyncTaskListener(new Handler.OnAsyncTaskListener() {

            @Override
            public void onReceive(Handler handler, Command command) {
                switch (command.getType()) {
                    case Command.CMD_DISPATCH_TASK:
                        String jsonStr = command.getInfo();
                        config = JSON.parseObject(jsonStr, Config.class);
                        System.out.println("jsonStr: " + jsonStr);
                        break;
                    case Command.CMD_START:
                        String info = command.getInfo();
                        System.out.println(info);
                        StartCrawler.getInstance(config).run(masterNode.getIp());
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onClose(String slaveId) {
                System.out.println("slave closed:" + slaveId);
            }

        });

    }

    public void send(Command command) {
        slaverClient.send(command);
    }
}
