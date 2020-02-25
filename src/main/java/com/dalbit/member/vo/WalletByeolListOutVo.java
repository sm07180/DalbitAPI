package com.dalbit.member.vo;

import com.dalbit.member.vo.procedure.P_ByeolVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WalletByeolListOutVo {

    private String updateDt;
    private Long updateTs;
    private int walletType;
    private String contents;
    private int byeolCnt;

    public WalletByeolListOutVo(P_ByeolVo target) {
        setUpdateDt(DalbitUtil.getUTCFormat(target.getUpdateDate()));
        setUpdateTs(DalbitUtil.getUTCTimeStamp(target.getUpdateDate()));
        setWalletType(target.getSlctType());
        setContents(target.getUseContents());
        setByeolCnt(target.getUseByeol());
    }

}

