package bocsodf.constant;

/**
 *
 * ClassName: PosCode
 * Function: 常量
 * date: 2019-9-18 上午10:34:39
 *
 * @author ZhangHao
 */
public class PosCode {
    public final static int POS_HEADER_LEN = 46;
    public final static String POS_ENCODING = "ASCII";//POS消息编码格式
    public final static String HEADER_LENGTH = "Header-Length";//报文头长度
    public final static String HEADER_FLG_VERSION = "Header-Flag-and-Version";//头标识和版本号：测试报文
    public final static String HEADER_TOT_MESS_LEN = "Total–Message-Length";//报文总长度
    public final static String HEADER_DEST_STAT_ID = "Destination-Station-ID";//目的 ID
    public final static String HEADER_SOUR_STAT_ID = "Source-Station-ID";//源 ID
    public final static String HEADER_RES_USE = "Reserved-for-Use";//保留使用
    public final static String HEADER_BAT_NUM = "Batch-Number";//批次号
    public final static String HEADER_TRAN_INF = "Transaction-Information";//交易信息
    public final static String HEADER_USER_INF = "User-Information";//用户信息
    public final static String HEADER_REJ_CODE = "Reject-Code";//拒绝码
}
