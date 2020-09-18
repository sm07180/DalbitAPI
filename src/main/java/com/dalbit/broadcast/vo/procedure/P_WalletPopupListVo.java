package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.WalletPopupListVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_WalletPopupListVo {

    public P_WalletPopupListVo(){}
    public P_WalletPopupListVo(WalletPopupListVo walletPopupListVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setSlctType(walletPopupListVo.getWalletType());
    }

    /* Input */
    private String mem_no;
    private int slctType;

}
