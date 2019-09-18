package bocsodf.message;

/**
 *
 * ClassName: DomainInfo
 * Function: POS报文8583域数据信息
 * date: 2019-9-18 上午10:34:39
 *
 * @author ZhangHao
 */
public class DomainInfo {
    //数据类型
    private PosTypeEnum type;
    //数据长度
    //*  定长： n
    //*        如 9 - 定长为9，不足补齐
    //*  变长:  VARn
    //*        如 VAR2 - 变长数据长度最大为两位，数据存储格式为 LL + 数据值
    private String length;

    public DomainInfo(PosTypeEnum type,String length){
        this.type = type;
        this.length = length;
    }

    public PosTypeEnum getType() {
        return type;
    }

    public String getLength() {
        return length;
    }
}
