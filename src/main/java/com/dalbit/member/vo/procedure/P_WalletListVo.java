package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.WalletListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter
public class P_WalletListVo extends P_ApiVo {

    public P_WalletListVo(){}
    public P_WalletListVo(WalletListVo walletListVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(walletListVo.getPage()) ? 1 : walletListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(walletListVo.getRecords()) ? 10 : walletListVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setSlctType(walletListVo.getWalletType());
        setSlctList(walletListVo.getWalletCode());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* Input */
    private String mem_no;
    private int slctType;
    private String slctList;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private String useContents;
    private int useByeol;
    private int useDal;
    private Date updateDate;
    private int type;
    private int cancelIdx;
}
