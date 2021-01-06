package com.dalbit.member.vo;

import com.dalbit.member.vo.procedure.P_WalletListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WalletListOutVo {

    private int type;
    private String contents;
    private int byeolCnt;
    private int dalCnt;
    private String updateDt;
    private Long updateTs;
    private int exchangeIdx;

    public WalletListOutVo(P_WalletListVo pWalletListVo){
        setType(pWalletListVo.getType());
        setContents(pWalletListVo.getUseContents());
        setByeolCnt(pWalletListVo.getUseByeol());
        setDalCnt(pWalletListVo.getUseDal());
        setUpdateDt(DalbitUtil.getUTCFormat(pWalletListVo.getUpdateDate()));
        setUpdateTs(DalbitUtil.getUTCTimeStamp(pWalletListVo.getUpdateDate()));
        setExchangeIdx(pWalletListVo.getCancelIdx());
    }
}
