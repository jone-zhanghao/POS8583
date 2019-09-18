package bocsodf.message;

import java.util.HashMap;

/**
 *
 * ClassName: PosDomain
 * Function: POS报文8583域格式定义
 * date: 2019-9-18 上午10:34:39
 *
 * @author ZhangHao
 */
public class PosDomain {

    private HashMap<String,DomainInfo> posMap = new HashMap<>();

    public PosDomain(){
        posMap.put("FIELD002",new DomainInfo(PosTypeEnum.STRING,"VAR2"));//primary_acct_num 主账号
        posMap.put("FIELD003",new DomainInfo(PosTypeEnum.INTEGER,"6"));//processing_code 交易处理码
//        posMap.put("FIELD004",new DomainInfo(DomainTypeEnum.STRING,"20"));//amt_trans 交易金额
//        posMap.put("FIELD005",new DomainInfo(DomainTypeEnum.STRING,"20"));//amt_settlmt 清算金额
//        posMap.put("FIELD006",new DomainInfo(DomainTypeEnum.STRING,"20"));//amt_cdhldr_bil 持卡人扣账金额
        posMap.put("FIELD007",new DomainInfo(PosTypeEnum.INTEGER,"10"));//transmsn_date_time 交易传输时间 （MMDDhhmmss）
//        posMap.put("FIELD009",new DomainInfo(DomainTypeEnum.STRING,"3"));//conv_rate_settlmt 清算汇率
//        posMap.put("FIELD010",new DomainInfo(DomainTypeEnum.STRING,"20"));//conv_rate_cdhldr_bil  持卡人扣账汇率
        posMap.put("FIELD011",new DomainInfo(PosTypeEnum.INTEGER,"6"));//sys_trace_audit_num 系统跟踪号
        posMap.put("FIELD012",new DomainInfo(PosTypeEnum.INTEGER,"6"));//time_local_trans 受卡方所在地时间 （hhmmss）
        posMap.put("FIELD013",new DomainInfo(PosTypeEnum.INTEGER,"4"));//date_local_trans 受卡方所在地日期 （MMDD）
        posMap.put("FIELD014",new DomainInfo(PosTypeEnum.INTEGER,"4"));//date_expr 卡有效期 （YYMM）
        posMap.put("FIELD015",new DomainInfo(PosTypeEnum.INTEGER,"4"));//date_settlmt 清算日期 （MMDD）
//        posMap.put("FIELD016",new DomainInfo(DomainTypeEnum.STRING,"6"));//date_conv 兑换日期
        posMap.put("FIELD018",new DomainInfo(PosTypeEnum.INTEGER,"4"));//mchnt_type 商户类型
        posMap.put("FIELD019",new DomainInfo(PosTypeEnum.INTEGER,"3"));//mchnt_cntry_code 商户国家代码
        posMap.put("FIELD022",new DomainInfo(PosTypeEnum.INTEGER,"3"));//pos_entry_mode_code 服务点输入方式码
//        posMap.put("FIELD023",new DomainInfo(DomainTypeEnum.STRING,"3"));//card_seq_id 卡序列号
        posMap.put("FIELD025",new DomainInfo(PosTypeEnum.INTEGER,"2"));//pos_cond_code 服务点条件码
        posMap.put("FIELD026",new DomainInfo(PosTypeEnum.INTEGER,"2"));//pos_pin_captr_code 服务点PIN获取码
//        posMap.put("FIELD028",new DomainInfo(DomainTypeEnum.STRING,"5"));//amt_trans_fee 交易费
        posMap.put("FIELD032",new DomainInfo(PosTypeEnum.INTEGER,"VAR2"));//acq_inst_id_code 代理机构标识码
        posMap.put("FIELD033",new DomainInfo(PosTypeEnum.INTEGER,"VAR2"));//fwd_inst_id_code 发送机构标识码
        posMap.put("FIELD035",new DomainInfo(PosTypeEnum.STRING,"VAR2"));//track_2_data 第二磁道数据
        posMap.put("FIELD036",new DomainInfo(PosTypeEnum.STRING,"VAR3"));//track_3_data 第三磁道数据
        posMap.put("FIELD037",new DomainInfo(PosTypeEnum.STRING,"12"));//retrivl_ref_num 检索参考号
//        posMap.put("FIELD038",new DomainInfo(DomainTypeEnum.STRING,"4"));//authr_id_resp 授权标识应答码
        posMap.put("FIELD039",new DomainInfo(PosTypeEnum.STRING,"2"));//resp_code 应答码
        posMap.put("FIELD041",new DomainInfo(PosTypeEnum.STRING,"8"));//card_accptr_termnl_id 受卡机终端标识码
        posMap.put("FIELD042",new DomainInfo(PosTypeEnum.STRING,"15"));//card_accptr_id 受卡方标识码
        posMap.put("FIELD043",new DomainInfo(PosTypeEnum.STRING,"40"));//card_accptr_name_loc 受卡方名称地址
//        posMap.put("FIELD044",new DomainInfo(DomainTypeEnum.STRING,"VAR2"));//addtnl_resp_code 附加响应数据
        posMap.put("FIELD045",new DomainInfo(PosTypeEnum.STRING,"VAR2"));//track _1_data 第一磁道数据
        posMap.put("FIELD048",new DomainInfo(PosTypeEnum.STRING,"VAR3"));//addtnl_data_private 附加数据——私有
        posMap.put("FIELD049",new DomainInfo(PosTypeEnum.STRING,"3"));//currcy_code_trans 交易货币代码
//        posMap.put("FIELD050",new DomainInfo(DomainTypeEnum.STRING,"VAR3"));//currcy_code_settlmt 清算货币代码
//        posMap.put("FIELD051",new DomainInfo(DomainTypeEnum.STRING,"VAR3"));//currcy_code_cdhldr_bil 持卡人帐户货币代码
        posMap.put("FIELD052",new DomainInfo(PosTypeEnum.STRING,"8"));//pin_data 个人标识码数据 b64
        posMap.put("FIELD053",new DomainInfo(PosTypeEnum.INTEGER,"16"));//sec_relatd_ctrl_info 安全控制信息
        posMap.put("FIELD054",new DomainInfo(PosTypeEnum.STRING,"VAR3"));//addtnl_amt 实际余额
//        posMap.put("FIELD055",new DomainInfo(DomainTypeEnum.STRING,"VAR3"));//ICC_data IC卡数据域
        posMap.put("FIELD056",new DomainInfo(PosTypeEnum.STRING,"VAR2"));//par 标记化支付账户参考号（PAR）
        posMap.put("FIELD057",new DomainInfo(PosTypeEnum.STRING,"VAR3"));//issr_addtnl_data 附加交易信息
//        posMap.put("FIELD059",new DomainInfo(DomainTypeEnum.STRING,"VAR3"));//detail_inqrng 明细查询数据
        posMap.put("FIELD060",new DomainInfo(PosTypeEnum.STRING,"VAR3"));//reserved 自定义域
        posMap.put("FIELD061",new DomainInfo(PosTypeEnum.STRING,"VAR3"));//ch_auth_info 持卡人身份认证信息
        posMap.put("FIELD062",new DomainInfo(PosTypeEnum.STRING,"VAR3"));//switching_data 交换中心数据
        posMap.put("FIELD063",new DomainInfo(PosTypeEnum.STRING,"VAR3"));//finacl_net_data 金融网络数据
//        posMap.put("FIELD070",new DomainInfo(DomainTypeEnum.STRING,"3"));//netwk_mgmt_info_code 网络管理信息码
//        posMap.put("FIELD090",new DomainInfo(DomainTypeEnum.STRING,"9"));//orig_data_elemts 原始数据元
//        posMap.put("FIELD096",new DomainInfo(DomainTypeEnum.STRING,"8"));//msg_security_code 报文安全码
        posMap.put("FIELD100",new DomainInfo(PosTypeEnum.STRING,"VAR2"));//rcvg_inst_id_code 接收机构标识码
//        posMap.put("FIELD102",new DomainInfo(DomainTypeEnum.STRING,"80"));//acct_id1 帐户标识1
        posMap.put("FIELD104",new DomainInfo(PosTypeEnum.STRING,"VAR3"));//trans_industry_app_inf 交易和行业应用相关信息
        posMap.put("FIELD117",new DomainInfo(PosTypeEnum.STRING,"VAR3"));//natl_region_info 国家和地区信息
        posMap.put("FIELD121",new DomainInfo(PosTypeEnum.STRING,"VAR3"));//national_sw_resved 交换中心保留
        posMap.put("FIELD122",new DomainInfo(PosTypeEnum.STRING,"VAR3"));//acq_inst_resvd 受理方保留
        posMap.put("FIELD123",new DomainInfo(PosTypeEnum.STRING,"VAR3"));//issr_inst_resvd 发卡方保留
//        posMap.put("FIELD125",new DomainInfo(DomainTypeEnum.STRING,"VAR3"));//risk_asse_info 安全和风险评估信息
        posMap.put("FIELD128",new DomainInfo(PosTypeEnum.STRING,"8"));//msg_authn_code 报文鉴别码 b64
    }

    public HashMap<String,DomainInfo> getMap(){
        return posMap;
    }


}
