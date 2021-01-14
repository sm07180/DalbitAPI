package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_MemberReceiveListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class MemberReceiveListOutVo {

    private String memNo;
    private ImageVo profImg;
    private String nickNm;
    private String regDt;
    private long regTs;

    public MemberReceiveListOutVo(){}
    public MemberReceiveListOutVo(P_MemberReceiveListVo target){
        setMemNo(target.getMem_no_star());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
        setNickNm(target.getMem_nick_star());
        setRegDt(DalbitUtil.isEmpty(target.getLast_upd_date()) ? "" : DalbitUtil.getUTCFormat(target.getLast_upd_date()));
        setRegTs(DalbitUtil.isEmpty(target.getLast_upd_date()) ? 0 : DalbitUtil.getUTCTimeStamp(target.getLast_upd_date()));
    }
}
