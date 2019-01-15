package com.thinkgem.jeesite.test;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author mh
 * @create 2018-11-29 15:51
 */
public class SocketServer {

    public static void main(String[] agrs){
        try{
            // 1.创建一个服务器socket
            ServerSocket serverSocket = new ServerSocket(5000);
            // 2.使用accept（）等待客户的通信
            Socket socket = serverSocket.accept();
            // 3.获得输入流，获得相应的用户请求
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader((is)));
            String info;
            while((info=br.readLine())!=null){
                System.out.println("客户端说："+info);
            }

            // 给客户端一个响应
            String reply="欢迎访问！";
            byte[] replys=reply.getBytes();
            OutputStream os = socket.getOutputStream();
            os.write(replys);

            // 释放相应资源
            os.close();
            br.close();
            is.close();
            socket.close();

            System.out.println("服务端运行结束-->");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
