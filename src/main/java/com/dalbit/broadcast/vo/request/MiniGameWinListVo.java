package com.dalbit.broadcast.vo.request;

import com.dalbit.broadcast.vo.procedure.P_MiniGameWinListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MiniGameWinListVo {

    public MiniGameWinListVo() {}
    public MiniGameWinListVo(P_MiniGameWinListVo pMiniGameWinListVo) {
        setMemNo(pMiniGameWinListVo.getMem_no());
        setRoomNo(pMiniGameWinListVo.getRoom_no());
        setMemNick(pMiniGameWinListVo.getMem_nick());
        setMemSex(pMiniGameWinListVo.getMem_sex());
        setMemBirthYear(pMiniGameWinListVo.getMem_birth_year());
        setPayAmt(pMiniGameWinListVo.getPay_amt());
        setOpt(pMiniGameWinListVo.getOpt());
        setLastUpdDate(pMiniGameWinListVo.getLast_upd_date());
        setImageProfile(pMiniGameWinListVo.getImage_profile());
        setImageProfile(pMiniGameWinListVo.getImage_profile());
    }

    private String roomNo;
    private String memNo;

    private String memNick;
    private String memSex;
    private String memBirthYear;
    private Integer payAmt;
    private String opt;
    private String lastUpdDate;
//    private String imageProfile;
    private Object imageProfile;
}
