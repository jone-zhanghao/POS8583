package bocsodf.until;

import bocsodf.constant.PosCode;
import bocsodf.message.HeaderInfo;
import bocsodf.message.PosHeader;
import bocsodf.message.PosTypeEnum;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * ClassName: PosHeaderUntil
 * Function: POS报文头组装公共until
 * date: 2019-9-18 上午10:34:39
 *
 * @author ZhangHao
 */
public class PosHeaderUntil {
    // 报文编码
    private static String packet_encoding = PosCode.POS_ENCODING;
    // 8583报文128域定义器
    private static TreeMap mapHeaderDefinition = null;

    static{
        //获取pos中8583协议使用域定义
        PosHeaderUntil.mapHeaderDefinition = new PosHeader().getMap();
    }

    /**
     * 组装pos报文头
     * @param filedMap 输入
     * @return
     */
    public static byte[] makeHeader(TreeMap filedMap){
        //初始化输出
        byte[] sendHeader = null;
        StringBuffer header = new StringBuffer();
        if(filedMap != null && mapHeaderDefinition != null){
            //循环处理报文头10个域
            Iterator it = mapHeaderDefinition.keySet().iterator();
            for(;it.hasNext();) {
                String fieldName = (String) it.next();//例如HEADER001
                HeaderInfo headerInfo = (HeaderInfo) mapHeaderDefinition.get(fieldName);//字段值
                try {
                    //解析配置
                    String domainName = headerInfo.getDomainName();
                    PosTypeEnum typeEnum = headerInfo.getType();
                    int length = headerInfo.getLength();
                    //获取传入的值
                    String fieldValue = (String) filedMap.get(domainName);
                    if (fieldValue == null) {
                        //抛出异常
                        System.out.println("error:[" + domainName + "]报文头必输项未输入!");
                        return null;
                    }
                    //将域值编码转换，保证报文编码统一
                    fieldValue = new String(fieldValue.getBytes(packet_encoding), packet_encoding);
                    //字段值得实际长度
                    int fieldLen = fieldValue.getBytes(packet_encoding).length;
                    if (fieldLen > length) {
                        //抛出异常
                        System.out.println("error:字段[" + domainName + "]的数据定义长度为" + fieldLen + "位,长度不能超过"+ length);
                        return null;
                    }
                    switch (typeEnum) {
                        case STRING:
                            fieldValue = FieldUntil.getFixFieldValue(fieldValue,length,packet_encoding,String.class);
                            break;
                        case INTEGER:
                            fieldValue = FieldUntil.getFixFieldValue(fieldValue,length,packet_encoding,Integer.class);
                            break;
                        case BINARY:
                            fieldValue = new String(FieldUntil.get16BitByteFromStr(fieldValue,packet_encoding),packet_encoding);
                            break;
                        default:
                            //抛出异常
                            System.out.println("error!invalid type!" + typeEnum);
                            return null;
                    }
                    header.append(fieldValue);
                    sendHeader = header.toString().getBytes(packet_encoding);
                }catch (Exception e){
                    //抛出异常
                    e.printStackTrace();
                }
            }
        }
        return sendHeader;
    }

    /**
     * 解析报文头
     * @param content
     */
    public static Map analyzeHeader(byte[] content){
        TreeMap filedMap = new TreeMap();
        try {
            //记录当前位置
            int pos = 0;
            // 遍历报文头
            Iterator it = mapHeaderDefinition.keySet().iterator();
            for(;it.hasNext();) {
                String fieldName = (String) it.next();//例如HEADER001
                HeaderInfo headerInfo = (HeaderInfo) mapHeaderDefinition.get(fieldName);//字段值
                //解析配置
                String domainName = headerInfo.getDomainName();
                PosTypeEnum typeEnum = headerInfo.getType();
                int length = headerInfo.getLength();

                //解析字段值
                String filedValue = "";

                switch (typeEnum) {
                    case STRING:
                    case INTEGER:
                        filedValue = new String(content, pos, length, packet_encoding);
                        pos += length;//记录当前位置
                        break;
                    case BINARY:
                        int binLength = length / 8;
                        byte[] bitMap16byte = new byte[binLength];
                        System.arraycopy(content, pos, bitMap16byte, 0, binLength);
                        filedValue = FieldUntil.get16BitMapStr(bitMap16byte);
                        pos += binLength;//记录当前位置
                        break;
                    default:
                        //抛出异常
                        System.out.println("error!invalid type!" + typeEnum);
                        return null;
                }
                filedMap.put(domainName, filedValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filedMap;
    }
}
