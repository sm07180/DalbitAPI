package com.dalbit.member.vo;

import com.dalbit.member.vo.procedure.P_SearchUserVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SearchUserOutVo {

    private String memNo;
    private String nickNm;
    private String memId;

    public SearchUserOutVo(){}
    public SearchUserOutVo(P_SearchUserVo target) {
        setMemNo(target.getMem_no());
        setNickNm(target.getMem_nick());
        setMemId(target.getMem_id());
    }
}
