package bocsodf.message;

import bocsodf.constant.PosCode;

import java.util.TreeMap;

/**
 *
 * ClassName: PosHeader
 * Function: POS报文头格式定义
 * date: 2019-9-18 上午10:34:39
 *
 * @author ZhangHao
 */
public class PosHeader {
    private TreeMap posMap = new TreeMap<>();

    public PosHeader(){
        //域1 报文头长度:该域的值必须为 46
        posMap.put("HEADER001",new HeaderInfo(PosCode.HEADER_LENGTH,PosTypeEnum.BINARY,8));
        //域2 头标识和版本号
        //  第一位:0 表示该报文是一个生产报文、1 表示该报文是一个测试报文
        posMap.put("HEADER002",new HeaderInfo(PosCode.HEADER_FLG_VERSION,PosTypeEnum.BINARY,8));
        //域3 报文总长度
        // 正确的报文,该域的值必须大于 46 且小于等于 1846 字节
        // 拒绝的报文,即必须大于 92 且小于等于 1892 字节
        posMap.put("HEADER003",new HeaderInfo(PosCode.HEADER_TOT_MESS_LEN,PosTypeEnum.INTEGER,4));
        //域4 目的ID
        // 境内入网机构发出的报文中，该域必须为 CUPS 的 ID，00010000
        // 境外入网机构发出的报文中，该域必须为 CUPS 的 ID，00010344
        posMap.put("HEADER004",new HeaderInfo(PosCode.HEADER_DEST_STAT_ID,PosTypeEnum.STRING,11));
        //域5 源ID
        posMap.put("HEADER005",new HeaderInfo(PosCode.HEADER_SOUR_STAT_ID,PosTypeEnum.STRING,11));
        //域6 保留使用
        // 入网机构发出的请求报文，该域值为 0；
        // 应答报文，则该域值与请求报文中的值一致
        posMap.put("HEADER006",new HeaderInfo(PosCode.HEADER_RES_USE,PosTypeEnum.BINARY,24));
        //域7 批次号
        // 入网机构主动发出的请求报文，该域值为 0；
        // 入网机构向 CUPS 返回应答时该域的值与其相应的请求报文中的值相同
        posMap.put("HEADER007",new HeaderInfo(PosCode.HEADER_BAT_NUM,PosTypeEnum.BINARY,8));
        //域8 交易信息(1位交易标识 + 1位通知交易标识 + 6位保留使用)
        // 入网机构发出的请求报文中，该域值为全零，入网机构返回中心的应答报文中必须将该域值原样返回，中心发出的报文中需要将该域置为有效值.
        // 交易标识:
        // 0－银联卡境内交易：受理方和发卡方均在中国大陆境内；
        // 1－银联卡跨境交易：受理方和发卡方有且只有一方在中国大陆境内，卡片为银联标识卡；
        // 2－外卡收单交易：受理方为中国大陆境内机构，卡片为其他信用卡组织的卡片；
        // 3－银联卡境外交易：受理方和发卡方都在中国大陆以外（中国大陆境内机构不会收到此类交易）；
        // 通知交易标识:
        // 0  缺省值
        // 1  无含义
        // 2  无含义
        // 3  风险管理通知类
        posMap.put("HEADER008",new HeaderInfo(PosCode.HEADER_TRAN_INF,PosTypeEnum.STRING,8));
        //域9 用户信息
        // 在请求报文中，该域需要包含在入网机构选项中由用户定义的值
        // 不需要用户信息，该域必须填 0 值；
        // 应答报文，入网机构必须保留请求中的域值并在应答时原封不动地返回
        posMap.put("HEADER009",new HeaderInfo(PosCode.HEADER_USER_INF,PosTypeEnum.BINARY,8));
        //域10 拒绝码
        // CUPS 填写本域表示拒绝该报文的原因
        posMap.put("HEADER010",new HeaderInfo(PosCode.HEADER_REJ_CODE,PosTypeEnum.INTEGER,5));
    }

    public TreeMap getMap(){
        return posMap;
    }
}
