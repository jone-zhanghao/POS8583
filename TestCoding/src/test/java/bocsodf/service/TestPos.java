package bocsodf.service;

import bocsodf.until.Pos8583Until;
import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.util.TreeMap;

import java.util.Map;

public class TestPos {

    private static String packet_encoding="ASCII";
    //发送请求
    @Test
    public void sendPos(){
        try {
            //***********************组装8583报文测试--start***********************//
            TreeMap filedMap = new TreeMap();//报文域
            filedMap.put("FIELD013", "1106");//受卡方所在地日期
            filedMap.put("FIELD002", "12345678901");//主账号
            filedMap.put("FIELD003", "1799");//交易码

            filedMap.put("FIELD033", "111222333");//发送机构标识码
            filedMap.put("FIELD036", "asdzxc");//第三磁道数据
            filedMap.put("FIELD052", "asdzxc");//个人标识码数据

            byte[] send = Pos8583Until.make8583(filedMap);
            System.out.println("完成组装8583报文=="+ new String(send,packet_encoding)+"==");
            System.out.println();
            //***********************组装8583报文测试--end***********************//

            Socket socket = new Socket("localhost", 9195);
            //向服务器端发送数据
            OutputStream outputStream = socket.getOutputStream();
            //读取服务器端数据
            InputStream inputStream = socket.getInputStream();

            outputStream.write(send);
            outputStream.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, packet_encoding));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //接收请求
    @Test
    public void sendTest() {
        try {
            //***********************组装8583报文测试--start***********************//
            TreeMap filedMap=new TreeMap();//报文域
            filedMap.put("FIELD002", "12345678901");//主账号
            filedMap.put("FIELD003", "1799");//交易码
            filedMap.put("FIELD013", "1106");//受卡方所在地日期
            filedMap.put("FIELD033", "111222333");//发送机构标识码
            filedMap.put("FIELD036", "asdzxc");//第三磁道数据
            filedMap.put("FIELD052", "asdzxc");//个人标识码数据

            byte[] send=Pos8583Until.make8583(filedMap);
            System.out.println("完成组装8583报文=="+new String(send,packet_encoding)+"==");
            //***********************组装8583报文测试--end***********************//

            //***********************解析8583报文测试--start***********************//
            Map back=Pos8583Until.analyze8583(send);
            System.out.println("完成解析8583报文=="+back.toString()+"==");
            //***********************解析8583报文测试--end***********************//
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
