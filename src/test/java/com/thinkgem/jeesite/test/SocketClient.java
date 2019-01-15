package com.thinkgem.jeesite.test;


import com.drew.lang.BufferReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author mh
 * @create 2018-11-29 16:08
 */
public class SocketClient {


    public static void main(String[] args){
        try{
            // 1.创建一个客户端socket
            Socket socket = new Socket("localhost",5000);
            // 2.通过输出流发送请求
            OutputStream os = socket.getOutputStream();
            String info = "用户名：tom；密码：123456";

            byte[] infoByte = info.getBytes();
            os.write(infoByte);

            socket.shutdownOutput();
            //通过输入流来接收到服务器给客户端的响应
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String reply;
            while ((reply=br.readLine())!= null){
                System.out.println("服务器的响应："+reply);
            }

            // 3.释放资源
            os.close();
            socket.close();

            System.out.println("客户端发送结束-->");

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
