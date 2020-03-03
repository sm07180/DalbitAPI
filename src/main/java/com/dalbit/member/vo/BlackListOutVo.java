package com.dalbit.member.vo;

import com.dalbit.member.vo.procedure.P_MypageBlackVo;
import com.dalbit.member.vo.procedure.P_MypageManagerVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BlackListOutVo {
    private String regDt;
    private long regTs;
    private String memNo;
    private String nickNm;
    private String memId;

    public BlackListOutVo(){}
    public BlackListOutVo(P_MypageBlackVo target) {
        setRegDt(DalbitUtil.getUTCFormat(target.getRegDate()));
        setRegTs(DalbitUtil.getUTCTimeStamp(target.getRegDate()));
        setMemNo(target.getMem_no());
        setNickNm(target.getMem_nick());
        setMemId(target.getMem_id());
    }
}
