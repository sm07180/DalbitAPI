package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.ExchangeApplyVo;
import com.dalbit.util.AES;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Getter @Setter
public class P_ExchangeApplyVo extends P_ApiVo {

    private String mem_no;
    private BigDecimal byeol;
    private String account_name;
    private String bank_code;
    private String account_no;
    private String social_no;
    private String phone_no;
    private String address1;
    private String address2;
    private String add_file1;
    private String add_file2;
    private String add_file3;
    private int terms_agree;
    private int os;
    private String ip;
    private int latest_idx;

    public P_ExchangeApplyVo(){}
    public P_ExchangeApplyVo(ExchangeApplyVo exchangeApplyVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setByeol(exchangeApplyVo.getByeol());
        setAccount_name(exchangeApplyVo.getAccountName());
        setBank_code(exchangeApplyVo.getBankCode());
        setAccount_no(exchangeApplyVo.getAccountNo());
        setSocial_no(AES.encrypt(exchangeApplyVo.getSocialNo(), DalbitUtil.getProperty("social.secret.key")));
        setPhone_no(exchangeApplyVo.getPhoneNo());
        setAddress1(exchangeApplyVo.getAddress1());
        setAddress2(exchangeApplyVo.getAddress2());
        setAdd_file1(exchangeApplyVo.getAddFile1());
        setAdd_file2(exchangeApplyVo.getAddFile2());
        setAdd_file3(exchangeApplyVo.getAddFile3());
        setTerms_agree(exchangeApplyVo.getTermsAgree());
        setOs(new DeviceVo(request).getOs());
        setIp(new DeviceVo(request).getIp());
        setLatest_idx(DalbitUtil.isEmpty(exchangeApplyVo.getLatestIdx()) ? 0 : exchangeApplyVo.getLatestIdx());
    }
}
