package bocsodf.service;

import bocsodf.constant.PosCode;
import bocsodf.until.PosMessageUntil;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.Socket;
import java.util.TreeMap;

/**
 *
 * ClassName: inquiryBalanceService
 * Function: POS查询余额服务
 * date: 2019-9-18 上午10:34:39
 *
 * @author ZhangHao
 */
@Service("inquiryBalanceService")
public class inquiryBalanceService {

    private static String packet_encoding = PosCode.POS_ENCODING;

    public static void main(String[] args) {
        //组装余额查询请求报文：报文头 + 报文类型标识符 + 位图 + 报文域(8583域)

        //*报文类型标识符：请求
        String messageTypeID = "0200";
        //*位图 + 报文域(8583域)
        TreeMap filedMap = new TreeMap();//报文域
        filedMap.put("FIELD002", "12345678901");//主账号
        filedMap.put("FIELD003", "1799");//交易码
        filedMap.put("FIELD007", "");//
        filedMap.put("FIELD011", "");//
        filedMap.put("FIELD012", "");//
        filedMap.put("FIELD013", "1106");//受卡方所在地日期
        filedMap.put("FIELD014", "");//
        filedMap.put("FIELD015", "");//
        filedMap.put("FIELD018", "");//
        filedMap.put("FIELD019", "");//
        filedMap.put("FIELD022", "");//
        filedMap.put("FIELD025", "");//
        filedMap.put("FIELD026", "");//
        filedMap.put("FIELD032", "");//
        filedMap.put("FIELD033", "111222333");//发送机构标识码
        filedMap.put("FIELD035", "");//
        filedMap.put("FIELD036", "asdzxc");//第三磁道数据
        filedMap.put("FIELD037", "");//
        filedMap.put("FIELD039", "");//
        filedMap.put("FIELD041", "");//
        filedMap.put("FIELD042", "");//
        filedMap.put("FIELD043", "");//
        filedMap.put("FIELD045", "");//
        filedMap.put("FIELD048", "");//
        filedMap.put("FIELD049", "");//
        filedMap.put("FIELD052", "00101010");//个人标识码数据
        filedMap.put("FIELD053", "");//
        filedMap.put("FIELD054", "");//
        filedMap.put("FIELD056", "");//
        filedMap.put("FIELD057", "");//
        filedMap.put("FIELD060", "");//
        filedMap.put("FIELD061", "");//
        filedMap.put("FIELD062", "");//
        filedMap.put("FIELD063", "");//
        filedMap.put("FIELD100", "");//
        filedMap.put("FIELD104", "");//
        filedMap.put("FIELD117", "");//
        filedMap.put("FIELD121", "");//
        filedMap.put("FIELD122", "");//
        filedMap.put("FIELD123", "");//
        filedMap.put("FIELD128", "0010");//

        //*报文头
        TreeMap headerMap = new TreeMap();
        headerMap.put(PosCode.HEADER_LENGTH,"00101110");//报文头长度
        headerMap.put(PosCode.HEADER_FLG_VERSION,"10000010");//头标识和版本号：测试报文
        headerMap.put(PosCode.HEADER_DEST_STAT_ID,"00010344   ");//目的 ID
        headerMap.put(PosCode.HEADER_SOUR_STAT_ID,"10000010   ");//源 ID
        headerMap.put(PosCode.HEADER_RES_USE,"000000000000000000000000");//保留使用
        headerMap.put(PosCode.HEADER_BAT_NUM,"00000000");//批次号
        headerMap.put(PosCode.HEADER_TRAN_INF,"00000000");//交易信息
        headerMap.put(PosCode.HEADER_USER_INF,"00000000");//用户信息
        headerMap.put(PosCode.HEADER_REJ_CODE,"00000");//拒绝码

        try{

            byte[] send = PosMessageUntil.makePosMessage(headerMap,messageTypeID,filedMap,packet_encoding);

            //----------------test start---------------
//            byte[] abc = new String(send, 50, 67 ,"UTF-8").getBytes("UTF-8");
//            byte[] abc = FieldUntil.subBytes(send,50,67);
//            Map back = Pos8583Until.analyze8583(abc);
//            System.out.println("完成解析8583报文==" + back.toString() + "==");
            //----------------test end---------------

            //发送消息
            Socket socket = new Socket("127.0.0.1", 9195);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(send);
            outputStream.flush();

            //接收服务器返回消息
            socket.setSoTimeout(1000);//设置超时时间
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, packet_encoding));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
