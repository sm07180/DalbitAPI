package com.dalbit.member.vo;

import com.dalbit.member.vo.procedure.P_MypageManagerVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ManagerOutVo {
    private String regDt;
    private long regTs;
    private String memNo;
    private String nickNm;
    private String memId;
    private String ctrlRole;

    public ManagerOutVo(){}
    public ManagerOutVo(P_MypageManagerVo target) {
        setRegDt(DalbitUtil.getUTCFormat(target.getRegDate()));
        setRegTs(DalbitUtil.getUTCTimeStamp(target.getRegDate()));
        setMemNo(target.getMem_no());
        setNickNm(target.getMem_nick());
        setMemId(target.getMem_id());
        setCtrlRole(target.getControlRole());
    }
}
