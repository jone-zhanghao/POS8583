package bocsodf.until;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 *
 * ClassName: FieldUntil
 * Function: 栏位处理公共until
 * date: 2019-9-18 上午10:34:39
 *
 * @author ZhangHao
 */
public class FieldUntil {

    /**
     * 将字段值做定长处理，不足定长则在后面补空格
     * @param valueStr 原字符串
     * @param defLen 长度
     * @param encoding 编码格式
     * @return
     */
    public static String getFixFieldValue(String valueStr,int defLen,String encoding,Object object){
        String fixValue = "";
        int length = 0;
        try {
            if(valueStr == null){
                valueStr = "";
                length = defLen;
            }else{
                byte[] valueStrByte;
                //这里最好指定编码，不使用平台默认编码
                if(encoding == null || encoding.trim().equals("")){
                    valueStrByte = valueStr.getBytes();
                }else{
                    valueStrByte = valueStr.getBytes(encoding);
                }
                //长度的判断使用转化后的字节数组长度，因为中文在不同的编码方式下，长度是不同的，GBK是2，UTF-8是3，按字符创长度算就是1.
                //解析报文是按照字节来解析的，所以长度以字节长度为准，防止中文带来乱码
                if(valueStrByte.length > defLen){
                    return null;
                }else{
                    length = defLen - valueStrByte.length;
                }
            }
            //判断数据类型：字符左对齐右补空格，数值右对齐左补零
            String copyChar = " ";
            if (Integer.class.equals(object)){
                copyChar = "0";
                fixValue = strCopy(copyChar,length) + valueStr;
            }else{
                fixValue = valueStr + strCopy(copyChar,length);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e1){
            e1.printStackTrace();
        }
        return fixValue;
    }

    /**
     * 复制字符
     * @param str
     * @param count
     * @return
     */
    public static String strCopy(String str,int count){
        StringBuffer sb = new StringBuffer();
        for(int i=0;i < count;i++){
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 返回a和b的组合,实现累加功能
     * @param a
     * @param b
     * @return
     */
    public static byte[] arrayApend(byte[] a,byte[] b){
        int a_len = (a==null?0:a.length);
        int b_len = (b==null?0:b.length);
        byte[] c = new byte[a_len + b_len];
        if(a_len == 0 && b_len == 0){
            return null;
        }else if(a_len == 0){
            System.arraycopy(b, 0, c, 0, b.length);
        }else if(b_len == 0){
            System.arraycopy(a, 0, c, 0, a.length);
        }else{
            System.arraycopy(a, 0, c, 0, a.length);
            System.arraycopy(b, 0, c, a.length, b.length);
        }
        return c;
    }

    /**
     *  位图操作
     *
     * 把128位01字符串转化成16位图的字节数组
     * @param
     * @return
     */
    public static byte[] get16BitByteFromStr(String bitStr,String encoding){
        if(bitStr == null){
            return null;
        }
        int length = bitStr.length() / 8;
        byte[]  bit16 = new byte[length];
        try {
            //128域位图二进制字符串转16位16进制
            byte[] tmp = bitStr.getBytes(encoding);
            int weight;//权重
            byte[] strout = new byte[bitStr.length()];
            int i, j, w = 0;
            for (i = 0; i < length; i++) {
                weight = 0x0080;
                for (j = 0; j < 8; j++) {
                    strout[i] += ((tmp[w]) - '0') * weight;
                    weight /= 2;
                    w++;
                }
                bit16[i] = strout[i];
            }
        } catch (UnsupportedEncodingException e) {
            //抛出异常
            e.printStackTrace();
        }
        return bit16;
    }

    /**
     * 位图操作
     *
     * 把16位图的字节数组转化成01字符串
     * @param
     * @return
     */
    public static String get16BitMapStr(byte[] bitMap16){
        String bitMapStr = "";
        // 16位图转2进制位图128位字符串
        for (int i = 0; i < bitMap16.length; i++) {
            int bc = bitMap16[i];
            bc = (bc<0)?(bc+256):bc;
            String bitnaryStr = Integer.toBinaryString(bc);//二进制字符串
            // 左补零，保证是8位
            String rightBitnaryStr = FieldUntil.strCopy("0",Math.abs(8 - bitnaryStr.length())) + bitnaryStr;//位图二进制字符串
            // 先去除多余的零，然后组装128域二进制字符串
            bitMapStr += rightBitnaryStr;
        }
        return bitMapStr;
    }

    /**
     * 从一个byte[]数组中截取一部分
     * @param
     * @return
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {
        //Todo 效率优化
        byte[] bs = new byte[count];
        for (int i=begin;i<begin+count; i++) {
            bs[i-begin] = src[i];
        }
        return bs;
    }

    /**
     * @Description: 十进制转换成二进制
     * @param decimalSource
     * @return String
     */
    public static String decimalToBinary(int decimalSource) {
        BigInteger bi = new BigInteger(String.valueOf(decimalSource));
        return bi.toString(2);  //参数2指定的是转化成X进制，默认10进制
    }

    /**
     * @Description: 二进制转换成十进制
     * @param binarySource
     * @return int
     */
    public static int binaryToDecimal(String binarySource) {
        BigInteger bi = new BigInteger(binarySource, 2);    //转换为BigInteger类型
        return bi.intValue();     //转换成十进制
    }
}
