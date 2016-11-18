package com.moecheng.distributedcrawler;

import com.moecheng.distributedcrawler.network.Handler;
import com.moecheng.distributedcrawler.network.SocketServer;
import com.moecheng.distributedcrawler.network.model.Command;
import com.moecheng.distributedcrawler.network.model.ServerNode;
import com.moecheng.distributedcrawler.utils.FileReaderUtil;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by mengchenyun on 2016/11/10.
 */
public class StartMaster {

    private SocketServer masterServer;

    private String configStr;

    private Map<String, ServerNode> slavers;

    private StartMaster() {
        initMaster();
        slavers = new HashMap<>();
        bindAsyncEvent();
        System.out.println("Master server start...");
    }

    private static Options opts = new Options();

    static {
        // 配置两个参数
        // -h --help 帮助文档
        // -f --file file参数
        opts.addOption("h", "help", false, "The command help.");
        opts.addOption("c", "config", true, "read local configuration.");
    }

    /**
     * 提供程序的帮助文档
     */
    private static void printHelp(Options opts) {
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("The crawler app. Show how to use crawler app.", opts);
    }

    public static void main(String[] args) throws ParseException {
        StartMaster startMaster = new StartMaster();
        startMaster.masterServer.start();
        System.out.print("请输入命令:");
        // 解析参数
        CommandLineParser parser = new PosixParser();
        CommandLine cl = parser.parse(opts, args);
        if(cl.hasOption("h")) {
            printHelp(opts);
            return;
        }
        if(cl.hasOption("c")) {
            String optionValue = cl.getOptionValue("c");
            startMaster.configStr = FileReaderUtil.readFile(optionValue);
        }
        Scanner sc = new Scanner(System.in);

        for (prompt(); sc.hasNextLine(); prompt()) {

            String line = sc.nextLine().replaceAll("\n", "");

            // return pressed
            if (line.length() == 0)
                continue;

            // split line into arguments
            String[] params = line.split(" ");

            // process arguments
            if (params.length == 1) {
                if (params[0].equalsIgnoreCase("exit"))
                    System.exit(0);
                if (params[0].equalsIgnoreCase("list")) {
                    int i = 0;
                    if (!startMaster.slavers.isEmpty()) {
                        for(Map.Entry<String, ServerNode> entry : startMaster.slavers.entrySet()) {
                            System.out.println(String.format("Slaver %s: ip: %s port: %s", i++, entry.getValue().getIp(), entry.getValue().getPort()));
                        }
                    } else {
                        System.out.println("Slaver list is empty.");
                    }
                }
                if (params[0].equalsIgnoreCase("run")) {
                    startMaster.dispatchTask(startMaster.configStr);
                    startMaster.startCrawling();
                }
                if(params[0].equalsIgnoreCase("msg")) {
                    startMaster.sendMsg("just to test...");
                }
                if(params[0].equalsIgnoreCase("stop")) {
                    startMaster.stopCrawling();
                }
            } else if (params.length == 2){
                if(params[0].equalsIgnoreCase("stop")) {
                    String slaverId = params[1];
                    startMaster.stopCrawling(slaverId);
                    System.out.println("Stop " + slaverId + "...");
                }
                if(params[0].equalsIgnoreCase("read")) {
                    String config = params[1];
                    startMaster.configStr = FileReaderUtil.readFile(config);
                    startMaster.stopCrawling();
                }
            }
        }
    }

    public static void prompt() {
        System.out.print("127.0.0.1:9090 > ");
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
                String ip = handler.getSocket().getInetAddress().getHostAddress();
                int port = handler.getSocket().getPort();
                String key = ip + ":" + port;
                slavers.put(key, new ServerNode(ip, port));

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
                masterServer.removeSlave(slaveId);
                slavers.remove(slaveId);
            }

        });

    }

    private void dispatchTask(String configStr) {
        masterServer.sendAll(new Command(Command.CMD_DISPATCH_TASK, configStr));
    }

    private void startCrawling() {
        masterServer.sendAll(new Command(Command.CMD_START, "Start crawling"));
    }

    private void stopCrawling(String slaverId) {
        masterServer.send(slaverId, new Command(Command.CMD_STOP, "Stop crawling for " + slaverId));
    }

    private void stopCrawling() {
        masterServer.sendAll(new Command(Command.CMD_STOP, "Stop crawling for all..."));
    }

    private void sendMsg(String info) {
        masterServer.sendAll(new Command(Command.CMD_MSG, info));
    }

}
