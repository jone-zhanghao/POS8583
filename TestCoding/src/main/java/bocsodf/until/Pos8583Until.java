package bocsodf.until;

import bocsodf.constant.PosCode;
import bocsodf.message.DomainInfo;
import bocsodf.message.PosDomain;
import bocsodf.message.PosTypeEnum;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * ClassName: Pos8583Until
 * Function: POS报文8583区域（位图+数据元区）组装公共until
 * date: 2019-9-18 上午10:34:39
 *
 * @author ZhangHao
 */
public class Pos8583Until {

    // 报文编码
    private static String packet_encoding = PosCode.POS_ENCODING;
    // 8583报文128域定义器
    private static Map map8583Definition = null;

    static{

        System.out.println("Pos8583Until使用编码配置:[encoding:"+packet_encoding+"]");

        //获取pos中8583协议使用域定义
        Pos8583Until.map8583Definition = new PosDomain().getMap();
    }

    /**
     * 8583报文初始位图:128位01字符串
     */
    public static String getInitBitMap(){
        //Todo 优化
        String initBitMap = "10000000" + "00000000" + "00000000" + "00000000"
                        + "00000000" + "00000000" + "00000000" + "00000000"
                        + "00000000" + "00000000" + "00000000" + "00000000"
                        + "00000000" + "00000000" + "00000000" + "00000000";
        return initBitMap;
    }

    /**
     * 组装8583报文
     * @param filedMap 输入
     * @return
     */
    public static byte[] make8583(TreeMap filedMap){
        byte[] send8583 = null;
        if(filedMap != null){
            try {
                //获取初始化的128域位图
                String bitMap128 = getInitBitMap();
                //按照8583定义器格式化各个域的内容
                Map all = formatValueTo8583(filedMap,bitMap128);
                //获取上送报文内容
                send8583 = getWhole8583Packet(all);
            } catch (Exception e) {
                //抛出异常
                e.printStackTrace();
            }
        }
        return send8583;
    }

    /**
     * 组装8583报文
     * @param filedMap 输入
     * @param bitMap128 128域位图
     * @return
     */
    public static Map formatValueTo8583(TreeMap filedMap,String bitMap128){
        Map all = new HashMap();
        //初始化结果
        TreeMap formatedFiledMap = new TreeMap();
        if(filedMap != null){
            Iterator it = filedMap.keySet().iterator();
            for(;it.hasNext();){
                String fieldName = (String)it.next();//例如FIELD005
                String fieldValue = (String)filedMap.get(fieldName);//字段值

                try{
                    if (fieldValue == null || fieldValue.length() == 0) {
                        //抛出异常 or 继续
                        System.out.println("error:报文域 {" + fieldName + "}为空值");
                        fieldValue = "";
//                        return null;
                        continue;
                    }
                    //将域值编码转换，保证报文编码统一
                    fieldValue = new String(fieldValue.getBytes(packet_encoding),packet_encoding);

                    //pos服务定义使用的128域
                    DomainInfo domainInfo = (DomainInfo)Pos8583Until.map8583Definition.get(fieldName);
                    if (domainInfo == null) {
                        //抛出异常
                        System.out.println("error:" + fieldName + "配置文件中不存在!");
                        return null;
                    }

                    //获取域位
                    String fieldNo = fieldName.substring(5, 8);//例如005
                    //组二进制位图串
                    bitMap128 = change16bitMapFlag(fieldNo, bitMap128);

                    //获取域定义信息
                    PosTypeEnum defType = domainInfo.getType();//类型定义例string
                    String defLen = domainInfo.getLength();//长度定义,例20

                    //是否定长判断
                    boolean isFixLen = true;
                    if(defLen.startsWith("VAR")){//变长域
                        isFixLen = false;
                        defLen = defLen.substring(3);//获取VAR2后面的2
                    }
                    int fieldLen = fieldValue.getBytes(packet_encoding).length;//字段值得实际长度

                    //判断是否为变长域
                    if (!isFixLen) {
                        // 变长域(变长域最后组装成的效果：例如变长3位，定义VAR3，这里的3是指长度值占3位，字段值是123456，最后结果就是006123456)
                        int variableLength = Integer.parseInt(defLen);
                        if (String.valueOf(fieldLen).length() > (10 * variableLength)) {
                            //抛出异常
                            System.out.println("error:字段" + fieldName + "的数据定义长度的长度为" + defLen + "位,长度不能超过"+ (10 * variableLength));
                            return null;
                        }else{
                            //将长度值组装入字段
                            fieldValue = getVaryLengthValue(fieldValue, variableLength) + fieldValue;
                        }
                    } else {
                        //定长域(定长比较好理解，一个字段规定是N位，那么字段值绝对不能超过N位，不足N位就在后面补空格)
                        int fixLength = Integer.parseInt(defLen);
                        if (defType == PosTypeEnum.BINARY) {
                            fixLength = fixLength * 8;
                        }
                        if (fieldLen > fixLength) {
                            System.out.println("error:字段" + fieldName + "的数据定义长度为" + defLen + "位,长度不能超过"+defLen);
                            return null;
                        }
                        fieldValue = FieldUntil.getFixFieldValue(fieldValue, fixLength, packet_encoding, defType);//定长处理
                        //二进制来为转换
                        if (defType == PosTypeEnum.BINARY && fieldValue != null) {
                            fieldValue = new String(FieldUntil.get16BitByteFromStr(fieldValue,packet_encoding),packet_encoding);
                        }
                    }
                    System.out.println("组装后报文域 {" + fieldName + "}==" + fieldValue+"==,域长度:"+fieldValue.getBytes(packet_encoding).length);

                    // 返回结果赋值
                    if (formatedFiledMap.containsKey(fieldName)) {
                        formatedFiledMap.remove(fieldName);
                    }
                    formatedFiledMap.put(fieldName, fieldValue);


                } catch (Exception e) {
                    //抛出异常
                    e.printStackTrace();
                }
            }
        }
        all.put("formatedFiledMap", formatedFiledMap);
        all.put("bitMap128", bitMap128);
        return all;
    }

    /**
     * 解析8583报文
     * @param content8583 位图+8583域
     */
    public static Map analyze8583(byte[] content8583) {
        TreeMap filedMap = new TreeMap();
        try {
            // 取位图
            byte[] bitMap16byte = new byte[16];
            System.arraycopy(content8583, 0, bitMap16byte, 0, 16);
            // 16位图转2进制位图128位字符串
            String bitMap128Str = FieldUntil.get16BitMapStr(bitMap16byte);

            //记录当前位置,从位图后开始遍历取值

            int pos = 16;
            // 遍历128位图，取值。注意从FIELD002开始
            for (int i = 1; i < bitMap128Str.length(); i++) {
                String filedValue = "";//字段值
                String filedName = "FIELD" + getNumThree((i+1));//FIELD005

                if (bitMap128Str.charAt(i) == '1') {
                    // 获取域定义信息
                    DomainInfo domainInfo = (DomainInfo)Pos8583Until.map8583Definition.get(filedName);
                    PosTypeEnum defType = domainInfo.getType();//类型定义例string
                    String defLen = domainInfo.getLength();//长度定义,例20

                    //是否定长判断
                    boolean isFixLen = true;
                    if(defLen.startsWith("VAR")){//变长域
                        isFixLen = false;
                        defLen = defLen.substring(3);//获取VAR2后面的2
                    }
                    // 截取该域信息
                    if (!isFixLen) {
                        //变长域
                        int defLen1 = Integer.valueOf(defLen);//VAR2后面的2
                        String realLen1 = new String(content8583, pos, defLen1, packet_encoding);//报文中实际记录域长,例如16,023
                        int realAllLen = defLen1 + Integer.valueOf(realLen1);//该字段总长度（包括长度值占的长度）
                        byte[] filedValueByte = new byte[Integer.valueOf(realLen1)];
                        System.arraycopy(content8583, pos+defLen1, filedValueByte, 0, filedValueByte.length);
                        filedValue = new String(filedValueByte,packet_encoding);
                        pos += realAllLen;//记录当前位置
                    } else {
                        //定长域
                        int defLen2 = Integer.valueOf(defLen);//长度值占的位数
                        filedValue = new String(content8583, pos, defLen2, packet_encoding);
                        pos += defLen2;//记录当前位置
                    }
                    //二进制特殊处理
                    if (defType == PosTypeEnum.BINARY){
                        filedValue = FieldUntil.get16BitMapStr(filedValue,packet_encoding);
                    }
                    filedMap.put(filedName, filedValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filedMap;
    }

    /**
     * 获取完整的8583报文体（128域）
     * @param
     * @return
     */
    public static byte[] getWhole8583Packet(Map all){
        if(all == null || all.get("formatedFiledMap") == null || all.get("bitMap128") == null){
            return null;
        }
        try {
            String bitMap128 = (String)all.get("bitMap128");
            // 128域位图二进制字符串转16位16进制
            byte[] bitmaps = FieldUntil.get16BitByteFromStr(bitMap128,packet_encoding);

            TreeMap pacBody=(TreeMap)all.get("formatedFiledMap");
            StringBuffer last128 = new StringBuffer();
            Iterator it = pacBody.keySet().iterator();
            for(;it.hasNext();){
                String key = (String)it.next();
                String value = (String)pacBody.get(key);
                last128.append(value);
            }
            byte[] bitContent = last128.toString().getBytes(packet_encoding);//域值

            //组装
            byte[] package8583 = null;
            package8583 = FieldUntil.arrayApend(package8583, bitmaps);
            package8583 = FieldUntil.arrayApend(package8583, bitContent);

            return package8583;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 改变128位图中的标志为1
     * @param fieldNo
     * @param res
     * @return
     */
    private static String change16bitMapFlag(String fieldNo, String res) {
        int indexNo = Integer.parseInt(fieldNo);
        res = res.substring(0, indexNo-1) + "1" + res.substring(indexNo);
        return res;
    }

    /**
     * 获取8583域Map的key值
     * @param num 序号
     * @return String
     */
    public static String getNumThree(int num){
        String len = "";
        String iStr = String.valueOf(num);
        len = FieldUntil.strCopy("0",3-iStr.length()) + iStr;
        return len;
    }

    /**
     * 获取字符串变长值
     * @param valueStr 变长字符
     * @param defLen 变长字符长度
     * 例如getFixLengthValue("12345678", 2)返回08
     * 例如getFixLengthValue("12345678", 3)返回008
     *
     * 注意变长长度的计算：
     * 长度的判断使用转化后的字节数组长度，因为中文在不同的编码方式下，长度是不同的，GBK是2，UTF-8是3，按字符创长度算就是1.
     * 解析报文是按照字节来解析的，所以长度以字节长度为准，防止中文带来乱码。
     *
     * 比如一个变长域:aa索隆bb，如果按照字符串计算长度那么就是6，最后是06aa索隆bb。
     * 这样在解析时按照字节解析长度就乱了，因为按照GBK字节解析，一个汉字占2，按照UTF-8解析，一个汉字占3.
     * 所以在计算时必须按照字节长度为准！按照我们设置的UTF-8编码结果就是10aa索隆bb.
     * 这样在解析时长度正好是10，也就不会乱了。
     * @return
     */
    public static String getVaryLengthValue(String valueStr,int defLen){
        return getVaryLengthValue(valueStr,defLen,packet_encoding);
    }

    public static String getVaryLengthValue(String valueStr,int defLen,String encoding){
        String fixLen = "";
        try{
            if(valueStr == null){
                return FieldUntil.strCopy("0",defLen);
            }else{
                byte[] valueStrByte = null;
                //这里最好指定编码，不使用平台默认编码
                if(encoding == null || encoding.trim().equals("")){
                    valueStrByte = valueStr.getBytes();
                }else{
                    valueStrByte = valueStr.getBytes(encoding);
                }
                //长度的判断使用转化后的字节数组长度，因为中文在不同的编码方式下，长度是不同的，GBK是2，UTF-8是3，按字符创长度算就是1.
                //解析报文是按照字节来解析的，所以长度以字节长度为准，防止中文带来乱码
                if(valueStrByte.length>(10*defLen)){
                    return null;
                }else{
                    int len = valueStrByte.length;//字段实际长度
                    String len1 = String.valueOf(len);
                    fixLen = FieldUntil.strCopy("0",(defLen-len1.length())) + len1;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fixLen;
    }
}
