package bocsodf.message;

/**
 *
 * ClassName: HeaderInfo
 * Function: POS报文头数据信息
 * date: 2019-9-18 上午10:34:39
 *
 * @author ZhangHao
 */
public class HeaderInfo {
    private String domainName;
    //数据类型
    private PosTypeEnum type;
    //数据长度
    private int length;

    public HeaderInfo(String domainName,PosTypeEnum type,int length){
        this.domainName = domainName;
        this.type = type;
        this.length = length;
    }

    public String getDomainName() {
        return domainName;
    }

    public PosTypeEnum getType() {
        return type;
    }

    public int getLength() {
        return length;
    }
}
