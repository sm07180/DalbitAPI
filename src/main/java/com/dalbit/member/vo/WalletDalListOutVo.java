package com.dalbit.member.vo;

import com.dalbit.member.vo.procedure.P_DalVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WalletDalListOutVo {

    private String updateDt;
    private Long updateTs;
    private int walletType;
    private String contents;
    private int dalCnt;


    public WalletDalListOutVo(P_DalVo target) {
        setUpdateDt(DalbitUtil.getUTCFormat(target.getUpdateDate()));
        setUpdateTs(DalbitUtil.getUTCTimeStamp(target.getUpdateDate()));
        setWalletType(target.getSlctType());
        setContents(target.getUseContents());
        setDalCnt(target.getUseDal());
    }

}

