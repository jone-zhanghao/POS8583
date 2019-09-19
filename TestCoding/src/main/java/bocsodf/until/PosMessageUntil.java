package bocsodf.until;

import bocsodf.constant.PosCode;

import java.util.TreeMap;

/**
 *
 * ClassName: PosMessageUntil
 * Function: POS报文组装公共until
 * date: 2019-9-18 上午10:34:39
 *
 * @author ZhangHao
 */
public class PosMessageUntil {

    /**
     * @Description: 十进制转换成二进制
     * @param headerMap 报文头
     * @param message_Type_ID 报文类型标识符
     * @param filedMap 位图+8583域
     * @param packet_encoding 编码
     * @return byte[]
     */
    public static byte[] makePosMessage(TreeMap headerMap, String message_Type_ID, TreeMap filedMap,String packet_encoding){
        byte[] posMessage = null;
        try {
            //组包 ：位图 + 报文域
            byte[] send8583 = Pos8583Until.make8583(filedMap);
            System.out.println("完成组装位图 + 8583报文域==" + new String(send8583, packet_encoding) + "==");

            //组包 ：报文类型标识符
            byte[] sendTypeId = message_Type_ID.getBytes(packet_encoding);

            //处理报文头中的报文总长度(报文头46 + 报文类型标识符4 + 位图 + 报文域)
            headerMap.put("Total–Message-Length",String.valueOf(PosCode.POS_HEADER_LEN + sendTypeId.length + send8583.length));//报文总长度
            //组包 ：头
            byte[] sendHeader = PosHeaderUntil.makeHeader(headerMap);
            System.out.println("完成组装报文头==" + new String(sendHeader, packet_encoding) + "==");
            //组完整报文
            posMessage = FieldUntil.arrayApend(posMessage, sendHeader);
            posMessage = FieldUntil.arrayApend(posMessage, sendTypeId);
            posMessage = FieldUntil.arrayApend(posMessage, send8583);
            System.out.println("完成组装完整报文==" + new String(posMessage, packet_encoding) + "==");
        }catch(Exception e){
            //抛出异常
            System.out.println("Error in PosMessageUntil");
        }
        return posMessage;
    }
}
