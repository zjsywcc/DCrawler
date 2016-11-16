package com.moecheng.distributedcrawler;

import com.moecheng.distributedcrawler.network.Handler;
import com.moecheng.distributedcrawler.network.SocketServer;
import com.moecheng.distributedcrawler.network.model.Command;
import com.moecheng.distributedcrawler.utils.FileReaderUtil;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by mengchenyun on 2016/11/10.
 */
public class StartMaster {

    private SocketServer masterServer;

    private int status = -1;

    private StartMaster() {
        initMaster();
        bindAsyncEvent();
        System.out.println("Master server start...");
    }

    private static Options opts = new Options();

    static {
        // 配置两个参数
        // -h --help 帮助文档
        // -f --file file参数
        opts.addOption("h", "help", false, "The command help.");
        opts.addOption("start", "start", false,
                "Start master server.");
        opts.addOption("d", "dispatch", false, "Dispatch configuration to all slavers.");
        opts.addOption("l", "list", false, "List all the slavers.");
        opts.addOption("r", "run", false, "Trigger all the slavers to start running.");
    }

    /**
     * 提供程序的帮助文档
     */
    private static void printHelp(Options opts) {
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("The crawler app. Show how to use crawler app.", opts);
    }

    public static void main(String[] args) throws ParseException {
        StartMaster startMaster = null;
        // 解析参数
        CommandLineParser parser = new PosixParser();
        CommandLine cl = parser.parse(opts, args);
        if(cl.hasOption("h")) {
            printHelp(opts);
            return;
        }
        if(cl.hasOption("start")) {
            startMaster = new StartMaster();
            startMaster.masterServer.start();
            startMaster.status = 0;
            System.out.print("请输入命令:");
            while (true) {
                try {
                    char c = (char)System.in.read();
                    if(c == 'r') {
                        System.out.println("开始运行...");
                        if(startMaster == null) {
                            System.out.println("Please start server first.");
                        } else {
                            startMaster.dispatchTask();
        //                    startMaster.status = 1;
                            startMaster.startCrawling();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        if(cl.hasOption("d")) {
//            if(startMaster == null) {
//                System.out.println("Please start server first.");
//            } else {
//                startMaster.dispatchTask();
//                startMaster.status = 1;
//            }
//        }
//        if(cl.hasOption("l")) {
//            if(startMaster == null) {
//                System.out.println("Please start server first.");
//            } else {
//                int i = 0;
//                for(ServerNode serverNode : Config.getInstance().getSlavers()) {
//                    System.out.println(String.format("Slaver %s: ip: %s port: %s", i++, serverNode.getIp(), serverNode.getPort()));
//                }
//            }
//        }
//        if(cl.hasOption("r")) {
//            if(startMaster == null) {
//                System.out.println("Please start server first.");
//            } else if(startMaster.status == 0) {
//                System.out.println("You should dispatch the configuration to all slavers.");
//            } else {
//                startMaster.startCrawling();
//            }
//        }
    }

    private void initMaster() {
        try {
            //默认绑定9090端口
            this.masterServer = new SocketServer(9090);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void bindAsyncEvent(){
        //listening slave socket
        masterServer.setOnAsyncTaskListener(new Handler.OnAsyncTaskListener() {

            @Override
            public void onAccept(String slaveId, Handler handler) {
                System.out.println(handler.getInetAddress());
            }

            @Override
            public void onReceive(Handler handler, Command command) {
                switch (command.getType()) {
                    case Command.CMD_MSG:
                        System.out.println(command.getInfo());
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

    private void dispatchTask() {
        String configJsonStr = FileReaderUtil.readFile("config.txt");
        masterServer.sendAll(new Command(Command.CMD_DISPATCH_TASK, configJsonStr));
    }

    private void startCrawling() {
        masterServer.sendAll(new Command(Command.CMD_START, "Start crawling"));
    }
}
