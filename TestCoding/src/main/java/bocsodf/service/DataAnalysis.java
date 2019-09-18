package bocsodf.service;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

import bocsodf.constant.PosCode;
import bocsodf.until.FieldUntil;
import bocsodf.until.Pos8583Until;
import bocsodf.until.PosHeaderUntil;
import org.springframework.stereotype.Service;

/**
 *
 * ClassName: DataAnalysis
 * Function: 解析POS请求数据包
 * date: 2019-9-18 上午10:34:39
 *
 * @author ZhangHao
 */
@Service
public class DataAnalysis {
    /**
     * 解析数据包类
     * @param buffer
     * @param socket
     */
    public void Analysis(byte[] buffer, Socket socket) {
        try {
            System.out.println("收到线程发送的消息");
            String input = new String (buffer, PosCode.POS_ENCODING);
            System.out.println("请求消息:" + input);

            //检查请求报文信息是否正常
            //* 长度是否在46 到 1892(1846＋46)区间
            if (buffer.length < 46 || buffer.length > 1892){
                System.out.println("请求消息长度错误");
                //抛出异常
            }

            //------------------------------------------------------------
            // 解析请求报文(报文头 + 报文类型标识符 + 位图 + 报文域)
            //正确报文：报文头 + 报文类型标识符 + 位图 + 报文域)
            //错误报文：新增报文头 + (原始报文头 + 报文类型标识符 + 位图 + 报文域)
            //------------------------------------------------------------
            //*解析报文头
            Map header = PosHeaderUntil.analyzeHeader(buffer);
            System.out.println("完成解析报文头==" + header.toString() + "==");

            //*解析报文类型标识符
            int startNum;
            int headerLength = FieldUntil.binaryToDecimal((String) header.get(PosCode.HEADER_LENGTH));
            if ("00000".equals(header.get(PosCode.HEADER_REJ_CODE))){//拒绝码
                startNum = headerLength;//报文头长度
            }else{
                startNum = 2 * headerLength;//2个报文头长度
            }
            String messageTypeID = new String(buffer, startNum, 4, PosCode.POS_ENCODING);
            System.out.println("完成解析报文类型标识符==" + messageTypeID + "==");

            //*解析位图 + 报文域
            startNum += 4;
            int totLength = Integer.parseInt((String) header.get(PosCode.HEADER_TOT_MESS_LEN)) - startNum;
            Map back = Pos8583Until.analyze8583(FieldUntil.subBytes(buffer,startNum,totLength));
            System.out.println("完成解析8583报文==" + back.toString() + "==");

            //处理请求
            //Todo 调用其他服务、记流水都毛问题

            //组织返回报文
            TreeMap filedMap=new TreeMap();//报文域
            filedMap.put("FIELD003", "0000");
            filedMap.put("FIELD033", "交易正常");
            byte[] out = Pos8583Until.make8583(filedMap);

            //返回输出报文

            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(out);
            outputStream.flush();

            socket.close();
        } catch (IOException e) {
            //抛出异常
            e.printStackTrace();
        }
    }
}
