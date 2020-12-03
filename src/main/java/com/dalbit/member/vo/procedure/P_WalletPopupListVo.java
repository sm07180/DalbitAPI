package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.request.WalletPopupListVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_WalletPopupListVo extends P_ApiVo {

    public P_WalletPopupListVo(){}
    public P_WalletPopupListVo(WalletPopupListVo walletPopupListVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setSlctType(walletPopupListVo.getWalletType());
    }

    /* Input */
    private String mem_no;
    private int slctType;

}
